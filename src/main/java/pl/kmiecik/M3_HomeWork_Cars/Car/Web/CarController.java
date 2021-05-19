package pl.kmiecik.M3_HomeWork_Cars.Car.Web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kmiecik.M3_HomeWork_Cars.Car.Application.port.CarUseCase;
import pl.kmiecik.M3_HomeWork_Cars.Car.Application.port.CarUseCase.CreateCarCommand;
import pl.kmiecik.M3_HomeWork_Cars.Car.Application.port.CarUseCase.UpdateCarResponse;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.Car;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.CarColor;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.CarMark;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
@AllArgsConstructor
class CarController {

    private final CarUseCase service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Car> getAll(@RequestParam Optional<CarColor> color) {
        if (color.isPresent()) {
            return service.findCarByColor(color.get());
        }
        return service.findAllCars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return service.findCarById(id)
                .map(car -> ResponseEntity.ok(car))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addCar(@RequestBody RestCarCommand command) {
        Car car = service.addCar(command.toCreateCarCommand());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/" + car.getId().toString())
                .build()
                .toUri();
        return ResponseEntity.created(uri).build();
    }


    @PutMapping("/{id}")
    public void updateCar(@PathVariable Long id, @RequestBody RestCarCommand command) {
        UpdateCarResponse response = service.updateCar(command.toUpdateComand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }


    @PatchMapping("/{id}")
    public void updateCarPATCH(@PathVariable Long id, @RequestBody RestCarCommand command) {
        UpdateCarResponse response = service.updateCar(command.toUpdateComand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCar(@PathVariable Long id) {
        service.removeById(id);
    }


    @Data
    private static class RestCarCommand {
        private Long id;
        private CarMark mark;
        private String model;
        private CarColor color;


        public CreateCarCommand toCreateCarCommand() {
            return new CreateCarCommand(this.getId(), this.getMark(), this.getModel(), this.getColor());
        }

        public CreateCarCommand toUpdateComand(Long id) {
            return new CreateCarCommand(id, this.getMark(), this.getModel(), this.getColor());
        }
    }

}
