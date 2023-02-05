// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.21" apply false
    id("org.sonarqube") version "3.5.0.2730"
    id("org.jetbrains.kotlin.jvm") version "1.7.21" apply false
    id("coverage")
}

sonarqube {
    properties {
        property("sonar.projectKey", "ddaeyeonkim_circleciSample")
        property("sonar.organization", "danielkim")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.junit.reportPaths",
            listOf(
                "**/build/test-results/testDebugUnitTest",
                "**/build/test-results/testStagingDebugUnitTest",
                "**/build/test-results/test",
            )
        )
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            listOf(
                "**/build/reports/coverage/test/debug/report.xml",
                "**/build/reports/coverage/test/staging/debug/report.xml",
                "**/build/reports/coverageReport/coverageReport.xml",
            )
        )
    }
}