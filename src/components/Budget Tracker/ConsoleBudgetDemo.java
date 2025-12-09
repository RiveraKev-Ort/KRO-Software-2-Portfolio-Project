
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.budgettracker;

/**
 * Demonstrates setting monthly income, adding expenses, setting budget limits,
 * and printing formatted summaries.
 */
public final class ConsoleBudgetDemo {

    /** Prevent instantiation. */
    private ConsoleBudgetDemo() {
    }

    /**
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        // I/O setup using OSU SimpleReader/SimpleWriter
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // Create tracker (concrete implementation of your component)
        BudgetTracker tracker = new BudgetTrackerOnMap();

        // Prompt for income
        out.print("Enter monthly income in cents (non-negative): ");
        String incomeStr = in.nextLine();
        long income = parseNonNegativeLong(incomeStr);
        tracker.setMonthlyIncome(income);

        // Add a few sample expenses
        out.println("Adding sample expenses...");
        tracker.addExpense("Groceries", "2025-12-01", 12_500L); // $125.00
        tracker.addExpense("Groceries", "2025-12-05", 3_499L); // $34.99
        tracker.addExpense("Transport", "2025-12-03", 4_200L); // $42.00
        tracker.addExpense("Rent", "2025-12-01", 90_000L); // $900.00

        // Set some budget limits
        tracker.setBudgetLimit("Groceries", 50_000L); // $500.00
        tracker.setBudgetLimit("Transport", 10_000L); // $100.00
        tracker.setBudgetLimit("Rent", 100_000L); // $1,000.00

        // Print summaries
        out.println();
        out.println("===== All Expenses Summary =====");
        out.println(tracker.getAllExpensesSummary());

        out.println();
        out.println("===== Budget Summary =====");
        out.println(tracker.getBudgetSummary());

        // Remaining budget
        long left = tracker.leftToBudget();
        out.println();
        out.println("Remaining to budget (cents): " + left);

        in.close();
        out.close();
    }

    /**
     * Parse a non-negative long from a string; if invalid, returns 0.
     *
     * @param s
     *            input string
     * @return parsed non-negative long (defaults to 0 on errors)
     * @requires s != null
     * @ensures parseNonNegativeLong >= 0
     */
    private static long parseNonNegativeLong(String s) {
        long value = 0L;
        try {
            long parsed = Long.parseLong(s.trim());
            if (parsed >= 0L) {
                value = parsed;
            }
        } catch (NumberFormatException ex) {
            // default to 0
        }
        return value;
    }
}