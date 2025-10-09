package ra.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.com.model.User;

public interface IUser extends JpaRepository<User,Long> {
    User findUserByUsername(String username);
}
