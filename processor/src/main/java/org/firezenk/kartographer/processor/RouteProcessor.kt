package org.firezenk.kartographer.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import org.firezenk.kartographer.annotations.RoutableActivity
import org.firezenk.kartographer.annotations.RoutableView
import java.io.File
import java.util.*
import javax.annotation.Generated
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@AutoService(Processor::class)
class RouteProcessor : AbstractProcessor() {

    private var filer: Filer? = null
    private var messager: Messager? = null

    override fun init(env: ProcessingEnvironment?) {
        super.init(env)
        this.filer = env?.filer;
        this.messager = env?.messager;
    }

    override fun getSupportedAnnotationTypes() = setOf<String>(
            RoutableActivity::class.java.canonicalName,
            RoutableView::class.java.canonicalName
    )

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun process(annotations: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        env.getElementsAnnotatedWith(RoutableActivity::class.java)
                .map { generateRoute(it as TypeElement) }
                .forEach {
                    try {
                        it.writeTo(File(it.packageName))
                    } catch (e: Exception) {
                        messager?.printMessage(Diagnostic.Kind.ERROR, e.message)
                        e.printStackTrace()
                    }
                }

        env.getElementsAnnotatedWith(RoutableView::class.java)
                .map { generateRoute(it as TypeElement) }
                .forEach {
                    try {
                        it.writeTo(File(it.packageName))
                    } catch (e: Exception) {
                        messager?.printMessage(Diagnostic.Kind.ERROR, e.message)
                        e.printStackTrace()
                    }
                }

        return true
    }

    private fun generateRoute(typeElement: TypeElement): FileSpec {
        messager?.printMessage(Diagnostic.Kind.NOTE, "Creating route...")

        val methods = arrayListOf<FunSpec>(addRouteMethod(typeElement))

        val myClass = createRoute(typeElement, methods)

        messager?.printMessage(Diagnostic.Kind.NOTE, "Save location: "
                + typeElement.qualifiedName.toString().replace("."
                + typeElement.simpleName, ""))

        return FileSpec
                .builder(typeElement.qualifiedName.toString().replace("."
                        + typeElement.simpleName, ""), myClass.name!!)
                .build()
    }

    private fun addRouteMethod(typeElement: TypeElement): FunSpec {
        messager?.printMessage(Diagnostic.Kind.NOTE, "Generating route method")

        val isActivity = typeElement.getAnnotation(RoutableActivity::class.java) != null
        val requestCode: Int
        val params: List<TypeMirror>?

        if (isActivity) {
            requestCode = this.getForResult(typeElement.getAnnotation(RoutableActivity::class.java))
            params = this.getParameters(typeElement.getAnnotation(RoutableActivity::class.java))
        } else {
            requestCode = this.getForResult(typeElement.getAnnotation(RoutableView::class.java))
            params = this.getParameters(typeElement.getAnnotation(RoutableView::class.java))
        }

        val sb = StringBuilder()

        sb.append("" +
                "  if (parameters.length < " + params?.size + ") {\n" +
                "      throw new NotEnoughParametersException(\"Need " + params?.size + " params\");\n" +
                "  }\n")

        for ((i, tm) in params!!.withIndex()) {
            sb.append("" +
                    "  if (parameters[" + i + "] == null || !(parameters[" + i + "] instanceof " + tm.toString() + ")) {\n" +
                    "      throw new ParameterNotFoundException(\"Need " + tm.toString() + "\");\n" +
                    "  }\n")
        }

        sb.append("\n")

        if (isActivity) {
            sb.append("  final android.content.Intent intent = new android.content.Intent((android.content.Context) context, " + typeElement.simpleName + ".class);\n")

            sb.append("  android.os.Bundle bundle = new android.os.Bundle();\n\n")
            sb.append("  bundle.putString(\"uuid\", uuid.toString());\n")

            if (params.isNotEmpty()) {
                sb.append(this.parametersToBundle(params))
            }

            sb.append("  intent.putExtras(bundle);\n")

            sb.append("" +
                    "  if (context instanceof android.app.Activity) {\n" +
                    "      ((android.app.Activity) context).startActivityForResult(intent, " + requestCode + ");\n" +
                    "  } else if (context instanceof android.content.Context) {\n" +
                    "      ((android.content.Context) context).startActivity(intent);\n" +
                    "  }\n")
        } else {
            sb.append("" +
                    "  if (viewParent == null || !(viewParent instanceof android.view.ViewGroup)) {\n" +
                    "      throw new ParameterNotFoundException(\"Need a view parent or is not a ViewGroup\");\n" +
                    "  }\n")

            if (params.isNotEmpty()) {
                sb.append("" +
                        "  ((android.view.ViewGroup) viewParent).removeAllViews();\n" +
                        "  ((android.view.ViewGroup) viewParent).addView(" + typeElement.simpleName
                        + ".newInstance((android.content.Context) context, uuid" + this.parametersToString(params) + "));\n")
            } else {
                sb.append("" +
                        "  ((android.view.ViewGroup) viewParent).removeAllViews();\n" +
                        "  ((android.view.ViewGroup) viewParent).addView(" + typeElement.simpleName
                        + ".newInstance((android.content.Context) context, uuid));\n")
            }
        }

        messager?.printMessage(Diagnostic.Kind.NOTE, sb.toString())

        return FunSpec.builder("route")
                .addModifiers(KModifier.PUBLIC)
                .addAnnotation(Override::class.java)
                //.addException(ParameterNotFoundException::class.java)
                //.addException(NotEnoughParametersException::class.java)
                .returns(Void.TYPE)
                .addParameter("context", Any::class)
                .addParameter("uuid", UUID::class)
                .addParameter("parameters", Array<Any>::class)
                .addParameter("viewParent", Any::class)
                .addCode(sb.toString())
                .build()
    }

