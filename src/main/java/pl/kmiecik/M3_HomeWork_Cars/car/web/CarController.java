package pl.kmiecik.M3_HomeWork_Cars.car.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kmiecik.M3_HomeWork_Cars.car.application.port.CarUseCase;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.Car;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@AllArgsConstructor
public class CarController {
    private final CarUseCase service;

    @GetMapping("/cars")
    public String getAllCars(Model model) {
        List<Car> allCars = service.findAllCars();
        model.addAttribute("cars", allCars);
        model.addAttribute("newCar", new Car());
        return "cars";
    }

    @PostMapping("/add-carButton")
    public String addCarButton() {
        return "redirect:add-car";
    }


    @GetMapping("/add-car")
    public String getNewCar(Model model) {
        model.addAttribute("newCar", new Car());
        return "add-car";
    }


    @PostMapping("/add-car")
    public String addCar(@ModelAttribute Car car) {
        service.addCar(new CarUseCase.CreateCarCommand(car.getId(), car.getMark(), car.getModel(), car.getColor()));
        return "redirect:/cars";
    }

    @PostMapping("/delete-car")
    public String deleteCar(@RequestParam String id){
        if (id!=null) {
            service.removeById(Long.valueOf(id));
        }
        return"redirect:/cars";
    }

    @GetMapping("/edit-car")
    public String getEditedCar(Model model,@RequestParam String id) {

        Optional<Car> carById = service.findCarById(Long.valueOf(id));
        Car myCar = carById.isPresent() ? carById.get() : new Car();

        model.addAttribute("newCar", new Car());
        model.addAttribute("car",myCar);
        return "edit-car";
    }
    @PostMapping("/edit-car")
    public String postEditedCar(@ModelAttribute Car car) {
        service.updateCar(new CarUseCase.CreateCarCommand(car.getId(), car.getMark(), car.getModel(), car.getColor()));
        return "redirect:/cars";
    }

    @PostMapping("/edit-carButton")
    public String editCarButton(@RequestParam String id) {
        return "redirect:edit-car?id="+id;
    }
}
