//import java.io.*;
//import java.util.ArrayList;
//
//public class ExpenseStorage {
//    private static final String FILENAME = "expenses.txt";
//
//    public static void saveExpenses(ArrayList<Expense> expenses) {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
//            for (Expense e : expenses) {
//                writer.println(e.getDate() + "," + e.getDescription() + "," + e.getAmount());
//            }
//        } catch (IOException e) {
//            System.out.println("Error saving expenses: " + e.getMessage());
//        }
//    }
//
//    public static ArrayList<Expense> loadExpenses() {
//        ArrayList<Expense> expenses = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                expenses.add(new Expense(parts[0], parts[1], Double.parseDouble(parts[2])));
//            }
//        } catch (IOException e) {
//            System.out.println("Error loading expenses: " + e.getMessage());
//        }
//        return expenses;
//    }
//}

import java.sql.*;
import java.util.ArrayList;

public class ExpenseStorage {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/JAVA_MiniProj";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "202201495";

    public static void saveExpenses(ArrayList<Expense> expenses) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO expenses (date, description, amount) VALUES (?, ?, ?)")) {
            for (Expense e : expenses) {
                pstmt.setString(1, e.getDate());
                pstmt.setString(2, e.getDescription());
                pstmt.setDouble(3, e.getAmount());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }
    public static void deleteAllExpenses() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE expenses");
        } catch (SQLException e) {
            System.out.println("Error deleting expenses: " + e.getMessage());
        }
    }

    public static ArrayList<Expense> loadExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM expenses")) {
            while (rs.next()) {
                String date = rs.getString("date");
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                expenses.add(new Expense(date, description, amount));
            }
        } catch (SQLException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
        return expenses;
    }
}
