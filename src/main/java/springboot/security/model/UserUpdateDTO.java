package springboot.security.model;

import jakarta.validation.constraints.Size;

import java.util.List;

public class UserUpdateDTO {
    private String username;

    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
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

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}