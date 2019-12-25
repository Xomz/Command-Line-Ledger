import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.ArrayList;

public class Ledger{

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Account> accounts = readFromFile("accounts.ser");
        if (accounts == null) {
            throw new IllegalArgumentException("Broken path.");
        }
        while (true) {
            System.out.println("---------------------ACCOUNT OVERVIEW---------------------");
            System.out.println("Accounts:\n");
            int counter = 1;
            String printString = "";
            for (Account account : accounts) {
                printString += counter + ".";
                if (counter > 9) {
                    printString += " ";
                } else {
                    printString += "  ";
                }
                printString += account.name + ":";
                for (int i = 0; i < 12 - account.name.length(); i++) {
                    printString += " ";
                }
                System.out.printf("%s %.2f\n", printString, account.getBalance());
                printString = "";
                counter++;
            }
            System.out.println("\nTo access an account enter its " +
                    "corresponding number, type 'add' to add a new one, 'delete' to remove it, or type 'exit' to exit.");
            String s = "";
            int accountIndex = -1;
            while (accountIndex < 1 || accountIndex > accounts.size()) {
//                System.out.println("Iteration.");
//                System.out.printf("accounts size: %d", accounts.size());
                s = in.nextLine();
                try {
                    accountIndex = Integer.parseInt(s);
//                    System.out.printf("Index found: %d", accountIndex);
                } catch (NumberFormatException e) {
//                    System.out.println("Caught.");
                    if (!s.equals("exit")) {
                        if (s.equals("add")) {
                            System.out.println("What would you like to name this new account? (1-12 Characters)");
                            String name = in.nextLine();
                            while (accounts.contains(new Account(name))) {
                                System.out.println("Cannot have two accounts of the same name. Please enter a unique account name.");
                                name = in.nextLine();
                            }
                            while (name.length() > 12 || name.length() == 0) {
                                System.out.println("Please enter an account name between 1 and 12 characters.");
                                name = in.nextLine();
                            }
                            System.out.println("Would you like to initiate this account with a balance? (y/n)");
                            s = in.nextLine();
                            while (!s.equals("y") && !s.equals("n")) {
                                s = in.nextLine();
                            }
                            float amount = 0;
                            if (s.equals("y")) {
                                System.out.println("What amount would you like to initiate your account with?");
                                while (!in.hasNextFloat()) {
                                    in.nextLine();
                                }
                                amount = in.nextFloat();
                            }
                            accounts.add(new Account(name, amount));
                            s = "refresh";
                            break;
                        } else if (s.equals("delete")) {
                            System.out.println("Enter the number of the account you want deleted.");
                            int index = -1;
                            while (index < 1 || index > accounts.size()) {
                                s = in.nextLine();
                                try {
                                    index = Integer.parseInt(s);
                                } catch (NumberFormatException e2) {
                                    System.out.println("Please enter one of the numbers listed on screen.");
                                    continue;
                                }
                                if (index < 1 || index > accounts.size()) {
                                    System.out.println("Please enter one of the numbers listed on screen.");
                                }
                            }
                            accounts.remove(index - 1);
                            System.out.println("Removed.");
                            s = "refresh";
                            break;
                        } else if (!s.equals("exit") && !s.equals("")){
                            System.out.println("No accounts detected. Please only enter numbers corresponding to accounts.");
                        }
                    } else {
                        break;
                    }
                }

            }
            if (s.equals("exit")) {
                break;
            } else if (!s.equals("refresh")){
                accountInterface(in, accounts.get(accountIndex - 1));
            }
        }
        saveAccounts(accounts);
    }

    private static void accountInterface(Scanner in, Account acc) {
        while (true) {
            String printString = "";
            for (int i = 0; i < (42 - acc.name.length()) / 2; i++) {
                printString += "-";
            }
            printString += acc.name.toUpperCase();
            for (int i = 0; i < (42 - acc.name.length()) / 2; i++) {
                printString += "-";
            }
            System.out.println(printString);
            System.out.printf("Total Account Balance: %.2f\n", acc.getBalance());
            LinkedList<BudgetAmt> subAccounts = acc.getsubAccounts();
            if (subAccounts != null && !subAccounts.isEmpty()) {
                System.out.printf("Budgets:                   \n");
                float budgetTotal = 0;
                for (BudgetAmt subAccount : subAccounts) {
                    printString = "    " + subAccount.name;
                    for (int i = 0; i < 15 - subAccount.name.length(); i++) {
                        printString += " ";
                    }
                    System.out.printf("%s: %.2f\n", printString, subAccount.amount);
                    budgetTotal += subAccount.amount;
                }
                System.out.printf("Balance After Budgets: %.2f\n", acc.getBalance() - budgetTotal);

            }
            System.out.println("------------------------------------------\n");
            System.out.println("<Press (1) to make a deposit, (2) for a withdrawal, (3) to create a new budget, or (4) to delete a budget, or (5) to exit.>");
            String s;
            while (true) {
                s = in.nextLine();
                if (s.equals("")) {
                    continue;
                }
                if (!s.equals("1") && !s.equals("2") && !s.equals("3") && !s.equals("4") && !s.equals("5")) {
                    System.out.println("Please only enter the prompted numbers.");
                } else {
                    break;
                }
            }
            if (s.equals("1")) { // Deposit
                System.out.println("How much would you like to deposit?");
                while (!in.hasNextFloat()) {
                    System.out.println("Please enter a positive numerical value.");
                    in.nextLine();
                }
                float deposit = in.nextFloat();
                while (deposit < 0) {
                    System.out.println("Do you want your money lost to the void? Positive numbers only.");
                    deposit = in.nextFloat();
                }
                acc.deposit(deposit);
            } else if (s.equals("2")) { // Withdraw
                System.out.println("How much would you like to withdraw?");
                while (!in.hasNextFloat()) {
                    System.out.println("Please enter a positive numerical value.");
                    in.nextLine();
                }
                float withdrawal = in.nextFloat();
                while (withdrawal < 0) {
                    System.out.println("Think robbing a bank is that easy? Positive numbers only.");
                    withdrawal = in.nextFloat();
                }
                while (acc.getBalance() - withdrawal < 0) {
                    System.out.println("Cannot withdraw more than you have in your account.");
                    withdrawal = in.nextFloat();
                }
                acc.withdraw(withdrawal);
            } else if (s.equals("3")){ // Add Budget
                System.out.println("What would you like to name your budget? (1-15 characters)");
                String name = in.nextLine();
                while (name.length() == 0) {
                    System.out.println("Please enter between 1 and 15 characters.");
                    name = in.nextLine();
                }
                while (name.length() > 15) {
                    System.out.println("Please enter between 1 and 15 characters.");
                    name = in.nextLine();
                }
                System.out.println("How much would you like to budget from your account?");
                while (!in.hasNextFloat()) {
                    System.out.println("Please enter a positive numerical value.");
                    in.nextLine();
                }
                float budget = in.nextFloat();
                while (budget < 0) {
                    System.out.println("Cannot add negative budgets.");
                    budget = in.nextFloat();
                }
                if (acc.getBalance() - budget < 0) {
                    System.out.println("<WARNING: This budget will exceed your account balance. Proceed? (y/n)>");
                    s = in.nextLine();
                    while (!s.equals("y") && !s.equals("n")) {
                        s = in.nextLine();
                    }
                }
                if (s.equals("n")) { continue; }
                acc.addBudget(name, budget);
                System.out.printf("Budget Added.\n");
            } else if (s.equals("4")) { // Delete Budget
                if (subAccounts == null || subAccounts.isEmpty()) {
                    System.out.println("There are no budgets to delete. Create a budget first.");
                    continue;
                }
                System.out.println("Which budget would you like to delete?");
                s = in.nextLine();
                while (s.equals("")) {
                    s = in.nextLine();
                }
                boolean found = subAccounts.remove(new BudgetAmt(s));
                while (!found) {
                    System.out.println("No budget exists under that name. Please enter the name of an existing budget or type 'exit' to exit.");
                    s = in.nextLine();
                    if (s.equals("exit")) {
                        break;
                    }
                    found = subAccounts.remove(new BudgetAmt(s));
                }
                if (found) {
                    System.out.println("Budget removed.");
                }
            } else if (s.equals("5")) { // Exit
                break;
            }
        }
    }

    private static void savingsInterface(Scanner in, Account acc) {
        while (true) {
            System.out.println("\n-----------------SAVINGS------------------");
            System.out.printf("Total Account Balance: %.2f\n", acc.getBalance());
            LinkedList<BudgetAmt> subAccounts = acc.getsubAccounts();
            if (subAccounts != null && !subAccounts.isEmpty()) {
                System.out.printf("Budgets:                   \n");
                String printString;
                float budgetTotal = 0;
                for (BudgetAmt subAccount : subAccounts) {
                    printString = "    " + subAccount.name;
                    for (int i = 0; i < 15 - subAccount.name.length(); i++) {
                        printString += " ";
                    }
                    System.out.printf("%s: %.2f\n", printString, subAccount.amount);
                    budgetTotal += subAccount.amount;
                }
                System.out.printf("Balance After Budgets: %.2f\n", acc.getBalance() - budgetTotal);

            }
            System.out.println("------------------------------------------\n");
            System.out.println("<Press (1) to make a deposit, (2) for a withdrawal, (3) to create a new budget, or (4) to delete a budget, or (5) to exit.>");
            String s;
            while (true) {
                s = in.nextLine();
                if (s.equals("")) {
                    continue;
                }
                if (!s.equals("1") && !s.equals("2") && !s.equals("3") && !s.equals("4") && !s.equals("5")) {
                    System.out.println("Please only enter the prompted numbers.");
                } else {
                    break;
                }
            }
            if (s.equals("1")) {
                System.out.println("How much would you like to deposit?");
                while (!in.hasNextFloat()) {
                    System.out.println("Please enter a positive numerical value.");
                    in.nextLine();
                }
                float deposit = in.nextFloat();
                while (deposit < 0) {
                    System.out.println("Do you want your money lost to the void? Positive numbers only.");
                    deposit = in.nextFloat();
                }
                acc.deposit(deposit);
            } else if (s.equals("2")) {
                System.out.println("How much would you like to withdraw?");
                while (!in.hasNextFloat()) {
                    System.out.println("Please enter a positive numerical value.");
                    in.nextLine();
                }
                float withdrawal = in.nextFloat();
                while (withdrawal < 0) {
                    System.out.println("Think robbing a bank is that easy? Positive numbers only.");
                    withdrawal = in.nextFloat();
                }
                acc.withdraw(withdrawal);
            } else if (s.equals("3")){
                System.out.println("What would you like to name your budget? (1-15 characters)");
                String name = in.nextLine();
                while (name.length() == 0) {
                    System.out.println("Please enter between 1 and 15 characters.");
                    name = in.nextLine();
                }
                while (name.length() > 15) {
                    System.out.println("Please enter between 1 and 15 characters.");
                    name = in.nextLine();
                }
                System.out.println("How much would you like to budget from your account?");
                while (!in.hasNextFloat()) {
                    System.out.println("Please enter a positive numerical value.");
                    in.nextLine();
                }
                float budget = in.nextFloat();
                while (budget < 0) {
                    System.out.println("Think robbing a bank is that easy? Positive numbers only.");
                    budget = in.nextFloat();
                }
                if (acc.getBalance() - budget < 0) {
                    System.out.println("<WARNING: This budget will exceed your account balance. Proceed? (y/n)>");
                    s = in.nextLine();
                    while (!s.equals("y") && !s.equals("n")) {
                        s = in.nextLine();
                    }
                }
                if (s.equals("n")) { continue; }
                acc.addBudget(name, budget);
                System.out.printf("Budget Added.\n");
            } else if (s.equals("4")) {
                if (subAccounts == null || subAccounts.isEmpty()) {
                    System.out.println("There are no budgets to delete. Create a budget first.");
                    continue;
                }
                System.out.println("Which budget would you like to delete?");
                s = in.nextLine();
                while (s.equals("")) {
                    s = in.nextLine();
                }
                boolean found = subAccounts.remove(new BudgetAmt(s));
                while (!found) {
                    System.out.println("No budget exists under that name. Please enter the name of an existing budget or type 'exit' to exit.");
                    s = in.nextLine();
                    if (s.equals("exit")) {
                        break;
                    }
                    found = subAccounts.remove(new BudgetAmt(s));
                }
                if (found) {
                    System.out.println("Budget removed.");
                }
            } else if (s.equals("5")) {
                break;
            }
        }
    }

    private static ArrayList<Account> readFromFile(String filepath) {
        try {
            FileInputStream f = new FileInputStream(new File(filepath));
            ObjectInputStream o = new ObjectInputStream(f);
            ArrayList<Account> acc = (ArrayList<Account>) o.readObject();
            o.close();
            f.close();
            return acc;
        } catch (FileNotFoundException e) {
            if (filepath.equals("accounts.ser")) {
                ArrayList<Account> accounts = new ArrayList<>();
                accounts.add(new Account("Checking"));
                accounts.add(new Account("Savings"));
                return accounts;
            } else {
                System.out.println("Invalid filepath.");
            }
        } catch (IOException e) {
            System.out.println("IO Input Stream Error.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");
            e.printStackTrace();
        }
        return null;
    }

    private static void saveAccounts(ArrayList<Account> accounts) {
        try {
            FileOutputStream f = new FileOutputStream(new File("accounts.ser"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(accounts);
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
