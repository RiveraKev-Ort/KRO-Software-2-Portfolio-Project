import components.budgettracker.BudgetTracker;

/**
 * Layered implementations of secondary methods for {@code BudgetTracker}.
 */
public abstract class BudgetTrackerSecondary implements BudgetTracker {

    /*
     * Object methods
     * ------------------------------------------------------------
     */

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder("BudgetTracker(");
        result.append("Income: $")
                .append(String.format("%.2f", this.getMonthlyIncome() / 100.0))
                .append(", ");
        result.append("Remaining: $")
                .append(String.format("%.2f", this.leftToBudget() / 100.0))
                .append(", ");
        result.append("Categories: ").append(this.getCategories());
        result.append(")");
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BudgetTracker) {
            BudgetTracker other = (BudgetTracker) obj;
            return this.getMonthlyIncome() == other.getMonthlyIncome()
                    && this.getBudgetSummary().equals(other.getBudgetSummary())
                    && this.getAllExpensesSummary()
                            .equals(other.getAllExpensesSummary());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(this.getMonthlyIncome());
        result = 31 * result + this.getBudgetSummary().hashCode();
        result = 31 * result + this.getAllExpensesSummary().hashCode();
        return result;
    }

    /*
     * Secondary methods
     * ------------------------------------------------------------
     */

    @Override
    public String getBudgetSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Budget Summary:\n");
        summary.append("Income: ").append(this.getMonthlyIncome()).append("\n");
        summary.append("Remaining: ").append(this.leftToBudget()).append("\n");
        summary.append("Categories:\n");

        for (String category : this.getCategories()) {
            summary.append("- ").append(category).append(": Limit=")
                    .append(this.getBudgetLimit(category)).append(", Spent=")
                    .append(this.getCategoryExpense(category)).append("\n");
        }
        return summary.toString();
    }

    @Override
    public String getAllExpensesSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Expenses:\n");
        for (String category : this.getCategories()) {
            summary.append(category).append(":\n");
            for (String date : this.getExpenseDates(category)) {
                summary.append("  ").append(date).append(" -> ")
                        .append(this.getExpenseAmount(category, date))
                        .append("\n");
            }
        }
        return summary.toString();
    }

    @Override
    public long leftToBudget() {
        long totalSpent = 0;
        for (String category : this.getCategories()) {
            totalSpent += this.getCategoryExpense(category);
        }
        return this.getMonthlyIncome() - totalSpent;
    }
}
