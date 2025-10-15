package com.pluralsight;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FinancialTracker {

    // ------------------------------------------------------------------
    // Shared data and formatters
    // ------------------------------------------------------------------
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    // ------------------------------------------------------------------
    // Main method and menu
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        java.util.Scanner scanner = new java.util.Scanner(System.in);
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

    // ------------------------------------------------------------------
    // Temporary method stubs (to remove errors)
    // ------------------------------------------------------------------
    private static void loadTransactions(String fileName) { }
    private static void addDeposit(java.util.Scanner scanner) { }
    private static void addPayment(java.util.Scanner scanner) { }
    private static void ledgerMenu(java.util.Scanner scanner) { }
}
