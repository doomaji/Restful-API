package springboot.security.service;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException() { super("Пользователь с таким именем уже существует"); }
}
