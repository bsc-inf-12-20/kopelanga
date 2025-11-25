package com.example.kopelanga.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.kopelanga.data.NoteBlock
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream

fun exportToPdf(context: Context, title: String, blocks: List<NoteBlock>) {
    val file = File(context.externalCacheDir, "$title.pdf")
    val writer = PdfWriter(file)
    val pdf = PdfDocument(writer)
    val document = Document(pdf)

    document.add(Paragraph(title).setBold().setFontSize(20f))

    blocks.forEach { block ->
        val p = Paragraph(block.text)
            .setFontSize(block.fontSize.toFloat())
        // TODO: Add more styling based on block properties
        document.add(p)
    }

    document.close()
    shareFile(context, file, "application/pdf")
}

fun exportToCsv(context: Context, title: String, blocks: List<NoteBlock>) {
    val csvContent = blocks.joinToString("\n") { it.text }
    val file = File(context.externalCacheDir, "$title.csv")
    FileOutputStream(file).use {
        it.write(csvContent.toByteArray())
    }
    shareFile(context, file, "text/csv")
}

fun exportToTxt(context: Context, title: String, blocks: List<NoteBlock>) {
    val txtContent = blocks.joinToString("\n") { it.text }
    val file = File(context.externalCacheDir, "$title.txt")
    FileOutputStream(file).use {
        it.write(txtContent.toByteArray())
    }
    shareFile(context, file, "text/plain")
}

private fun shareFile(context: Context, file: File, mimeType: String) {
    val uri: Uri = FileProvider.getUriForFile(context, "com.example.kopelanga.provider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mimeType
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}