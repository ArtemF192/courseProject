package ru.urfu.courseProject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.courseProject.entity.Car;
import ru.urfu.courseProject.repository.CarsRepository;
import ru.urfu.courseProject.service.CarsAtParkingService;
import ru.urfu.courseProject.service.GetRoleService;
import ru.urfu.courseProject.service.GetRoleServiceImp;
import ru.urfu.courseProject.service.GetUsernameService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class CarsController {

    @Autowired
    private CarsRepository carsRepository;
    private GetRoleService getRoleServiceImp;
    private GetUsernameService getUsernameService;

    private CarsAtParkingService carsAtParkingService;
    public CarsController(GetRoleServiceImp getRoleServiceImp,
                          GetUsernameService getUsernameService,
                          CarsAtParkingService carsAtParkingService) {
        this.getRoleServiceImp = getRoleServiceImp;
        this.getUsernameService = getUsernameService;
        this.carsAtParkingService = carsAtParkingService;
    }

    @GetMapping("/list")
    public ModelAndView getAllCar() {
        ModelAndView mav = new ModelAndView("list-cars");
        if (getRoleServiceImp.getRoleCurrentUser().equals("ROLE_ADMIN"))
        {
            mav.addObject("cars", carsRepository.findAll());
        }
        else
        {
            mav.addObject("cars", carsRepository.findByEmail(getUsernameService.getusername()));
        }
        mav.addObject("mainUserRole", getRoleServiceImp.getRoleCurrentUser());
        return mav;
    }

    @GetMapping("/CalcCarNumber")
    public ModelAndView calcCarNumber(ModelAndView modelAndView) {
        ModelAndView mav = new ModelAndView("list-cars");
        List<Car> carList = null;
        if (getRoleServiceImp.getRoleCurrentUser().equals("ROLE_ADMIN"))
        {
            carList = carsRepository.findAll();
        }
        else
        {
            carList = carsRepository.findByEmail(getUsernameService.getusername());

        }
        mav.addObject("mainUserRole", getRoleServiceImp.getRoleCurrentUser());
        mav.addObject("cars", carList);
        mav.addObject("carNumber", Integer.toString(carList.size()));
        return mav;
    }

    @GetMapping("/addCarForm")
    public ModelAndView addCarForm() {
        ModelAndView mav = new ModelAndView("add-car-form");
        Car car = new Car();
        mav.addObject("car", car);
        return mav;
    }

    @PostMapping("/saveCar")
    public String saveCar(@ModelAttribute Car car) {
        car.setEmail(getUsernameService.getusername());
        carsRepository.save(car);
        carsAtParkingService.SaveNewCarModel(car);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long carId) {
        ModelAndView mav = new ModelAndView("add-car-form");
        Optional<Car> optionalCar = carsRepository.findById(carId);
        Car car = new Car();
        if(optionalCar.isPresent())
        {
            car = optionalCar.get();
        }
        mav.addObject("car", car);
        return mav;
    }

    @GetMapping("/deleteCar")
    public String deleteCar(@RequestParam Long carId) {
        carsRepository.deleteById(carId);
        return "redirect:/list";
    }
}
