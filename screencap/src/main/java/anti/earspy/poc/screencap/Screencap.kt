package anti.earspy.poc.screencap

import android.graphics.Bitmap
import android.graphics.Rect
import android.media.MediaCodec
import android.media.MediaFormat
import android.os.IBinder
import android.view.Display
import android.view.Surface
import android.view.SurfaceControl
import android.view.SurfaceControlHidden
import android.view.SurfaceControlHidden.openTransaction
import java.io.FileDescriptor

class Screencap {

    /**
     * @references:
     * - https://codezjx.com/posts/scrcpy-source-code-analysis/
     * -
     */
    fun capture(): Bitmap? {
        // 首先通过 MediaCodec 创建了一个 H.264 类型的编码器
        val codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)

        // 通过反射 SurfaceControl 创建了一个虚拟显示（此处为链接）
        val display = SurfaceControlHidden.createDisplay("screencap", true)

        // ......

        codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        // 通过 createInputSurface() 获取编码器的输入 Surface
        // 后续将这块 Surface 的内容作为编码器的输入数据源
        val surface = codec.createInputSurface()
        codec.start()

        try {
            // 进行编码并发送数据
//             alive = encode(codec, fd);
            // do not call stop() on exception, it would trigger an IllegalStateException
            codec.stop()
        } catch (err: Throwable) {
            err.printStackTrace()
        }

        return null
    }

    // 通过反射 SurfaceControl 并调用一系列方法，初始化录屏相关的环境，并与 Surface 进行绑定
    private fun setDisplaySurface(display: IBinder, surface: Surface, deviceRect: Rect, displayRect: Rect) {
        SurfaceControlHidden.openTransaction()
        try {
            SurfaceControlHidden.setDisplaySurface(display, surface);
            SurfaceControlHidden.setDisplayProjection(display, 0, deviceRect, displayRect);
            SurfaceControlHidden.setDisplayLayerStack(display, 0);
        } catch (err: Throwable) {
            err.printStackTrace()
        } finally {
            SurfaceControlHidden.closeTransaction()
        }
    }

//    private fun encode(codec: MediaCodec, fd: FileDescriptor): Boolean {
//        // 通过 dequeueOutputBuffer() 从输出缓存队列中取出buffer
//        val outputBufferId = codec.dequeueOutputBuffer(bufferInfo, -1);
//
//        // 若 flag 为 BUFFER_FLAG_END_OF_STREAM 代表到了流的结尾处，这个时候该停止编码
//        // eof 将置为 true 并停止 while 循环
//        val eof = (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
//
//        try {
//            // ......
//            if (outputBufferId >= 0) {
//                // 根据出队的 buffer id 去获取指定的 ByteBuffer 对象
//                val codecBuffer = codec.getOutputBuffer(outputBufferId);
//                if (sendFrameMeta) {
//                    writeFrameMeta(fd, bufferInfo, codecBuffer.remaining());
//                }
//                // 此时 buffer 中的数据已经是编码后的 H.264 包数据，发送到展示端即可
//                IO.writeFully(fd, codecBuffer);
//            }
//        } finally {
//            if (outputBufferId >= 0) {
//                // 处理完编码后的数据后需要及时释放 buffer
//                codec.releaseOutputBuffer(outputBufferId, false);
//            }
//        }
//    }
}