package com.example.taxifare.bluetooth

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

class Printer {

    companion object {
        private const val LINE_FEED = "\n"
        private const val ALIGN_LEFT = 0
        private const val ALIGN_CENTER = 1
        private const val ALIGN_RIGHT = 2
        private const val FONT_SIZE_SMALL = 0
        private const val FONT_SIZE_MEDIUM = 1
        private const val FONT_SIZE_LARGE = 2
        private const val BARCODE_TYPE_CODE_128 = 73

        private const val WIDTH_PIXELS = 384
        private const val FONT_SIZE_PIXELS_SMALL = 16
        private const val FONT_SIZE_PIXELS_MEDIUM = 20
        private const val FONT_SIZE_PIXELS_LARGE = 24
    }

    private val data = ByteArrayOutputStream()

    // Thêm dòng vào dữ liệu in
    fun addLine(text: String, align: Int = ALIGN_LEFT, fontSize: Int = FONT_SIZE_MEDIUM) {
        val paint = Paint().apply {
            textSize = when (fontSize) {
                FONT_SIZE_SMALL -> FONT_SIZE_PIXELS_SMALL.toFloat()
                FONT_SIZE_MEDIUM -> FONT_SIZE_PIXELS_MEDIUM.toFloat()
                FONT_SIZE_LARGE -> FONT_SIZE_PIXELS_LARGE.toFloat()
                else -> FONT_SIZE_PIXELS_MEDIUM.toFloat()
            }
            color = Color.BLACK
            isAntiAlias = true
        }

        val textWidth = paint.measureText(text)
        val x = when (align) {
            ALIGN_LEFT -> 0f
            ALIGN_CENTER -> (WIDTH_PIXELS - textWidth) / 2
            ALIGN_RIGHT -> WIDTH_PIXELS - textWidth
            else -> 0f
        }

        val y = data.size()
        data.write(0x1B)
        data.write(0x61)
        data.write(align)
        data.write(text.toByteArray())
        data.write(LINE_FEED.toByteArray())
    }

    // Thêm ảnh vào dữ liệu in
fun addImage(bitmap: Bitmap) {
    val width = bitmap.width
    val height = bitmap.height
    val aspectRatio = width.toFloat() / height.toFloat()

    val targetWidth = (aspectRatio * WIDTH_PIXELS).roundToInt()
    val targetHeight = (targetWidth / aspectRatio).roundToInt()

    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)

    val stream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

    data.write(0x1B)
    data.write(0x33)
    data.write(0x00)

    data.write(stream.toByteArray())

    data.write(0x1B)
    data.write(0x32)
}
	// Thêm mã vạch vào dữ liệu in
    fun addBarcode(barcodeData: String, barcodeType: Int = BARCODE_TYPE_CODE_128) {
        data.write(0x1D)
        data.write(0x6B)
        data.write(barcodeType)
        data.write(barcodeData.toByteArray())
        data.write(0x00)
    }

    // Lấy dữ liệu in
    fun getData(): ByteArray {
        return data.toByteArray()
    }

    // Xóa dữ liệu in
    fun clearData() {
        data.reset()
    }
}