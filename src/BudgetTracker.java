package components.budgettracker;

/**
 * Enhanced interface for BudgetTracker.
 *
 * Extends the kernel interface with additional methods for reporting and
 * calculating budget summaries and remaining balances.
 *
 * @author Kevin Rivera Ortiz
 */
public interface BudgetTracker extends BudgetTrackerKernel {

    /**
     * Returns a formatted summary of all expense entries.
     *
     * The summary is grouped by category (sorted alphabetically), and within
     * each category, expenses are listed by date (newest to oldest). Amounts
     * are displayed in dollars.
     *
     * @return a formatted string listing all expenses
     * @ensures result != null
     */
    String getAllExpensesSummary();

    /**
     * Returns a formatted budget summary.
     *
     * The summary includes total income, total expenses, per-category spending,
     * budget limits, remaining amounts, and warnings for overspending.
     *
     * @return a formatted string summarizing the budget status
     * @ensures result != null
     */
    String getBudgetSummary();

    /**
     * Returns the remaining budget.
     *
     * This is calculated as the difference between total income and total
     * expenses.
     *
     * @return the remaining budget in cents
     * @ensures result >= 0
     */
    long leftToBudget();

    /**
     * Reports the monthly income.
     *
     * @return monthly income in cents (non-negative)
     * @ensures getMonthlyIncome() >= 0
     */
    long getMonthlyIncome();

    /**
     * Reports all categories currently in the tracker.
     *
     * @return set of category names (non-empty strings)
     * @ensures getCategories() != null
     */
    Set<String> getCategories();

    /**
     * Reports the budget limit for a given category.
     *
     * @param category
     *            the category name
     * @return budget limit for the category in cents (non-negative)
     * @requires category != null and category is in getCategories()
     * @ensures getBudgetLimit(category) >= 0
     */
    long getBudgetLimit(String category);

    /**
     * Reports the total expense for a given category.
     *
     * @param category
     *            the category name
     * @return total expense for the category in cents (non-negative)
     * @requires category != null and category is in getCategories()
     * @ensures getCategoryExpense(category) >= 0
     */
    long getCategoryExpense(String category);

    /**
     * Reports all dates for which expenses exist in a given category.
     *
     * @param category
     *            the category name
     * @return set of dates (non-empty strings)
     * @requires category != null and category is in getCategories()
     * @ensures getExpenseDates(category) != null
     */
    Set<String> getExpenseDates(String category);

    /**
     * Reports the expense amount for a given category and date.
     *
     * @param category
     *            the category name
     * @param date
     *            the date string
     * @return expense amount in cents (non-negative)
     * @requires category != null and date != null and category is in
     *           getCategories() and date is in getExpenseDates(category)
     * @ensures getExpenseAmount(category, date) >= 0
     */
    long getExpenseAmount(String category, String date);

}