import java.io.*;

public class Account implements Serializable {
    private static final long serialVersionUID = 1234L;
    public boolean isChecking;
    private double balance;

    public Account(boolean isChecking) {
        balance = 0;
        this.isChecking = isChecking;
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
}
