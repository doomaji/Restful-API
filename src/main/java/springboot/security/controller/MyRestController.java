package springboot.security.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springboot.security.model.Role;
import springboot.security.model.User;
import springboot.security.model.UserCreateDTO;
import springboot.security.model.UserUpdateDTO;
import springboot.security.service.DuplicateUsernameException;
import springboot.security.service.UserService;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO userDto, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User user = new User();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());

            List<Long> roleIds = userDto.getRoleIds() != null
                    ? userDto.getRoleIds()
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
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userDto, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        User existing = userService.getUser(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User user = new User();
            user.setId(id);

            if (userDto.getFirstName() != null) {
                user.setFirstName(userDto.getFirstName());
            } else {
                user.setFirstName(existing.getFirstName());
            }

            if (userDto.getLastName() != null) {
                user.setLastName(userDto.getLastName());
            } else {
                user.setLastName(existing.getLastName());
            }

            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            } else {
                user.setEmail(existing.getEmail());
            }

            if (userDto.getUsername() != null && !userDto.getUsername().trim().isEmpty()) {

                if (!userDto.getUsername().equals(existing.getUsername()) &&
                        userService.existsByUsernameAndIdNot(userDto.getUsername(), id)) {
                    errors.put("username", "Пользователь с таким именем уже существует");
                    return ResponseEntity.badRequest().body(errors);
                }
                user.setUsername(userDto.getUsername());
            } else if (userDto.getUsername() != null && userDto.getUsername().trim().isEmpty()) {
                errors.put("username", "Имя пользователя не может быть пустой строкой");
                return ResponseEntity.badRequest().body(errors);
            } else {
                user.setUsername(existing.getUsername());
            }

            if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
                user.setPassword(userDto.getPassword());
            }

            List<Long> roleIds = userDto.getRoleIds();
            if (roleIds == null) {
                roleIds = existing.getRoles().stream()
                        .map(Role::getId)
                        .toList();
            }

            User saved = userService.saveUser(user, roleIds);
            return ResponseEntity.ok(saved);

        } catch (DuplicateUsernameException | IncorrectResultSizeDataAccessException e) {
            errors.put("username", "Пользователь с таким именем уже существует");
            return ResponseEntity.badRequest().body(errors);
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