package api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import utils.Post
import utils.Todo
import utils.User
import utils.log

val githubApi by lazy {
    val retrofit = retrofit2.Retrofit.Builder()
        .client(OkHttpClient.Builder().addInterceptor(Interceptor {
            it.proceed(it.request()).apply {
                log("request: ${code()}")
            }
        }).build())
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(GitHubApi::class.java)
}

interface GitHubApi {
    @GET("todos/{id}")
    suspend fun getTodo(@Path(value = "id") todoId: Int): Response<Todo>

    @GET("posts/1")
    suspend fun getPost(): Response<Post>

    @GET("posts/{id}")
    suspend fun getPostById(@Path(value = "id") id: Int): Response<Post>

    @GET("posts/")
    suspend fun getUserById(@Query(value = "userId") userId: Int): Response<List<Post>>
    @GET("posts/")
    suspend fun getUserSortId(@Query(value = "userId") userId: Int,@Query(value = "_sort") sort: String,@Query(value = "_order") order: String): Response<List<Post>>

    @GET("posts/")
    suspend fun getUserByOptions(@Query(value = "userId") userId: Int,@QueryMap option: Map<String,String>): Response<List<Post>>
}

fun main() = runBlocking {

    var result = githubApi.getTodo(1)
    if (result.isSuccessful) {
        log(result.body())
    }
    var result2 = githubApi.getPost()
    if (result2.isSuccessful) {
        log(result2.body())
    }
    result2 = githubApi.getPostById(2)
    if (result2.isSuccessful) {
        log(result2.body())
    }

    var result3 = githubApi.getUserById(2)
    if (result3.isSuccessful) {
        log(result3.body())
    }
    log("desc----------------------------------------------------------")
     result3 = githubApi.getUserSortId(2,"id","desc")
    if (result3.isSuccessful) {
        log(result3.body())
    }
    log("options----------------------------------------------------------")
    val map = HashMap<String,String>()
    map["_sort"] = "id"
    map["_order"] = "desc"
    result3 = githubApi.getUserByOptions(2,map)
    if (result3.isSuccessful) {
        log(result3.body())
    }

}