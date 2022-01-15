package application.service

import domain.repository.ContentRepository
import infrastracture.database.repository.ContentRepositoryImpl
import kotlinx.coroutines.delay

/**
 * コンテンツ生成クラス.
 */
class ContentGenerator(
    private val threadNum: Int,
    private val contentRepository: ContentRepository = ContentRepositoryImpl.createInstance()
) {
    /**
     * コルーチン内でコンテンツを繰り返し生成する.
     */
    suspend fun loopRegisterContent(count: Int) {
        for (i in 1..count) {
            val size = count.toString().length
            //count: 100, コルーチン番号: 1, ループ番号: 1 -> id: 01001
            val id = "%02d".format(threadNum) + "%0${size}d".format(i)
            contentRepository.randomRegister(id)
            delay(5 * 1000L)
        }
    }
}