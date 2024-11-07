import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ExpenseTracker is a console-based Java application that allows users to track their expenses.
 * Users can add, view, and delete expenses, with data saved persistently in a text file.
 */
public class ExpenseTracker {
    // The name of the file where expenses will be saved and loaded.
    private static final String FILE_NAME = "expenses.txt";
    // A list to hold all the expenses in memory.
    private static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        // Load existing expenses from the file at startup.
        loadExpenses();

        // Initialize the scanner for user input.
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Expense Tracker ===");

        // Main application loop.
        while (true) {
            // Display the menu options to the user.
            System.out.println("\nMenu:");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. Exit");

            // Prompt the user to select an option.
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline character.

            // Handle the user's choice.
            switch (choice) {
                case 1 -> addExpense(scanner); // Add a new expense.
                case 2 -> viewExpenses(); // View all expenses.
                case 3 -> deleteExpense(scanner); // Delete an expense by its index.
                case 4 -> {
                    saveExpenses(); // Save all expenses to the file before exiting.
                    System.out.println("Exiting. Goodbye!");
                    return; // Exit the program.
                }
                default -> System.out.println("Invalid option. Try again."); // Handle invalid inputs.
            }
        }
    }

    /**
     * Adds a new expense to the list.
     *
     * @param scanner The scanner object for reading user input.
     */
    private static void addExpense(Scanner scanner) {
        System.out.print("Enter expense description: ");
        String description = scanner.nextLine(); // Read the description of the expense.
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble(); // Read the expense amount.
        scanner.nextLine(); // Consume the leftover newline character.

        // Add the new expense to the list.
        expenses.add(new Expense(description, amount));
        System.out.println("Expense added successfully!");
    }

    /**
     * Displays all expenses in the list with their indices.
     */
    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            System.out.println("\n=== Expenses ===");
            // Iterate through the list of expenses and display each one.
            for (int i = 0; i < expenses.size(); i++) {
                System.out.println((i + 1) + ". " + expenses.get(i));
            }
        }
    }

    /**
     * Deletes an expense from the list by its index.
     *
     * @param scanner The scanner object for reading user input.
     */
    private static void deleteExpense(Scanner scanner) {
        viewExpenses(); // Display all expenses to help the user choose which one to delete.
        if (!expenses.isEmpty()) {
            System.out.print("Enter the number of the expense to delete: ");
            int index = scanner.nextInt() - 1; // Adjust for 0-based indexing.
            scanner.nextLine(); // Consume the leftover newline character.

            // Validate the index and remove the corresponding expense.
            if (index >= 0 && index < expenses.size()) {
                expenses.remove(index);
                System.out.println("Expense deleted successfully!");
            } else {
                System.out.println("Invalid number. No expense deleted.");
            }
        }
    }

    /**
     * Loads expenses from the file into the list at startup.
     */
    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            // Read each line from the file.
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2); // Split the line into description and amount.
                if (parts.length == 2) {
                    String description = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    expenses.add(new Expense(description, amount)); // Add the expense to the list.
                }
            }
        } catch (IOException e) {
            System.out.println("No previous expenses found. Starting fresh.");
        }
    }

    /**
     * Saves all expenses from the list into the file.
     */
    private static void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // Write each expense to the file in CSV format.
            for (Expense expense : expenses) {
                writer.write(expense.getDescription() + "," + expense.getAmount());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }
}

/**
 * Represents a single expense with a description and an amount.
 */
class Expense {
    private final String description;
    private final double amount;

    // Constructor to initialize the expense object.
    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    // Getter for the description.
    public String getDescription() {
        return description;
    }

    // Getter for the amount.
    public double getAmount() {
        return amount;
    }

    // Override the toString method to display the expense details.
    @Override
    public String toString() {
        return description + " - $" + amount;
    }
}
