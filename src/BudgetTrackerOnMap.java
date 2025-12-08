package components.budgettracker;

import components.map.Map;
import components.map.Map1L;

/**
 * {@code BudgetTracker} represented using OSU Map components.
 *
 * @convention income >= 0 AND all expense amounts >= 0 AND all budget limits >=
 *             0
 * @correspondence this.income = monthly income this.expenses = category → (date
 *                 → amount) this.budgetLimits = category → limit
 */
public class BudgetTrackerOnMap extends BudgetTrackerKernel {

    private long income;
    private Map<String, Map<String, Long>> expenses;
    private Map<String, Long> budgetLimits;

    /**
     * Creates initial representation.
     */
    private void createNewRep() {
        this.income = 0;
        this.expenses = new Map1L<>();
        this.budgetLimits = new Map1L<>();
    }

    /**
     * Default constructor.
     */
    public BudgetTrackerOnMap() {
        this.createNewRep();
    }

    // ===== Standard Methods =====
    @Override
    public final BudgetTrackerKernel newInstance() {
        return new BudgetTrackerOnMap();
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(BudgetTrackerKernel source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof BudgetTrackerOnMap : "Violation of: source is of dynamic type BudgetTrackerOnMap";

        BudgetTrackerOnMap localSource = (BudgetTrackerOnMap) source;
        this.income = localSource.income;
        this.expenses.transferFrom(localSource.expenses);
        this.budgetLimits.transferFrom(localSource.budgetLimits);
    }

    // ===== Kernel Methods =====
    @Override
    public final void setMonthlyIncome(long income) {
        assert income >= 0 : "Violation of: income >= 0";
        this.income = income;
    }

    @Override
    public final void addExpense(String category, String date, long amount) {
        assert category != null
                && date != null : "Violation of: category and date are not null";
        assert amount >= 0 : "Violation of: amount >= 0";

        if (!this.expenses.hasKey(category)) {
            this.expenses.add(category, new Map1L<>());
        }
        Map<String, Long> categoryMap = this.expenses.value(category);
        if (categoryMap.hasKey(date)) {
            long current = categoryMap.value(date);
            categoryMap.replaceValue(date, current + amount);
        } else {
            categoryMap.add(date, amount);
        }
    }

    @Override
    public final void setBudgetLimit(String category, long limit) {
        assert category != null : "Violation of: category is not null";
        assert limit >= 0 : "Violation of: limit >= 0";

        if (this.budgetLimits.hasKey(category)) {
            this.budgetLimits.replaceValue(category, limit);
        } else {
            this.budgetLimits.add(category, limit);
        }
    }
}
