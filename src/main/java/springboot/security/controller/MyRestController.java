package springboot.security.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.security.model.Role;
import springboot.security.model.User;
import springboot.security.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
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
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user, List<Long> roleId) {
        User saved = userService.saveUser(user, roleId);
        return ResponseEntity
                .created(URI.create("api/users" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existing = userService.getUser(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setUsername(user.getUsername());
        List<Long> roleIds = user.getRoles() != null
                ? user.getRoles().stream().map(Role::getId).collect(Collectors.toList())
                : null;

        User saved = userService.saveUser(existing, roleIds);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User existing = userService.getUser(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
