package task3;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Library library = new Library();
        Scanner sc = new Scanner(System.in);
        int choice;

        library.addBook("101", "The Alchemist", "Paulo Coelho");
        library.addBook("102", "Clean Code", "Robert C. Martin");

        do {
            System.out.println("===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. Display All Books");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Error: Please enter a number between 1 and 5.");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    library.addBook(isbn, title, author);
                    break;

                case 2:
                    System.out.print("Enter ISBN of book to borrow: ");
                    String borrowIsbn = sc.nextLine();
                    library.borrowBook(borrowIsbn);
                    break;

                case 3:
                    System.out.print("Enter ISBN of book to return: ");
                    String returnIsbn = sc.nextLine();
                    library.returnBook(returnIsbn);
                    break;

                case 4:
                    library.displayBooks();
                    break;

                case 5:
                    System.out.println("Exiting... Thank you!");
                    break;

                default:
                    System.out.println("Error: Invalid choice. Please select between 1 and 5.");
            }

        } while (choice != 5);

        sc.close();
    }
}