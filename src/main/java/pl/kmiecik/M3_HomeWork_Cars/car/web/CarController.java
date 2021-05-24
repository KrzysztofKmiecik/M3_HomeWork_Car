package pl.kmiecik.M3_HomeWork_Cars.car.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kmiecik.M3_HomeWork_Cars.car.application.port.CarUseCase;
import pl.kmiecik.M3_HomeWork_Cars.car.application.port.CarUseCase.CreateCarCommand;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.Car;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.CarColor;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.CarMark;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@AllArgsConstructor
@Slf4j
@Validated
class CarController {
    private final CarUseCase service;

    @GetMapping("/cars")
    public String getAllCars(Model model) {
        log.debug("getAllCars method");
        List<Car> allCars = service.findAllCars();
        model.addAttribute("cars", allCars);
        model.addAttribute("newCar", new Car());
        return "cars";
    }

    @PostMapping("/add-carButton")
    public String addCarButton() {
        log.debug("addCarButton method");
        return "redirect:/add-car";
    }


    @GetMapping("/add-car")
    public String getNewCar(Model model) {
        log.debug("getNewCar method");
        model.addAttribute("newCar", new Car());
        return "add-car";
    }


    @PostMapping("/add-car")
    public String addCar(@ModelAttribute @Valid CarControllerCommand command) {
        log.debug("addCar method");
        service.addCar(command.toCreateommand());
        return "redirect:/cars";
    }

    @PostMapping("/delete-car")
    public String deleteCar(@RequestParam  @NotBlank @Min(0) String id) {
        log.debug("deleteCar method");
        if (id != null) {
            service.removeById(Long.valueOf(id));
        }
        return "redirect:/cars";
    }

    @GetMapping("/edit-car")
    public String getEditedCar(Model model, @RequestParam @NotBlank @Min(0) String id) {
        log.debug("geteditedCar method");
        Optional<Car> carById = service.findCarById(Long.valueOf(id));
        Car myCar = carById.isPresent() ? carById.get() : new Car();

        model.addAttribute("newCar", new Car());
        model.addAttribute("car", myCar);
        return "edit-car";
    }

    @PostMapping("/edit-car")
    public String postEditedCar(@ModelAttribute CarControllerCommand command) {
        log.debug("postEditedCar method");
        service.updateCar(command.toCreateommand());
        return "redirect:/cars";
    }

    @PostMapping("/edit-carButton")
    public String postEditCarButton(@RequestParam  @NotBlank @Min(0) String id) {
        log.debug("postEditCarButton method");
        return "redirect:edit-car?id=" + id;
    }

    @Data
    private static class CarControllerCommand {

        @Min(value = 0, message = "Id should be greater than 0 ")
        @NotNull
        private Long id;
        @NotNull
        private CarMark mark;
        @NotBlank
        @Size(min = 1)
        private String model;
        @NotNull
        private CarColor color;

        CreateCarCommand toCreateommand() {
            return new CreateCarCommand(id, mark, model, color);
        }

    }

}
