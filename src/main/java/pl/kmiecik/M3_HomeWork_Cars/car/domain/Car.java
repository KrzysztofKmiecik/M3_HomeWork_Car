package pl.kmiecik.M3_HomeWork_Cars.car.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Setter
@Getter
public class Car {
    private Long id;
    private CarMark mark;
    private String model;
    private CarColor color;

    public Car(final Long id, final CarMark mark, final String model, final CarColor color) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.color = color;
    }
}
