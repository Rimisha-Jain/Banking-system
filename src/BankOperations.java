import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class BankOperations {

    private static final Scanner scanner = new Scanner(System.in);

    public static void checkBalance() {
        System.out.print("Enter account holder's name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT balance FROM accounts WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Balance: ₹" + rs.getDouble("balance"));
            } else {
                System.out.println("Error: No such user '" + name + "' found in the database.");
            }

        } catch (SQLException e) {
            System.out.println("Error checking balance: " + e.getMessage());
        }
    }

    public static void deposit() {
        System.out.print("Enter account holder's name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE accounts SET balance = balance + ? WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, name);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Amount deposited successfully.");
            } else {
                System.out.println("Error: No such user '" + name + "' found in the database.");
            }

        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    public static void withdraw() {
        System.out.print("Enter account holder's name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Check if account exists and get balance
            String checkSql = "SELECT balance FROM accounts WHERE name = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    String withdrawSql = "UPDATE accounts SET balance = balance - ? WHERE name = ?";
                    PreparedStatement withdrawStmt = conn.prepareStatement(withdrawSql);
                    withdrawStmt.setDouble(1, amount);
                    withdrawStmt.setString(2, name);
                    withdrawStmt.executeUpdate();
                    System.out.println("Amount withdrawn successfully.");
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Error: No such user '" + name + "' found in the database.");
            }

        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }

    public static void createAccount(String name, double initialBalance) {
    if (name == null || name.trim().isEmpty()) {
        System.out.println("Account name cannot be empty.");
        return;
    }

    if (initialBalance < 0) {
        System.out.println("Initial balance cannot be negative.");
        return;
    }

    String accountNumber = generateUniqueAccountNumber();
    String query = "INSERT INTO accounts (name, balance, account_number) VALUES (?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, name);
        stmt.setDouble(2, initialBalance);
        stmt.setString(3, accountNumber);
        int rows = stmt.executeUpdate();
        if (rows > 0) {
        System.out.println("\n✅ Account created successfully!");
        System.out.println("📄 Account Number: " + accountNumber);
        System.out.println("👤 Account Holder: " + name);
        } else {
        System.out.println("❌ Account creation failed.");
        }

    } catch (SQLException e) {
        System.out.println("Error creating account: " + e.getMessage());
    }
}

// Utility method to generate a unique 8-digit account number
private static String generateUniqueAccountNumber() {
    Random rand = new Random();
    String accountNumber;
    boolean isUnique = false;

    try (Connection conn = DBConnection.getConnection()) {
        while (!isUnique) {
            accountNumber = String.valueOf(10000000 + rand.nextInt(90000000)); // 8-digit number
            String checkQuery = "SELECT id FROM accounts WHERE account_number = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, accountNumber);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    isUnique = true;
                    return accountNumber;
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Error generating account number: " + e.getMessage());
    }
    return null;
}

public static void loginWithAccountNumber(Scanner scanner) {
    System.out.print("Enter your account number: ");
    String accNumber = scanner.nextLine().trim();

    if (accNumber.isEmpty()) {
        System.out.println("Account number cannot be empty.");
        return;
    }

    String sql = "SELECT * FROM accounts WHERE account_number = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, accNumber);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("\n--- Account Details ---");
            System.out.println("Account Number: " + rs.getString("account_number"));
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Balance: ₹" + rs.getDouble("balance"));
        } else {
            System.out.println("No account found with this account number.");
        }
    } catch (SQLException e) {
        System.out.println("Error during login: " + e.getMessage());
    }
}


}


