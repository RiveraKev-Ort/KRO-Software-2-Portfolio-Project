import components.budgettracker.BudgetTracker1L;
import org.junit.Test;
import components.map.Map;
import components.set.Set;

/**
 * Kernel-only demo: - Sets income - Adds expenses by category/date via
 * addExpense - Sets limits via setBudgetLimit - Verifies state with standard
 * queries: income(), totalExpenses(), categorySet(), dateToAmount(category),
 * budgetLimit(category)
 */
public final class KernelImporterDemo {

    // ----- Class-level constants (avoid magic numbers) -----
    private static final long INCOME_4500_00 = 4_500_00L;
    private static final long RENT_1200_00 = 1_200_00L;
    private static final long FOOD_65_00 = 65_00L;
    private static final long FOOD_42_50 = 42_50L;
    private static final long UTIL_180_00 = 180_00L;
    private static final long FOOD_LIMIT_400_00 = 400_00L;
    private static final long UTIL_LIMIT_250_00 = 250_00L;

    private static final String CAT_RENT = "Rent";
    private static final String CAT_FOOD = "Food";
    private static final String CAT_UTIL = "Utilities";

    private static final String DATE_2025_10_01 = "2025-10-01";
    private static final String DATE_2025_10_02 = "2025-10-02";
    private static final String DATE_2025_10_05 = "2025-10-05";
    private static final String DATE_2025_10_10 = "2025-10-10";

    /**
     * Program entry point.
     *
     * @param args
     *            command line arguments (unused)
     */
    public static void main(String[] args) {
        // Create a new kernel implementation (with standard queries)
        BudgetTracker1L tracker = new BudgetTracker1L();

        // 1) Set income (kernel)
        tracker.setMonthlyIncome(INCOME_4500_00);

        // 2) Add transactions (kernel)
        tracker.addExpense(CAT_RENT, DATE_2025_10_01, RENT_1200_00);
        tracker.addExpense(CAT_FOOD, DATE_2025_10_02, FOOD_65_00);
        tracker.addExpense(CAT_FOOD, DATE_2025_10_10, FOOD_42_50);
        tracker.addExpense(CAT_UTIL, DATE_2025_10_05, UTIL_180_00);

        // 3) Set limits (kernel)
        tracker.setBudgetLimit(CAT_FOOD, FOOD_LIMIT_400_00);
        tracker.setBudgetLimit(CAT_UTIL, UTIL_LIMIT_250_00);

        // 4) Verify post-state using standard queries (read-only)
        System.out.println("== Kernel Importer Demo ==");
        System.out.println("Income (cents): " + tracker.income());
        System.out
                .println("Total expenses (cents): " + tracker.totalExpenses());
        System.out.println();

        // Category union
        Set<String> cats = tracker.categorySet();
        System.out.println("Categories:");
        while (cats.size() > 0) {
            components.set.Set.Pair<String> p = cats.removeAny();
            String c = p.value();
            System.out.println(" - " + c + " (limit: " + tracker.budgetLimit(c)
                    + " cents)");
        }
        System.out.println();

        // Per-category details (dates and amounts)
        System.out.println("Details by category:");
        Set<String> cats2 = tracker.categorySet(); // fresh copy; original set was consumed
        while (cats2.size() > 0) {
            components.set.Set.Pair<String> p = cats2.removeAny();
            String c = p.value();
            Map<String, Long> byDate = tracker.dateToAmount(c);
            System.out.println(" " + c + ":");
            while (byDate.size() > 0) {
                components.map.Map.Pair<String, Long> d = byDate.removeAny();
                System.out.println(
                        "   " + d.key() + " -> " + d.value() + " cents");
            }
        }
    }
}
