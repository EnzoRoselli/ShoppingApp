package com.shoppingapp.cli;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleInput(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public String prompt(String message) {
        out.print(message);
        return scanner.nextLine().trim();
    }
}
