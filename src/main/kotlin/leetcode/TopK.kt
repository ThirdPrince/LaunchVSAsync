package leetcode

import utils.log
import java.util.PriorityQueue

fun main() {
     val intArray = intArrayOf(1,2,2,3,4,5,6,6,7,8,8,8,7,7,7)
     val k = 2
    log(topKFrequent(intArray,k).toString())

}

fun topKFrequent(nums:IntArray,k:Int):List<Int>{
    val frq = HashMap<Int,Int>()
    nums.forEach {
        frq[it] = (frq[it]?:0)+1
    }
    val pq = PriorityQueue<Pair<Int,Int>>(compareBy{it.second} )
    for ((num,f ) in frq) {
        pq.offer(num to f)
        if(pq.size > k)pq.poll()
    }
    return pq.map { it.first }
}