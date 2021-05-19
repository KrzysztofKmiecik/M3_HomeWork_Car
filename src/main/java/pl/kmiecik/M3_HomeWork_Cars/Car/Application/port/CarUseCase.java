package pl.kmiecik.M3_HomeWork_Cars.Car.Application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.Car;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.CarColor;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.CarMark;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface CarUseCase {
    List<Car> findAllCars();

    Optional<Car> findCarById(final Long id);

    List<Car> findCarByColor(final CarColor color);

    Car addCar(final CreateCarCommand car);

    UpdateCarResponse updateCar(final CreateCarCommand command);

    void removeById(final Long id);


    @Value
    @AllArgsConstructor
    class CreateCarCommand {
        Long id;
        CarMark mark;
        String model;
        CarColor color;

        public Car toCar() {
            return new Car(this.getId(), this.getMark(), this.getModel(), this.getColor());
        }

        public Car updateFields(final Car car) {
            if (id != null) {
                car.setId(id);
            }
            if (mark != null) {
                car.setMark(mark);
            }
            if (model != null) {
                car.setModel(model);
            }
            if (color != null) {
                car.setColor(color);
            }
            return car;
        }
    }

    @Value
    class UpdateCarResponse {
        boolean success;
        List<String> errors;

        public static final UpdateCarResponse SUCCESS = new UpdateCarResponse(true, Collections.emptyList());
    }
}
