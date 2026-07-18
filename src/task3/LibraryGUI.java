package task3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryGUI extends JFrame implements ActionListener {

    private Library library;

    private Color primaryColor = new Color(41, 84, 144);
    private Color bgColor = new Color(245, 247, 250);
    private Color cardColor = Color.WHITE;

    private JTextField isbnField;
    private JTextField titleField;
    private JTextField authorField;

    private JButton addButton;
    private JButton borrowButton;
    private JButton returnButton;
    private JButton clearButton;

    private DefaultTableModel tableModel;
    private JTable table;

    private JLabel statusLabel;

    public LibraryGUI() {
        library = new Library();
        library.addBook("101", "The Alchemist", "Paulo Coelho");
        library.addBook("102", "Clean Code", "Robert C. Martin");
        library.addBook("103", "Atomic Habits", "James Clear");

        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout(10, 10));

        add(buildTitlePanel(), BorderLayout.NORTH);
        add(buildFormPanel(), BorderLayout.WEST);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildStatusPanel(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel buildTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(primaryColor);
        panel.setPreferredSize(new Dimension(100, 60));

        JLabel label = new JLabel("Library Management System");
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(Color.WHITE);

        panel.add(label);
        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(cardColor);
        panel.setPreferredSize(new Dimension(260, 100));
        panel.setLayout(new GridLayout(9, 1, 5, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel formTitle = new JLabel("Book Details");
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel isbnLabel = new JLabel("ISBN:");
        isbnField = new JTextField();

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();

        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField();

        addButton = new JButton("Add Book");
        addButton.setBackground(primaryColor);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(this);

        borrowButton = new JButton("Borrow Book");
        borrowButton.setBackground(new Color(46, 139, 87));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.addActionListener(this);

        returnButton = new JButton("Return Book");
        returnButton.setBackground(new Color(230, 160, 40));
        returnButton.setForeground(Color.WHITE);
        returnButton.addActionListener(this);

        clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(this);

        panel.add(formTitle);
        panel.add(isbnLabel);
        panel.add(isbnField);
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(addButton);
        panel.add(borrowButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(cardColor);
        wrapper.add(panel, BorderLayout.CENTER);

        JPanel bottomButtons = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomButtons.setBackground(cardColor);
        bottomButtons.add(returnButton);
        bottomButtons.add(clearButton);
        wrapper.add(bottomButtons, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 15));

        JLabel label = new JLabel("Library Catalog");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        String[] columns = {"ISBN", "Title", "Author", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 233, 238));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(primaryColor);
        panel.setPreferredSize(new Dimension(100, 30));

        statusLabel = new JLabel("  Ready.");
        statusLabel.setForeground(Color.WHITE);
        panel.add(statusLabel, BorderLayout.WEST);

        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == addButton) {
            handleAdd();
        } else if (source == borrowButton) {
            handleBorrow();
        } else if (source == returnButton) {
            handleReturn();
        } else if (source == clearButton) {
            clearFields();
        }
    }

    private void handleAdd() {
        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();

        if (isbn.equals("") || title.equals("") || author.equals("")) {
            showError("All fields are required to add a book.");
            return;
        }

        int before = tableModel.getRowCount();
        library.addBook(isbn, title, author);
        refreshTable();

        if (tableModel.getRowCount() > before) {
            statusLabel.setText("  Book added: " + title);
            clearFields();
        } else {
            showError("A book with ISBN '" + isbn + "' already exists.");
        }
    }

    private void handleBorrow() {
        String isbn = isbnField.getText().trim();
        if (isbn.equals("")) {
            showError("Enter the ISBN of the book you want to borrow.");
            return;
        }

        Book book = findBookByIsbn(isbn);
        if (book == null) {
            showError("No book found with ISBN " + isbn);
            return;
        }
        if (!book.isAvailable()) {
            showError("'" + book.getTitle() + "' is already borrowed.");
            return;
        }

        library.borrowBook(isbn);
        refreshTable();
        statusLabel.setText("  Borrowed: " + book.getTitle());
        clearFields();
    }

    private void handleReturn() {
        String isbn = isbnField.getText().trim();
        if (isbn.equals("")) {
            showError("Enter the ISBN of the book you want to return.");
            return;
        }

        Book book = findBookByIsbn(isbn);
        if (book == null) {
            showError("No book found with ISBN " + isbn);
            return;
        }
        if (book.isAvailable()) {
            showError("'" + book.getTitle() + "' was not borrowed.");
            return;
        }

        library.returnBook(isbn);
        refreshTable();
        statusLabel.setText("  Returned: " + book.getTitle());
        clearFields();
    }

    private Book findBookByIsbn(String isbn) {
        for (Book b : library.getAllBooks()) {
            if (b.getIsbn().equals(isbn)) {
                return b;
            }
        }
        return null;
    }

    private void showError(String message) {
        statusLabel.setText("  Error: " + message);
        JOptionPane.showMessageDialog(this, message, "Action Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields() {
        isbnField.setText("");
        titleField.setText("");
        authorField.setText("");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Book book : library.getAllBooks()) {
            Object[] row = new Object[4];
            row[0] = book.getIsbn();
            row[1] = book.getTitle();
            row[2] = book.getAuthor();
            row[3] = book.isAvailable() ? "Available" : "Borrowed";
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        LibraryGUI gui = new LibraryGUI();
        gui.setVisible(true);
    }
}