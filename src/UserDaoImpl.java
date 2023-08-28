import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Set;

import static java.sql.DriverManager.getConnection;

public class UserDaoImpl implements DAO {
    @Override
    public Optional<User> getUser(int id) {
        try (Statement statement = ConnectionFactory.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE id =" + id)
        ) {
            if (resultSet.next()) {
                return Optional.of(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        resultSet.getInt("age")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    @Override
    public Set<User> getAllUsers() {
        return null;
    }
}
