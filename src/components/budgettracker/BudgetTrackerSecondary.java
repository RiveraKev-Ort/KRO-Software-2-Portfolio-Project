
import components.map.Map;
import components.map.Map1L;
import components.map.Map.Pair;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.set.Set;
import components.set.Set1L;

/**
 * Secondary (enhanced) abstract class layered over the BudgetTracker kernel.
 * Implements all methods declared in the enhanced interface using ONLY kernel +
 * Standard methods; does not access any representation directly.
 *
 * NOTE: A concrete class must implement all BudgetTrackerKernel methods.
 *
 * <p>
 * Style compliance: - Single return statements in functions. - No ternary
 * operator. - Descriptive names and braces on all blocks. - OSU components for
 * temporary containers and sorting.
 * </p>
 *
 * @author Kevin Rivera Ortiz
 */
public abstract class BudgetTrackerSecondary implements BudgetTracker {

    // ---------- Private helpers (representation-free) ----------

    /**
     * Insert a word into a sequence in ascending lexicographic order (A–Z).
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
     * Insert a line (prefixed with date) into a sequence in DESC date order
     * (newest → oldest) using lexicographic compare on "YYYY-MM-DD".
     *
     * @param seq
     *            the sequence to modify
     * @param line
     *            the rendered line (begins with date)
     * @param date
     *            the date associated with the line
     * @updates seq
     * @requires seq != null and line != null and date != null
     * @ensures seq contains line and is sorted descending by date
     */
    private static void insertByDateDesc(Sequence<String> seq, String line,
            String date) {
        int index = 0;
        boolean shouldInsert = false;
        while (!shouldInsert && index < seq.length()) {
            String existing = seq.entry(index);
            String existingDate = existing.substring(0,
                    BudgetTrackerKernel.DATE_LENGTH);
            if (existingDate.compareTo(date) < 0) {
                shouldInsert = true;
            } else {
                index++;
            }
        }
        seq.add(index, line);
    }

    /**
     * Format non-negative cents to "$d.cc" without using the ternary operator.
     *
     * @param cents
     *            non-negative amount in cents
     * @return formatted dollars string
     * @requires cents >= 0
     * @ensures result != null
     */
    private static String formatDollars(long cents) {
        long dollars = cents / 100;
        long remainder = cents % 100;

        String centsTwoDigits = Long.toString(remainder);
        if (remainder < 10) {
            centsTwoDigits = "0" + centsTwoDigits;
        }

        String result = "$" + dollars + "." + centsTwoDigits;
        return result;
    }

    /**
     * Compute total expenses across all categories and dates using kernel
     * methods only.
     *
     * @return total expenses in cents (>= 0)
     */
    private long totalExpenses() {
        long total = 0L;
        Set<String> categories = this.getCategories(); // kernel
        while (categories.size() > 0) {
            String cat = categories.removeAny();
            Set<String> dates = this.getExpenseDates(cat); // kernel
            while (dates.size() > 0) {
                String date = dates.removeAny();
                total += this.getExpenseAmount(cat, date); // kernel
            }
        }
        return total;
    }

    // ---------- Enhanced interface methods (secondary) ----------

    @Override
    public String getAllExpensesSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("All Expenses (categories A–Z; dates newest→oldest)\n");

        // categories ascending
        Sequence<String> cats = new Sequence1L<>();
        Set<String> categories = this.getCategories(); // kernel
        while (categories.size() > 0) {
            String c = categories.removeAny();
            insertAscending(cats, c);
        }

        int i = 0;
        while (i < cats.length()) {
            String cat = cats.entry(i);
            sb.append(cat).append(":\n");

            // Gather lines sorted by date desc
            Map<String, Long> byDate = new Map1L<>();
            Set<String> dates = this.getExpenseDates(cat); // kernel
            while (dates.size() > 0) {
                String date = dates.removeAny();
                long amount = this.getExpenseAmount(cat, date); // kernel
                byDate.add(date, amount);
            }

            Sequence<String> lines = new Sequence1L<>();
            while (byDate.size() > 0) {
                Pair<String, Long> p = byDate.removeAny();
                String date = p.key();
                String line = date + ": " + formatDollars(p.value());
                insertByDateDesc(lines, line, date);
            }

            int j = 0;
            while (j < lines.length()) {
                sb.append("   ").append(lines.entry(j)).append("\n");
                j++;
            }

            i++;
        }

