package pl.kmiecik.M3_HomeWork_Cars.Car.Web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kmiecik.M3_HomeWork_Cars.Car.Application.port.CarUseCase;
import pl.kmiecik.M3_HomeWork_Cars.Car.Application.port.CarUseCase.CreateCarCommand;
import pl.kmiecik.M3_HomeWork_Cars.Car.Application.port.CarUseCase.UpdateCarResponse;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.Car;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.CarColor;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.CarMark;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
@AllArgsConstructor
class CarController {

    private final CarUseCase service;

    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public List<Car> getAll(@RequestParam Optional<CarColor> color) {
        if (color.isPresent()) {
            return service.findCarByColor(color.get());
        }
        return service.findAllCars();
    }

    @GetMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return service.findCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addCar(@Validated @RequestBody RestCarCommand command) {
        Car car = service.addCar(command.toCreateCarCommand());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/" + car.getId().toString())
                .build()
                .toUri();
        return ResponseEntity.created(uri).build();
    }


    @PutMapping("/{id}")
    public void updateCar(@PathVariable Long id, @Validated @RequestBody RestCarCommand command) {
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

        @Min(value = 0, message = "Id should be greater than 0 ")
        private Long id;
        @NotNull
        private CarMark mark;
        @NotBlank
        @Size(min = 2)
        private String model;
        @NotNull
        private CarColor color;


        public CreateCarCommand toCreateCarCommand() {
            return new CreateCarCommand(this.getId(), this.getMark(), this.getModel(), this.getColor());
        }

        public CreateCarCommand toUpdateComand(Long id) {
            return new CreateCarCommand(id, this.getMark(), this.getModel(), this.getColor());
        }
    }

}
