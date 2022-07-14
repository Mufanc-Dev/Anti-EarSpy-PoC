package anti.earspy.poc.screencap

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Rect
import android.hardware.display.IDisplayManager
import android.media.ImageReader
import android.os.Handler
import android.os.Looper
import android.os.ServiceManager
import android.view.Display
import android.view.SurfaceControlHidden
import java.io.File
import kotlin.system.exitProcess

object Screencap {

    fun capture() {
        val manager = IDisplayManager.Stub.asInterface(
            ServiceManager.getService(Context.DISPLAY_SERVICE)
        )
        val ( width, height ) = manager.getDisplayInfo(Display.DEFAULT_DISPLAY).let { info ->
            arrayOf(info.logicalWidth, info.logicalHeight)
        }
        println("width: $width, height: $height")
        val display = SurfaceControlHidden.createDisplay("screencap", true)

        @SuppressLint("WrongConstant")
        val reader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1)
        val surface = reader.surface

        SurfaceControlHidden.openTransaction()
        try {
            SurfaceControlHidden.setDisplaySurface(display, surface)
            Rect(0, 0, width, height).let {
                SurfaceControlHidden.setDisplayProjection(display, 0, it, it)
            }
            SurfaceControlHidden.setDisplayLayerStack(display, 0)
        } finally {
            SurfaceControlHidden.closeTransaction()
        }

        Looper.prepare()
        reader.setOnImageAvailableListener(
            { _reader->
                _reader.acquireLatestImage().use { image ->
                    val bitmap = image.planes[0].let { plane ->
                        Bitmap.createBitmap(
                            plane.rowStride / plane.pixelStride, height,
                            Bitmap.Config.ARGB_8888
                        ).apply {
                            copyPixelsFromBuffer(plane.buffer)
                        }
                    }
                    bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        File("/sdcard/Workshop/screencap.png").outputStream()
                    )
                    println(bitmap)
                    exitProcess(0)
                }
            },
            Handler(Looper.myLooper()!!)
        )
        Looper.loop()
    }
}
