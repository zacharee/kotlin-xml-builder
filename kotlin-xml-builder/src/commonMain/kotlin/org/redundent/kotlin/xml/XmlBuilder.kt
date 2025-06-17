package org.redundent.kotlin.xml

import com.fleeksoft.io.Reader
import com.fleeksoft.ksoup.nodes.CDataNode
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.TextNode
import com.fleeksoft.ksoup.parser.Parser

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

fun parse(reader: Reader, baseUri: String = ""): Node =
	parse(Parser.xmlParser().parseInput(reader, baseUri))

fun parse(document: Document): Node {
	val root = document.root()

	val result = xml(root.tagName())

	copyAttributes(root, result)

	val children = root.childNodes
	(0 until children.size)
		.map(children::elementAt)
		.forEach { copy(it, result) }

	return result
}

private fun copy(source: com.fleeksoft.ksoup.nodes.Node, dest: Node) {
	when (source) {
		is Element -> {
			val cur = dest.element(source.nodeName())

			copyAttributes(source, cur)

			val children = source.childNodes
			(0 until children.size)
				.map(children::elementAt)
				.forEach { copy(it, cur) }
		}

		is CDataNode -> {
			dest.cdata(source.text())
		}

		is TextNode -> {
			dest.text(source.text().trim { it.isWhitespace() || it == '\r' || it == '\n' })
		}
	}
}

private fun copyAttributes(source: com.fleeksoft.ksoup.nodes.Node, dest: Node) {
	val attributes = source.attributes()
	if (attributes.size == 0) {
		return
	}

	(0 until attributes.size)
		.map(attributes::elementAt)
		.forEach {
			dest.namespace(it.localName(), it.value)
		}
}
