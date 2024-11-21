package ru.urfu.courseProject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.courseProject.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
