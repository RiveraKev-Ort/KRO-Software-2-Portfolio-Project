
import components.budgettracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit test fixture for {@code BudgetTracker} secondary/standard methods and
 * object overrides. Includes state-preservation checks after inspections.
 */
public abstract class BudgetTrackerSecondaryTest {

    // Constants
    private static final long INCOME_50K = 50_000L;
    private static final long INCOME_100K = 100_000L;

    private static final long AMT_1_200 = 1_200L;
    private static final long AMT_1_800 = 1_800L;
    private static final long AMT_2_000 = 2_000L;
    private static final long AMT_12_000 = 12_000L;

    private static final String CAT_GROCERIES = "Groceries";
    private static final String CAT_ENTERTAINMENT = "Entertainment";
    private static final String CAT_COFFEE = "Coffee";

    private static final String DATE_1 = "2025-12-01";
    private static final String DATE_2 = "2025-12-05";
    private static final String DATE_3 = "2025-12-08";

    /**
     * Returns a new instance of the implementation under test with type
     * {@code BudgetTracker}.
     *
     * @return new tracker
     * @ensures constructor != null
     */
    protected abstract BudgetTracker constructor();

    // Helpers

    private void setIncome(BudgetTrackerKernel bt, long income) {
        bt.setMonthlyIncome(income);
    }

    private void add(BudgetTrackerKernel bt, String cat, String date,
            long amt) {
        bt.addExpense(cat, date, amt);
    }

    private void limit(BudgetTrackerKernel bt, String cat, long limit) {
        bt.setBudgetLimit(cat, limit);
    }

    /**
     * Builds a common scenario on the given tracker.
     *
     * @param bt
     *            tracker under construction (kernel view)
     * @updates bt
     * @requires bt != null
     * @ensures bt has income and sample expenses populated
     */
    private void buildGroceriesEntertainmentScenario(BudgetTrackerKernel bt) {
        this.setIncome(bt, INCOME_50K);
        this.add(bt, CAT_GROCERIES, DATE_1, AMT_1_200);
        this.add(bt, CAT_GROCERIES, DATE_3, AMT_1_800);
        this.add(bt, CAT_ENTERTAINMENT, DATE_2, AMT_2_000);
    }

    // Object overrides

    @Test
    public final void testToStringMatchesGetBudgetSummary() {
        BudgetTracker bt = this.constructor();
        this.buildGroceriesEntertainmentScenario(bt);

        String summary = bt.getBudgetSummary();
        String text = bt.toString();

        assertEquals(summary, text);
    }

    @Test
    public final void testEqualsSameContentDifferentInstances() {
        BudgetTracker bt1 = this.constructor();
        BudgetTracker bt2 = this.constructor();

        this.buildGroceriesEntertainmentScenario(bt1);
        this.buildGroceriesEntertainmentScenario(bt2);

        assertEquals(bt2, bt1);
        assertEquals(bt1.hashCode(), bt2.hashCode());
    }

    @Test
    public final void testEqualsDifferentContent() {
        BudgetTracker bt1 = this.constructor();
        BudgetTracker bt2 = this.constructor();

        this.setIncome(bt1, INCOME_50K);
        this.setIncome(bt2, INCOME_100K);

        assertTrue(!bt1.equals(bt2));
    }

    // Inspection methods with state-preservation checks

    @Test
    public final void testGetMonthlyIncomeDoesNotMutate() {
        BudgetTracker bt = this.constructor();
        BudgetTracker before = this.constructor();

        this.setIncome(bt, INCOME_50K);
        this.setIncome(before, INCOME_50K);

        long val = bt.getMonthlyIncome();
        assertEquals(INCOME_50K, val);
        assertEquals(before, bt); // state unchanged
    }

