package application.service

import infrastracture.database.repository.ContentRepositoryImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContentGeneratorTest : FunSpec({
    val contentRepository = ContentRepositoryImpl.createInstance()

    test("コンテンツを繰り返し生成できること") {
        //開始前にDB初期化
        contentRepository.deleteAll()

        val generator = ContentGenerator(1)
        println("start generate.")
        withContext(Dispatchers.Default) { generator.loopRegisterContent(5) }
        println("end generate.")
        contentRepository.getKeys() shouldHaveSize 5
    }
})