package domain.repository

/**
 * コンテンツのレコードを管理する.
 */
interface ContentRepository {
    /**
     * ランダムなタグでコンテンツを登録する
     */
    fun randomRegister(key: String)

    /**
     * コンテンツを取得する
     */
    fun getContent(key: String): Map<String, String>

    /**
     * キーを全て取得する
     */
    fun getKeys(): Set<String>

    /**
     * 指定のキーでレコードを削除する
     */
    fun delete(key: String): Long

    /**
     * コンテンツレコードを全て削除する
     */
    fun deleteAll(): Long
}