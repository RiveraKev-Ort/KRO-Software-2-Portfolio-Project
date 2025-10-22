import components.map.Map;
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
public final class BudgetTracker {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private BudgetTracker() {
    }

    /*
     * Private members --------------------------------------------------------
     */
    private static int income = 0; // A static variable that can be accessed by other methods.

    /**
     * Total amount of monthly income.
     *
     * @param i
     *            the amount of total monthly income.
     */
    private static int monthlyIncome(int i) {
        /*
         * Put your code for myMethod here
         */
    }

    /**
     * Add additional income to the current month.
     *
     * @param j
     *            the amount of income to be added to the current month.
     */
    private static int addIncome(int j) {
        /*
         * Put your code for myMethod here
         */
    }

    /**
     * Add an expense entry with its catergory, amount, and date.
     *
     * @param amount
     *            the amount of the expense.
     * @param date
     *            the date of the expense.
     * @param category
     *            the category of the expense.
     */
    private static int addExpense(amount, date, category) {
        /*
         * Put your code for myMethod here
         */
    }

    /**
     * Return all expense entries for the month with their data.
     *
     * @param amount
     *            the amount of the expense.
     * @param date
     *            the date of the expense.
     * @param category
     *            the category of the expense.
     */
    private static getExpenses(amount, date, category) {
        /*
         * Put your code for myMethod here
         */
    }

    /**
     * Pick a budget limit for a specific category.
     *
     * @param category
     *            the category of the expense.
     * @param limit
     *            the monthly spneding limit for the category.
     */
    private static Map<K,V> setBudget(category, limit) {
        /*
         * Put your code for myMethod here
         */
    }

    /**
     * Return summary of the budget data
     * (i.e. income, expenses, amounts remaining in each category).
     */
    private static Map.Pair getBudget(income, expenses, category, remaining) {
        /*
         * Put your code for myMethod here
         */
    }

    /**
     * Total amount of savings = income - expenses (Savings).
     */
    private static int leftToBudget() {
        /*
         * Put your code for myMethod here
         */
    }

    /**
     * Ensures that an expense is tied to a exists before adding an expense to
     * it.
     */
    private static String hasCategory() {
        /*
         * Put your code for myMethod here
         */
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
        /*
         * Put your main program code here; it may call myMethod as shown
         */
        myMethod();
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
