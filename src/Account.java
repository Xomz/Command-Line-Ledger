import java.io.*;
import java.util.LinkedList;

public class Account implements Serializable {
    private static final long serialVersionUID = 1234L;
    String name;
    private double balance;
    private LinkedList<BudgetAmt> subAccounts;

    public LinkedList<BudgetAmt> getsubAccounts() { return this.subAccounts; }

//    public Account() {
//        balance = 0;
//        name = null;
//        subAccounts = new LinkedList<>();
//    }

    Account() {
        name = null;
        balance = 0;
        subAccounts = new LinkedList<>();
    }

    Account(String name) {
        balance = 0;
        this.name = name;
        subAccounts = new LinkedList<>();
    }

    Account(String name, float balance) {
        this.balance = balance;
        this.name = name;
        subAccounts = new LinkedList<>();
    }

    double getBalance() {
        return balance;
    }

    void withdraw(double amount) {
        balance -= amount;
    }

    void deposit(double amount) {
        balance += amount;
    }

    void saveToFile(String path) {
        try {
            FileOutputStream f = new FileOutputStream(new File(path));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(this);
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Error Initializing Stream.");
            e.printStackTrace();
        }
    }

    void addBudget(String name, float amount) {
        BudgetAmt thisBudget = new BudgetAmt(name, amount);
        if (subAccounts == null) {
            subAccounts = new LinkedList<>();
        }
        subAccounts.add(thisBudget);
    }

    @Override
    public boolean equals(Object o) {
        try {
            Account acc2 = (Account) o;
            return acc2.name.equals(this.name);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
