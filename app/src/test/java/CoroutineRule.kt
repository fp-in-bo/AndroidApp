import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineRule(
    private val contextProvider: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description ?: Description.EMPTY)
        Dispatchers.setMain(contextProvider)
    }

    override fun finished(description: Description?) {
        super.finished(description ?: Description.EMPTY)
        contextProvider.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        kotlinx.coroutines.test.runBlockingTest(contextProvider, block)
}
