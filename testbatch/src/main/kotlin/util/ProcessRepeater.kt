package util

/**
 * コルーチン内で指定された処理を開始する
 */
class ProcessRepeater(
    private val number: Int,
    private val repeatFunction: suspend () -> Unit
) {
    /**
     * 処理が終わったかどうか.
     */
    var isEnd: Boolean = false

    /**
     * 指定された処理を開始する.
     */
    suspend fun start() {
        println("No.$number coroutine start")
        repeatFunction()
        println("No.$number coroutine end")
        isEnd = true
    }
}