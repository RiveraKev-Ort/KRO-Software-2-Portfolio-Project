public class BudgetTracker {

    /*
     * Kernel Methods: addExpense(category, amount, date), getExpenses(),
     * setBudget(category, limit), getBudget(category), addIncome(amount),
     * hasCategory()
     */

    public String addExpense;
    public String getBudget;
    public String hasCategory;
    public int addIncome;
    public int getExpense;
    public int setBudget;

    /**
     * Total amount of monthly income.
     */
    private int totalMonthIncome;

    /**
     * Total amount of all budget entries.
     */
    private int totalExpenseAmount;

    /**
     * Total amount of savings = income - expenses.
     */
    private int leftToBudget;

    public int addIncome(int x) {
        int amount = x;
        return amount;
    }
}