        String result = sb.toString();
        return result;
    }

    @Override
    public String getBudgetSummary() {
        StringBuilder sb = new StringBuilder();
        long income = this.getMonthlyIncome(); // kernel
        long expenses = this.totalExpenses(); // computed using kernel
        long remaining = income - expenses;
        if (remaining < 0) {
            remaining = 0; // contract: non-negative remaining
        }

        sb.append("Budget Summary\n");
        sb.append("Income: ").append(formatDollars(income)).append("\n");
        sb.append("Expenses: ").append(formatDollars(expenses)).append("\n");
        sb.append("Remaining Budget: ").append(formatDollars(remaining))
                .append("\n");

        // Per-category section
        Sequence<String> cats = new Sequence1L<>();
        Set<String> categories = this.getCategories(); // kernel
        while (categories.size() > 0) {
            String c = categories.removeAny();
            insertAscending(cats, c);
        }

        int i = 0;
        while (i < cats.length()) {
            String cat = cats.entry(i);
            long limit = this.getBudgetLimit(cat); // kernel

            // Sum category expenses via kernel getters
            long catTotal = 0L;
            Set<String> dates = this.getExpenseDates(cat); // kernel
            while (dates.size() > 0) {
                String date = dates.removeAny();
                catTotal += this.getExpenseAmount(cat, date); // kernel
            }

            sb.append(" - ").append(cat).append(": ");
            if (limit == BudgetTrackerKernel.NO_LIMIT) {
                sb.append("limit = (no limit), spent = ")
                        .append(formatDollars(catTotal))
                        .append(", remaining = (unbounded)");
            } else {
                long rem = limit - catTotal;
                sb.append("limit = ").append(formatDollars(limit))
                        .append(", spent = ").append(formatDollars(catTotal))
                        .append(", remaining = ").append(formatDollars(rem));
                if (rem < 0) {
                    sb.append(" [OVERSPENT]");
                }
            }
            sb.append("\n");
            i++;
        }

        String result = sb.toString();
        return result;
    }

    @Override
    public long leftToBudget() {
        long income = this.getMonthlyIncome(); // kernel
        long expenses = this.totalExpenses(); // computed using kernel
        long remaining = income - expenses;
        if (remaining < 0) {
            remaining = 0; // ensures non-negative per interface contract
        }
        return remaining;
    }

    // ---------- Object methods implemented using kernel only ----------

    @Override
    public String toString() {
        String result = "BudgetTracker{" + "income="
                + formatDollars(this.getMonthlyIncome()) + ", expenses="
                + formatDollars(this.totalExpenses()) + ", leftToBudget="
                + formatDollars(this.leftToBudget()) + ", categories="
                + this.getCategories().size() + "}";
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = true;

        if (this == obj) {
            equal = true;
        } else if (obj == null || !(obj instanceof BudgetTracker)) {
            equal = false;
        } else {
            BudgetTracker other = (BudgetTracker) obj;

            if (equal && this.getMonthlyIncome() != other.getMonthlyIncome()) {
                equal = false;
            }

            // Compare category sets (order-insensitive)
            Set<String> cats1 = this.getCategories();
            Set<String> cats2 = other.getCategories();

            if (equal && cats1.size() != cats2.size()) {
                equal = false;
            }

            Set<String> seen = new Set1L<>();
            while (equal && cats1.size() > 0) {
                String c = cats1.removeAny();
                seen.add(c);

                if (!cats2.contains(c)) {
                    equal = false;
                } else {
                    // Limits must match
                    if (this.getBudgetLimit(c) != other.getBudgetLimit(c)) {
                        equal = false;
                    } else {
                        // Compare per-date amounts
                        Set<String> d1 = this.getExpenseDates(c);
                        Set<String> d2 = other.getExpenseDates(c);

                        if (d1.size() != d2.size()) {
                            equal = false;
                        } else {
                            while (equal && d1.size() > 0) {
                                String date = d1.removeAny();
                                if (!d2.contains(date)) {
                                    equal = false;
                                } else {
                                    long a1 = this.getExpenseAmount(c, date);
                                    long a2 = other.getExpenseAmount(c, date);
                                    if (a1 != a2) {
                                        equal = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Ensure cats2 doesn’t contain extras
            if (equal && cats2.size() != seen.size()) {
                equal = false;
            }
        }

        return equal;
    }

    @Override
    public int hashCode() {
        // Local named constants to avoid magic numbers
        final int hashBase = 17;
        final int hashPrime = 31;

        int h = hashBase;
        h = hashPrime * h + Long.hashCode(this.getMonthlyIncome());

        Set<String> categories = this.getCategories();
        while (categories.size() > 0) {
            String c = categories.removeAny();
            h = hashPrime * h + c.hashCode();
            h = hashPrime * h + Long.hashCode(this.getBudgetLimit(c));

            Set<String> dates = this.getExpenseDates(c);
            while (dates.size() > 0) {
                String d = dates.removeAny();
                h = hashPrime * h + d.hashCode();
                h = hashPrime * h + Long.hashCode(this.getExpenseAmount(c, d));
            }
        }

        return h;
    }
}
