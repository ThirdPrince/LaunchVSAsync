package shareflow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.transform
import utils.log

suspend fun main() {
    val sharedFlow = MutableSharedFlow<String>(replay = 2)
    val job = GlobalScope.launch {


        sharedFlow.emit("emit 1")

        sharedFlow.collect {
            log(it)
        }
        delay(2000)
        //launch(Dispatchers.IO) {

      //  }

        log("end")
    }
    sharedFlow.tryEmit("emit 2")
    job.join()


}