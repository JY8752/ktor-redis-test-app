package testadmin.application.service

import infrastracture.database.repository.ContentRepositoryImpl
import infrastracture.database.repository.TagRepositoryImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ContentServiceTest: FunSpec({
    val contentService = ContentsService.createInstance()
    val contentRepository = ContentRepositoryImpl.createInstance()
    val tagRepository = TagRepositoryImpl.createInstance()

    /**
     * コンテンツを指定の件数登録する
     */
    fun registerContents(count: Int) = repeat(count) {
        contentRepository.randomRegister("key-${it + 1}")
    }

    //テスト前後でDB初期化
    beforeTest { contentRepository.deleteAll() }
    afterTest { contentRepository.deleteAll() }

    test("タグの一覧を取得できること") {
        //コンテンツ100件登録
        registerContents(100)

        val tags = contentService.getTags()

        tags.size shouldBe 10
        tags.values.sumOf { it.toInt() } shouldBe 100
        tags.keys shouldContainAll tagRepository.getTagList().values

        tags.map { println("key: ${it.key} value: ${it.value}") }
    }
    test("最新コンテンツを10件取得できること") {
        //コンテンツ100件登録
        registerContents(100)

        val contents = contentService.getLatestContents()
        var counter = 100

        contents.size shouldBe 10
        contents.forEach { content ->
            content["title"] shouldBe "title-key-$counter"
            content["tag"] shouldNotBe null
            content["created_at"] shouldNotBe null
            content["updated_at"] shouldNotBe null
            counter--
        }
    }
})