package launch

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main()  = runBlocking{
    async  {
        log("------")
    }

    log("end")
}