
import components.budgettracker.test;

import components.budgettracker.BudgetTracker1L;

/**
 * Concrete subclass wiring BudgetTracker1LKernelTest to
 * BudgetTracker1LKernelConstructorTest.
 */
public final class BudgetTracker1LKernelConstructorTest
        extends BudgetTracker1LKernelTest {

    @Override
    protected BudgetTracker1L constructorTest() {
        BudgetTracker1L result = new BudgetTracker1L();
        return result;
    }
}
