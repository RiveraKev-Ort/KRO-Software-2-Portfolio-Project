import components.budgettracker;
import components.map.Map;
import components.set.Set;
import java.util.Objects;

/**
 * Layered implementations of secondary methods for {@code BudgetTracker}.
 */
public abstract class BudgetTrackerSecondary implements BudgetTracker {

    /**
     * Representation fields (visible to subclasses).
     */
    protected long monthlyIncome;
    protected Map<String, Map<String, Long>> expenses;
    protected Map<String, Long> budgetLimits;

    /*
     * Object methods
     */
    @Override
    public String toString() {
        return this.getBudgetSummary();
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
        return Objects.hash(this.monthlyIncome, this.budgetLimits,
                this.expenses);
    }

    /*
     * ------------------------------------------------------------------
     * Secondary methods
     * ------------------------------------------------------------------
     */
    @Override
    public String getAllExpensesSummary() {
        StringBuilder sb = new StringBuilder();

        Set<String> categories = this.expenses.keySet();
        Set<String> categoriesCopy = categories.newInstance();
        categoriesCopy.transferFrom(categories);

        while (categoriesCopy.size() > 0) {
            String category = categoriesCopy.removeAny();
            sb.append("Category: ").append(category).append("\n");

            Map<String, Long> categoryMap = this.expenses.value(category);
            Set<String> dates = categoryMap.keySet();
            Set<String> datesCopy = dates.newInstance();
            datesCopy.transferFrom(dates);

            while (datesCopy.size() > 0) {
                String date = datesCopy.removeAny();
                sb.append(" ").append(date).append(": $")
                        .append(categoryMap.value(date) / 100.0).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String getBudgetSummary() {
        StringBuilder sb = new StringBuilder();
        long totalExpenses = 0;

        sb.append("Monthly Income: $").append(this.monthlyIncome / 100.0)
                .append("\n");

        Set<String> categories = this.expenses.keySet();
        Set<String> categoriesCopy = categories.newInstance();
        categoriesCopy.transferFrom(categories);

        while (categoriesCopy.size() > 0) {
            String category = categoriesCopy.removeAny();
            long categoryExpense = this.getCategoryExpense(category);
            totalExpenses += categoryExpense;

            long limit = this.budgetLimits.hasKey(category)
                    ? this.budgetLimits.value(category)
                    : 0;

            sb.append(category).append(": $").append(categoryExpense / 100.0)
                    .append(" / Limit: $").append(limit / 100.0);

            if (limit > 0 && categoryExpense > limit) {
                sb.append(" [OVERSPENT]");
            }
            sb.append("\n");
        }

        sb.append("Total Expenses: $").append(totalExpenses / 100.0)
                .append("\n");
        sb.append("Remaining Budget: $").append(this.leftToBudget() / 100.0)
                .append("\n");

        return sb.toString();
    }

    @Override
    public long leftToBudget() {
        long totalExpenses = 0;

        Set<String> categories = this.expenses.keySet();
        Set<String> categoriesCopy = categories.newInstance();
        categoriesCopy.transferFrom(categories);

        while (categoriesCopy.size() > 0) {
            String category = categoriesCopy.removeAny();
            totalExpenses += this.getCategoryExpense(category);
        }
        return Math.max(this.monthlyIncome - totalExpenses, 0);
    }

    @Override
    public long getMonthlyIncome() {
        return this.monthlyIncome;
    }

    @Override
    public Set<String> getCategories() {
        return this.expenses.keySet();
    }

    @Override
    public long getBudgetLimit(String category) {
        assert category != null
                && this.budgetLimits.hasKey(category) : "Invalid category";
        return this.budgetLimits.value(category);
    }

    @Override
    public long getCategoryExpense(String category) {
        assert category != null
                && this.expenses.hasKey(category) : "Invalid category";

        long sum = 0;
        Map<String, Long> categoryMap = this.expenses.value(category);
        Set<String> dates = categoryMap.keySet();
        Set<String> datesCopy = dates.newInstance();
        datesCopy.transferFrom(dates);

        while (datesCopy.size() > 0) {
            String date = datesCopy.removeAny();
            sum += categoryMap.value(date);
        }
        return sum;
    }

    @Override
    public Set<String> getExpenseDates(String category) {
        assert category != null
                && this.expenses.hasKey(category) : "Invalid category";
        return this.expenses.value(category).keySet();
    }

    @Override
    public long getExpenseAmount(String category, String date) {
        assert category != null
                && date != null : "Category and date cannot be null";
        assert this.expenses.hasKey(category) && this.expenses.value(category)
                .hasKey(date) : "Expense not found";
        return this.expenses.value(category).value(date);
    }
}
