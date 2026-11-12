import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import javax.imageio.ImageIO

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.bqsqwad.heartbreakhealer"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.bqsqwad.heartbreakhealer"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["admobAppId"] = "ca-app-pub-2748789567113929~6610046816"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            manifestPlaceholders["admobAppId"] = "ca-app-pub-2748789567113929~6610046816"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

val generateOfficialBrandAssets by tasks.registering {
    val logoSource = rootProject.file("branding/logo/logo.png")
    val monoSource = rootProject.file("branding/logo/logo_mono.png")
    val generatedRes = layout.buildDirectory.dir("generated/res/officialBrand")
    inputs.files(logoSource, monoSource)
    outputs.dir(generatedRes)
    doLast {
        require(logoSource.exists()) { "Missing official logo: branding/logo/logo.png" }
        require(monoSource.exists()) { "Missing notification logo: branding/logo/logo_mono.png" }
        val logoOutput = generatedRes.get().file("drawable-nodpi/official_healed_heart.png").asFile
        val monoOutput = generatedRes.get().file("drawable-nodpi/official_healed_heart_mono.png").asFile
        logoOutput.parentFile.mkdirs()
        // The uploaded files carry a .png name but are JPEG-encoded.
        // Decode by content and write genuine PNG resources for AAPT2.
        val logoImage = ImageIO.read(logoSource)
            ?: error("Unable to decode official logo: ${logoSource.path}")
        val monoImage = ImageIO.read(monoSource)
            ?: error("Unable to decode monochrome logo: ${monoSource.path}")
        check(ImageIO.write(logoImage, "png", logoOutput)) {
            "No PNG writer available for official logo"
        }
        check(ImageIO.write(monoImage, "png", monoOutput)) {
            "No PNG writer available for monochrome logo"
        }
    }
}
android.sourceSets.getByName("main").res.srcDir(layout.buildDirectory.dir("generated/res/officialBrand"))
tasks.named("preBuild").configure { dependsOn(generateOfficialBrandAssets) }

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:journal"))
    implementation(project(":feature:sos"))
    implementation(project(":feature:nocontact"))
    implementation(project(":feature:rebuild"))
    implementation(project(":feature:selfcare"))
    implementation(project(":feature:affirmations"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:memories"))
    implementation(project(":feature:heartsshop"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.play.services.ads)
    implementation(libs.user.messaging.platform)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.biometric)
    implementation(libs.coil.compose)
    implementation(libs.play.review)
    implementation(libs.play.review.ktx)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)
    implementation(libs.work.runtime.ktx)
    debugImplementation(libs.androidx.ui.tooling)
    testImplementation(libs.junit)
}
