
import components.budgettracker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * JUnit test fixture for {@code BudgetTrackerKernel}'s constructor and kernel
 * methods, using self-checking (no reference implementation).
 *
 * OSU style: camelCase test names, SCREAMING_SNAKE_CASE constants, and helpers
 * to avoid duplicated code.
 */
public abstract class BudgetTrackerKernelTest {

    // ---------------------------------------------------------------------
    // Constants (SCREAMING_SNAKE_CASE)
    // ---------------------------------------------------------------------
    private static final long INCOME_0 = 0L;
    private static final long INCOME_100K = 100_000L;
    private static final long INCOME_200K = 200_000L;

    private static final long AMT_450 = 450L;
    private static final long AMT_50 = 50L;
    private static final long AMT_1K = 1_000L;
    private static final long AMT_1_200 = 1_200L;
    private static final long AMT_1_800 = 1_800L;
    private static final long AMT_2_000 = 2_000L;
    private static final long AMT_2_500 = 2_500L;
    private static final long AMT_150K = 150_000L;

    private static final String CAT_FOOD = "Food";
    private static final String CAT_TRANSPORT = "Transport";
    private static final String CAT_RENT = "Rent";

    private static final String DATE_1 = "2025-12-01";
    private static final String DATE_2 = "2025-12-03";
    private static final String DATE_3 = "2025-12-05";
    private static final String DATE_4 = "2025-12-08";

    // ---------------------------------------------------------------------
    // Abstract constructor hook
    // ---------------------------------------------------------------------

    /**
     * Returns a new instance of the implementation under test.
     *
     * @return new {@code BudgetTrackerKernel}
     * @ensures constructor != null
     */
    protected abstract BudgetTrackerKernel constructor();

    // ---------------------------------------------------------------------
    // Helpers (avoid duplicated code)
    // ---------------------------------------------------------------------

    /**
     * Sets monthly income using the kernel method.
     *
     * @param bt
     *            tracker
     * @param income
     *            income in cents
     * @updates bt
     * @requires bt != null && income >= 0
     * @ensures bt has monthlyIncome = income
     */
    private void setIncome(BudgetTrackerKernel bt, long income) {
        bt.setMonthlyIncome(income);
    }

    /**
     * Adds one expense using the kernel method.
     *
     * @param bt
     *            tracker
     * @param cat
     *            category
     * @param date
     *            ISO date
     * @param amt
     *            amount in cents
     * @updates bt
     * @requires bt != null && cat != null && date != null && amt >= 0
     * @ensures bt records amt added to (cat, date)
     */
    private void add(BudgetTrackerKernel bt, String cat, String date,
            long amt) {
        bt.addExpense(cat, date, amt);
    }

    /**
     * Sets budget limit using the kernel method.
     *
     * @param bt
     *            tracker
     * @param cat
     *            category
     * @param limit
     *            limit in cents
     * @updates bt
     * @requires bt != null && cat != null && limit >= 0
     * @ensures bt records limit for cat
     */
    private void setLimit(BudgetTrackerKernel bt, String cat, long limit) {
        bt.setBudgetLimit(cat, limit);
    }

    // ---------------------------------------------------------------------
    // Tests: constructor and kernel methods
    // ---------------------------------------------------------------------

    @Test
    public final void testConstructorBaseline() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();
        assertEquals(expected, actual);
    }

    @Test
    public final void testSetMonthlyIncomeZero() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();

        this.setIncome(actual, INCOME_0);
        this.setIncome(expected, INCOME_0);

        assertEquals(expected, actual);
    }

    @Test
    public final void testSetMonthlyIncomeOverwrite() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();

        this.setIncome(actual, INCOME_100K);
        this.setIncome(expected, INCOME_100K);

        this.setIncome(actual, INCOME_200K);
        this.setIncome(expected, INCOME_200K);

        assertEquals(expected, actual);
    }

    @Test
    public final void testAddExpenseNewCategoryNewDate() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();

        this.setIncome(actual, INCOME_100K);
        this.setIncome(expected, INCOME_100K);

        this.add(actual, CAT_FOOD, DATE_1, AMT_2_500);
        this.add(expected, CAT_FOOD, DATE_1, AMT_2_500);

        assertEquals(expected, actual);
    }

    @Test
    public final void testAddExpenseSameDateAggregates() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();

        this.setIncome(actual, INCOME_100K);
        this.setIncome(expected, INCOME_100K);

        this.add(actual, CAT_TRANSPORT, DATE_2, AMT_450);
        this.add(expected, CAT_TRANSPORT, DATE_2, AMT_450);

        this.add(actual, CAT_TRANSPORT, DATE_2, AMT_50);
        this.add(expected, CAT_TRANSPORT, DATE_2, AMT_50);

        assertEquals(expected, actual);
    }

    @Test
    public final void testAddExpenseMultipleDatesSameCategory() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();

        this.setIncome(actual, INCOME_100K);
        this.setIncome(expected, INCOME_100K);

        this.add(actual, CAT_TRANSPORT, DATE_1, AMT_1K);
        this.add(expected, CAT_TRANSPORT, DATE_1, AMT_1K);

        this.add(actual, CAT_TRANSPORT, DATE_2, AMT_450);
        this.add(expected, CAT_TRANSPORT, DATE_2, AMT_450);

        assertEquals(expected, actual);
    }

    @Test
    public final void testAddExpenseZeroAmountAllowed() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();

        this.setIncome(actual, INCOME_100K);
        this.setIncome(expected, INCOME_100K);

        this.add(actual, CAT_TRANSPORT, DATE_1, 0L);
        this.add(expected, CAT_TRANSPORT, DATE_1, 0L);

        assertEquals(expected, actual);
    }

    @Test
    public final void testSetBudgetLimitNewCategory() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();

        this.setIncome(actual, INCOME_200K);
        this.setIncome(expected, INCOME_200K);

        this.setLimit(actual, CAT_RENT, AMT_150K);
        this.setLimit(expected, CAT_RENT, AMT_150K);

        assertEquals(expected, actual);
    }

    @Test
    public final void testSetBudgetLimitOverwrite() {
        BudgetTrackerKernel actual = this.constructor();
        BudgetTrackerKernel expected = this.constructor();

        this.setIncome(actual, INCOME_200K);
        this.setIncome(expected, INCOME_200K);

        this.setLimit(actual, CAT_RENT, AMT_150K);
        this.setLimit(expected, CAT_RENT, AMT_150K);

        this.setLimit(actual, CAT_RENT, AMT_2_000);
        this.setLimit(expected, CAT_RENT, AMT_2_000);

        assertEquals(expected, actual);
    }
}
