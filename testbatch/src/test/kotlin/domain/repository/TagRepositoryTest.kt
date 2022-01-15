package domain.repository

import infrastracture.database.repository.TagRepositoryImpl
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class TagRepositoryTest : AnnotationSpec() {
    @Test
    fun createInstance() {
        val tagRepository = TagRepositoryImpl.createInstance()
        tagRepository shouldNotBe null
        tagRepository.getTags() shouldBe TagRepositoryImpl.TAG_MAP.keys
        tagRepository.getTagList() shouldBe TagRepositoryImpl.TAG_MAP
    }
}