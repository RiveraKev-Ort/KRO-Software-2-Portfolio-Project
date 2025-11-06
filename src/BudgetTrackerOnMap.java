package components.budgettracker;

import components.map.Map;
import components.map.Map1L;

/**
 * {@code BudgetTracker} represented using nested maps with implementations of
 * primary methods.
 *
 * @convention income >= 0 AND all expense amounts >= 0 AND all budget limits >=
 *             0
 * @correspondence this.income = monthly income this.expenses = category → (date
 *                 → amount) this.budgetLimits = category → limit
 */

public class BudgetTrackerOnMap extends BudgetTrackerSecondary {

    /*
     * Private members
     * ------------------------------------------------------------
     */
    private long income;
    private Map<String, Map<String, Long>> expenses;
    private Map<String, Long> budgetLimits;

    /*
     * Creator of initial representation
     */
    private void createNewRep() {
        this.income = 0;
        this.expenses = new HashMap<>();
        this.budgetLimits = new HashMap<>();
    }

    /*
     * Constructors ------------------------------------------------------------
     */
    public BudgetTracker1L() {
        this.createNewRep();
    }

    /*
     * Standard methods
     * ------------------------------------------------------------
     */
    @Override
    public final BudgetTracker newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(BudgetTracker source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof BudgetTracker1L : "Violation of: source is of dynamic type BudgetTracker1L";

        BudgetTracker1L localSource = (BudgetTracker1L) source;
        this.income = localSource.income;
        this.expenses = localSource.expenses;
        this.budgetLimits = localSource.budgetLimits;

        localSource.createNewRep();
    }

    /*
     * Kernel methods
     * ------------------------------------------------------------
     */
    @Override
    public final void setMonthlyIncome(long income) {
        assert income >= 0 : "Violation of: income >= 0";
        this.income = income;
    }

    @Override
    public final long getMonthlyIncome() {
        return this.income;
    }

    @Override
    public final void addExpense(String category, String date, long amount) {
        assert category != null
                && date != null : "Violation of: category and date are not null";
        assert amount >= 0 : "Violation of: amount >= 0";

        this.expenses.putIfAbsent(category, new HashMap<>());
        this.expenses.get(category).put(date, amount);
    }

    @Override
    public final void setBudgetLimit(String category, long limit) {
        assert category != null : "Violation of: category is not null";
        assert limit >= 0 : "Violation of: limit >= 0";

        this.budgetLimits.put(category, limit);
    }

    @Override
    public final Iterable<String> getCategories() {
        return this.expenses.keySet();
    }

    @Override
    public final long getBudgetLimit(String category) {
        return this.budgetLimits.getOrDefault(category, 0L);
    }

    @Override
    public final long getCategoryExpense(String category) {
        long total = 0;
        if (this.expenses.containsKey(category)) {
            for (long amt : this.expenses.get(category).values()) {
                total += amt;
            }
        }
        return total;
    }

    @Override
    public final Iterable<String> getExpenseDates(String category) {
        return this.expenses.containsKey(category)
                ? this.expenses.get(category).keySet()
                : new HashMap<String, Long>().keySet();
    }

    @Override
    public final long getExpenseAmount(String category, String date) {
        if (this.expenses.containsKey(category)) {
            return this.expenses.get(category).getOrDefault(date, 0L);
        }
        return 0L;
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
                    .append(this.getCategoryExpense(category));
            if (this.getCategoryExpense(category) > this
                    .getBudgetLimit(category)) {
                summary.append(" [OVER LIMIT!]");
            }
            summary.append("\n");
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

    /**
     * Calculates the percentage of budget spent for a given category.
     */
    public double getCategoryUsagePercent(String category) {
        long spent = this.getCategoryExpense(category);
        long limit = this.getBudgetLimit(category);
        return limit > 0 ? (spent * 100.0) / limit : 0.0;
    }

    /**
     * Finds the category with the highest spending.
     */
    public String getHighestSpendingCategory() {
        String highestCategory = "";
        long maxSpent = 0;
        for (String category : this.getCategories()) {
            long spent = this.getCategoryExpense(category);
            if (spent > maxSpent) {
                maxSpent = spent;
                highestCategory = category;
            }
        }
        return highestCategory;
    }

    /**
     * Checks if the user is overspending overall.
     */
    public boolean isOverspending() {
        return this.leftToBudget() < 0;
    }

}
