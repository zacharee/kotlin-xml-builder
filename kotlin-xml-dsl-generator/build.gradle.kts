plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	implementation(libs.kotlin.stdlib)
	implementation(libs.kotlin.reflect)
	implementation(libs.jaxb.xjc)

	testImplementation(project(":kotlin-xml-builder"))
	testImplementation(libs.junit)
	testImplementation(kotlin("test-junit", libs.versions.kotlin.get()))
}
