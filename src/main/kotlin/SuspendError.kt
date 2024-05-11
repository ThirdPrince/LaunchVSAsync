import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import utils.log
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



fun main() = runBlocking {
    val result = doActions("test")
    log(result)
}

suspend fun doActions(str:String):Boolean {
    when(str){
        "test"->{
           return performAction1()
        }
    }
    return false

}

suspend fun performAction1(): Boolean {
    return suspendCoroutine { continuation ->
        setActionSetting { isSuccess ->
            if (!isSuccess) {
                log("失败！")
            }
            continuation.resume(isSuccess)
        }

    }
}

fun setActionSetting(response: ((isSuccess: Boolean) -> Unit)?) {
    ioScope.launch {
        flowOf(::setActionFinal).onEach { delay(100) }
            .collectIndexed { _, value ->
                value {
                    log(value)
                    response?.invoke(it)
                }
            }
    }
}
fun setActionFinal(response: ((isSuccess: Boolean) -> Unit)) {
    ActionFuc.sendAsyncRequest { res ->
        if (res == "success") {
            response.invoke(true)
        } else {
            response.invoke(false)
        }
    }
}

object ActionFuc : DomainRequest {
    override fun sendAsyncRequest(callback: (bundle: String) -> Unit) {
        ioScope.launch {
            //  模拟耗时
            delay(1000)
            callback.invoke("success")
        }
    }

}
val ioScope = CoroutineScope(Dispatchers.IO)

interface DomainRequest {
    fun sendAsyncRequest(callback: (bundle: String) -> Unit)
}
