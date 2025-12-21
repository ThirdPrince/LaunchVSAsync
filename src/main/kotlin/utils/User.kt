package utils

/**
 * User
 * @author dhl
 * @date 2023 0810
 */
data class User(val userId:Int,val userName:String,val avatar:String="avatar", var file:String)
data class Todo(
    val id: Int = 0,
    val title: String = "",
    val completed: Boolean = false
)

data class Post(
    val userId :Int = 0,
    val id: Int = 0,
    val title: String = "",
    val body: String = ""
)