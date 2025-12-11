package components.budgettracker;

import components.map.Map;
import components.map.Map1L;
import components.map.Map.Pair;
import components.set.Set;
import components.set.Set1L;

/**
 * BudgetTracker1L
 *
 * Convention (Representation Invariant) expensesByCategory and limitByCategory
 * are not null. For each (category -> dateToAmount) in expensesByCategory: -
 * category is non-null and non-empty (after trim). - dateToAmount is not null.
 * - For each (date -> amount) in dateToAmount: * date is non-null, length =
 * DATE_LENGTH, and is formatted "YYYY-MM-DD" (dashes at DATE_DASH_POS_1 and
 * DATE_DASH_POS_2). * amount >= 0 (in cents). For each (category -> limit) in
 * limitByCategory: - category is non-null and non-empty (after trim). - limit
 * >= 0, or equals NO_LIMIT. monthlyIncome >= 0 (in cents). All methods that
 * return maps/sets return fresh copies; the internal representation is never
 * exposed directly (kernel purity).
 *
 * Correspondence (Abstraction Function) - income(this) == monthlyIncome. - For
 * any category c: limit(this, c) == limitByCategory[c] if present; otherwise
 * NO_LIMIT. - For any category c and date d: expense(this, c, d) ==
 * expensesByCategory[c][d] if present; otherwise 0. - categorySet(this) ==
 * keys(expensesByCategory) U keys(limitByCategory). - totalExpenses(this) ==
 * sum of all amounts across all categories and dates.
 *
 * This class provides only kernel and standard operations used by
 * BudgetTrackerSecondary; enhanced behaviors (sorted summaries, formatting) are
 * implemented in the secondary layer.
 *
 * @author Kevin Rivera Ortiz
 */
