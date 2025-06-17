[![Download](https://maven-badges.herokuapp.com/maven-central/dev.zwander/xmlbuilder/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.zwander/xmlbuilder)

Kotlin XML Builder
=

This library can be used to build xml documents from Kotlin code.
It is based on the HTML builder described in the [Kotlin docs](https://kotlinlang.org/docs/reference/type-safe-builders.html)

It is designed to be lightweight and fast. There isn't any validation except to 
escape text to not violate xml standards.

License
-
Apache 2.0

Usage
=
To use in Gradle, simply add the Maven Central repository and then add the following dependency.
```gradle
repositories {
    mavenCentral()
}

dependencies {
    compile("dev.zwander.xmlbuilder:[VERSION]")
}
```

Similarly in Maven:
```xml
<dependencies>
    <dependency>
        <groupId>dev.zwander</groupId>
        <artifactId>xmlbuilder</artifactId>
        <version>[VERSION]</version>
    </dependency>
</dependencies>
```

Example
=
```kotlin
val people = xml("people") {
    xmlns = "http://example.com/people"
    "person" {
        attribute("id", 1)
        "firstName" {
            -"John"
        }
        "lastName" {
            -"Doe"
        }
        "phone" {
            -"555-555-5555"
        }
    }
}

val asString = people.toString()
```
produces
```xml
<people xmlns="http://example.com/people">
    <person id="1">
        <firstName>
            John
        </firstName>
        <lastName>
            Doe
        </lastName>
        <phone>
            555-555-5555
        </phone>
    </person>
</people>
```

```kotlin
class Person(val id: Long, val firstName: String, val lastName: String, val phone: String)

val listOfPeople = listOf(
    Person(1, "John", "Doe", "555-555-5555"),
    Person(2, "Jane", "Doe", "555-555-6666")
)

val people = xml("people") {
    xmlns = "http://example.com/people"
    for (person in listOfPeople) {
        "person" {
            attribute("id", person.id)
            "firstName" {
                -person.firstName
            }
            "lastName" {
                -person.lastName
            }
            "phone" {
                -person.phone
            }
        }
    }    
}

val asString = people.toString()
```
produces
```xml
<people xmlns="http://example.com/people">
    <person id="1">
        <firstName>
            John
        </firstName>
        <lastName
            >Doe
        </lastName>
        <phone>
            555-555-5555
        </phone>
    </person>
    <person id="2">
        <firstName>
            Jane
        </firstName>
        <lastName>
            Doe
        </lastName>
        <phone>
            555-555-6666
        </phone>
    </person>
</people>
```

### Namespaces
```xml
<t:root xmlns:t="https://ns.org">
    <t:element t:key="value"/>
</t:root>
```

```kotlin
val ns = Namespace("t", "https://ns.org")
xml("root", ns) {
    "element"(ns, Attribute("key", "value", ns))
}
```

You can also use the `Namespace("https://ns.org")` constructor to create a Namespace object that represents the default xmlns.

#### Things to be aware of

* All namespaces get added at render time. To retrieve a list of the current namespaces of a node, use the `namespaces` property.
* When a namespace is provided for a node or attribute, it will be declared on that element IF it is not already declared on the root element or one of the element's parents.

### Processing Instructions
You can add processing instructions to any element by using the `processingInstruction` method.

```kotlin
xml("root") {
    processingInstruction("instruction")
}
```

```xml
<root>
    <?instruction?>
</root>
```

#### Global Instructions
Similarly you can add a global (top-level) instruction by call `globalProcessingInstruction` on the
root node. This method only applies to the root. If it is called on any other element, it will be ignored.


```kotlin
xml("root") {
    globalProcessingInstruction("xml-stylesheet", "type" to "text/xsl", "href" to "style.xsl")
}
```

```xml
<?xml-stylesheet type="text/xsl" href="style.xsl"?>
<root/>
```

## DOCTYPE

You can specify a DTD (Document Type Declaration).

```kotlin
xml("root") {
    doctype(systemId = "mydtd.dtd")
}
```

```xml
<!DOCTYPE root SYSTEM "mydtd.dtd">
<root/>
```

### Limitations with DTD

Complex DTDs are not supported.

## Unsafe
You can use unsafe text for element and attribute values.
```kotlin
xml("root") {
	unsafeText("<xml/>")
}
```
produces
```xml
<root>
    <xml/>
</root>
```

```kotlin
xml("root") {
	attribute("attr", unsafe("&#123;"))
}
```
produces
```xml
<root attr="&amp;#123;"/>
```

## Print Options
You can now control how your xml will look when rendered by passing the new PrintOptions class as an argument to `toString`.

`pretty` - This is the default and will produce the xml you see above.

`singleLineTextElements` - This will render single text element nodes on a single line if `pretty` is true
```xml
<root>
    <element>value</element>
</root>
```
as opposed to:
```xml
<root>
    <element>
        value
    </element>
</root>
```
`useSelfClosingTags` - Use `<element/>` instead of `<element></element>` for empty tags

`useCharacterReference` - Use character references instead of escaped characters. i.e. `&#39;` instead of `&apos;`

## Reading XML
Check out the excellent [Ksoup](https://github.com/fleeksoft/ksoup/) library for multiplatform XML parsing.
