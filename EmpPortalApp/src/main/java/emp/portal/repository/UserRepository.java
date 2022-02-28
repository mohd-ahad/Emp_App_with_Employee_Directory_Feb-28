package emp.portal.repository;

import emp.portal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 User findByEmail(String email);
	   // Optional<User> findByUsernameOrEmail(String email);
	    Boolean existsByEmail(String email);

}
