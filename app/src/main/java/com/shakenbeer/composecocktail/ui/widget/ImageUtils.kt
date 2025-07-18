package com.shakenbeer.composecocktail.ui.widget

import android.content.Context
import android.util.Size
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

object ImageUtils {

  /**
   * Returns maximum bytes allowed per the appwidget limits for bitmaps in the remote views.
   */
  fun Context.getMaxWidgetMemoryAllowedSizeInBytes(): Int {
    val size = resources.displayMetrics.run { Size(widthPixels, heightPixels) }
    // Cap memory usage at 1.5 times the size of the display
    // 1.5 * 4 bytes/pixel * w * h ==> 6 * w * h
    // See https://cs.android.com/android/platform/superproject/+/master:frameworks/base/services/appwidget/java/com/android/server/appwidget/AppWidgetServiceImpl.java;l=274-281;drc=389cb6f54a5a5bb8dea540f57a3a8ac3c3c1c758
    return 6 * size.width * size.height
  }

  /**
   * Returns maximum possible size for each image in the widget when provided as bitmap.
   */
  fun getMaxPossibleImageSize(aspectRatio: Double, memoryLimitBytes: Int, maxImages: Int): Size {
    // for each orientation (landscape, portrait, +2 for fold).
    val limit = (memoryLimitBytes / 4) / maxImages
    val maxSizeAllowedPerPixel: Int = limit / BYTES_PER_PIXEL

    val side = sqrt(maxSizeAllowedPerPixel.toDouble()).toInt()
    val width = if (aspectRatio > 1) side else max(1, (side * aspectRatio).roundToInt())
    val height = if (aspectRatio > 1) max(1, (side / aspectRatio).roundToInt()) else side
    return Size(width, height)
  }

  private const val BYTES_PER_PIXEL = 4
}