package infrastracture.database.redis

/**
 * redisライブラリのラッパー.
 */
interface RedisManager {
    /**
     * string型の値を取得する
     */
    fun getStringValue(key: String): String?

    /**
     * string型の値をセットする
     */
    fun setStringValue(key: String, value: String): String?

    /**
     * string型の値を複数取得する.
     */
    fun getMultiStringValue(vararg keys: String): List<String>

    /**
     * string型の値を複数セットする.
     */
    fun setMultiStringValue(map: Map<String, String>): String?

    /**
     * hash型のレコードの指定のfield値を取得する.
     */
    fun getHashValue(key: String, field: String): String?

    /**
     * hash型のレコードを登録する.
     */
    fun setHashValue(key: String, hash: Map<String, String>): Long

    /**
     * hash型のレコードのキーを全て取得する.
     */
    fun getHashKeys(key: String): Set<String>

    /**
     * hash型のレコードの値を全て取得する.
     */
    fun getHashVals(key: String): List<String>

    /**
     * hash型のレコードのkey: valueを全て取得する.
     */
    fun getHashAll(key: String): Map<String, String>

    /**
     * set型のレコードを取得する.
     */
    fun getSetValue(key: String): Set<String>

    /**
     * set型のレコードを登録する.
     */
    fun setSetValue(key: String, list: Set<String>): Long

    /**
     * set型のレコードを登録する.
     */
    fun setSetValue(key: String, value: String): Long

    /**
     * set型のレコードから値を除去する.
     */
    fun removeSetValue(key: String, vararg member: String): Long

    /**
     * list型のレコードから値を全て取得する.
     */
    fun getListAllValue(key: String): List<String>

    /**
     * list型のレコードから値を取得する.
     */
    fun getListValue(key: String, start: Long, end: Long): List<String>

    /**
     * list型に値を追加する.
     */
    fun setLeftListValue(key: String, vararg value: String): Long

    /**
     * list型に値を追加する.
     */
    fun setRightListValue(key: String, vararg value: String): Long

    /**
     * レコードを削除する.
     */
    fun delete(vararg keys: String): Long

    /**
     * キーを全て取得する.
     */
    fun getAllKeys(): Set<String>

    /**
     * 指定のパターンにマッチするキーを取得する.
     */
    fun getKeys(pattern: String): Set<String>

    /**
     * 指定のキーでレコードが存在するか.
     */
    fun isExistKey(key: String): Boolean

}