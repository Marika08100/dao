import java.sql.*;
import java.util.Scanner;

public class ConnectionFactory {
    Scanner scanner = new Scanner(System.in);
    Connection connection;
    public static final String URL = System.getenv("url");
    public static final String USER = System.getenv("user");
    public static final String PASSWORD = System.getenv("password");

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public void run() {
        connection = getConnection();
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    listUsers();
                    break;
                case "2":
                    findUserById();
                    break;
                case "3":
                    selectUsername();
                    break;
                case "4":
                    deleteUser();
                    break;
                case "5":
                    updateUserPassword();
                    break;
                case "6":
                    System.out.println("Kilepes");
                    break;
                default:
                    System.out.println("Ervenytelen valasz! ");
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println("Válassz egy opciót:");
        System.out.println("1. Felhasználók listája");
        System.out.println("2. Felhasználó keresése ID alapján");
        System.out.println("3. Felhasználó keresése név alapján");
        System.out.println("4. Felhasználó törlése");
        System.out.println("5. Felhasználó jelszavának módosítása");
        System.out.println("6. Kilépés");
    }

    private void listUsers() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getString("id") + ", Név: " + resultSet.getString("name"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findUserById() {
        System.out.print("Keresett felhasználó ID-je: ");
        String userId = scanner.nextLine();
        try {
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Felhasználó adatok:");
                System.out.println("ID: " + resultSet.getString("id") + ", Név: " + resultSet.getString("name") + ", Jelszó: " + resultSet.getString("password"));
            } else {
                System.out.println("Nem található ilyen felhasználó.");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void selectUsername() {
        System.out.print("Keresett névrészlet: ");
        String searchName = scanner.nextLine();
        try {
            String query = "SELECT * FROM users WHERE name LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchName + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean found = false;
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getString("id") + ", Név: " + resultSet.getString("name"));
                found = true;
            }
            if (!found) {
                System.out.println("Nem található ilyen név.");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        System.out.println("Felhasznalo ID-je akit torolni szeretnel : ");
        String userID = scanner.nextLine();
        try {
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userID);

            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("A felhasznalot sikeresen toroltuk");
            } else {
                System.out.println("Nem talalhato ilyen felhasznalo! ");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void updateUserPassword() {
        System.out.println("Felhasznalo akinek szeretned felulirni a jelszavat");
        String userID = scanner.nextLine();
        try {
            String query = "UPDATE users SET password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            System.out.println("Uj jelszo : ");
            String newPassword = scanner.nextLine();
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, userID);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Jelszo sikeresen megvaltoztatva");
            } else {
                System.out.println("Nem talalhato ilyen id");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws SQLException {
        getConnection();
        DAO userDao = new UserDaoImpl();
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.run();


    }
}
