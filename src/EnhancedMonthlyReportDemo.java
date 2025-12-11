
import components.budgettracker.demo;
import components.budgettracker.BudgetTracker;
import components.budgettracker.BudgetTracker1L;
import components.set.Set;
import components.set.Set1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.map.Map;
import components.map.Map1L;
import components.map.Map.Pair;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * Demo that uses secondary/enhanced methods to render formatted summaries -
 * Computes a month-specific per-category view using
 * getExpenseDates/getExpenseAmount - Displays "left to budget" with an
 * OSU-style cents-to-dollars formatter
 */
public final class EnhancedMonthlyReportDemo {

    /*
     * constants
     */
    private static final long INCOME_4500_00 = 4_500_00L;
    private static final long FOOD_LIMIT_500_00 = 500_00L;
    private static final long TRAVEL_LIMIT_300_00 = 300_00L;

    private static final String MONTH_PREFIX_2025_11 = "2025-11-";

    private static final String CAT_FOOD = "Food";
    private static final String CAT_TRAVEL = "Travel";
    private static final String CAT_MISC = "Misc";

    private static final String DATE_2025_11_01 = "2025-11-01";
    private static final String DATE_2025_11_12 = "2025-11-12";
    private static final String DATE_2025_11_20 = "2025-11-20";
    private static final String DATE_2025_11_22 = "2025-11-22";

    private static final long FOOD_100_00 = 100_00L;
    private static final long FOOD_200_00 = 200_00L;
    private static final long TRAVEL_150_00 = 150_00L;
    private static final long MISC_75_00 = 75_00L;

    /**
     * Program entry point.
     *
     * @param args
     *            command line arguments (unused)
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();

        BudgetTracker bt = new BudgetTracker1L();

        // 1) Income and limits (kernel via enhanced interface)
        bt.setMonthlyIncome(INCOME_4500_00);
        bt.setBudgetLimit(CAT_FOOD, FOOD_LIMIT_500_00);
        bt.setBudgetLimit(CAT_TRAVEL, TRAVEL_LIMIT_300_00);

        // 2) Expenses (kernel)
        bt.addExpense(CAT_FOOD, DATE_2025_11_01, FOOD_100_00);
        bt.addExpense(CAT_FOOD, DATE_2025_11_12, FOOD_200_00);
        bt.addExpense(CAT_TRAVEL, DATE_2025_11_20, TRAVEL_150_00);
        bt.addExpense(CAT_MISC, DATE_2025_11_22, MISC_75_00);

        // 3) Enhanced summaries (secondary)
        out.println("== Enhanced Monthly Report Demo ==");
        out.println(bt.getBudgetSummary());
        out.println(bt.getAllExpensesSummary());

        // 4) Per-category totals for the chosen month using enhanced queries only
        out.println("Per-category totals for "
                + MONTH_PREFIX_2025_11.substring(0, 7) + ":");

        Set<String> categories = bt.getCategories();
        // Build a Sequence for stable A–Z display
        Sequence<String> categoriesSorted = new Sequence1L<String>();
        while (categories.size() > 0) {
            components.set.Set.Pair<String> p = categories.removeAny();
            insertAscending(categoriesSorted, p.value());
        }

        int i = 0;
        while (i < categoriesSorted.length()) {
            String cat = categoriesSorted.entry(i);
            long sumForMonth = sumCategoryMonth(bt, cat, MONTH_PREFIX_2025_11);
            out.println(" - " + cat + ": " + toDollarsString(sumForMonth));
            i++;
        }

        // 5) Remaining overall (enhanced)
        long left = bt.leftToBudget();
        out.println();
        out.println("Left to budget overall: " + toDollarsString(left));

        out.close();
    }

    /**
     * Inserts a word into a sequence in ascending lexicographic order (A–Z).
     *
     * @param seq
     *            the sequence to modify
     * @param word
     *            the word to insert
     * @updates seq
     * @requires seq != null and word != null
     * @ensures seq contains word and is sorted ascending
     */
    private static void insertAscending(Sequence<String> seq, String word) {
        int index = 0;
        while (index < seq.length() && seq.entry(index).compareTo(word) < 0) {
            index++;
        }
        seq.add(index, word);
    }

    /**
     * Sums a category's expenses for dates beginning with a given "YYYY-MM-"
     * prefix. Uses only OSU components (Set, Map) via enhanced interface calls.
     *
     * @param bt
     *            budget tracker
     * @param category
     *            category name
     * @param monthPrefix
     *            "YYYY-MM-"
     * @return sum in cents for the category in the given month
     */
    private static long sumCategoryMonth(BudgetTracker bt, String category,
            String monthPrefix) {
        Set<String> dates = bt.getExpenseDates(category);
        long sum = 0L;

        while (dates.size() > 0) {
            components.set.Set.Pair<String> d = dates.removeAny();
            String date = d.value();
            if (date.startsWith(monthPrefix)) {
                long amount = bt.getExpenseAmount(category, date);
                sum += amount;
            }
        }
        long result = sum;
        return result;
    }

    /**
     * Convert non-negative cents to a simple "$d.cc" string without using the
     * ternary operator.
     *
     * @param cents
     *            non-negative cents amount
     * @return formatted string "$d.cc"
     * @requires cents >= 0
     * @ensures toDollarsString != null
     */
    private static String toDollarsString(long cents) {
        long dollars = cents / 100;
        long remainder = cents % 100;

        String twoDigits = Long.toString(remainder);
        if (remainder < 10) {
            twoDigits = "0" + twoDigits;
        }

        String result = "$" + dollars + "." + twoDigits;
        return result;
    }
}
