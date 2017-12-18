package org.firezenk.kartographer.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import org.firezenk.kartographer.annotations.RoutableActivity
import org.firezenk.kartographer.annotations.RoutableView
import org.firezenk.kartographer.annotations.RouteAnimation
import java.io.File
import java.util.*
import javax.annotation.Generated
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
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

        val methods = arrayListOf<FunSpec>(addRouteMethod(typeElement), addPathGetter(typeElement))

        if (!isActivity(typeElement)) {
            methods.add(addReplaceFun())
        }

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

        val isActivity = isActivity(typeElement)

        val requestCode = if (isActivity) {
            getForResult(typeElement.getAnnotation(RoutableActivity::class.java))
        } else {
            getForResult(typeElement.getAnnotation(RoutableView::class.java))
        }

        val sb = StringBuilder()

        if (isActivity) {
            sb.append("  val intent = android.content.Intent(context as android.content.Context, " + typeElement.simpleName + "::class.java)\n")
            sb.append("  intent.putExtras(parameters as android.os.Bundle)\n")

            sb.append("" +
                    "  if (context is android.app.Activity) {\n" +
                    "      context.startActivityForResult(intent, " + requestCode + ")\n" +
                    "  } else {\n" +
                    "      intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);\n" +
                    "      context.startActivity(intent)\n" +
                    "  }\n")
        } else {
            sb.append("" +
                    "  if (!(viewParent is android.view.ViewGroup)) {\n" +
                    "      throw org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException(\"Need a view parent or is not a ViewGroup\")\n" +
                    "  }\n")

            sb.append("" +
                    "  val next: android.view.View = ${typeElement.simpleName}(context as android.content.Context)\n" +
                    "  val prev: android.view.View? = viewParent.getChildAt(viewParent.childCount - 1)\n\n" +
                    "  animation?.let {\n" +
                    "    prev?.let {\n" +
                    "      animation.prepare(prev, next)\n" +
                    "      viewParent.addView(next)\n" +
                    "      animation.animate(prev, next, {viewParent.removeView(prev)})\n" +
                    "    } ?: replace(viewParent, next)\n" +
                    "  } ?: replace(viewParent, next)")
        }

        messager?.printMessage(Diagnostic.Kind.NOTE, sb.toString())

        return FunSpec.builder("route")
                .addModifiers(KModifier.PUBLIC)
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("context", ANY)
                .addParameter("uuid", UUID::class)
                .addParameter("parameters", ANY)
                .addParameter("viewParent", ANY.asNullable())
                .addParameter("animation", RouteAnimation::class.asClassName().asNullable())
                .addCode(sb.toString())
                .returns(Void.TYPE)
                .build()
    }

    private fun addPathGetter(typeElement: TypeElement): FunSpec {
        return FunSpec.builder("path")
                .addModifiers(KModifier.PUBLIC)
                .addModifiers(KModifier.OVERRIDE)
                .addCode("return ${typeElement.simpleName}Route.PATH\n")
                .returns(String::class)
                .build()
    }

    private fun addReplaceFun(): FunSpec {
        return FunSpec.builder("replace")
                .addModifiers(KModifier.PRIVATE)
                .addParameter("viewParent", ANY)
                .addParameter("next", ANY)
                .addCode("" +
                        "  (viewParent as android.view.ViewGroup).removeAllViews()\n" +
                        "  viewParent.addView(next as android.view.View)\n")
                .build()
    }

    private fun createRoute(typeElement: TypeElement, methods: ArrayList<FunSpec>): TypeSpec {
        messager?.printMessage(Diagnostic.Kind.NOTE, "Saving route file...")

        val path = if (isActivity(typeElement)) {
            getRoutePath(typeElement.getAnnotation(RoutableActivity::class.java))
        } else {
            getRoutePath(typeElement.getAnnotation(RoutableView::class.java))
        }

        return TypeSpec.classBuilder("${typeElement.simpleName}Route")
                .addAnnotation(AnnotationSpec.builder(Generated::class)
                        .addMember("value", "%S", this.javaClass.simpleName)
                        .build())
                .addModifiers(KModifier.PUBLIC)
                .addSuperinterface(org.firezenk.kartographer.processor.interfaces.Routable::class.java)
                .addFunctions(methods)
                .companionObject(TypeSpec
                        .companionObjectBuilder()
                        .addProperty(
                                PropertySpec.builder("PATH", String::class, KModifier.CONST)
                                        .initializer("\"$path\"")
                                        .build())
                        .build())
                .build()
    }

    private fun getForResult(annotation: RoutableActivity) = annotation.requestCode

    private fun getForResult(annotation: RoutableView) = annotation.requestCode

    private fun getRoutePath(annotation: RoutableActivity) = annotation.path

    private fun getRoutePath(annotation: RoutableView) = annotation.path

    private fun isActivity(typeElement: TypeElement) = typeElement.getAnnotation(RoutableActivity::class.java) != null
}