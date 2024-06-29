import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) = runBlocking {

    val singleThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    val bank = Bank()
    bank.receiveMoney()
   val costTime =  measureTime {
        val jobs = List(10){
          launch(singleThread) {
                bank.spendMoney()
            }
        }
        jobs.forEach {
            it.join()
        }
    }
    println("costTime -->$costTime")
    println(bank._currentBalance)

}