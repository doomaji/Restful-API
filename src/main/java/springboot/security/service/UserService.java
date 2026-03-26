package springboot.security.service;

import springboot.security.model.Role;
import springboot.security.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUser(Long id);
    User saveUser(User user, List<Long> roleIds);
    void deleteUser(Long id);
    User getByUsername(String username);
    List<Role> getAllRoles();
    boolean usernameExists(String username);
    boolean existsByUsernameAndIdNot(String username, Long id);
}