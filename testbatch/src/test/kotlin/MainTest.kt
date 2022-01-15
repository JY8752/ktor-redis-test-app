import infrastracture.database.repository.ContentRepositoryImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize

class MainTest : StringSpec({
    val contentRepository = ContentRepositoryImpl.createInstance()
    "main" {
        //実行前にDB初期化
        contentRepository.deleteAll()
        main()
        contentRepository.getKeys() shouldHaveSize 250
    }
})