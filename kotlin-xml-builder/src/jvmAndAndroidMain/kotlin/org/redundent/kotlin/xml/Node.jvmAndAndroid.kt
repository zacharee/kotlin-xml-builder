package org.redundent.kotlin.xml

actual fun isReflectionAvailable(): Boolean {
	return Node::class.java.classLoader?.getResource("kotlin/reflect/full")?.also { println(it) } != null
}

actual fun Node.processAnnotations(): Map<String, Int>? {
	val xmlTypeAnnotation = this::class.annotations.firstOrNull { it is XmlType } as? XmlType ?: return null

	val childOrder = xmlTypeAnnotation.childOrder

	return childOrder.indices.associateBy { childOrder[it] }
}