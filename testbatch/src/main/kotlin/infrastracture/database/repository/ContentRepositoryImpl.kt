package infrastracture.database.repository

import domain.repository.ContentRepository
import domain.repository.TagRepository
import infrastracture.database.redis.RedisManager
import infrastracture.database.redis.RedisManagerImpl

/**
 * ContentRepositoryの実装クラス.
 */
class ContentRepositoryImpl private constructor() : ContentRepository {
    companion object {
        private val redis: RedisManager = RedisManagerImpl.createRedisManager()
        private val tagRepository: TagRepository = TagRepositoryImpl.createInstance()
        private var instance: ContentRepository? = null
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
                TAG to tag
            )
        )
        //マスターテーブルにも登録
        redis.setSetValue(CONTENTS, key)
    }

    override fun getContent(key: String): Map<String, String> {
        return redis.getHashAll(key)
    }

    override fun getKeys(): Set<String> {
        return redis.getSetValue(CONTENTS)
    }

    override fun delete(key: String): Long {
        if (redis.isExistKey(key)) {
            redis.removeSetValue(CONTENTS, key)
            return redis.delete(key)
        }
        return 0
    }

    override fun deleteAll(): Long {
        val keys = getKeys().toTypedArray()
        if (keys.isNotEmpty()) {
            redis.removeSetValue(CONTENTS, *keys)
            return redis.delete(*keys)
        }
        return 0
    }
}