package com.example.kopelanga.ui.screens

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.kopelanga.data.NoteBlock
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream

fun exportToTxt(context: Context, title: String, blocks: List<NoteBlock>) {
    val content = blocks.joinToString("\n") { it.text }
    try {
        val file = File(context.cacheDir, "$title.txt")
        file.writeText(content)

        val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Note as Text"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun exportToPdf(context: Context, title: String, blocks: List<NoteBlock>) {
    val pdfFile = File(context.cacheDir, "$title.pdf")
    try {
        val pdfWriter = PdfWriter(FileOutputStream(pdfFile))
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        document.add(Paragraph(title).setBold().setFontSize(20f))
        blocks.forEach { block ->
            document.add(Paragraph(block.text))
        }
        document.close()

        val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", pdfFile)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/pdf"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Note as PDF"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun exportToCsv(context: Context, title: String, blocks: List<NoteBlock>) {
    val content = blocks.joinToString("\n") { it.text }
    try {
        val file = File(context.cacheDir, "$title.csv")
        file.writeText(content)

        val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "text/csv"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Note as CSV"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
