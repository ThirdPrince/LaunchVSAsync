import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Bank {
    var _currentBalance = 0

    private val lock = Any()

    val mutex = Mutex()

    suspend fun receiveMoney() {
        delay(1000)
        _currentBalance += 1000
    }


    suspend fun spendMoney(amount: Int = 500) {
        mutex.withLock {
            if (_currentBalance >= amount) {
                delay(1000)
                _currentBalance -= amount
            }
        }



    }

    fun fibonacci(n: Int): Int {
        return if (n <= 1) {
            n
        } else {
            fibonacci(n - 1) + fibonacci(n - 2)
        }
    }
}