public final class BudgetTracker1L extends BudgetTrackerSecondary
        implements BudgetTrackerKernel {
    /*
     * Class-level constants (no magic numbers; screaming snake case)
     */

    /**
     * Expected ISO date length "YYYY-MM-DD".
     */
    public static final int DATE_LENGTH = 10;

    /**
     * First dash position in ISO date "YYYY-MM-DD".
     */
    public static final int DATE_DASH_POS_1 = 4;

    /**
     * Second dash position in ISO date "YYYY-MM-DD".
     */
    public static final int DATE_DASH_POS_2 = 7;

    /**
     * Sentinel for no category limit.
     */
    public static final long NO_LIMIT = -1L;

    /**
     * Dollar symbol used by secondary for formatting.
     */
    public static final String DOLLAR_SYMBOL = "$";

    /**
     * Unbounded remaining sentinel used by secondary.
     */
    public static final long UNBOUNDED_REMAINING = Long.MAX_VALUE;

    /*
     * Representation (private)
     */

    /**
     * Map of category -> (date -> amount) in cents.
     */
    private Map<String, Map<String, Long>> expensesByCategory;

    /**
     * Map of category -> limit in cents, or NO_LIMIT.
     */
    private Map<String, Long> limitByCategory;

    /**
     * Monthly income in cents.
     */
    private long monthlyIncome;

    /*
     * Constructors (Standard)
     */

    /**
     * Default constructor; creates an empty, valid tracker.
     *
     * @clears this
     *
     * @ensures this.income() == 0 and this.totalExpenses() == 0 and
     *          this.categorySet().size() == 0
     */
    public BudgetTracker1L() {
        this.createNewRep();
    }

    /**
     * Initializes a new empty representation.
     *
     * @replaces this
     *
     * @ensures expensesByCategory.size() == 0 and limitByCategory.size() == 0
     *          and monthlyIncome == 0
     */
    private void createNewRep() {
        this.expensesByCategory = new Map1L<>();
        this.limitByCategory = new Map1L<>();
        this.monthlyIncome = 0L;
    }

    /*
     * Kernel methods (BudgetTrackerKernel) — kernel purity enforced
     */

    /**
     * Sets the monthly income.
     *
     * @param income
     *            the total monthly income in cents
     *
     * @replaces this.monthlyIncome
     *
     * @requires income >= 0
     *
     * @ensures this.monthlyIncome == income
     */
    @Override
    public void setMonthlyIncome(long income) {
        if (income < 0L) {
            throw new IllegalArgumentException("income must be >= 0");
        }
        this.monthlyIncome = income;
    }

    /**
     * Adds an expense entry to the tracker.
     *
     * @param category
     *            the expense category (e.g., "Food", "Rent")
     *
     * @param date
     *            the date of the expense in format "YYYY-MM-DD"
     *
     * @param amount
     *            the amount of the expense in cents
     *
     * @updates this.expensesByCategory
     *
     * @requires category != null and category.trim().length() > 0
     *
     * @requires date != null and date.length() == DATE_LENGTH and
     *           date.charAt(DATE_DASH_POS_1) == '-' and
     *           date.charAt(DATE_DASH_POS_2) == '-'
     *
     * @requires amount >= 0
     *
     * @ensures the total recorded for (category, date) increases by amount
     */
    @Override
    public void addExpense(String category, String date, long amount) {
        if (category == null || category.trim().length() == 0) {
            throw new IllegalArgumentException("category must be non-empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("date must be non-null");
        }
        if (date.length() != DATE_LENGTH) {
            throw new IllegalArgumentException(
                    "date must be in ISO format YYYY-MM-DD");
        }
        if (date.charAt(DATE_DASH_POS_1) != '-'
                || date.charAt(DATE_DASH_POS_2) != '-') {
            throw new IllegalArgumentException(
                    "date must contain dashes at positions 4 and 7");
        }
        if (amount < 0L) {
            throw new IllegalArgumentException("amount must be >= 0");
        }

        Map<String, Long> byDate;
        if (this.expensesByCategory.hasKey(category)) {
            byDate = this.expensesByCategory.value(category);
        } else {
            byDate = new Map1L<>();
            this.expensesByCategory.add(category, byDate);
        }

        long newAmount = 0L;
        if (byDate.hasKey(date)) {
            long existing = byDate.value(date);
            newAmount = existing + amount;
            byDate.replaceValue(date, newAmount);
        } else {
            newAmount = amount;
            byDate.add(date, newAmount);
        }
    }

    /**
     * Sets a budget limit for a specific category.
     *
     * @param category
     *            the category to set the limit for
     *
     * @param limit
     *            the monthly spending limit in cents (non-negative)
     *
     * @updates this.limitByCategory
     *
     * @requires category != null and category.trim().length() > 0
     *
     * @requires limit >= 0
     *
     * @ensures the limit associated with category equals limit
     */
    @Override
    public void setBudgetLimit(String category, long limit) {
        if (category == null || category.trim().length() == 0) {
            throw new IllegalArgumentException("category must be non-empty");
        }
        if (limit < 0L) {
            throw new IllegalArgumentException("limit must be >= 0");
        }

        if (this.limitByCategory.hasKey(category)) {
            this.limitByCategory.replaceValue(category, limit);
        } else {
            this.limitByCategory.add(category, limit);
        }
    }

    /*
     * Standard methods required by BudgetTrackerSecondary (non-enhanced)
     */

    /**
     * Reports monthly income in cents.
     *
     * @return monthly income in cents
     * @ensures income() >= 0
     */
    public long income() {
        long result = this.monthlyIncome;
        return result;
    }

    /**
     * Reports the total of all expenses in cents across categories and dates.
     *
     * @return total expenses in cents
     *
     * @ensures totalExpenses() >= 0
     */
    public long totalExpenses() {
        long sum = 0L;

        Map<String, Map<String, Long>> outerCopy = this.deepCopyExpenses();
        while (outerCopy.size() > 0) {
            Pair<String, Map<String, Long>> catPair = outerCopy.removeAny();
            Map<String, Long> byDate = catPair.value();

            while (byDate.size() > 0) {
                Pair<String, Long> datePair = byDate.removeAny();
                sum += datePair.value();
            }
        }

        long result = sum;
        return result;
    }

    /**
     * Reports the budget limit for a given category, or NO_LIMIT if none set.
     *
     * @param category
     *            category name
     * @return budget limit in cents (non-negative) or NO_LIMIT if absent
     * @requires category != null and category.trim().length() > 0
     * @ensures result >= 0 or result == NO_LIMIT
     */
    public long budgetLimit(String category) {
        if (category == null || category.trim().length() == 0) {
            throw new IllegalArgumentException("category must be non-empty");
        }

        long result = NO_LIMIT;
        if (this.limitByCategory.hasKey(category)) {
            result = this.limitByCategory.value(category);
        }
        return result;
    }

    /**
     * Returns a new map (date -> amount in cents) for the given category. If
     * the category has no expenses, returns an empty map.
     *
     * @param category
     *            category name
     * @return new map of date -> amount (never exposes internal rep)
     * @requires category != null and category.trim().length() > 0
     * @ensures result != null
     */
    public Map<String, Long> dateToAmount(String category) {
        if (category == null || category.trim().length() == 0) {
            throw new IllegalArgumentException("category must be non-empty");
        }

        Map<String, Long> result = new Map1L<>();
        if (this.expensesByCategory.hasKey(category)) {
            Map<String, Long> byDate = this.expensesByCategory.value(category);
            Map<String, Long> copy = this.copyDateMap(byDate);
            result = copy;
        }

        return result;
    }

    /**
     * Returns a new set of all categories present in the tracker. Union of
     * categories in expenses and categories with limits.
     *
     * @return set of category names (non-empty strings)
     * @ensures result != null
     */
    public Set<String> categorySet() {
        Set<String> result = new Set1L<>();

        Map<String, Map<String, Long>> expensesCopy = this
                .shallowCopyExpensesKeys();
        while (expensesCopy.size() > 0) {
            Pair<String, Map<String, Long>> p = expensesCopy.removeAny();
            result.add(p.key());
        }

        Map<String, Long> limitsCopy = this.copyLimitMap();
        while (limitsCopy.size() > 0) {
            Pair<String, Long> p = limitsCopy.removeAny();
            result.add(p.key());
        }

        return result;
    }

    /*
     * Private helpers (copying; kernel purity)
     */

    /**
     * Creates a copy of expensesByCategory for safe traversal.
     *
     * @return new map category -> (date -> amount) copied
     * @ensures result != null
     */
    private Map<String, Map<String, Long>> deepCopyExpenses() {
        Map<String, Map<String, Long>> copy = new Map1L<>();
        Map<String, Map<String, Long>> keysOnly = this
                .shallowCopyExpensesKeys();

        while (keysOnly.size() > 0) {
            Pair<String, Map<String, Long>> p = keysOnly.removeAny();
            String category = p.key();
            Map<String, Long> inner = this.expensesByCategory.value(category);
            Map<String, Long> innerCopy = this.copyDateMap(inner);
            copy.add(category, innerCopy);
        }

        return copy;
    }

    /**
     * Creates a copy containing only category keys from expensesByCategory.
     * Values in this copy are the original inner maps.
     *
     * @return new map category -> original inner map reference
     * @ensures result != null
     */
    private Map<String, Map<String, Long>> shallowCopyExpensesKeys() {
        Map<String, Map<String, Long>> copy = new Map1L<>();

        Map<String, Map<String, Long>> working = new Map1L<>();
        while (this.expensesByCategory.size() > 0) {
            Pair<String, Map<String, Long>> p = this.expensesByCategory
                    .removeAny();
            working.add(p.key(), p.value());
        }
        while (working.size() > 0) {
            Pair<String, Map<String, Long>> p = working.removeAny();
            this.expensesByCategory.add(p.key(), p.value());
            copy.add(p.key(), p.value());
        }

        return copy;
    }

    /**
     * Produces a new copy of a date->amount map.
     *
     * @param original
     *            original map (date -> amount)
     * @return deep copy of original map
     * @requires original != null
     * @ensures result != null
     */
    private Map<String, Long> copyDateMap(Map<String, Long> original) {
        Map<String, Long> copy = new Map1L<>();

        Map<String, Long> working = new Map1L<>();
        while (original.size() > 0) {
            Pair<String, Long> p = original.removeAny();
            working.add(p.key(), p.value());
        }
        while (working.size() > 0) {
            Pair<String, Long> p = working.removeAny();
            original.add(p.key(), p.value());
            copy.add(p.key(), p.value());
        }

        return copy;
    }

    /**
     * Copy limit map.
     *
     * @return fresh copy of limitByCategory
     * @ensures result != null
     */
    private Map<String, Long> copyLimitMap() {
        Map<String, Long> copy = new Map1L<>();

        Map<String, Long> working = new Map1L<>();
        while (this.limitByCategory.size() > 0) {
            Pair<String, Long> p = this.limitByCategory.removeAny();
            working.add(p.key(), p.value());
        }
        while (working.size() > 0) {
            Pair<String, Long> p = working.removeAny();
            this.limitByCategory.add(p.key(), p.value());
            copy.add(p.key(), p.value());
        }

        return copy;
    }
}
