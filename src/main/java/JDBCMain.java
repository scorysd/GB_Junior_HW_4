import java.sql.*;


public class JDBCMain {
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:database.db");
        createTable(connection);
        insertBook(connection);
        printBook(connection);
    }

    public static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("create table books (id bigint, name varchar(255), author varchar(255)) ");
        }
    }

    public static void insertBook(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate("""
                    insert into books(id, name, author) 
                    values(1, 'Book #1', 'Author #1'),
                          (2, 'Book #2', 'Author #2'),
                          (3, 'Book #3', 'Author #3'),
                          (4, 'Book #4', 'Author #1'),
                          (5, 'Book #5', 'Author #2'),
                          (6, 'Book #6', 'Author #1'),
                          (7, 'Book #7', 'Author #3'),
                          (8, 'Book #8', 'Author #1'),
                          (9, 'Book #9', 'Author #3'),
                          (10, 'Book #10', 'Author #1')""");
            System.out.println(updatedRows);
        }
    }

    public static void printBook(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, name, author from books where author = 'Author #1'");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                System.out.printf("Книга №%d - название: %s, автор: %s\n", id, name, author);
            }
        }
    }
}
