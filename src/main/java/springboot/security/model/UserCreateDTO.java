package springboot.security.model;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class UserCreateDTO {
    @NotBlank(message = "Поле не может быть пустым")
    private String firstName;

    @NotBlank(message = "Поле не может быть пустым")
    private String lastName;

    @NotBlank(message = "Поле не может быть пустым")
    private String email;

    @NotBlank(message = "Поле не может быть пустым")
    private String username;

    @NotBlank(message = "Поле не может быть пустым")
    private String password;

    private List<Long> roleIds;

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

