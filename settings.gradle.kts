pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HeartbreakHealer"
include(":app")
include(":core:common")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":feature:onboarding")
include(":feature:dashboard")
include(":feature:journal")
include(":feature:sos")
include(":feature:nocontact")
include(":feature:rebuild")
include(":feature:selfcare")
include(":feature:affirmations")
include(":feature:settings")
include(":feature:memories")
include(":feature:heartsshop")
