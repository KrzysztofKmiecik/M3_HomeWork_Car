package pl.kmiecik.M3_HomeWork_Cars;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.kmiecik.M3_HomeWork_Cars.Car.Application.port.CarUseCase;
import pl.kmiecik.M3_HomeWork_Cars.Car.Application.port.CarUseCase.CreateCarCommand;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.CarColor;
import pl.kmiecik.M3_HomeWork_Cars.Car.Domain.CarMark;

@Component
@AllArgsConstructor
class Init {

    private final CarUseCase service;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        service.addCar(new CreateCarCommand(1L, CarMark.BMW, "i2", CarColor.BLACK));
        service.addCar(new CreateCarCommand(2L, CarMark.FIAT, "Ducato", CarColor.WHITE));
        service.addCar(new CreateCarCommand(3L, CarMark.OPEL, "Astra", CarColor.RED));
    }

}
