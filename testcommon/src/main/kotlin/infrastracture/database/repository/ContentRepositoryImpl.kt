package infrastracture.database.repository

import domain.repository.ContentRepository
import domain.repository.TagRepository
import infrastracture.database.redis.RedisManager
import infrastracture.database.redis.RedisManagerImpl
import java.time.LocalDateTime

/**
 * ContentRepositoryの実装クラス.
 */
class ContentRepositoryImpl private constructor() : ContentRepository {
    companion object {
        private val redis: RedisManager = RedisManagerImpl.createRedisManager()
        private val tagRepository: TagRepository = TagRepositoryImpl.createInstance()
        private var instance: ContentRepository? = null

        /**
         * コンテンツのマスターリスト.新しい順
         */
        private const val CONTENTS = "contents"

        /**
         * コンテンツのタイトル.
         * field name
         */
        const val TITLE: String = "title"

        /**
         * コンテンツのタグ.
         * field name
         */
        const val TAG: String = "tag"

        /**
         * コンテンツの作成日時
         */
        const val CONTENTS_CREATED_AT = "created_at"

        /**
         * コンテンツの更新日時
         */
        const val CONTENTS_UPDATED_AT = "updated_at"

        /**
         * ファクトリメソッド
         */
        fun createInstance(): ContentRepository = instance ?: synchronized(this) {
            instance ?: ContentRepositoryImpl().also { instance = it }
        }
    }

    override fun randomRegister(key: String) {
        val tag = tagRepository.getTagList().keys.random()
        //コンテンツ登録
        redis.setHashValue(
            key, mapOf(
                TITLE to "title-$key",
                TAG to tag,
                CONTENTS_CREATED_AT to LocalDateTime.now().toString(),
                CONTENTS_UPDATED_AT to LocalDateTime.now().toString()
            )
        )
        //マスターテーブルにも登録
        redis.setLeftListValue(CONTENTS, key)
    }

    override fun getContent(key: String): Map<String, String> {
        return redis.getHashAll(key)
    }

    override fun getContents(start: Long, end: Long): List<Map<String, String>> {
        return redis.getListValue(CONTENTS, start, end).map { getContent(it) }
    }

    override fun getContentTagId(key: String): String? {
        return getContent(key)[TAG]
    }

    override fun getContentTagName(key: String): String? {
        val tagId = getContentTagId(key)
        return tagId?.let { tagRepository.getTagById(it) }
    }

    override fun getKeys(): Set<String> {
        return redis.getListAllValue(CONTENTS).toSet()
    }

    override fun delete(key: String): Long {
        if (redis.isExistKey(key)) {
            //指定の値を取り除いた新しいマスターlistを一旦削除して登録し直す
            //transaction張りたいけどここでjedis呼びたくない...
            val newList = redis.getListAllValue(CONTENTS).mapNotNull { if (it != key) it else null }
            redis.delete(CONTENTS)
            if (newList.isNotEmpty()) {
                redis.setLeftListValue(CONTENTS, *newList.toTypedArray())
            }

            //コンテンツ削除
            return redis.delete(key)
        }
        return 0
    }

    override fun deleteAll(): Long {
        val keys = getKeys().toTypedArray()
        if (keys.isNotEmpty()) {
            redis.delete(CONTENTS)
            return redis.delete(*keys)
        }
        return 0
    }
}