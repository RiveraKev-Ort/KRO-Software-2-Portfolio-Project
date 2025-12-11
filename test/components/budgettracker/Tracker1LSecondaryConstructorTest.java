import components.budgettracker.test;
import components.budgettracker.BudgetTracker;
import components.budgettracker.BudgetTracker1L;

/**
 * Concrete subclass wiring AbstractBudgetTrackerSecondaryTest to
 * BudgetTracker1L.
 */
public final class Tracker1LSecondaryConstructorTest
        extends BudgetTrackerSecondaryTest {

    @Override
    protected BudgetTracker constructorTest() {
        BudgetTracker result = new BudgetTracker1L();
        return result;
    }
}
