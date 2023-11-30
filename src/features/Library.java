package features;

import common.StringValue;
import data.BookEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
    private final List<BookEntity> bookEntities;

    public Library() {
        this.bookEntities = new ArrayList<>();
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
                System.out.println(StringValue.PLEASE_ENTER_VALID_INTEGER);
                scanner.nextLine();
            }
        }

        switch (choice) {
            case 1 -> showBooks();
            case 2 -> addBook();
            case 3 -> borrowBook();
            case 4 -> returnBook();
            case 5 -> {
                System.out.println(StringValue.EXIT_PROGRAM);
                System.exit(0);
            }
            default -> System.out.println(StringValue.WRONG_INPUT);
        }

        scanner.close();
    }


    private void showBooks() {
        if (bookEntities.isEmpty()) {
            System.out.println(StringValue.NOT_HAVE_BOOK);
        } else {
            System.out.println(StringValue.BOOK_LIST);
            for (int i = 0; i < bookEntities.size(); i++) {
                BookEntity bookEntity = bookEntities.get(i);
                String index = String.valueOf(i + 1);
                String title = bookEntity.getTitle();
                String capitalizedTitle = title.substring(0, 1).toUpperCase() + title.substring(1);
                String author = bookEntity.getAuthor();
                int quantity = bookEntity.getQuantity();
                System.out.println(index + ": " + capitalizedTitle + ", " + author + ", " + quantity + " " + bookText(quantity));
            }
            System.out.println("++++++++++++++++++++++++++++++");

        }
        showMenu();
    }


    private boolean isExistBook(String title, String author) {
        for (BookEntity bookEntity : bookEntities) {
            if (bookEntity.getTitle().equals(title) && bookEntity.getAuthor().equals(author)) {
                return true;
            }
        }
        return false;
    }

    private void addQuantityToExistBook(String title, int quantity) {
        for (BookEntity bookEntity : bookEntities) {
            if (bookEntity.getTitle().equals(title)) {
                bookEntity.setQuantity(bookEntity.getQuantity() + quantity);
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

    private int getQuantityFromUser(Scanner scanner) {
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

    private void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(StringValue.ENTER_BOOK_TITLE);
        String title = scanner.nextLine().toLowerCase();
        System.out.println(StringValue.ENTER_BOOK_AUTHOR);
        String author = scanner.nextLine();

        int quantity = getQuantityFromUser(scanner);

        String addToTheLibraryText = "\n" + StringValue.ADDED + " " + title + " " + quantity + " " + bookText(quantity) + " " + StringValue.TO_THE_LIBRARY;

        if (isExistBook(title, author)) {
            System.out.println(StringValue.BOOK_ALREADY_EXIST + addToTheLibraryText);
            addQuantityToExistBook(title, quantity);
        } else {
            System.out.println(StringValue.BOOK_NOT_EXIST + addToTheLibraryText);
            BookEntity bookEntity = new BookEntity(title, author, quantity);
            bookEntities.add(bookEntity);
        }

        showMenu();
        scanner.close();
    }


    private void handleBookAction(String successMessage, boolean borrow) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(StringValue.ENTER_BOOK_TITLE);
        String title = scanner.nextLine().toLowerCase();

        int quantity = getQuantityFromUser(scanner);

        boolean bookFound = false;

        for (BookEntity bookEntity : bookEntities) {
            if (bookEntity.getTitle().equals(title)) {
                bookFound = true;
                if (!borrow || bookEntity.getQuantity() >= quantity) {
                    int newQuantity = borrow ? (bookEntity.getQuantity() - quantity) : (bookEntity.getQuantity() + quantity);
                    bookEntity.setQuantity(newQuantity);
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

    private void borrowBook() {
        handleBookAction(StringValue.YOU_BORROWED, true);
    }

    private void returnBook() {
        handleBookAction(StringValue.SUCCESSFUL_RETURN, false);
    }

}


