import components.budgettracker;

/**
 * Customized JUnit test fixture for {@code BudgetTrackerSecondary}.
 */
public class BudgetTrackerOnMapSecondaryTest
        extends BudgetTrackerSecondaryTest {

    @Override
    protected final BudgetTracker constructor() {
        return new BudgetTrackerOnMap();
    }
}
