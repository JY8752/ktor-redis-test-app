package domain.repository

import infrastracture.database.repository.ContentRepositoryImpl
import infrastracture.database.repository.TagRepositoryImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe

class ContentRepositoryTest : StringSpec() {
    private val contentRepository = ContentRepositoryImpl.createInstance()
    private val tagRepository = TagRepositoryImpl.createInstance()

    init {
        //テスト前後でDB初期化
        beforeTest { contentRepository.deleteAll() }
        afterTest { contentRepository.deleteAll() }

        "randomRegister" {
            val key = "key"
            contentRepository.randomRegister(key)

            val content = contentRepository.getContent(key)
            val keys = contentRepository.getKeys()

            content[ContentRepositoryImpl.TITLE] shouldBe "title-$key"
            content[ContentRepositoryImpl.TAG] shouldBeIn tagRepository.getTags()
            keys shouldBe setOf(key)
            println("content: $content keys: $keys")
        }
        "delete" {
            val key = "key"
            contentRepository.randomRegister(key)
            contentRepository.delete(key) shouldBe 1L

            contentRepository.getContent(key).isEmpty() shouldBe true
            contentRepository.getKeys().isEmpty() shouldBe true
        }
        "deleteAll" {
            repeat(2) { contentRepository.randomRegister("key$it") }
            contentRepository.deleteAll() shouldBe 2L

            contentRepository.getKeys().isEmpty() shouldBe true
        }
    }
}