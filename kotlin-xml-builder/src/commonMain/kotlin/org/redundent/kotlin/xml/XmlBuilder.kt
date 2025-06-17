package org.redundent.kotlin.xml

expect fun getLineSeparator(): String

internal fun getLineEnding(printOptions: PrintOptions) = if (printOptions.pretty) getLineSeparator() else ""

/**
 * Creates a new XML document with the specified root element name.
 *
 * @param root The root element name
 * @param encoding The encoding to use for the XML prolog
 * @param version The XML specification version to use for the xml prolog and attribute encoding
 * @param namespace Optional namespace object to use to build the name of the attribute. This will also add an `xmlns`
 * attribute for this value
 * @param init The block that defines the content of the XML
 */
fun xml(
	root: String,
	encoding: String? = null,
	version: XmlVersion? = null,
	namespace: Namespace? = null,
	init: (Node.() -> Unit)? = null
): Node {
	val node = Node(buildName(root, namespace))
	if (encoding != null) {
		node.encoding = encoding
	}

	if (version != null) {
		node.version = version
	}

	if (init != null) {
		node.init()
	}

	if (namespace != null) {
		node.namespace(namespace)
	}
	return node
}

/**
 * Creates a new XML document with the specified root element name.
 *
 * @param name The name of the element
 * @param init The block that defines the content of the XML
 */
fun node(name: String, namespace: Namespace? = null, init: (Node.() -> Unit)? = null): Node {
	val node = Node(buildName(name, namespace))
	if (init != null) {
		node.init()
	}
	return node
}
