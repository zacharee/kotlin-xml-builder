package org.redundent.kotlin.xml

data class Date(
	val year: Int,
	val month: Int,
	val dayOfMonth: Int,
)

const val DEFAULT_URLSET_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9"

class UrlSet internal constructor() : Node("urlset") {
	init {
		xmlns = DEFAULT_URLSET_NAMESPACE
	}

	fun url(
		loc: String,
		lastmod: Date? = null,
		changefreq: ChangeFreq? = null,
		priority: Double? = null
	) {
		"url" {
			"loc"(loc)

			lastmod?.let {
				"lastmod"(formatDate(it))
			}

			changefreq?.let {
				"changefreq"(it.name)
			}

			priority?.let {
				"priority"(it.toString())
			}
		}
	}
}

class Sitemapindex internal constructor() : Node("sitemapindex") {
	init {
		xmlns = DEFAULT_URLSET_NAMESPACE
	}

	fun sitemap(
		loc: String,
		lastmod: Date? = null
	) {
		"sitemap" {
			"loc"(loc)

			lastmod?.let {
				"lastmod"(formatDate(it))
			}
		}
	}
}

@Suppress("EnumEntryName", "ktlint:enum-entry-name-case")
enum class ChangeFreq {
	always,
	hourly,
	daily,
	weekly,
	monthly,
	yearly,
	never
}

private fun formatDate(date: Date): String {
	val year = "${date.year}"
	val month = "${date.month}".padStart(2, '0')
	val dayOfMonth = "${date.dayOfMonth}".padStart(2, '0')

	return "${year}-${month}-${dayOfMonth}"
}

fun urlset(init: UrlSet.() -> Unit) = UrlSet().apply(init)

fun sitemapindex(init: Sitemapindex.() -> Unit) = Sitemapindex().apply(init)
