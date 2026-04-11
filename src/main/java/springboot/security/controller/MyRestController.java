package springboot.security.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springboot.security.model.Role;
import springboot.security.model.User;
import springboot.security.service.DuplicateUsernameException;
import springboot.security.service.UserService;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
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
        } catch (ValidationException e) {
            errors.put("validation", e.getMessage());
            return ResponseEntity.badRequest().body(errors);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            User existing = userService.getUser(id);

            if (existing == null) {
                return ResponseEntity.notFound().build();
            }

            String newUsername = user.getUsername();
            String currentUsername = existing.getUsername();

            if (!newUsername.equals(currentUsername)) {

                if (userService.existsByUsernameAndIdNot(newUsername, id)) {
                    return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
                }
            }

            user.setId(id);

            List<Long> roleIds = user.getRoles() != null
                    ? user.getRoles().stream().map(Role::getId).collect(Collectors.toList())
                    : null;

            User saved = userService.saveUser(user, roleIds);
            return ResponseEntity.ok(saved);
        } catch (DuplicateUsernameException | IncorrectResultSizeDataAccessException e) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        } catch (ValidationException e) {
            errors.put("validation", e.getMessage());
            return ResponseEntity.badRequest().body(errors);
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