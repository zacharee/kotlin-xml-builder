package org.redundent.kotlin.xml

actual fun isReflectionAvailable(): Boolean {
	return false
}

actual fun Node.processAnnotations(): Map<String, Int>? {
	return null
}