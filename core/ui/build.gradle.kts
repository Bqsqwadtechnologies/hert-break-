import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import javax.imageio.ImageIO

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
android {
    namespace = "com.bqsqwad.heartbreakhealer.core.ui"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

val generateOfficialBrandAssets by tasks.registering {
    val logoSource = rootProject.file("branding/logo/logo.png")
    val generatedRes = layout.buildDirectory.dir("generated/res/officialBrand")
    inputs.file(logoSource)
    outputs.dir(generatedRes)
    doLast {
        require(logoSource.exists()) { "Missing official logo: branding/logo/logo.png" }
        val imageFile = generatedRes.get().file("drawable-nodpi/official_healed_heart.png").asFile
        imageFile.parentFile.mkdirs()
        // The uploaded .png file is JPEG-encoded; normalize it to a real PNG
        // before adding it to Android resources (AAPT2 validates the file data).
        val logoImage = ImageIO.read(logoSource)
            ?: error("Unable to decode official logo: ${logoSource.path}")
        check(ImageIO.write(logoImage, "png", imageFile)) {
            "No PNG writer available for official logo"
        }
    }
}
android.sourceSets.getByName("main").res.srcDir(layout.buildDirectory.dir("generated/res/officialBrand"))
tasks.named("preBuild").configure { dependsOn(generateOfficialBrandAssets) }

dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.play.services.ads)
}
