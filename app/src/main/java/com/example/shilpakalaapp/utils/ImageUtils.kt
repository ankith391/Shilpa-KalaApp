package com.example.shilpakalaapp.utils

import android.content.Context
import android.graphics.*
import android.net.Uri
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

object ImageUtils {

    /**
     * Converts a URI to a mutable Bitmap.
     */
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap?.copy(Bitmap.Config.ARGB_8888, true)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Processes the bitmap by adding branding and product details.
     */
    suspend fun processImage(
        bitmap: Bitmap,
        productName: String,
        woodType: String,
        price: String
    ): Bitmap = withContext(Dispatchers.Default) {
        val width = bitmap.width
        val height = bitmap.height
        val canvas = Canvas(bitmap)

        // 1. Setup Paints
        val watermarkPaint = Paint().apply {
            color = "#8B4513".toColorInt() // Saddle Brown
            alpha = 200 // Semi-transparent
            textSize = (width * 0.04f).coerceAtLeast(40f)
            isFakeBoldText = true
            isAntiAlias = true
            setShadowLayer(5f, 2f, 2f, Color.BLACK)
        }

        val overlayPaint = Paint().apply {
            color = Color.BLACK
            alpha = 160 // Semi-transparent black
            isAntiAlias = true
        }

        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = (width * 0.035f).coerceAtLeast(35f)
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val pricePaint = Paint().apply {
            color = "#FFD700".toColorInt() // Gold color
            textSize = (width * 0.045f).coerceAtLeast(45f)
            isFakeBoldText = true
            isAntiAlias = true
        }

        // 2. Draw Watermark (Top-Right)
        val watermarkText = "Handmade in Karnataka"
        val textBounds = Rect()
        watermarkPaint.getTextBounds(watermarkText, 0, watermarkText.length, textBounds)
        val xWatermark = width - textBounds.width() - 50f
        val yWatermark = textBounds.height() + 50f
        canvas.drawText(watermarkText, xWatermark, yWatermark, watermarkPaint)

        // 3. Draw Price Tag Section (Bottom Overlay)
        val overlayHeight = height * 0.22f
        val rect = RectF(0f, height - overlayHeight, width.toFloat(), height.toFloat())
        canvas.drawRect(rect, overlayPaint)

        // 4. Draw Details inside the overlay
        val padding = 50f
        var currentY = height - overlayHeight + padding + textPaint.textSize

        canvas.drawText("Product: $productName", padding, currentY, textPaint)
        
        currentY += textPaint.textSize + 25f
        canvas.drawText("Wood: $woodType", padding, currentY, textPaint)

        currentY += pricePaint.textSize + 30f
        canvas.drawText("Price: ₹$price", padding, currentY, pricePaint)

        bitmap
    }

    /**
     * Saves the processed bitmap and returns the local file URI.
     */
    suspend fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? = withContext(Dispatchers.IO) {
        val fileName = "Branded_${System.currentTimeMillis()}.jpg"
        val file = File(context.getExternalFilesDir(null), fileName)
        
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            out.flush()
            out.close()
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
