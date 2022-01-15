import application.service.ContentGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import util.ProcessRepeater

/**
 * 起動スレッド数.
 */
const val THREAD_COUNT: Int = 5

/**
 * main関数.
 * 指定のスレッド数 x 指定のループカウントの数だけコンテンツを登録する
 */
fun main() {
    println("start batch.")

    runBlocking {
        val repeaterList = mutableListOf<ProcessRepeater>()
        //5スレッド起動
        for (i in 1..THREAD_COUNT) {
            //非同期処理
            launch {
                val repeater = ProcessRepeater(i) {
                    val contentGenerator = ContentGenerator(i)
                    //50回ループしてレコードを登録する
                    contentGenerator.loopRegisterContent(50)
                }
                repeaterList.add(repeater)
                repeater.start()
            }
        }

        //全てのスレッドが完了するまでループ
        var isAllFinished = false
        while (true) {
            isAllFinished = repeaterList.all { it.isEnd }
            if (isAllFinished) {
                break
            } else {
                //終わってないので10秒待機
                delay(10 * 1000L)
            }
        }
    }

    println("end batch.")
}