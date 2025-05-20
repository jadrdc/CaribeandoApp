import com.agusteam.caribeando.data.model.PaginationDTO

class PaginationManager<T> {
    private var currentPage = 0
    private var isLastPage = false
    private var isLoading = false
    private val _items = mutableListOf<T>()
    val items: List<T> get() = _items

    fun reset() {
        currentPage = 0
        isLastPage = false
        isLoading = false
        _items.clear()
    }

    fun canLoadMore(): Boolean {
        return !isLoading && !isLastPage
    }

    suspend fun loadNextPage(
        fetch: suspend (page: Int) -> PaginationDTO<T>
    ): Result<List<T>> {
        return try {
            println("CRUSEL $currentPage")
            if (isLoading || isLastPage) {
                return Result.success(items)
            }

            isLoading = true
            val response = fetch(currentPage)
            if (response.body.isNotEmpty()) {
                _items.addAll(response.body)
                currentPage += 1
                isLastPage = response.last
            } else {
                isLastPage = true
            }
            println("CRUSEL 1 $currentPage")

            isLoading = false
            Result.success(items)
        } catch (e: Exception) {
            isLoading = false
            Result.failure(e)
        }
    }
}