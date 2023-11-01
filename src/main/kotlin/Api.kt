import kotlinx.coroutines.delay

/**
 * Test exception
 */
suspend fun getUserError(){
    delay(200)
   // log("finish")
    throw Exception("not permission to get User")
}