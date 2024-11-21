package ru.urfu.courseProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.courseProject.entity.Car;

import java.util.List;

@Repository
public interface CarsRepository extends JpaRepository<Car, Long> {

    List<Car> findByEmail(String email);
}
