package infrastracture.database.redis

import redis.clients.jedis.Jedis

/**
 * RedisManagerの実装クラス.
 * jedisモジュールをラップする.
 */
class RedisManagerImpl private constructor() : RedisManager {

    companion object {
        private const val REDIS_PORT = 6379
        private val jedis: Jedis = Jedis("localhost", REDIS_PORT)
        private var instance: RedisManager? = null

        /**
         * ファクトリメソッド
         */
        fun createRedisManager(): RedisManager = instance ?: synchronized(this) {
            instance ?: RedisManagerImpl().also { instance = it }
        }
    }

    override fun getStringValue(key: String): String? {
        return jedis.get(key)
    }

    override fun setStringValue(key: String, value: String): String? {
        return jedis.set(key, value)
    }

    override fun getMultiStringValue(vararg keys: String): List<String> {
        return jedis.mget(*keys)
    }

    override fun setMultiStringValue(map: Map<String, String>): String? {
        val list = mutableListOf<String>()
        map.mapNotNull {
            list.add(it.key)
            list.add(it.value)
        }
        return jedis.mset(*list.toTypedArray())
    }

    override fun getHashValue(key: String, field: String): String? {
        return jedis.hget(key, field)
    }

    override fun setHashValue(key: String, hash: Map<String, String>): Long {
        return jedis.hset(key, hash)
    }

    override fun getHashKeys(key: String): Set<String> {
        return jedis.hkeys(key)
    }

    override fun getHashVals(key: String): List<String> {
        return jedis.hvals(key)
    }

    override fun getHashAll(key: String): Map<String, String> {
        return jedis.hgetAll(key)
    }

    override fun getSetValue(key: String): Set<String> {
        return jedis.smembers(key)
    }

    override fun setSetValue(key: String, list: Set<String>): Long {
        return jedis.sadd(key, *list.toTypedArray())
    }

    override fun setSetValue(key: String, value: String): Long {
        return setSetValue(key, setOf(value))
    }

    override fun removeSetValue(key: String, vararg member: String): Long {
        return jedis.srem(key, *member)
    }

    override fun delete(vararg keys: String): Long {
        return if (keys.isNotEmpty()) jedis.del(*keys) else 0L
    }

    override fun getAllKeys(): Set<String> {
        return jedis.keys("*")
    }

    override fun getKeys(pattern: String): Set<String> {
        return jedis.keys(pattern)
    }

    override fun isExistKey(key: String): Boolean {
        return jedis.exists(key)
    }
}