import hu.akarnokd.kotlin.flow.replay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class FlowTests {
  private val testScope = TestCoroutineScope()

  @InternalCoroutinesApi
  @ExperimentalCoroutinesApi
  @Test
  fun sequentially() = testScope.runBlockingTest {
    val counter = AtomicInteger(0)
    val flow = flow {  emit(counter.incrementAndGet()) }.replay(1) { it }

    flow.collect { assertThat(it).isEqualTo(1) }
    flow.collect { assertThat(it).isEqualTo(1) }
  }
}
