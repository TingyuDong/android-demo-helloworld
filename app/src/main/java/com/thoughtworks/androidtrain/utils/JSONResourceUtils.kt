package com.thoughtworks.androidtrain.utils

import android.content.res.Resources
import java.io.BufferedReader
import java.io.InputStream
import java.io.StringWriter
import java.io.Writer
import java.io.InputStreamReader

class JSONResourceUtils {
    fun jsonResourceReader(resources: Resources, id: Int): String {
        val resourceReader: InputStream = resources.openRawResource(id)
        val writer: Writer = StringWriter()
        val reader = BufferedReader(InputStreamReader(resourceReader, "UTF-8"))
        var line: String? = reader.readLine()
        while (line != null) {
            writer.write(line)
            line = reader.readLine()
        }
        resourceReader.close()
        return writer.toString()
    }
}