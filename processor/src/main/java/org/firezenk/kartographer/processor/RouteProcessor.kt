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

    companion object {
        val KAPT_KOTLIN_GENERATED_OPTION = "kapt.kotlin.generated"
    }

    override fun init(env: ProcessingEnvironment?) {
        super.init(env)
        this.filer = env?.filer
        this.messager = env?.messager
    }

    override fun getSupportedAnnotationTypes() = setOf(
            RoutableActivity::class.java.canonicalName,
            RoutableView::class.java.canonicalName
    )

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun process(annotations: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        val kotlinGenerated = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION]

        env.getElementsAnnotatedWith(RoutableActivity::class.java)
                .map { generateRoute(it as TypeElement) }
                .forEach {
                    try {
                        it.writeTo(File(kotlinGenerated))
                    } catch (e: Exception) {
                        messager?.printMessage(Diagnostic.Kind.ERROR, e.message)
                        e.printStackTrace()
                    }
                }

        env.getElementsAnnotatedWith(RoutableView::class.java)
                .map { generateRoute(it as TypeElement) }
                .forEach {
                    try {
                        it.writeTo(File(kotlinGenerated))
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
                .addType(myClass)
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
                "  if (parameters.size < " + params?.size + ") {\n" +
                "      throw org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException(\"Need ${params?.size} params\")\n" +
                "  }\n")

        for ((i, tm) in params!!.withIndex()) {
            sb.append("" +
                    "  if (!(parameters[$i] is ${typeMirrorToString(tm)})) {\n" +
                    "      throw org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException(\"Need ${tm}\")\n" +
                    "  }\n")
        }

        sb.append("\n")

        if (isActivity) {
            sb.append("  intent android.content.Intent = android.content.Intent((android.content.Context) context, " + typeElement.simpleName + ".class)\n")

            sb.append("  bundle android.os.Bundle = android.os.Bundle()\n\n")
            sb.append("  bundle.putString(\"uuid\", uuid.toString())\n")

            if (params.isNotEmpty()) {
                sb.append(this.parametersToBundle(params))
            }

            sb.append("  intent.putExtras(bundle)\n")

            sb.append("" +
                    "  if (context is android.app.Activity) {\n" +
                    "      ((android.app.Activity) context).startActivityForResult(intent, " + requestCode + ")\n" +
                    "  } else if (context is android.content.Context) {\n" +
                    "      ((android.content.Context) context).startActivity(intent)\n" +
                    "  }\n")
        } else {
            sb.append("" +
                    "  if (!(viewParent is android.view.ViewGroup)) {\n" +
                    "      throw org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException(\"Need a view parent or is not a ViewGroup\")\n" +
                    "  }\n")

            if (params.isNotEmpty()) {
                sb.append("" +
                        "  viewParent.removeAllViews()\n" +
                        "  viewParent.addView(" + typeElement.simpleName
                        + ".newInstance(context as android.content.Context, uuid" + this.parametersToString(params) + "))\n")
            } else {
                sb.append("" +
                        "  viewParent.removeAllViews()\n" +
                        "  viewParent.addView(" + typeElement.simpleName
                        + ".newInstance(context as android.content.Context, uuid))\n")
            }
        }

        messager?.printMessage(Diagnostic.Kind.NOTE, sb.toString())

        return FunSpec.builder("route")
                .addModifiers(KModifier.PUBLIC)
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("context", Any::class)
                .addParameter("uuid", UUID::class)
                .addParameter(ParameterSpec.builder("parameters", ParameterizedTypeName.get(ARRAY, ANY)).build())
                .addParameter(ParameterSpec.builder("viewParent", ANY.asNullable()).build())
                .addCode(sb.toString())
                .returns(Void.TYPE)
                .build()
    }

    private fun typeMirrorToString(typeMirror: TypeMirror): String {
        return when (typeMirror.toString()) {
            "java.lang.Boolean" -> "kotlin.Boolean"
            "java.lang.Integer" -> "kotlin.Int"
            "java.lang.Character" -> "kotlin.Char"
            "java.lang.Float" -> "kotlin.Float"
            "java.lang.Double" -> "kotlin.Double"
            "java.lang.String" -> "kotlin.String"
            else -> typeMirror.asTypeName().toString()
        }
    }

    private fun parametersToString(params: List<TypeMirror>): String {
        val sb = StringBuilder()

        for ((i, tm) in params.withIndex()) {
            sb.append(", parameters[$i] as ${typeMirrorToString(tm)}")
        }

        return sb.toString()
    }

    private fun parametersToBundle(params: List<TypeMirror>): String {
        val sb = StringBuilder()

        for ((i, tm) in params.withIndex()) {

            val extra = "parameters[$i] as"
            val casting = "$tm)\n"

            when (tm.toString()) {
                "java.lang.Boolean" -> {
                    sb.append("  bundle.putBoolean(\"bool$i\", ")
                    sb.append(extra)
                    sb.append(casting)
                }
                "java.lang.Integer" -> {
                    sb.append("  bundle.putInt(\"int$i\", ")
                    sb.append(extra)
                    sb.append(casting)
                }
                "java.lang.Character" -> {
                    sb.append("  bundle.putChar(\"char$i\", ")
                    sb.append(extra)
                    sb.append(casting)
                }
                "java.lang.Float" -> {
                    sb.append("  bundle.putFloat(\"float$i\", ")
                    sb.append(extra)
                    sb.append(casting)
                }
                "java.lang.Double" -> {
                    sb.append("  bundle.putDouble(\"double$i\", ")
                    sb.append(extra)
                    sb.append(casting)
                }
                "java.lang.String" -> {
                    sb.append("  bundle.putString(\"string$i\", ")
                    sb.append(extra)
                    sb.append(casting)
                }
                else -> sb.append("  bundle.putParcelable(\"parcelable$i\", $extra as android.os.Parcelable")
            }

            sb.append("\n")
        }

        return sb.toString()
    }

    private fun createRoute(typeElement: TypeElement, methods: ArrayList<FunSpec>): TypeSpec {
        messager?.printMessage(Diagnostic.Kind.NOTE, "Saving route file...")

        return TypeSpec.classBuilder("${typeElement.simpleName}Route")
                .addAnnotation(AnnotationSpec.builder(Generated::class)
                        .addMember("value", "%S", this.javaClass.simpleName)
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