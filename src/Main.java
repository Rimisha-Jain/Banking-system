import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("\n--- BANKING SYSTEM ---");

        do {
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Create New Account");
            System.out.println("5. Login with account number");
            System.out.println("6. Exit");

 

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                scanner.next(); // discard invalid input
            }

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    BankOperations.checkBalance();
                    break;
                case 2:
                    BankOperations.deposit();
                    break;
                case 3:
                    BankOperations.withdraw();
                    break;
                case 4:
                    System.out.print("Enter new account holder's name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    double initBal = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    BankOperations.createAccount(newName, initBal);
                    break;
                case 5:
                    BankOperations.loginWithAccountNumber(scanner);
                    break;
    
                case 6:
                    System.out.println("Thank you for using the banking system!");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose between 1 and 4.");
            }
        } while (choice != 4);

        scanner.close();
    }
}

