package org.redundent.kotlin.xml

import org.junit.Test
import java.text.SimpleDateFormat

class sitemapTest : TestBase() {
	@Test
	fun basicTest() {
		val urlset = urlset {
			for (i in 1..3) {
				url("http://blog.redundent.org/post/$i")
			}
		}

		validate(urlset)
	}

	@Test
	fun allElements() {
		val urlset = urlset {
			url(
				"http://blog.redundent.org",
				Date(year = 2017, month = 10, dayOfMonth = 24),
				ChangeFreq.hourly,
				14.0
			)
		}
		validate(urlset)
	}

	@Test
	fun sitemapIndex() {
		val sitemapIndex = sitemapindex {
			sitemap("http://blog.redundent.org/sitemap1.xml", Date(year = 2017, month = 10, dayOfMonth = 24))
			sitemap("http://blog.redundent.org/sitemap2.xml", Date(year = 2016, month = 1, dayOfMonth = 1))
			sitemap("http://blog.redundent.org/sitemap3.xml")
		}

		validate(sitemapIndex)
	}
}
