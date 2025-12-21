package utils

fun main() {
    val thread = Thread()
    log(thread.state)
    thread.start()
    log(thread.state)
    test()
    log(thread.state)
}

fun test(){
    Thread.sleep(5000)
    log("-----")
}