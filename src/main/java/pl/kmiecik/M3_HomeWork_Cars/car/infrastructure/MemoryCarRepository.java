package pl.kmiecik.M3_HomeWork_Cars.car.infrastructure;

import org.springframework.stereotype.Repository;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.Car;
import pl.kmiecik.M3_HomeWork_Cars.car.domain.CarRepository;

import java.util.*;

@Repository
class MemoryCarRepository implements CarRepository {

    private Map<Long, Car> storage = new HashMap<>();

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Car save(final Car car) {
        storage.put(car.getId(), car);
        return car;
    }

    @Override
    public Optional<Car> findById(final Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(final Long id) {
        storage.remove(id);
    }
}
