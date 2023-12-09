pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {setUrl("https://jitpack.io")}
    }
}

rootProject.name = "AI TSG"
include(":app")
include(":core:repository")
include(":core:models")
include(":authentication")


include(":core:database")

include(":scanner")
include(":core:interfaces")
