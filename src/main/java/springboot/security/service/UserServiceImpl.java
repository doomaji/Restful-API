package springboot.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.security.jpa.RoleRepository;
import springboot.security.jpa.UserRepository;
import springboot.security.model.Role;
import springboot.security.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    @Override
    public User saveUser(User incoming, List<Long> roleIds) {

        if (userRepository.existsByUsername(incoming.getUsername())) {

            if (incoming.getId() == null) {
                throw new DuplicateUsernameException("Username already exists");
            }

            User existing = userRepository.findByUsername(incoming.getUsername()).orElse(null);
            if (existing != null && !existing.getId().equals(incoming.getId())) {
                throw new DuplicateUsernameException("Username already exists");
            }
        }
        Set<Role> roles = new HashSet<>();
        if (roleIds != null && !roleIds.isEmpty()) {
            roles.addAll(roleRepository.findAllById(roleIds));
        }
        incoming.setRoles(roles);

        if (incoming.getId() == null) {
            incoming.setPassword(passwordEncoder.encode(incoming.getPassword()));
            userRepository.save(incoming);
            return incoming;
        }

        User db = userRepository.findById(incoming.getId())
                .orElseThrow(() -> new RuntimeException("User not found: " + incoming.getId()));

        db.setFirstName(incoming.getFirstName());
        db.setLastName(incoming.getLastName());
        db.setEmail(incoming.getEmail());
        db.setUsername(incoming.getUsername());
        db.setRoles(roles);

        if (incoming.getPassword() != null && !incoming.getPassword().isBlank()) {
            db.setPassword(passwordEncoder.encode(incoming.getPassword()));
        }

        userRepository.save(db);
        return db;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}