import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.shadow)
}


tasks {
	val jar by getting(Jar::class) {
		manifest {
			attributes(mapOf("Main-Class" to "org.redundent.kotlin.xml.gen.DslGeneratorKt"))
		}
	}

	withType<ShadowJar> {
		archiveClassifier.set(null as? String)
	}

	register<Jar>("sourceJar") {
		from(sourceSets["main"].allSource)
		destinationDirectory.set(jar.destinationDirectory)
		archiveClassifier.set("sources")
	}
}

dependencies {
	implementation(libs.kotlin.stdlib)
	implementation(libs.kotlin.reflect)
	implementation(libs.jaxb.runtime)
	implementation(libs.jaxb.xjc)

	testImplementation(project(":kotlin-xml-builder"))
	testImplementation(libs.junit)
	testImplementation(kotlin("test-junit", libs.versions.kotlin.get()))
}
