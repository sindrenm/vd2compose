package parser

import java.io.InputStream
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory
import models.VectorDrawable
import models.VectorDrawablePath
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

object XmlParser {
    private val saxParser: SAXParser = SAXParserFactory.newDefaultInstance().newSAXParser()

    fun parse(xmlString: String): VectorDrawable {
        val inputStream: InputStream = xmlString.byteInputStream()
        val handler = VectorDrawableDefaultHandler()

        saxParser.parse(inputStream, handler)

        return handler.vectorDrawable
    }
}

private class VectorDrawableDefaultHandler : DefaultHandler() {
    val vectorDrawable: VectorDrawable
        get() = VectorDrawable(_vectorDrawable.paths)

    private val _vectorDrawable = MutableVectorDrawable()

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if (qName != "path") return
        if (attributes == null) return

        val pathData: String = requireNotNull(attributes.getValue("android:pathData")).normalize()
        val fillColor: String? = attributes.getValue("android:fillColor")
        val fillType: String? = attributes.getValue("android:fillType")
        val strokeColor: String? = attributes.getValue("android:strokeColor")
        val strokeWidth: String? = attributes.getValue("android:strokeWidth")

        val path = VectorDrawablePath(
            fillColor = fillColor,
            fillType = fillType,
            pathData = pathData,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
        )

        _vectorDrawable.paths += path
    }

    private fun String.normalize(): String = this
        .replace(" ", ",")
        .replace(Regex("(\\d)-")) { matchResult -> "${matchResult.groupValues[1]},-" }
}

private class MutableVectorDrawable {
    val paths: MutableList<VectorDrawablePath> = mutableListOf()
}
