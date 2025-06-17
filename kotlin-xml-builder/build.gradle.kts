import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.maven.publish)
}

val javaVersionEnum: JavaVersion = JavaVersion.VERSION_21

kotlin {
	jvmToolchain(javaVersionEnum.toString().toInt())

	androidTarget {
		compilations.all {
			compileTaskProvider.configure {
				compilerOptions {
					freeCompilerArgs.addAll("-opt-in=kotlin.RequiresOptIn", "-Xdont-warn-on-error-suppression")
					jvmTarget = JvmTarget.fromTarget(javaVersionEnum.toString())
				}
			}
		}
	}

	jvm {
		compilations.all {
			compileTaskProvider.configure {
				compilerOptions {
					jvmTarget = JvmTarget.fromTarget(javaVersionEnum.toString())
				}
			}
		}
	}

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64(),
		macosX64(),
		macosArm64(),
		tvosX64(),
		tvosArm64(),
		tvosSimulatorArm64(),
		watchosArm32(),
		watchosArm64(),
		watchosDeviceArm64(),
		watchosSimulatorArm64(),
	).forEach {
		it.binaries.framework {
			baseName = "XMLBuilder"
			isStatic = true
		}
	}

	@OptIn(ExperimentalWasmDsl::class)
	listOf(
		js(IR),
		wasmJs(),
	).forEach {
		it.outputModuleName.set("XMLBuilder")
		it.browser()
	}

	androidNativeArm32()
	androidNativeArm64()
	androidNativeX86()
	androidNativeX64()

	mingwX64()

	linuxArm64()
	linuxX64()

	targets.all {
		compilations.all {
			compileTaskProvider.configure {
				compilerOptions {
					freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xdont-warn-on-error-suppression")
				}
			}
		}
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.kotlin.reflect)
				implementation(libs.charset)
			}
		}

		val jvmAndAndroidMain by creating {
			dependsOn(commonMain)
		}

		val jvmMain by getting {
			dependsOn(jvmAndAndroidMain)
		}

		val jvmTest by getting {
			dependsOn(jvmMain)

			dependencies {
				implementation(libs.junit)
				implementation(kotlin("test-junit", libs.versions.kotlin.get()))
				implementation(libs.kotlin.reflect)
			}
		}

		val androidMain by getting {
			dependsOn(jvmAndAndroidMain)
		}

		val nativeMain by creating {
			dependsOn(commonMain)
		}

		val darwinMain by creating {
			dependsOn(nativeMain)
		}

		val iosMain by creating {
			dependsOn(darwinMain)
		}

		val iosX64Main by getting {
			dependsOn(iosMain)
		}

		val iosArm64Main by getting {
			dependsOn(iosMain)
		}

		val iosSimulatorArm64Main by getting {
			dependsOn(iosMain)
		}

		val macosMain by creating {
			dependsOn(darwinMain)
		}

		val macosArm64Main by getting {
			dependsOn(macosMain)
		}

		val macosX64Main by getting {
			dependsOn(macosMain)
		}

		val tvosMain by creating {
			dependsOn(darwinMain)
		}

		val tvosX64Main by getting {
			dependsOn(tvosMain)
		}

		val tvosArm64Main by getting {
			dependsOn(tvosMain)
		}

		val tvosSimulatorArm64Main by getting {
			dependsOn(tvosMain)
		}

		val watchosMain by creating {
			dependsOn(darwinMain)
		}

		val watchosArm32Main by getting {
			dependsOn(watchosMain)
		}

		val watchosArm64Main by getting {
			dependsOn(watchosMain)
		}

		val watchosDeviceArm64Main by getting {
			dependsOn(watchosMain)
		}

		val watchosSimulatorArm64Main by getting {
			dependsOn(watchosMain)
		}

		val jsMain by getting {
			dependsOn(commonMain)
		}

		val wasmJsMain by getting {
			dependsOn(commonMain)
		}

		val androidNativeMain by creating {
			dependsOn(nativeMain)
		}

		val androidNativeArm32Main by getting {
			dependsOn(androidNativeMain)
		}

		val androidNativeArm64Main by getting {
			dependsOn(androidNativeMain)
		}

		val androidNativeX86Main by getting {
			dependsOn(androidNativeMain)
		}

		val androidNativeX64Main by getting {
			dependsOn(androidNativeMain)
		}

		val mingwMain by creating {
			dependsOn(nativeMain)
		}

		val mingwX64Main by getting {
			dependsOn(mingwMain)
		}

		val linuxMain by creating {
			dependsOn(nativeMain)
		}

		val linuxArm64Main by getting {
			dependsOn(linuxMain)
		}

		val linuxX64Main by getting {
			dependsOn(linuxMain)
		}
	}
}

android {
	this.compileSdk = 35

	defaultConfig {
		this.minSdk = 21
	}

	namespace = "org.redundent.kotlin.xml"

	compileOptions {
		sourceCompatibility = javaVersionEnum
		targetCompatibility = javaVersionEnum
	}

	buildFeatures {
		aidl = true
	}

	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
	sourceSets["main"].res.srcDirs("src/androidMain/res")
}

tasks.withType<Copy> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
