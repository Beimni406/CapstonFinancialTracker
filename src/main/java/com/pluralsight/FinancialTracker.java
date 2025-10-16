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
        // TODO: Step 5 – Implement the Ledger menu
    }

    /* ------------------------------------------------------------------
       Reports menu
       ------------------------------------------------------------------ */
    private static void reportsMenu(Scanner scanner) {
        // TODO: Step 6 – Implement the Reports menu
    }

    /* ------------------------------------------------------------------
       Utility parsers
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) { return null; }
    private static Double parseDouble(String s) { return null; }
}
