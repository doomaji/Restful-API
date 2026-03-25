package springboot.security.controller;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.security.model.Role;
import springboot.security.model.User;
import springboot.security.service.DuplicateUsernameException;
import springboot.security.service.UserService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class MyRestController {

    private final UserService userService;

    public MyRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            List<Long> roleIds = user.getRoles() != null
                    ? user.getRoles().stream().map(Role::getId).collect(Collectors.toList())
                    : new ArrayList<>();

            User saved = userService.saveUser(user, roleIds);

            return ResponseEntity
                    .created(URI.create("/api/users/" + saved.getId()))
                    .body(saved);
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User existing = userService.getUser(id);

            if (existing == null) {
                return ResponseEntity.notFound().build();
            }

            existing.setUsername(user.getUsername());
            existing.setFirstName(user.getFirstName());
            existing.setLastName(user.getLastName());
            existing.setEmail(user.getEmail());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existing.setPassword(user.getPassword());
            }

            List<Long> roleIds = user.getRoles() != null
                    ? user.getRoles().stream().map(Role::getId).collect(Collectors.toList())
                    : null;

            User saved = userService.saveUser(existing, roleIds);
            return ResponseEntity.ok(saved);
        } catch (DuplicateUsernameException | IncorrectResultSizeDataAccessException e) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User existing = userService.getUser(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}