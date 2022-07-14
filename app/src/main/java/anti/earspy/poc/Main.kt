package anti.earspy.poc

import anti.earspy.poc.getevent.GetEvent
import anti.earspy.poc.screencap.Screencap

fun main(args: Array<String>) {
    try {
        println("package: ${BuildConfig.APPLICATION_ID}")
        println("args: ${args.contentToString()}")
        Screencap.capture()
    } catch (err: Throwable) {
        err.printStackTrace()
    }
}
