package com.example.kopelanga.data

import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class NoteBlock(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    var type: String = "Body", // Body, Heading 1, Heading 2, Caption, Bullet, Numbered, Roman
    var fontFamily: String = "Default", // Default, Serif, SansSerif, Monospace, Cursive
    var fontSize: Int = 16,
    var isBold: Boolean = false,
    var isItalic: Boolean = false,
    var isUnderline: Boolean = false,
    var alignment: String = "Left", // Left, Center, Right
    var textColor: Long = 0xFF000000, // Black
    var highlightColor: Long = 0x00000000, // Transparent
    var indentLevel: Int = 0 // For nested lists
)

object NoteSerializer {
    fun serialize(blocks: List<NoteBlock>): String {
        val jsonArray = JSONArray()
        blocks.forEach { block ->
            val obj = JSONObject()
            obj.put("id", block.id)
            obj.put("text", block.text)
            obj.put("type", block.type)
            obj.put("fontFamily", block.fontFamily)
            obj.put("fontSize", block.fontSize)
            obj.put("isBold", block.isBold)
            obj.put("isItalic", block.isItalic)
            obj.put("isUnderline", block.isUnderline)
            obj.put("alignment", block.alignment)
            obj.put("textColor", block.textColor)
            obj.put("highlightColor", block.highlightColor)
            obj.put("indentLevel", block.indentLevel)
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }

    fun deserialize(json: String): List<NoteBlock> {
        val blocks = mutableListOf<NoteBlock>()
        try {
            if (json.trim().startsWith("[")) {
                val jsonArray = JSONArray(json)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    blocks.add(
                        NoteBlock(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            text = obj.optString("text", ""),
                            type = obj.optString("type", "Body"),
                            fontFamily = obj.optString("fontFamily", "Default"),
                            fontSize = obj.optInt("fontSize", 16),
                            isBold = obj.optBoolean("isBold", false),
                            isItalic = obj.optBoolean("isItalic", false),
                            isUnderline = obj.optBoolean("isUnderline", false),
                            alignment = obj.optString("alignment", "Left"),
                            textColor = obj.optLong("textColor", 0xFF000000),
                            highlightColor = obj.optLong("highlightColor", 0x00000000),
                            indentLevel = obj.optInt("indentLevel", 0)
                        )
                    )
                }
            } else {
                // Legacy plain text
                if (json.isNotEmpty()) {
                    blocks.add(NoteBlock(text = json))
                } else {
                    blocks.add(NoteBlock())
                }
            }
        } catch (e: Exception) {
            // Fallback
            blocks.add(NoteBlock(text = json))
        }
        if (blocks.isEmpty()) {
            blocks.add(NoteBlock())
        }
        return blocks
    }
}
