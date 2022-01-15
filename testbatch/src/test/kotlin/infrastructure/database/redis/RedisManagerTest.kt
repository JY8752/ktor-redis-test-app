package infrastructure.database.redis

import infrastracture.database.redis.RedisManagerImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import redis.clients.jedis.exceptions.JedisDataException

class RedisManagerTest : StringSpec() {
    private val redis = RedisManagerImpl.createRedisManager()

    /**
     * DBレコード全削除
     */
    private fun cleanDB() {
        val keys = redis.getAllKeys().toTypedArray()
        redis.delete(*keys)
    }

    init {
        //テストの前後でDB初期化
        beforeTest { cleanDB() }
        afterTest { cleanDB() }

        "1件のレコードの登録取得ができること" {
            val key = "key"
            val value = "value"
            redis.setStringValue(key, value)
            redis.getStringValue(key) shouldBe value
        }
        "レコードがまだ存在していない" {
            redis.getStringValue("key") shouldBe null
        }
        "レコードの値を上書きできること" {
            val key = "key"
            val value = "value1"
            val updateValue = "updateValue"

            redis.setStringValue(key, value)
            redis.setStringValue(key, updateValue)

            redis.getStringValue(key) shouldBe updateValue
        }
        "複数レコードの登録取得ができること" {
            val recordMap = mapOf(
                "key1" to "value1",
                "key2" to "value2"
            )
            redis.setMultiStringValue(recordMap)
            redis.getMultiStringValue("key1", "key2") shouldBe listOf("value1", "value2")
        }
        "レコードがまだ存在していない:複数" {
            redis.getMultiStringValue("key", "key2") shouldBe listOf(null, null)
        }
        "複数レコードの登録で上書きできること" {
            val recordMap = mapOf(
                "key1" to "value1",
                "key2" to "value2"
            )
            val updateMap = mapOf(
                "key1" to "updateValue1",
                "key2" to "updateValue2",
            )
            redis.setMultiStringValue(recordMap)
            redis.setMultiStringValue(updateMap)
            redis.getMultiStringValue("key1", "key2") shouldBe listOf("updateValue1", "updateValue2")
        }
        "レコードが削除できること" {
            val key = "key"
            val value = "value"
            redis.setStringValue(key, value)
            redis.delete(key) shouldBe 1L
            redis.getStringValue(key) shouldBe null
        }
        "レコードが複数削除できること" {
            redis.setMultiStringValue(mapOf("key1" to "value", "key2" to "value"))
            redis.delete("key1", "key2") shouldBe 2L
            redis.getMultiStringValue("key1", "key2") shouldBe listOf(null, null)

        }
        "存在しないレコードを削除" {
            redis.delete("key") shouldBe 0L
        }
        "Hash型のレコードの登録取得ができること" {
            val key = "hashkey"
            val recordMap = mapOf(
                "field1" to "value1",
                "field2" to "value2"
            )
            val setResult = redis.setHashValue(key, recordMap)
            setResult shouldBe 2L

            redis.getHashValue(key, "field1") shouldBe "value1"
            redis.getHashValue(key, "field2") shouldBe "value2"
        }
        "Hash型: レコードがまだ存在しない" {
            val key = "key"
            redis.getHashValue(key, "field") shouldBe null
            redis.getHashKeys(key) shouldHaveSize 0
            redis.getHashVals(key) shouldHaveSize 0
            redis.getHashAll(key) shouldHaveSize 0
        }
        "Hash型のレコードで上書きできること" {
            val key = "hashkey"
            val recordMap = mapOf(
                "field1" to "value1",
                "field2" to "value2"
            )
            redis.setHashValue(key, recordMap)

            //fieldがなければ追加される
            val addField = mapOf(
                "field3" to "value3"
            )
            redis.setHashValue(key, addField)
            redis.getHashAll(key) shouldBe recordMap.plus(addField)

            //fieldがあれば上書きされる
            val updateMap = mapOf(
                "field3" to "updateValue"
            )
            redis.setHashValue(key, updateMap)
            redis.getHashAll(key) shouldBe recordMap.plus(updateMap)

        }
        "Hash型のレコードのkeyが全て取得できること" {
            val key = "hashkey"
            val recordMap = mapOf(
                "field1" to "value1",
                "field2" to "value2"
            )
            redis.setHashValue(key, recordMap)
            redis.getHashKeys(key) shouldBe recordMap.keys
        }
        "Hash型のレコードのvalueが全て取得できること" {
            val key = "hashkey"
            val recordMap = mapOf(
                "field1" to "value1",
                "field2" to "value2"
            )
            redis.setHashValue(key, recordMap)
            redis.getHashVals(key) shouldBe recordMap.values
        }
        "Hash型のレコードのkey,valueが全て取得できること" {
            val key = "hashkey"
            val recordMap = mapOf(
                "field1" to "value1",
                "field2" to "value2"
            )
            redis.setHashValue(key, recordMap)
            redis.getHashAll(key) shouldBe recordMap
        }
        "Set型:1件のレコードを登録取得ができること" {
            val key = "key"
            val set = setOf("member1", "member2")

            redis.setSetValue(key, set)
            redis.getSetValue(key) shouldBe set
        }
        "Set型:既にレコードが存在している時値が追加されること" {
            val key = "key"
            val set = setOf("member1", "member2")
            redis.setSetValue(key, set)

            val addMember = "member3"
            redis.setSetValue(key, addMember)

            val addMembers = setOf("member4", "member5")
            redis.setSetValue(key, addMembers)

            redis.getSetValue(key) shouldBe set.plus(addMember).plus(addMembers)
        }
        "Set型:レコードが存在しない" {
            redis.getSetValue("key") shouldHaveSize 0
        }
        "Set型:要素を削除できること" {
            val key = "key"
            val set = setOf("member1", "member2")
            redis.setSetValue(key, set)
            redis.removeSetValue(key, "member1")
            redis.getSetValue(key) shouldBe setOf("member2")
        }
        "Set型:要素を複数削除できること" {
            val key = "key"
            val set = setOf("member1", "member2")
            redis.setSetValue(key, set)
            redis.removeSetValue(key, *set.toTypedArray())
            redis.getSetValue(key) shouldHaveSize 0
        }
        "Set型:存在しない要素を削除" {
            val key = "key"
            val set = setOf("member1", "member2")
            redis.setSetValue(key, set)
            redis.removeSetValue(key, "member3") shouldBe 0L
        }
        "Set型:存在しないレコードを削除" {
            redis.removeSetValue("key", "member") shouldBe 0L
        }
        "存在するkeyを取得できること" {
            //string型
            redis.setStringValue("key1", "value1")

            //hash型
            redis.setHashValue("key2", mapOf("field1" to "value1"))

            //set型
            redis.setSetValue("key3", "value1")

            redis.getAllKeys() shouldBe setOf("key1", "key2", "key3")
            redis.getKeys("key*") shouldBe setOf("key1", "key2", "key3")
            redis.getKeys("key1") shouldBe setOf("key1")
            redis.getKeys("*1") shouldBe setOf("key1")
        }
        "isExistKey" {
            //string型
            redis.setStringValue("key1", "value1")

            //hash型
            redis.setHashValue("key2", mapOf("field1" to "value1"))

            //set型
            redis.setSetValue("key3", "value1")

            redis.isExistKey("key1") shouldBe true
            redis.isExistKey("key2") shouldBe true
            redis.isExistKey("key3") shouldBe true
            redis.isExistKey("key4") shouldBe false
        }
        "型違いでkeyが既に使われていた場合にエラーとなること" {
            val key = "key"

            //string型
            redis.setStringValue(key, "value")

            //hash型
            shouldThrow<JedisDataException> { redis.setHashValue(key, mapOf("field" to "value")) }

            //set型
            shouldThrow<JedisDataException> { redis.setSetValue(key, "value") }
        }
    }
}