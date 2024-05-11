package flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import utils.log


interface DomainRequest2 {
    suspend fun sendAsyncRequest2(): String
}
fun main() = runBlocking{

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
    return setActionSetting2()
}
suspend fun setActionSetting2():Boolean {
    return flowOf(::setActionFinal2)
        .onEach { delay(100) }
        .map { it.invoke() }
        .single()
}

suspend fun setActionFinal2(): Boolean {
    val res = ActionFuc2.sendAsyncRequest2()
    return res == "success"
}

object ActionFuc2 : DomainRequest2 {
    override suspend fun sendAsyncRequest2(): String = withContext(Dispatchers.IO) {
        delay(1000)
        return@withContext "success"
    }

}
