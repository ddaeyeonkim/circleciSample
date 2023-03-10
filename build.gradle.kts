import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.21" apply false
    id("org.sonarqube") version "3.5.0.2730"
    id("org.jetbrains.kotlin.jvm") version "1.7.21" apply false
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

val excludesPaths = listOf(
    // DataBinding
    "*.databinding.*",
    "*.BR",
    "*.DataBinderMapperImpl",
    "*.DataBinderMapperImpl\$*",
    "*.DataBindingTriggerClass",
    // Dagger
    "dagger.hilt.internal.aggregatedroot.codegen.*",
    "hilt_aggregated_deps.*",
    "*ComposableSingletons*",
    "*_HiltModules*",
    "*Hilt_*",
    "*BuildConfig",
    "*.DaggerAppComponent",
    "*.DaggerAppComponent\$*",
    "*_Factory\$*",
    "*_Provide*\$*",
    // Glide
    "com.bumptech.glide.*",
    "*.Glide*",
    // Navigation
    "*Args",
    "*Args\$*",
    "*Directions",
    "*Directions\$*",
    // Generated Callback
    "*.generated.*",
    // Room
    "*_Impl",
    "*_Impl\$*"
)

val excludesAnnotations = listOf(
    "dagger.Module",
    "dagger.internal.DaggerGenerated",
    "javax.annotation.Generated",
    "com.bumptech.glide.annotation.GlideModule"
)

subprojects {
    apply(plugin = "kover")
    kover {
        filters {
            classes {
                excludes += excludesPaths
            }
            annotations {
                excludes += excludesAnnotations
            }
        }

        instrumentation {
            excludeTasks.addAll(
                listOf(
                    "testReleaseUnitTest",
                    "testProdDebugUnitTest",
                    "testProdReleaseUnitTest",
                    "testStagingReleaseUnitTest",
                    "testDevDebugUnitTest",
                    "testDevReleaseUnitTest"
                )
            )
        }
    }

    plugins.withId("com.android.base") {
        val android = the<TestedExtension>()
        afterEvaluate {
            sonar {
                androidVariant = if(android.hasVariant("stagingDebug")) "stagingDebug" else "debug"
            }
        }
    }
}

koverMerged {
    enable()
    filters {
        classes {
            excludes += excludesPaths
        }
        annotations {
            excludes += excludesAnnotations
        }
    }
}

val sonarToken: String = gradleLocalProperties(rootDir).getProperty("sonar.token")

sonar {
    properties {
        property("sonar.projectKey", "ddaeyeonkim_circleciSample")
        property("sonar.organization", "danielkim")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", sonarToken)
        property(
            "sonar.junit.reportPaths",
            listOf(
                "**/build/test-results/testDebugUnitTest",
                "**/build/test-results/testStagingDebugUnitTest",
                "**/build/test-results/test",
            )
        )
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.rootDir}/build/reports/kover/merged/xml/report.xml")
    }
}

fun TestedExtension.hasVariant(variant: String): Boolean {
    return unitTestVariants.firstOrNull { it.name == "${variant}UnitTest" }?.let { true } ?: false
}
