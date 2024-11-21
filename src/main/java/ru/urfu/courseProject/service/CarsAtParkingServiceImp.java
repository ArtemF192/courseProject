package ru.urfu.courseProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.courseProject.entity.Car;
import ru.urfu.courseProject.entity.CarModel;
import ru.urfu.courseProject.repository.CarModelRepository;
import ru.urfu.courseProject.repository.CarsRepository;

import java.util.Objects;

@Service
public class CarsAtParkingServiceImp implements CarsAtParkingService {

    @Autowired
    CarsRepository carsRepository;
    @Autowired
    CarModelRepository carModelRepository;
    @Override
    public Integer CalcAtParkingCarsById(String email)
    {
        return carsRepository.findByEmail(email).size();
    }

    @Override
    public void SaveNewCarModel(Car car) {
        CarModel carModel = MapToCarModel(car);
        if(carModelRepository.findByBrand(carModel.getBrand())
                .stream()
                .noneMatch(x-> Objects.equals(x.getPrice(), car.getPrice())))
        {
            carModelRepository.save(carModel);
        }
    }

    @Override
    public CarModel MapToCarModel(Car car) {
        CarModel carModel = new CarModel();
        carModel.setBrand(car.getBrand());
        carModel.setPrice(car.getPrice());
        return carModel;
    }
}
