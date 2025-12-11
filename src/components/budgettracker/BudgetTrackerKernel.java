
import components.set.Set;

/**
 * Kernel interface for BudgetTracker.
 *
 * Defines core operations for managing income (in cents), budget limits (in
 * cents), and expenses per (category, date) accumulated in cents.
 * Implementations must adhere to OSU components usage; no representation
 * exposure via this interface.
 *
 * @author Kevin Rivera Ortiz
 */
public interface BudgetTrackerKernel {

    /* Constants */

    /**
     * Sentinel indicating that no budget limit is set for a category.
     */
    static final long NO_LIMIT = -1L;

    /**
     * Canonical date format used throughout the component.
     */
    static final String DATE_FORMAT = "YYYY-MM-DD";

    /**
     * Maximum number of characters in {@link #DATE_FORMAT}.
     */
    static final int DATE_LENGTH = 10;

    /**
     * Sets the monthly income (in cents).
     *
     * @param incomeCents
     *            monthly income, in non-negative cents
     * @replaces this.monthlyIncome
     * @requires incomeCents >= 0
     * @ensures this.monthlyIncome = incomeCents
     */
    void setMonthlyIncome(long incomeCents);

    /**
     * Reports the monthly income (in cents).
     *
     * @return current monthly income in cents
     * @ensures result >= 0
     */
    long getMonthlyIncome();

    /**
     * Associates/updates a budget limit for a category (in cents).
     *
     * @param category
     *            non-empty category name
     * @param limitCents
     *            non-negative limit in cents (use {@link #NO_LIMIT} to clear)
     * @updates this.budgetLimits
     * @requires category != null and category.trim().length() > 0
     * @requires limitCents >= 0 or limitCents == NO_LIMIT
     * @ensures (limitCents == NO_LIMIT) implies category has no limit
     * @ensures (limitCents >= 0) implies this.getBudgetLimit(category) =
     *          limitCents
     */
    void setBudgetLimit(String category, long limitCents);

    /**
     * Budget limit for a category (in cents), or {@link #NO_LIMIT} if no limit
     * has been set.
     *
     * @param category
     *            non-empty category name
     * @return budget limit in cents, or {@link #NO_LIMIT} if none is set
     * @requires category != null and category.trim().length() > 0
     */
    long getBudgetLimit(String category);

    /**
     * Records an expense (in cents) for (category, date). If an amount is
     * already recorded for the (category, date), it accumulates.
     *
     * @param category
     *            non-empty category name
     * @param date
     *            date in {@link #DATE_FORMAT} (YYYY-MM-DD)
     * @param amountCents
     *            positive amount in cents
     * @updates this.expenses
     * @requires category != null and category.trim().length() > 0
     * @requires date != null and date.length() == DATE_LENGTH
     * @requires date.charAt(4) == '-' and date.charAt(7) == '-'
     * @requires amountCents > 0
     * @ensures getExpenseAmount(category, date) = #getExpenseAmount(category,
     *          date) + amountCents
     */
    void addExpense(String category, String date, long amountCents);

    /**
     * Set of categories present in the tracker.
     *
     * @return set containing each category name recorded (possibly empty)
     * @ensures result != null
     */
    Set<String> getCategories();

    /**
     * For a given category, returns the set of dates for which expenses exist.
     *
     * @param category
     *            non-empty category name
     * @return set of date strings in {@link #DATE_FORMAT}
     * @requires category != null and category.trim().length() > 0
     * @ensures result != null
     */
    Set<String> getExpenseDates(String category);

    /**
     * Accumulated expense for a category on a given date.
     *
     * @param category
     *            non-empty category name
     * @param date
     *            date string in {@link #DATE_FORMAT}
     * @return amount in cents for (category, date); 0 if none recorded
     * @requires category != null and category.trim().length() > 0
     * @requires date != null and date.length() == DATE_LENGTH
     * @requires date.charAt(4) == '-' and date.charAt(7) == '-'
     * @ensures result >= 0
     */
    long getExpenseAmount(String category, String date);
}
