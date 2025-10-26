
package components.budgettracker;

/**
 * Kernel interface for BudgetTracker.
 *
 * Defines the core operations for managing income, expenses, and budget limits.
 * This interface should be implemented by any class that provides the basic
 * functionality of a budget tracking system.
 *
 * @author Kevin Rivera Ortiz
 */
public interface BudgetTrackerKernel {

    /**
     * Sets the monthly income.
     *
     * @param income
     *            the total monthly income in cents
     * @replaces this.income
     * @requires income >= 0
     * @ensures this.income = income
     */
    void setMonthlyIncome(long income);

    /**
     * Adds an expense entry to the tracker.
     *
     * @param category
     *            the category of the expense (e.g., "Food", "Rent")
     * @param date
     *            the date of the expense in format "YYYY-MM-DD"
     * @param amount
     *            the amount of the expense in cents
     * @updates this.expenses
     * @requires amount >= 0
     * @ensures this.expenses[category][date] += amount
     */
    void addExpense(String category, String date, long amount);

    /**
     * Sets a budget limit for a specific category.
     *
     * @param category
     *            the category to set the limit for
     * @param limit
     *            the monthly spending limit in cents
     * @updates this.budgetLimits
     * @requires limit >= 0
     * @ensures this.budgetLimits[category] = limit
     */
    void setBudgetLimit(String category, long limit);
}
