import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.jvm)
}

group = "dev.sindrenm.compose-image-vectors"
version = "0.1.0"

repositories {
    google()
    mavenCentral()

    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb)

            packageName = "compose-image-vectors"
            packageVersion = "0.1.0"
        }
    }
}
