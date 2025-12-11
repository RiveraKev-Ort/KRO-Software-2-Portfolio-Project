
/**
 * Enhanced client-facing interface for BudgetTracker.
 *
 * Layers higher-level summaries and calculations over the kernel: -
 * All-expenses summary (categories A–Z; dates newest→oldest) - Budget summary
 * with per-category limits/remaining and overspend warnings - Remaining budget
 * (income − total expenses), floored at 0
 *
 * Methods here are implemented using ONLY kernel + Standard methods in the
 * secondary abstract class, with no representation access.
 *
 * @author Kevin Rivera Ortiz
 */
public interface BudgetTracker extends BudgetTrackerKernel {

    /**
     * Returns a formatted summary of all expense entries. Grouped by category
     * (sorted alphabetically), and within each category, dates are listed
     * newest → oldest.
     *
     * @return formatted text listing all expenses
     * @ensures result != null
     */
    String getAllExpensesSummary();

    /**
     * Returns a formatted budget summary. Includes total income, total
     * expenses, per-category spending, budget limits, remaining amounts, and
     * overspend warnings.
     *
     * @return formatted text summarizing the budget status
     * @ensures result != null
     */
    String getBudgetSummary();

    /**
     * Remaining budget = max(0, monthly income − total expenses).
     *
     * @return remaining budget in cents (non-negative)
     * @ensures result >= 0
     */
    long leftToBudget();
}
