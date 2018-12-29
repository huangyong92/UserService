package user;

import org.junit.Before;
import org.springframework.test.context.TestContextManager;

public class SpringTest {

    private TestContextManager testContextManager;

    @Before
    public void setUp() throws Exception {
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }
}
