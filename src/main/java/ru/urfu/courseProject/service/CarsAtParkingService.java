package ru.urfu.courseProject.service;

import org.springframework.stereotype.Service;
import ru.urfu.courseProject.entity.Car;
import ru.urfu.courseProject.entity.CarModel;

@Service
public interface CarsAtParkingService {

    Integer CalcAtParkingCarsById(String email);
    void SaveNewCarModel(Car car);
    CarModel MapToCarModel(Car car);
}
