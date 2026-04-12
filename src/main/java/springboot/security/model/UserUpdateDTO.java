package springboot.security.model;

import jakarta.validation.constraints.Size;

import java.util.List;

public class UserUpdateDTO {

    private final String firstName;
    private final String lastName;
    private final String email;
    private String username;

    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    private final List<Long> roleIds;

    public UserUpdateDTO(String firstName, String lastName, String email, String username, String password, List<Long> roleIds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roleIds = roleIds;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<Long> getRoleIds() {
        return roleIds;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
}