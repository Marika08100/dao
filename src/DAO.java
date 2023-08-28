import java.util.Optional;
import java.util.Set;

public interface DAO {
    Optional<User> getUser(int id);



    Set<User> getAllUsers();
}
