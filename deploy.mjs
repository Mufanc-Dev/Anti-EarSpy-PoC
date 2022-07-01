/* configuration */
const apk_path = './app/build/outputs/apk/release/app-release-unsigned.apk'
const deploy_path = '/data/local/tmp/Anti-EarSpy-PoC.apk'
const entrance = 'anti.earspy.poc.MainKt'

/* build apk */
await $`rm ${apk_path}`.nothrow()
await $`./gradlew aRelease`

/* deploy to device */
await $`adb push ${apk_path} ${deploy_path}`

/* run program */
await $`adb shell app_process -Djava.class.path=${deploy_path} /system/bin ${entrance}`
