package com.example.kopelanga.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.kopelanga.data.NoteBlock
import java.io.File
import java.io.FileOutputStream

// --- Export Functions ---

fun exportToPdf(context: Context, title: String, blocks: List<NoteBlock>) {
    try {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        var yPosition = 50f
        val xPosition = 40f

        // Title
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText(title.ifEmpty { "Untitled Note" }, xPosition, yPosition, paint)
        yPosition += 40f

        // Content
        blocks.forEachIndexed { index, block ->
            paint.textSize = block.fontSize.toFloat()
            paint.isFakeBoldText = block.isBold
            paint.isUnderlineText = block.isUnderline
            if (block.isItalic) {
                 paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            } else {
                 paint.typeface = Typeface.DEFAULT
            }

            val indent = block.indentLevel * 20f
            val prefix = getListPrefix(block.type, index, blocks)
            val textToDraw = if (prefix.isNotEmpty()) "$prefix ${block.text}" else block.text
            
            // Alignment (simple implementation for PDF)
            val drawX = when (block.alignment) {
                "Center" -> (pageInfo.pageWidth / 2f) - (paint.measureText(textToDraw) / 2f)
                "Right" -> pageInfo.pageWidth - xPosition - paint.measureText(textToDraw)
                else -> xPosition + indent
            }

            canvas.drawText(textToDraw, drawX, yPosition, paint)
            yPosition += paint.textSize + 10f
        }

        pdfDocument.finishPage(page)

        val fileName = "Note_${System.currentTimeMillis()}.pdf"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        shareFile(context, file, "application/pdf")

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error exporting PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun exportToCsv(context: Context, title: String, blocks: List<NoteBlock>) {
    try {
        val fileName = "Note_${System.currentTimeMillis()}.csv"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        val csvContent = StringBuilder()
        csvContent.append("Type,Text,Font,Size,Bold,Italic,Underline,Align,IndentLevel\n")
        
        blocks.forEach { block ->
            // Simple CSV escaping
            val safeText = block.text.replace("\"", "\"\"")
            csvContent.append("${block.type},\"$safeText\",${block.fontFamily},${block.fontSize},${block.isBold},${block.isItalic},${block.isUnderline},${block.alignment},${block.indentLevel}\n")
        }

        FileOutputStream(file).use {
            it.write(csvContent.toString().toByteArray())
        }

        shareFile(context, file, "text/csv")

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error exporting CSV", Toast.LENGTH_SHORT).show()
    }
}

fun exportToTxt(context: Context, title: String, blocks: List<NoteBlock>) {
    try {
        val fileName = "Note_${System.currentTimeMillis()}.txt"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        val txtContent = StringBuilder()
        txtContent.append(title).append("\n\n")
        
        blocks.forEachIndexed { index, block ->
            val prefix = getListPrefix(block.type, index, blocks)
            val indent = "    ".repeat(block.indentLevel)
            txtContent.append("$indent$prefix ${block.text}").append("\n")
        }

        FileOutputStream(file).use {
            it.write(txtContent.toString().toByteArray())
        }

        shareFile(context, file, "text/plain")

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error exporting Text", Toast.LENGTH_SHORT).show()
    }
}

private fun shareFile(context: Context, file: File, mimeType: String) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mimeType
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share Note"))
}

fun getListPrefix(type: String, currentIndex: Int, allBlocks: List<NoteBlock>): String {
    if (type == "Body" || type.startsWith("Heading") || type == "Caption") return ""
    
    var count = 1
    // Count preceding items of the same type and indentation level
    for (i in currentIndex - 1 downTo 0) {
        val prev = allBlocks[i]
        if (prev.type == type && prev.indentLevel == allBlocks[currentIndex].indentLevel) {
            count++
        } else if (prev.indentLevel < allBlocks[currentIndex].indentLevel) {
            break // Stop if we hit a parent level item
        }
        // Continue if it's a nested item (higher indentation)
    }

    return when (type) {
        "Bullet" -> "•"
        "Numbered" -> "$count."
        "Roman" -> "${toRoman(count)}."
        else -> ""
    }
}

fun toRoman(number: Int): String {
    // Simple Roman numeral converter for list purposes (up to reasonable limits)
    val values = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
    val romanLiterals = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
    
    var num = number
    val roman = StringBuilder()
    
    for (i in values.indices) {
        while (num >= values[i]) {
            num -= values[i]
            roman.append(romanLiterals[i])
        }
    }
    return roman.toString().lowercase()
}
