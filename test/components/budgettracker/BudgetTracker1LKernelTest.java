
import components.budgettracker.BudgetTracker1L;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import components.map.Map;
import components.set.Set;

/**
 * Abstract base test fixture for kernel-level behavior of BudgetTracker.
 */
public abstract class BudgetTracker1LKernelTest {

    // ----- Test constants (avoid magic numbers) -----
    private static final long INCOME_250_DOLLARS = 250_00L;
    private static final long FOOD_12_99 = 1_299L;
    private static final long FOOD_5_00 = 500L;
    private static final long FOOD_2_50 = 250L;
    private static final long FOOD_1_00 = 100L;
    private static final long RENT_1200_00 = 1_200_00L;
    private static final long UTIL_180_00 = 180_00L;
    private static final long LIMIT_10_000 = 10_000L;
    private static final long LIMIT_8_000 = 8_000L;

    private static final String CAT_FOOD = "Food";
    private static final String CAT_RENT = "Rent";
    private static final String CAT_UTIL = "Utilities";
    private static final String CAT_TRAVEL = "Travel";

    private static final String DATE_2025_10_01 = "2025-10-01";
    private static final String DATE_2025_10_02 = "2025-10-02";
    private static final String DATE_2025_10_05 = "2025-10-05";
    private static final String BAD_DATE_SLASHES = "2025/10/01";

    /**
     * Factory method for the implementation under test.
     *
     * @return a new, empty BudgetTracker1L instance
     * @ensures constructorTest().income() == 0 and
     *          constructorTest().totalExpenses() == 0
     */
    protected abstract BudgetTracker1L constructorTest();

    // ----- Initialization -----

    @Test
    public final void testInitialization_emptyTracker() {
        BudgetTracker1L bt = this.constructorTest();
        assertEquals("Default income must be 0", 0L, bt.income());
        assertEquals("Default total expenses must be 0", 0L,
                bt.totalExpenses());
        assertEquals("No categories initially", 0, bt.categorySet().size());
    }

    // ----- setMonthlyIncome -----

    @Test
    public final void testSetMonthlyIncome_basic() {
        BudgetTracker1L bt = this.constructorTest();
        bt.setMonthlyIncome(INCOME_250_DOLLARS);
        assertEquals(INCOME_250_DOLLARS, bt.income());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetMonthlyIncome_negative_throws() {
        BudgetTracker1L bt = this.constructorTest();
        bt.setMonthlyIncome(-1L);
    }

    // ----- addExpense -----

    @Test
    public final void testAddExpense_newCategoryNewDate_singleEntry() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, DATE_2025_10_01, FOOD_12_99);
        Map<String, Long> food = bt.dateToAmount(CAT_FOOD);

        assertEquals(Long.valueOf(FOOD_12_99), food.value(DATE_2025_10_01));
        assertEquals(1, food.size());
        assertEquals(FOOD_12_99, bt.totalExpenses());
    }

    @Test
    public final void testAddExpense_accumulateSameDate() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, DATE_2025_10_01, FOOD_5_00);
        bt.addExpense(CAT_FOOD, DATE_2025_10_01, FOOD_2_50);

        Map<String, Long> food = bt.dateToAmount(CAT_FOOD);
        assertEquals(Long.valueOf(FOOD_5_00 + FOOD_2_50),
                food.value(DATE_2025_10_01));
        assertEquals(FOOD_5_00 + FOOD_2_50, bt.totalExpenses());
    }

    @Test
    public final void testAddExpense_multipleCategoriesDates_totalCorrect() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, DATE_2025_10_01, FOOD_1_00);
        bt.addExpense(CAT_FOOD, DATE_2025_10_02, FOOD_2_50);
        bt.addExpense(CAT_RENT, DATE_2025_10_01, RENT_1200_00);
        bt.addExpense(CAT_UTIL, DATE_2025_10_05, UTIL_180_00);

        long expected = FOOD_1_00 + FOOD_2_50 + RENT_1200_00 + UTIL_180_00;
        assertEquals(expected, bt.totalExpenses());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testAddExpense_invalidDateFormat_throws() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, BAD_DATE_SLASHES, FOOD_5_00);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testAddExpense_emptyCategory_throws() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense("   ", DATE_2025_10_01, FOOD_5_00);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testAddExpense_negativeAmount_throws() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, DATE_2025_10_01, -1L);
    }

    // ----- setBudgetLimit -----

    @Test
    public final void testSetBudgetLimit_addAndReplace() {
        BudgetTracker1L bt = this.constructorTest();
        bt.setBudgetLimit(CAT_TRAVEL, LIMIT_10_000);
        assertEquals(LIMIT_10_000, bt.budgetLimit(CAT_TRAVEL));

        bt.setBudgetLimit(CAT_TRAVEL, LIMIT_8_000);
        assertEquals(LIMIT_8_000, bt.budgetLimit(CAT_TRAVEL));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetBudgetLimit_negative_throws() {
        BudgetTracker1L bt = this.constructorTest();
        bt.setBudgetLimit(CAT_TRAVEL, -5L);
    }

    @Test
    public final void testBudgetLimit_absentReturnsNoLimit() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, DATE_2025_10_01, FOOD_1_00);
        assertEquals(BudgetTracker1L.NO_LIMIT, bt.budgetLimit(CAT_FOOD));
    }

    // ----- Standard queries (verify kernel post-state) -----

    @Test
    public final void testCategorySet_unionOfExpensesAndLimits() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, DATE_2025_10_01, FOOD_1_00);
        bt.addExpense(CAT_RENT, DATE_2025_10_01, FOOD_2_50);
        bt.setBudgetLimit(CAT_UTIL, UTIL_180_00);

        Set<String> cats = bt.categorySet();
        assertTrue(cats.contains(CAT_FOOD));
        assertTrue(cats.contains(CAT_RENT));
        assertTrue(cats.contains(CAT_UTIL));
    }

    @Test
    public final void testDateToAmount_returnsFreshCopy_kernelPurity() {
        BudgetTracker1L bt = this.constructorTest();
        bt.addExpense(CAT_FOOD, DATE_2025_10_01, FOOD_1_00);

        Map<String, Long> copy = bt.dateToAmount(CAT_FOOD);
        // Mutate the copy; internal representation must remain unchanged.
        copy.replaceValue(DATE_2025_10_01, 999_999L);

        Map<String, Long> secondCopy = bt.dateToAmount(CAT_FOOD);
        assertEquals(Long.valueOf(FOOD_1_00),
                secondCopy.value(DATE_2025_10_01));
    }
}
