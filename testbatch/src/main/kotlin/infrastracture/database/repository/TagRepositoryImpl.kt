package infrastracture.database.repository

import domain.repository.TagRepository
import infrastracture.database.redis.RedisManager
import infrastracture.database.redis.RedisManagerImpl

/**
 * TagRepositoryの実装クラス.
 */
class TagRepositoryImpl private constructor() : TagRepository {
    init {
        //tag_listの登録がなければ登録する
        if (!redis.isExistKey(TAG_LIST)) {
            redis.setHashValue(TAG_LIST, TAG_MAP)
        }
        //tagsの登録がなければ登録する
        if (!redis.isExistKey(TAGS)) {
            redis.setSetValue(TAGS, TAG_MAP.keys)
        }
    }

    companion object {
        private val redis: RedisManager = RedisManagerImpl.createRedisManager()
        private var instance: TagRepository? = null
        private const val TAG_LIST = "tag_list"
        private const val TAGS = "tags"

        /**
         * タグ対応表.
         */
        val TAG_MAP: Map<String, String> = mapOf(
            "1" to "java",
            "2" to "kotlin",
            "3" to "groovy",
            "4" to "ruby",
            "5" to "go",
            "6" to "php",
            "7" to "javascript",
            "8" to "rust",
            "9" to "python",
            "10" to "C#"
        )

        /**
         * ファクトリメソッド
         */
        fun createInstance(): TagRepository = instance ?: synchronized(this) {
            instance ?: TagRepositoryImpl().also { instance = it }
        }
    }

    override fun getTags(): Set<String> {
        return redis.getSetValue(TAGS)
    }

    override fun getTagList(): Map<String, String> {
        return redis.getHashAll(TAG_LIST)
    }
}