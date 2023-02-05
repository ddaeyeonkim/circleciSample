import com.android.build.gradle.TestedExtension
import org.gradle.configurationcache.extensions.capitalized

check(project == rootProject) { "The coverage plugin can only be applied at root project" }

apply(plugin = "jacoco")

val buildVariant = "stagingDebug"

val jacocoVersion = "0.8.8"
val taskName = "coverageReport"

val excludesPaths = listOf(
    "**/R.class",
    "**/BuildConfig.*",
    "**/BR.class",
    "**/*\$Lambda\$*.*",
    "**/*\$inlined\$*.*",
    // DataBinding
    "**/*BindingImpl.*",
    "**/*Binding.*",
    "**/DataBinderMapperImpl.*",
    "**/DataBinderMapperImpl\$*.*",
    "**/DataBindingTriggerClass.*",
    // Serializer
    "**/*\$serializer.*",
    // Dagger Hilt
    "**/aggregatedroot/codegen/*.*",
    "**/processedrootsentinel/codegen/*.*",
    "hilt_aggregated_deps/*.*",
    "**/Hilt*.*",
    "**/*Hilt*.*",
    "**/*_ComponentTreeDeps.*",
    // Dagger
    "**/Dagger*Component.class",
    "**/Dagger*Component\$*.*",
    "**/*Module.*",
    "**/*Module_Provide*.*",
    "**/*Builder.*",
    "**/*Binds.*",
    "**/*_Factory.*",
    "**/*_Factory*.*",
    // Glide
    "**/Glide*.*",
    "com/bumptech/glide/*.*",
    // Navigation
    "**/*Args.*",
    "**/*Args\$*.*",
    "**/*Directions.*",
    "**/*Directions\$*.*",
    // Generated Callback
    "**/generated/callback/*.*",
    // Room
    "**/*_Impl.*",
    "**/*_Impl$*.*",
)

extensions.configure<JacocoPluginExtension>("jacoco") {
    toolVersion = jacocoVersion
}

subprojects {
    apply(plugin = "jacoco")

    plugins.withId("java") {
        extensions.configure<JacocoPluginExtension>("jacoco") {
            toolVersion = jacocoVersion
        }

        afterEvaluate {
            tasks.create<JacocoReport>(taskName) {
                dependsOn("test")

                group = "reporting"
                description = "Generate Jacoco coverage reports."

                reports {
                    html.required.set(true)
                    xml.required.set(true)
                }

                val sourceDirs = "src/main/java"
                val classes = "$buildDir/classes/kotlin/main"
                val classDirs = fileTree(classes) {
                    setExcludes(excludesPaths)
                }

                sourceDirectories.setFrom(files(listOf(sourceDirs)))
                classDirectories.setFrom(files(listOf(classDirs)))
                executionData.setFrom(files(listOf("$buildDir/jacoco/test.exec")))
            }
        }
    }

    plugins.withId("com.android.base") {
        extensions.configure<JacocoPluginExtension>("jacoco") {
            toolVersion = jacocoVersion
        }

        val android = the<TestedExtension>()

        android.buildTypes["debug"].enableUnitTestCoverage = true

        // support for Robolectric tests
        android.testOptions.unitTests.all {
            plugins.withId("jacoco") {
                it.configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
        }

        afterEvaluate {
            // name: debugUnitTest, releaseUnitTest, stagingDebugUnitTest, stagingReleaseUnitTest, etc..
            val targetVariant = android.unitTestVariants
                .firstOrNull { it.name == "${buildVariant}UnitTest" }
                ?.name
                ?.let { buildVariant }
                ?: "debug"

            val task = "test${targetVariant.capitalized()}UnitTest"

            tasks.create<JacocoReport>(taskName) {
                dependsOn(task)
                finalizedBy("create${targetVariant.capitalized()}UnitTestCoverageReport")

                group = "reporting"
                description = "Generate Jacoco coverage reports for the $targetVariant build."

                reports {
                    html.required.set(true)
                    xml.required.set(true)
                }

                val sourceDirs = "src/main/java"
                val classes = "$buildDir/intermediates/classes/${targetVariant}"
                val classDirs = fileTree(classes) {
                    setExcludes(excludesPaths)
                }

                sourceDirectories.setFrom(files(listOf(sourceDirs)))
                classDirectories.setFrom(files(listOf(classDirs)))
                executionData.setFrom(files(listOf("$buildDir/jacoco/${task}.exec")))
            }
        }
    }
}