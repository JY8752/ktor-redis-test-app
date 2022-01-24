package domain.repository

import infrastracture.database.repository.TagRepositoryImpl
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class TagRepositoryTest : AnnotationSpec() {
    private val tagRepository = TagRepositoryImpl.createInstance()

    @Test
    fun createInstance() {
        tagRepository shouldNotBe null
        tagRepository.getTags() shouldBe TagRepositoryImpl.TAG_MAP.keys
        tagRepository.getTagList() shouldBe TagRepositoryImpl.TAG_MAP
    }

    @Test
    fun getTagById() {
        TagRepositoryImpl.TAG_MAP.forEach { (key, value) ->
            tagRepository.getTagById(key) shouldBe value
        }
    }
}