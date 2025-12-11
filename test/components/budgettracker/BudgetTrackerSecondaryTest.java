import components.budgettracker.BudgetTracker1L;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import components.map.Map;
import components.set.Set;

/**
 * Abstract base test fixture for secondary/enhanced behavior of BudgetTracker.
 */
public abstract class AbstractBudgetTrackerSecondaryTest {

    // ----- Test constants (avoid magic numbers) -----
    private static final long INCOME_1000_00 = 1_000_00L;
    private static final long INCOME_3000_00 = 3_000_00L;
    private static final long FOOD_100_00 = 100_00L;
    private static final long FOOD_50_00 = 50_00L;
    private static final long FOOD_LIMIT_400_00 = 400_00L;
    private static final long MISC_75_00 = 75_00L;

    private static final String CAT_FOOD = "Food";
    private static final String CAT_RENT = "Rent";
    private static final String CAT_UTIL = "Utilities";
    private static final String CAT_MISC = "Misc";

    private static final String DATE_2025_11_01 = "2025-11-01";
    private static final String DATE_2025_11_02 = "2025-11-02";
    private static final String DATE_2025_11_05 = "2025-11-05";
    private static final String DATE_2025_11_10 = "2025-11-10";
    private static final String DATE_2025_11_15 = "2025-11-15";
    private static final String ABSENT_DATE = "2025-11-99";

    /**
     * Default method for the enhanced implementation under test.
     *
     * @return a new BudgetTracker instance (e.g., BudgetTracker1L)
     */
    protected abstract BudgetTracker constructorTest();

    // ----- Derived / summary computations -----

    @Test
    public final void testLeftToBudget_nonNegativeFloor() {
        BudgetTracker bt = this.constructorTest();
        bt.setMonthlyIncome(INCOME_1000_00);
        bt.addExpense(CAT_FOOD, DATE_2025_11_01, 800_00L);
        bt.addExpense(CAT_FOOD, DATE_2025_11_02, 300_00L);

        assertEquals(0L, bt.leftToBudget());
    }

    @Test
    public final void testGetBudgetSummary_containsKeyFields() {
        BudgetTracker bt = this.constructorTest();
        bt.setMonthlyIncome(INCOME_3000_00);
        bt.setBudgetLimit(CAT_FOOD, FOOD_LIMIT_400_00);
        bt.addExpense(CAT_FOOD, DATE_2025_11_01, FOOD_100_00);

        String summary = bt.getBudgetSummary();

        assertTrue(summary.contains("Budget Summary"));
        assertTrue(summary.contains("Income: $3000.00"));
        assertTrue(summary.contains("Expenses: $100.00"));
        assertTrue(summary.contains("Left to Budget: $2900.00"));
        assertTrue(summary.contains(CAT_FOOD));
        assertTrue(summary.contains("limit = $400.00"));
    }

    @Test
    public final void testGetAllExpensesSummary_sortedNewestToOldest() {
        BudgetTracker bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, DATE_2025_11_01, FOOD_100_00);
        bt.addExpense(CAT_FOOD, DATE_2025_11_10, FOOD_50_00);
        bt.addExpense(CAT_RENT, DATE_2025_11_01, 1_200_00L);

        String all = bt.getAllExpensesSummary();

        assertTrue(all.startsWith("All Expenses"));
        assertTrue(all.contains(CAT_FOOD + ":\n"));
        assertTrue(all.contains(CAT_RENT + ":\n"));
        assertTrue(all.contains(DATE_2025_11_10 + ": $50.00"));
        assertTrue(all.contains(DATE_2025_11_01 + ": $100.00"));

        int idxNewest = all.indexOf(DATE_2025_11_10 + ": $50.00");
        int idxOldest = all.indexOf(DATE_2025_11_01 + ": $100.00");
        assertTrue("Newest date must precede oldest date",
                idxNewest != -1 && idxOldest != -1 && idxNewest < idxOldest);
    }

    // ----- Query helpers -----

    @Test
    public final void testGetMonthlyIncome_andGetCategories() {
        BudgetTracker bt = this.constructorTest();
        bt.setMonthlyIncome(INCOME_3000_00);
        bt.addExpense(CAT_FOOD, DATE_2025_11_01, FOOD_100_00);
        bt.setBudgetLimit(CAT_RENT, 1_200_00L);

        assertEquals(INCOME_3000_00, bt.getMonthlyIncome());

        Set<String> cats = bt.getCategories();
        assertTrue(cats.contains(CAT_FOOD));
        assertTrue(cats.contains(CAT_RENT));
    }

    @Test
    public final void testCategoryExpense_andDates_andAmount() {
        BudgetTracker bt = this.constructorTest();
        bt.addExpense(CAT_UTIL, DATE_2025_11_05, 180_00L);
        bt.addExpense(CAT_UTIL, DATE_2025_11_10, FOOD_50_00);

        assertEquals(230_00L, bt.getCategoryExpense(CAT_UTIL));

        Set<String> dates = bt.getExpenseDates(CAT_UTIL);
        assertTrue(dates.contains(DATE_2025_11_05));
        assertTrue(dates.contains(DATE_2025_11_10));

        assertEquals(180_00L, bt.getExpenseAmount(CAT_UTIL, DATE_2025_11_05));
        assertEquals(FOOD_50_00,
                bt.getExpenseAmount(CAT_UTIL, DATE_2025_11_10));
        assertEquals(0L, bt.getExpenseAmount(CAT_UTIL, ABSENT_DATE));
    }

    @Test
    public final void testGetBudgetLimit_noLimitReflectedInSummary() {
        BudgetTracker bt = this.constructorTest();
        bt.addExpense(CAT_MISC, DATE_2025_11_15, MISC_75_00);

        String summary = bt.getBudgetSummary();
        assertTrue(summary.contains(CAT_MISC));
        assertTrue(summary.contains("limit = (no limit)"));
    }

    // ----- Defensive checks -----

    @Test(expected = IllegalArgumentException.class)
    public final void testGetBudgetLimit_nullCategory_throws() {
        BudgetTracker bt = this.constructorTest();
        bt.getBudgetLimit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetExpenseAmount_nullCategory_throws() {
        BudgetTracker bt = this.constructorTest();
        bt.getExpenseAmount(null, DATE_2025_11_01);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetExpenseAmount_nullDate_throws() {
        BudgetTracker bt = this.constructorTest();
        bt.getExpenseAmount(CAT_FOOD, null);
    }
}