    private fun parametersToString(params: List<TypeMirror>): String {
        val sb = StringBuilder()

        for ((i, tm) in params.withIndex()) {
            sb.append(", (" + tm.toString() + ") parameters[" + i + "]")
        }

        return sb.toString()
    }

    private fun parametersToBundle(params: List<TypeMirror>): String {
        val sb = StringBuilder()

        for ((i, tm) in params.withIndex()) {

            val extra = "parameters[$i]);\n"
            val casting = "(" + tm.toString() + ") "

            when (tm.toString()) {
                "java.lang.Boolean" -> {
                    sb.append("  bundle.putBoolean(\"bool$i\", ")
                    sb.append(casting)
                    sb.append(extra)
                }
                "java.lang.Integer" -> {
                    sb.append("  bundle.putInt(\"int$i\", ")
                    sb.append(casting)
                    sb.append(extra)
                }
                "java.lang.Character" -> {
                    sb.append("  bundle.putChar(\"char$i\", ")
                    sb.append(casting)
                    sb.append(extra)
                }
                "java.lang.Float" -> {
                    sb.append("  bundle.putFloat(\"float$i\", ")
                    sb.append(casting)
                    sb.append(extra)
                }
                "java.lang.Double" -> {
                    sb.append("  bundle.putDouble(\"double$i\", ")
                    sb.append(casting)
                    sb.append(extra)
                }
                "java.lang.String" -> {
                    sb.append("  bundle.putString(\"string$i\", ")
                    sb.append(casting)
                    sb.append(extra)
                }
                else -> sb.append("  bundle.putParcelable(\"parcelable$i\", (android.os.Parcelable) $extra")
            }

            sb.append("\n")
        }

        return sb.toString()
    }

    private fun createRoute(typeElement: TypeElement, methods: ArrayList<FunSpec>): TypeSpec {
        messager?.printMessage(Diagnostic.Kind.NOTE, "Saving route file...")
        return TypeSpec.classBuilder("${typeElement.simpleName}Route")
                .addAnnotation(AnnotationSpec.builder(Generated::class.java)
                        .addMember("value", "\$S", this.javaClass.name)
                        .build())
                .addModifiers(KModifier.PUBLIC)
                .addSuperinterface(org.firezenk.kartographer.processor.interfaces.Routable::class.java)
                .addFunctions(methods)
                .build()
    }

    private fun getParameters(annotation: RoutableActivity): List<TypeMirror>? {
        try {
            annotation.params // TODO get forResult
        } catch (e: MirroredTypesException) {
            return e.typeMirrors as List<TypeMirror>
        }

        return null
    }

    private fun getParameters(annotation: RoutableView): List<TypeMirror>? {
        try {
            annotation.params // TODO get forResult
        } catch (e: MirroredTypesException) {
            return e.typeMirrors as List<TypeMirror>
        }

        return null
    }

    private fun getForResult(annotation: RoutableActivity): Int {
        return annotation.requestCode
    }

    private fun getForResult(annotation: RoutableView): Int {
        return annotation.requestCode
    }
}