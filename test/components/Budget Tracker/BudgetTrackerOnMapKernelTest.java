
import components.budgettracker;

/**
 * Customized JUnit test fixture for {@code BudgetTrackerOnMap}.
 */
public class BudgetTrackerOnMapKernelTest extends BudgetTrackerKernelTest {
    @Override
    protected final BudgetTrackerKernel constructor() {
        return new BudgetTrackerOnMap();
    }
}
