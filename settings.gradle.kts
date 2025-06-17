@file:Suppress("UnstableApiUsage")

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
	repositories {
		mavenCentral()
		google()
	}
}

rootProject.name = "kotlin-xml-builder"

include(":kotlin-xml-dsl-generator")
include(":kotlin-xml-builder")
