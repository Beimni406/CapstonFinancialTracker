package com.pluralsight;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Capstone skeleton – personal finance tracker.
 * ------------------------------------------------
 * File format  (pipe-delimited)
 *     yyyy-MM-dd|HH:mm:ss|description|vendor|amount
 * A deposit has a positive amount; a payment is stored
 * as a negative amount.
 */
public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    /* ------------------------------------------------------------------
       File I/O
       ------------------------------------------------------------------ */
    public static void loadTransactions(String fileName) {
        File file = new File(fileName);

        // 1️⃣ Create file if it doesn’t exist
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("New transactions file created: " + fileName);
            } catch (Exception e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
            return; // Stop here since there’s nothing to read yet
        }

        // 2️⃣ Read existing transactions
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip blank lines

                // Split each line into 5 parts
                String[] parts = line.split("\\|");
                if (parts.length != 5) continue; // skip invalid lines

                // Parse the data
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                // Create a transaction and add to list
                transactions.add(new Transaction(date, time, description, vendor, amount));
            }

            System.out.println("Loaded " + transactions.size() + " transaction(s) from file.");

        } catch (Exception e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
    }


    /* ------------------------------------------------------------------
       Add new transactions
       ------------------------------------------------------------------ */
    private static void addDeposit(Scanner scanner) {
        try {
            System.out.print("Enter date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Enter time (HH:mm:ss): ");
            LocalTime time = LocalTime.parse(scanner.nextLine());

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            // Validate that deposit is positive
            if (amount <= 0) {
                System.out.println("⚠️  Amount must be positive!");
                return;
            }

            // Create a new transaction object
            Transaction t = new Transaction(date, time, description, vendor, amount);
            transactions.add(t);

            // Save it to the CSV file
            try (java.io.BufferedWriter writer =
                         new java.io.BufferedWriter(new java.io.FileWriter(FILE_NAME, true))) {
                String line = String.format("%s|%s|%s|%s|%.2f",
                        date, time, description, vendor, amount);
                writer.newLine();
                writer.write(line);
            }

            System.out.println("✅ Deposit added successfully!\n");

        } catch (Exception e) {
            System.out.println("⚠️  Error adding deposit: " + e.getMessage());
        }
    }


    private static void addPayment(Scanner scanner) {
        try {
            System.out.print("Enter date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Enter time (HH:mm:ss): ");
            LocalTime time = LocalTime.parse(scanner.nextLine());

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            // Validate that the user enters a positive number
            if (amount <= 0) {
                System.out.println("⚠️  Amount must be positive!");
                return;
            }

            // Convert amount to negative (since it's a payment)
            amount = -amount;

            // Create a new Transaction object
            Transaction t = new Transaction(date, time, description, vendor, amount);
            transactions.add(t);

            // Save the payment to file
            try (java.io.BufferedWriter writer =
                         new java.io.BufferedWriter(new java.io.FileWriter(FILE_NAME, true))) {
                String line = String.format("%s|%s|%s|%s|%.2f",
                        date, time, description, vendor, amount);
                writer.newLine();
                writer.write(line);
            }

            System.out.println("✅ Payment added successfully!\n");

        } catch (Exception e) {
            System.out.println("⚠️  Error adding payment: " + e.getMessage());
        }
    }

    /* ------------------------------------------------------------------
       Ledger menu
       ------------------------------------------------------------------ */
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("========================================");
            System.out.println("                LEDGER MENU             ");
            System.out.println("========================================");
            System.out.println("A) View All Transactions");
            System.out.println("D) View Deposits Only");
            System.out.println("P) View Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("⚠️ Invalid option. Please try again.");
            }
        }
    }






    /* ------------------------------------------------------------------
       Reports menu
       ------------------------------------------------------------------ */
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========================================");
            System.out.println("              REPORTS MENU              ");
            System.out.println("========================================");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> { // Month To Date
                    LocalDate now = LocalDate.now();
                    LocalDate start = now.withDayOfMonth(1);
                    filterTransactionsByDate(start, now);
                }
                case "2" -> { // Previous Month
                    LocalDate now = LocalDate.now();
                    LocalDate firstOfPrev = now.minusMonths(1).withDayOfMonth(1);
                    LocalDate endOfPrev = firstOfPrev.plusMonths(1).minusDays(1);
                    filterTransactionsByDate(firstOfPrev, endOfPrev);
                }
                case "3" -> { // Year To Date
                    LocalDate now = LocalDate.now();
                    LocalDate start = LocalDate.of(now.getYear(), 1, 1);
                    filterTransactionsByDate(start, now);
                }
                case "4" -> { // Previous Year
                    LocalDate now = LocalDate.now();
                    LocalDate start = LocalDate.of(now.getYear() - 1, 1, 1);
                    LocalDate end = LocalDate.of(now.getYear() - 1, 12, 31);
                    filterTransactionsByDate(start, end);
                }
                case "5" -> { // Search by Vendor
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine();
                    filterTransactionsByVendor(vendor);
                }
                case "6" -> customSearch(scanner); // Bonus feature
                case "0" -> running = false;
                default -> System.out.println("⚠️ Invalid option. Please try again.");
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        System.out.printf("%-12s %-10s %-25s %-20s %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------");

        for (Transaction t : transactions) {
            if ((t.getDate().isEqual(start) || t.getDate().isAfter(start)) &&
                    (t.getDate().isEqual(end)   || t.getDate().isBefore(end))) {
                System.out.printf("%-12s %-10s %-25s %-20s %10.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
    }
    private static void filterTransactionsByVendor(String vendor) {
        System.out.printf("%-12s %-10s %-25s %-20s %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------");

        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(vendor.trim())) {
                System.out.printf("%-12s %-10s %-25s %-20s %10.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
    }

    /* ------------------------------------------------------------------
       Utility parsers
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) { return null; }
    private static Double parseDouble(String s) { return null; }
}
