package ru.urfu.courseProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.courseProject.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
