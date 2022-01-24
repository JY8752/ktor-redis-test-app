package testadmin.presentation.controller

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.thymeleaf.*
import testadmin.application.service.ContentsService
import java.time.LocalDateTime

/**
 * トップ画面に対応したController
 */
fun Route.topController() {
    val service = ContentsService.createInstance()
    get("/") {
        val tags = service.getTags().map { TagInfo(it.key, it.value) }
        val contents = service.getLatestContents().map { toContentInfo(it) }
        call.respond(ThymeleafContent("index", mapOf(
            "tags" to tags,
            "contents" to contents
        )))
    }
}

data class TagInfo(
    val name: String,
    val count: Long
)

data class ContentInfo(
    val title: String,
    val tag: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

private fun toContentInfo(content: Map<String, String>): ContentInfo {
    return ContentInfo(
        content["title"]!!,
        content["tag"]!!,
        LocalDateTime.parse(content["created_at"]),
        LocalDateTime.parse(content["updated_at"])
    )
}