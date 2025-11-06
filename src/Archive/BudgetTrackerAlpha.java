import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import components.map.Map;
import components.map.Map1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Budget tracker for expences and income.
 *
 * @author Kevin Rivera Ortiz
 *
 */
public final class BudgetTrackerAlpha {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private BudgetTracker1L() {
    }

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * A static variable that can be accessed by other methods.
     */
    private static Long income = (long) 0;

    /**
     * A map to store expense entries with category, date and amount. Outer Map
     * Key (String): Category (e.g., "Food", "Rent"). Inner Map Key (String):
     * Date (e.g., "2025-10-21"). Inner Map Value (Long): Total amount spent on
     * that date in cents
     */
    private static Map<String, Map<String, Long>> expenses = new Map1L<>();

    /**
     * A a Map to store budget limits.
     */
    private static Map<String, Long> budgetLimits = new Map1L<>();

    /**
     * Total amount of monthly income.
     *
     * @param i
     *            the amount of total monthly income.
     */
    private static Long monthlyIncome(Long i) {
        income = i;
        return income;
    }

    /**
     * Add additional income to the current month.
     *
     * @param j
     *            the amount of income to be added to the current month.
     */
    private static Long addIncome(Long j) {
        income += j;
        return income;
    }

    /**
     * Add an expense entry.
     *
     * @param amount
     *            the amount of the expense.
     * @param date
     *            the date of the expense.
     * @param category
     *            the category of the expense.
     */
    private static void addExpense(String category, String date, Long amount) {

        // Check if the category exists in the outer map.
        // If not, create a new inner map for that category.
        if (!expenses.hasKey(category)) {
            expenses.add(category, new Map1L<>());
        }

        Map<String, Long> dateMap = expenses.value(category);

        if (dateMap.hasKey(date)) {
            Long current = dateMap.value(date);
            dateMap.replaceValue(date, current + amount);
        } else {
            dateMap.add(date, amount);
        }

    }

    /**
     * Returns all expense entries as a formatted string, sorted by category
     * (A-Z), then by date (newest to oldest), and shows amount in dollars.
     *
     *
     * @return A list of all the expenses map for review.
     */
    private static String getAllExpensesSummary() {

        StringBuilder sb = new StringBuilder();

        // Create a list of categories and sort alphabetically
        List<String> categories = new ArrayList<>();

        for (Map.Pair<String, Map<String, Long>> entry : expenses) {
            categories.add(entry.key());
        }

        Collections.sort(categories);

        for (String category : categories) {
            sb.append("Category: ").append(category).append("\n");

            Map<String, Long> dateMap = expenses.value(category);

            // Create a list of dates and sort in reverse (newest to oldest)
            List<String> dates = new ArrayList<>();
            for (Map.Pair<String, Long> dateEntry : dateMap) {
                dates.add(dateEntry.key());
            }
            Collections.sort(dates, Collections.reverseOrder());

            for (String date : dates) {
                Long amountInCents = dateMap.value(date);
                double amountInDollars = amountInCents / 100.0;

                sb.append("  Date: ").append(date).append(" | Amount: $")
                        .append(String.format("%.2f", amountInDollars))
                        .append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Set a spending limit for a category.
     *
     * @param category
     *            the category of the expense.
     * @param limit
     *            the monthly spneding limit for the category.
     */
    private static Map<String, Long> setBudget(String category, Long limit) {

        if (budgetLimits.hasKey(category)) {
            budgetLimits.replaceValue(category, limit);
        } else {
            budgetLimits.add(category, limit);
        }
        return budgetLimits;
    }

    /**
     * Return summary of the budget data (i.e. income, expenses, amounts
     * remaining in each category).
     */
    private static String getBudgetSummary() {

        StringBuilder sb = new StringBuilder();

        sb.append("=== Budget Summary ===\n");
        sb.append(
                String.format("Total Monthly Income: $%.2f\n", income / 100.0));
        sb.append("-----------------------------\n");

        int totalExpenses = 0;

        for (Map.Pair<String, Map<String, Long>> categoryEntry : expenses) {
            String category = categoryEntry.key();
            Map<String, Long> dateMap = categoryEntry.value();

            int spent = 0;

            for (Map.Pair<String, Long> dateEntry : dateMap) {
                spent += dateEntry.value();
            }

            totalExpenses += spent;

            Long limit = budgetLimits.hasKey(category)
                    ? budgetLimits.value(category)
                    : 0;
            Long remaining = limit - spent;

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

        sb.append("-----------------------------\n");
        sb.append(String.format("Total Expenses: $%.2f\n",
                totalExpenses / 100.0));
        sb.append(String.format("Savings: $%.2f\n",
                (income - totalExpenses) / 100.0));
        sb.append("\n=============================\n");

        return sb.toString();

    }

    /**
     * Total amount of savings = income - expenses (Savings).
     */
    private static Long leftToBudget() {

        int totalExpenses = 0;

        for (Map.Pair<String, Map<String, Long>> categoryEntry : expenses) {
            Map<String, Long> dateMap = categoryEntry.value();
            for (Map.Pair<String, Long> dateEntry : dateMap) {
                totalExpenses += dateEntry.value();
            }
        }

        return income - totalExpenses;

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.print("Please enter your monthly income (in dollars): ");
        double incomeDollars = in.nextDouble();
        income = (long) (incomeDollars * 100); // Convert to cents for accuracy

        out.println("\nYour monthly income is set to: $"
                + String.format("%.2f", income / 100.0) + "\n");

        // Can now call other methods like:
        out.println(getBudgetSummary());

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
