public class BudgetAmt extends Account {
    double amount;
    String name;

    public BudgetAmt() {
        amount = 0;
        name = null;
    }
    public BudgetAmt(String name) {
        this.name = null;
    }

    public BudgetAmt(String name, float amount) {
        this.amount = amount;
        this.name = name;
    }
}
