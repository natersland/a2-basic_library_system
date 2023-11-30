package features;

import common.StringValue;
import data.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
    private final List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    private void welcomeMessage() {
        System.out.println(StringValue.WELCOME);
    }

    public void showMenu() {
        welcomeMessage();

        Scanner scanner = new Scanner(System.in);

        int choice = 0;

        while (true) {
            System.out.println("1. Show all books");
            System.out.println("2. Add book");
            System.out.println("3. Borrow book");
            System.out.println("4. Return book");
            System.out.println("5. Exit");

            try {
                System.out.println(StringValue.ENTER_YOUR_CHOICE);
                choice = scanner.nextInt();
                break;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        }

        switch (choice) {
            case 1 -> showBooks();
            case 2 -> addBook();
            case 3 -> borrowBook();
            case 4 -> returnBook();
            case 5 -> {
                System.out.println("Exiting the program. Goodbye!");
                System.exit(0);
            }
            default -> System.out.println(StringValue.WRONG_INPUT);
        }

        scanner.close();
    }


    private void showBooks() {
        if (books.isEmpty()) {
            System.out.println(StringValue.NOT_HAVE_BOOK);
        } else {
            System.out.println(StringValue.BOOK_LIST);
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                String index = String.valueOf(i + 1);
                String title = book.getTitle();
                String capitalizedTitle = title.substring(0, 1).toUpperCase() + title.substring(1);
                String author = book.getAuthor();
                int quantity = book.getQuantity();
                System.out.println(index + ": " + capitalizedTitle + ", " + author + ", " + quantity + " " + bookText(quantity));
            }
            System.out.println("++++++++++++++++++++++++++++++");

        }
        showMenu();
    }


    private boolean isExistBook(String title, String author) {
        for (Book book : books) {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
                return true;
            }
        }
        return false;
    }

    private void addQuantityToExistBook(String title, int quantity) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                book.setQuantity(book.getQuantity() + quantity);
            }
        }
    }

    private String bookText(int quantity) {
        if (quantity > 1) {
            return StringValue.BOOKS;
        } else {
            return StringValue.BOOK;
        }
    }

    private int getQuantityFromUser(Scanner scanner, String enterBookQuantity) {
        int quantity = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println(StringValue.ENTER_BOOK_QUANTITY);

            if (scanner.hasNextInt()) {
                quantity = scanner.nextInt();
                validInput = true;
            } else {
                System.out.println(StringValue.PLEASE_ENTER_VALID_INTEGER);
                scanner.next();
            }
        }

        return quantity;
    }

    public void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(StringValue.ENTER_BOOK_TITLE);
        String title = scanner.nextLine().toLowerCase();
        System.out.println(StringValue.ENTER_BOOK_AUTHOR);
        String author = scanner.nextLine();

        int quantity = getQuantityFromUser(scanner, StringValue.ENTER_BOOK_QUANTITY);

        String addToTheLibraryText = "\n" + StringValue.ADDED + " " + title + " " + quantity + " " + bookText(quantity) + " " + StringValue.TO_THE_LIBRARY;

        if (isExistBook(title, author)) {
            System.out.println(StringValue.BOOK_ALREADY_EXIST + addToTheLibraryText);
            addQuantityToExistBook(title, quantity);
        } else {
            System.out.println(StringValue.BOOK_NOT_EXIST + addToTheLibraryText);
            Book book = new Book(title, author, quantity);
            books.add(book);
        }

        showMenu();
        scanner.close();
    }


    private void handleBookAction(String actionPrompt, String successMessage, boolean borrow) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(StringValue.ENTER_BOOK_TITLE);
        String title = scanner.nextLine().toLowerCase();

        int quantity = getQuantityFromUser(scanner, StringValue.ENTER_BOOK_QUANTITY);

        boolean bookFound = false;

        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                bookFound = true;
                if (!borrow || book.getQuantity() >= quantity) {
                    int newQuantity = borrow ? (book.getQuantity() - quantity) : (book.getQuantity() + quantity);
                    book.setQuantity(newQuantity);
                    System.out.println(successMessage + " " + title + " " + quantity + " " + bookText(quantity));
                } else {
                    System.out.println(StringValue.NOT_ENOUGH_BOOK);
                }
                break;
            }
        }

        if (!bookFound) {
            System.out.println(StringValue.BOOK_NOT_EXIST);
        }

        showMenu();
    }

    public void borrowBook() {
        handleBookAction(StringValue.ENTER_BOOK_QUANTITY, StringValue.YOU_BORROWED, true);
    }

    public void returnBook() {
        handleBookAction(StringValue.ENTER_BOOK_QUANTITY, StringValue.SUCCESSFUL_RETURN, false);
    }

}


