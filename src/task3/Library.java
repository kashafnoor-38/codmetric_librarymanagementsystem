package task3;

import java.util.HashMap;
import java.util.Collection;

public class Library {
    private HashMap<String, Book> books;

    public Library() {
        books = new HashMap<String, Book>();
    }

    public void addBook(String isbn, String title, String author) {
        if (books.containsKey(isbn)) {
            System.out.println("Error: A book with this ISBN already exists.");
            return;
        }
        Book newBook = new Book(isbn, title, author);
        books.put(isbn, newBook);
        System.out.println("Book added successfully: " + title);
    }

    public void borrowBook(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            System.out.println("Error: No book found with ISBN " + isbn);
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Error: '" + book.getTitle() + "' is already borrowed.");
            return;
        }
        book.setAvailable(false);
        System.out.println("You have successfully borrowed: " + book.getTitle());
    }

    public void returnBook(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            System.out.println("Error: No book found with ISBN " + isbn);
            return;
        }
        if (book.isAvailable()) {
            System.out.println("Error: '" + book.getTitle() + "' was not borrowed, so it cannot be returned.");
            return;
        }
        book.setAvailable(true);
        System.out.println("Thank you for returning: " + book.getTitle());
    }

    public Collection<Book> getAllBooks() {
        return books.values();
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library yet.");
            return;
        }
        System.out.println("----- Library Catalog -----");
        for (Book b : books.values()) {
            System.out.println(b);
        }
        System.out.println("----------------------------");
    }
}