    @Test
    public final void testGetCategoriesContainsAndDoesNotMutate() {
        BudgetTracker bt = this.constructor();
        BudgetTracker before = this.constructor();

        this.buildGroceriesEntertainmentScenario(bt);
        this.buildGroceriesEntertainmentScenario(before);

        assertTrue(bt.getCategories().contains(CAT_GROCERIES));
        assertTrue(bt.getCategories().contains(CAT_ENTERTAINMENT));
        assertEquals(before, bt); // state unchanged
    }

    @Test
    public final void testGetBudgetLimitValidCategory() {
        BudgetTracker bt = this.constructor();
        BudgetTracker before = this.constructor();

        this.setIncome(bt, INCOME_100K);
        this.setIncome(before, INCOME_100K);

        this.limit(bt, CAT_ENTERTAINMENT, AMT_2_000);
        this.limit(before, CAT_ENTERTAINMENT, AMT_2_000);

        long limitVal = bt.getBudgetLimit(CAT_ENTERTAINMENT);
        assertEquals(AMT_2_000, limitVal);
        assertEquals(before, bt); // state unchanged
    }

    @Test
    public final void testGetCategoryExpenseAggregatesAndDoesNotMutate() {
        BudgetTracker bt = this.constructor();
        BudgetTracker before = this.constructor();

        this.buildGroceriesEntertainmentScenario(bt);
        this.buildGroceriesEntertainmentScenario(before);

        long groceriesSum = bt.getCategoryExpense(CAT_GROCERIES);
        assertEquals(AMT_1_200 + AMT_1_800, groceriesSum);
        assertEquals(before, bt); // state unchanged
    }

    @Test
    public final void testGetExpenseDatesContainsDatesAndDoesNotMutate() {
        BudgetTracker bt = this.constructor();
        BudgetTracker before = this.constructor();

        this.buildGroceriesEntertainmentScenario(bt);
        this.buildGroceriesEntertainmentScenario(before);

        assertTrue(bt.getExpenseDates(CAT_GROCERIES).contains(DATE_1));
        assertTrue(bt.getExpenseDates(CAT_GROCERIES).contains(DATE_3));
        assertEquals(before, bt); // state unchanged
    }

    @Test
    public final void testGetExpenseAmountExactAndDoesNotMutate() {
        BudgetTracker bt = this.constructor();
        BudgetTracker before = this.constructor();

        this.buildGroceriesEntertainmentScenario(bt);
        this.buildGroceriesEntertainmentScenario(before);

        long amt = bt.getExpenseAmount(CAT_GROCERIES, DATE_3);
        assertEquals(AMT_1_800, amt);
        assertEquals(before, bt); // state unchanged
    }

    @Test
    public final void testLeftToBudgetNeverNegative() {
        BudgetTracker bt = this.constructor();

        this.setIncome(bt, INCOME_50K);
        this.add(bt, CAT_COFFEE, DATE_1, AMT_12_000); // overspend category

        long left = bt.leftToBudget();
        assertTrue(left >= 0); // clamped to zero if overspent
    }

    // Summary methods

    @Test
    public final void testGetAllExpensesSummaryContainsLines() {
        BudgetTracker bt = this.constructor();
        this.buildGroceriesEntertainmentScenario(bt);

        String all = bt.getAllExpensesSummary();

        assertTrue(all.contains("Category: " + CAT_GROCERIES));
        assertTrue(all.contains(" " + DATE_1 + ": $"));
        assertTrue(all.contains(" " + DATE_3 + ": $"));
        assertTrue(all.contains("Category: " + CAT_ENTERTAINMENT));
        assertTrue(all.contains(" " + DATE_2 + ": $"));
    }

    @Test
    public final void testGetBudgetSummaryOverspentMarkerWhenLimitExceeded() {
        BudgetTracker bt = this.constructor();

        this.setIncome(bt, INCOME_50K);
        this.add(bt, CAT_ENTERTAINMENT, DATE_2, AMT_2_000);
        this.limit(bt, CAT_ENTERTAINMENT, 1_000L);

        String summary = bt.getBudgetSummary();
        assertTrue(summary.contains("[OVERSPENT]"));
    }
}
