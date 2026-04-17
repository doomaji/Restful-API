package springboot.security.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserUpdateDTO {

    @Pattern(
            regexp = "^[^0-9]+$",
            message = "Поле не должно содержать цифр"
    )
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String firstName;

    @Pattern(
            regexp = "^[^0-9]+$",
            message = "Поле не должно содержать цифр"
    )
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    private String lastName;

    @NotBlank(message = "Поле не может быть пустым")
    @Email(message = "Неверный email")
    private String email;

    @Size(min = 3, max = 30, message = "Имя пользователя должно содержать от 3 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Имя пользователя может содержать только буквы, цифры, точки, дефисы и нижнее подчеркивание")
    private String username;

    @Size(min = 6, max = 100, message = "Пароль должен содержать минимум 6 символов")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$", message = "Пароль должен содержать хотя бы одну букву и одну цифру")
    private String password;

    private List<Long> roleIds;

    public UserUpdateDTO() {}

    public UserUpdateDTO(String firstName, String lastName, String email, String username, String password, List<Long> roleIds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roleIds = roleIds;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Long> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
}