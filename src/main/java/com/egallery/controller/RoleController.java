
package com.egallery.controller;

import com.egallery.model.entity.Role;
import com.egallery.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public Role create(@RequestBody Role entity) {
        return roleService.create(entity);
    }

    @GetMapping("/{id}")
    public Role getById(@PathVariable UUID id) {
        return roleService.getById(id);
    }

    @GetMapping
    public List<Role> getAll() {
        return roleService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        roleService.delete(id);
    }
}
