package testadmin.application.service

import infrastracture.database.repository.ContentRepositoryImpl
import infrastracture.database.repository.TagRepositoryImpl

/**
 * コンテンツの情報を扱うサービスクラス
 */
class ContentsService private constructor() {
    private val tagRepository = TagRepositoryImpl.createInstance()
    private val contentRepository = ContentRepositoryImpl.createInstance()

    companion object {
        /**
         * 1回に返すコンテンツの件数
         * 実際に取得できる件数は 値 + 1 件
         */
        private const val ONCE_CONTENTS_COUNT = 9L
        private var instance: ContentsService? = null

        /**
         * ファクトリメソッド
         */
        fun createInstance(): ContentsService = instance ?: synchronized(this) {
            instance ?: ContentsService().also { instance = it }
        }
    }

    /**
     * タグの一覧を件数とともに取得する
     * key: タグ名 value: 件数
     */
    fun getTags(): Map<String, Long> {
        //返却用のmap key: tag_id value: 件数
        val tagCountMap = tagRepository.getTagList().keys.associateWith { 0L }.toMutableMap()
        //コンテンツを順番に取得し集計する
        contentRepository.getKeys().forEach { key ->
            val tagId = contentRepository.getContentTagId(key)
            tagId?.let {
                tagCountMap[tagId]?.let {
                    tagCountMap[tagId] = tagCountMap[tagId]!! + 1
                }
            }
        }
        //tag_idをtag名に変える
        return tagCountMap.mapKeys { tagRepository.getTagById(it.key) ?: throw RuntimeException("タグ名が解決できませんでした. tag_id: $it.key") }
    }

    /**
     * 最新のコンテンツを取得する
     */
    fun getLatestContents(): List<Map<String, String>> {
        return contentRepository.getContents(0, ONCE_CONTENTS_COUNT)
    }
}