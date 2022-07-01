package anti.earspy.poc

import anti.earspy.poc.getevent.GetEvent

fun main(args: Array<String>) {
    println("package: ${BuildConfig.APPLICATION_ID}")
    println("args: ${args.contentToString()}")
    while (true) {
        GetEvent.waitFor()
    }
}
