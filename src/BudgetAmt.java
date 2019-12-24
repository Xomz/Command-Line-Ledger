public class BudgetAmt extends Account {
    double amount;
    String name;
    private static final long serialVersionUID = 1234L;


    public BudgetAmt() {
        amount = 0;
        name = null;
    }
    public BudgetAmt(String name) {
        this.name = name;
        amount = 0;
    }

    public BudgetAmt(String name, float amount) {
        this.amount = amount;
        this.name = name;
    }

    @Override
    public boolean equals(Object acc2) {
        if (acc2.getClass().equals(this.getClass())) {
            BudgetAmt other = (BudgetAmt) acc2;
            return other.name.equals(this.name);
        }
        return false;
    }
}
