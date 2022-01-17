package domain.repository

/**
 * タグのレコードを管理する.
 */
interface TagRepository {
    /**
     * タグidの一覧を取得.
     */
    fun getTags(): Set<String>

    /**
     * タグ一覧を取得.
     */
    fun getTagList(): Map<String, String>
}