package anti.earspy.poc.getevent

import android.annotation.SuppressLint
import android.view.View
import kotlin.concurrent.thread

@SuppressLint("UnsafeDynamicallyLoadedCode")
object GetEvent {
    init {
        System.load(
            "file:(.*)".toRegex().find(
                javaClass.classLoader!!.getResource("lib/arm64-v8a/libgetevent.so").path
            )!!.groupValues[1]
        )
    }

    interface OnEventListener {

    }

    fun setOnEventListener() {
        thread {

        }
    }

    external fun waitFor(): Unit
}