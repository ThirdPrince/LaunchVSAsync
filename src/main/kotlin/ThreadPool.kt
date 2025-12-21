import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

fun main() {
    val threadPool = ThreadPoolExecutor(64,64,1 ,TimeUnit.DAYS,ArrayBlockingQueue(34))
    for(i in (1..100)){
          val countDownLatch :CountDownLatch = CountDownLatch(34)
           for (j in (1..34)){
               threadPool.submit{
                   try {
                       Thread.sleep(300)

                       println("$i and $j"+"${Thread.currentThread().name}--end")
                   }catch (e:Exception){
                       e.printStackTrace()
                   }finally {
                       countDownLatch.countDown()
                   }

               }
           }
        try {
            countDownLatch.await()
            println("$i "+"${Thread.currentThread().name}-- 执行完成")
        }catch (e:InterruptedException){
            e.printStackTrace()
        }

    }
    println("end")

}