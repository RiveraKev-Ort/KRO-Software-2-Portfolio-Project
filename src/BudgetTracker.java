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
}