package springboot.security.service;

import springboot.security.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    Role findByName(String name);
}