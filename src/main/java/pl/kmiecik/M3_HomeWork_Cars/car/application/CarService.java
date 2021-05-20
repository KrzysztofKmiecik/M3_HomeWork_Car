package pl.kmiecik.M3_HomeWork_Cars.car.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kmiecik.M3_HomeWork_Cars.car.application.port.CarUseCase;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.Car;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.CarColor;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.CarRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CarService implements CarUseCase {

    private final CarRepository repository;

    @Override
    public List<Car> findAllCars() {
        return repository.findAll();
    }

    @Override
    public Optional<Car> findCarById(final Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Car> findCarByColor(final CarColor color) {
        return repository.findAll()
                .stream()
                .filter(car -> car.getColor().equals(color))
                .collect(Collectors.toList());
    }

    @Override
    public Car addCar(final CreateCarCommand command) {
        Car car = command.toCar();
        return repository.save(car);
    }

    @Override
    public UpdateCarResponse updateCar(final CreateCarCommand command) {
        return repository.findById(command.getId())
                .map(car -> {
                    Car updatedCar = command.updateFields(car);
                    repository.save(updatedCar);
                    return UpdateCarResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateCarResponse(false, Collections.singletonList("Car not found with id: " + command.getId())));
    }

    @Override
    public void removeById(final Long id) {
        repository.removeById(id);
    }

}
