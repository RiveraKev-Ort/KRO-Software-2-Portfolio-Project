package components.budgettracker;

import components.map.Map;
import components.map.Map1L;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of BudgetTracker.
 */
public class BudgetTracker1L implements BudgetTracker {

    private long income;
    private Map<String, Map<String, Long>> expenses;
    private Map<String, Long> budgetLimits;

    public BudgetTracker1L() {
        this.income = 0;
        this.expenses = new Map1L<>();
        this.budgetLimits = new Map1L<>();
    }

    @Override
    public void setMonthlyIncome(long income) {
        this.income = income;
    }

    @Override
    public void addExpense(String category, String date, long amount) {
        if (!this.expenses.hasKey(category)) {
            this.expenses.add(category, new Map1L<>());
        }
        Map<String, Long> dateMap = this.expenses.value(category);
        if (dateMap.hasKey(date)) {
            long current = dateMap.value(date);
            dateMap.replaceValue(date, current + amount);
        } else {
            dateMap.add(date, amount);
        }
    }

    @Override
    public void setBudgetLimit(String category, long limit) {
        if (this.budgetLimits.hasKey(category)) {
            this.budgetLimits.replaceValue(category, limit);
        } else {
            this.budgetLimits.add(category, limit);
        }
    }

    @Override
    public String getAllExpensesSummary() {
        StringBuilder sb = new StringBuilder();
        List<String> categories = new ArrayList<>();
        for (Map.Pair<String, Map<String, Long>> entry : this.expenses) {
            categories.add(entry.key());
        }
        Collections.sort(categories);
        for (String category : categories) {
            sb.append("Category: ").append(category).append("\n");
            Map<String, Long> dateMap = this.expenses.value(category);
            List<String> dates = new ArrayList<>();
            for (Map.Pair<String, Long> dateEntry : dateMap) {
                dates.add(dateEntry.key());
            }
            Collections.sort(dates, Collections.reverseOrder());
            for (String date : dates) {
                long amountInCents = dateMap.value(date);
                double amountInDollars = amountInCents / 100.0;
                sb.append("  Date: ").append(date).append("  Amount: $")
                        .append(String.format("%.2f", amountInDollars))
                        .append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getBudgetSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Budget Summary ===\n");
        sb.append(
                String.format("Total Monthly Income: $%.2f\n", this.income / 100.0));
        sb.append("-------------------------------\n");

        long totalExpenses = 0;
        for (Map.Pair<String, Map<String, Long>> categoryEntry : this.expenses) {
            String category = categoryEntry.key();
            Map<String, Long> dateMap = categoryEntry.value();
            long spent = 0;
            for (Map.Pair<String, Long> dateEntry : dateMap) {
                spent += dateEntry.value();
            }
            totalExpenses += spent;

            long limit = this.budgetLimits.hasKey(category)
                    ? this.budgetLimits.value(category)
                    : 0;
            long remaining = limit - spent;

            sb.append("Category: ").append(category).append("\n");
            sb.append(String.format("  Spent: $%.2f\n", spent / 100.0));
            sb.append(String.format("  Limit: $%.2f\n", limit / 100.0));
            sb.append(String.format("  Remaining: $%.2f\n", remaining / 100.0));
            if (remaining < 0) {
                sb.append("  WARNING: Over budget by $").append(
                        String.format("%.2f", Math.abs(remaining) / 100.0))
                        .append("!\n");
            }
            sb.append("\n");
        }

        sb.append("-------------------------------\n");
        sb.append(String.format("Total Expenses: $%.2f\n",
                totalExpenses / 100.0));
        sb.append(String.format("Savings: $%.2f\n",
                (this.income - totalExpenses) / 100.0));
        sb.append("\n===============================\n");

        return sb.toString();
    }

    @Override
    public long leftToBudget() {
        long totalExpenses = 0;
        for (Map.Pair<String, Map<String, Long>> categoryEntry : this.expenses) {
            Map<String, Long> dateMap = categoryEntry.value();
            for (Map.Pair<String, Long> dateEntry : dateMap) {
                totalExpenses += dateEntry.value();
            }
        }
        return this.income - totalExpenses;
    }
}