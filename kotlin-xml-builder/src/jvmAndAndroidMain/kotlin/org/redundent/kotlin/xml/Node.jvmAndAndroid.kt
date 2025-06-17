package org.redundent.kotlin.xml

actual fun isReflectionAvailable(): Boolean {
	return Node::class.java.classLoader?.getResource("kotlin/reflect/full") != null
}

actual fun processAnnotations(): Map<String, Int>? {
	val xmlTypeAnnotation = Node::class.annotations.firstOrNull { it is XmlType } as? XmlType ?: return null

	val childOrder = xmlTypeAnnotation.childOrder

	return childOrder.indices.associateBy { childOrder[it] }
}