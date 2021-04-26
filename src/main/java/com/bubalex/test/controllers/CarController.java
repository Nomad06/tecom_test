package com.bubalex.test.controllers;

import com.bubalex.test.entities.CarEntity;
import com.bubalex.test.entities.CarEntity_;
import com.bubalex.test.services.CarService;
import com.bubalex.test.specifications.car.BetweenSpecification;
import com.bubalex.test.specifications.car.BooleanSpecification;
import com.bubalex.test.specifications.car.ValueInSpecification;
import com.bubalex.test.web.api.CarsApi;
import com.bubalex.test.web.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bubalex.test.utils.PageUtils.getPageable;

@RestController
@AllArgsConstructor
public class CarController implements CarsApi {

    private final CarService carService;

    @Override
    public ResponseEntity<ApiModelCar> createCar(ApiModelCarRequest apiModelCarRequest) {
        ApiModelCar car = carService.create(apiModelCarRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(car);
    }

    @Override
    public ResponseEntity<ApiModelCar> updateCar(UUID id, ApiModelCarRequest apiModelCarRequest) {
        ApiModelCar car = carService.update(id, apiModelCarRequest);

        return ResponseEntity.ok(car);
    }

    @Override
    public ResponseEntity<Void> deleteCar(UUID id) {
        carService.removeCar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<ApiModelCar> getCar(UUID id) {
        ApiModelCar car = carService.getCar(id);
        if (car == null) {
            return ResponseEntity.notFound()
                    .build();
        }
        return ResponseEntity.ok(car);
    }

    @Override
    public ResponseEntity<ApiModelCars> getCars(List<String> models,
                                                List<String> makes,
                                                Integer capacityStart,
                                                Integer capacityEnd,
                                                Integer volumeStart,
                                                Integer volumeEnd,
                                                Boolean turbocharger,
                                                List<ApiModelTransmission> transmissions,
                                                List<ApiModelDrive> drives,
                                                List<ApiModelBody> bodies,
                                                List<String> colors,
                                                Integer issueYearStart,
                                                Integer issueYearEnd,
                                                Integer issueMonthStart,
                                                Integer issueMonthEnd,
                                                Long pageNumber, Long pageSize, List<String> sortValues) {
        List<Specification<CarEntity>> specificationList = new ArrayList<>();
        Optional.ofNullable(models).ifPresent(modelsList -> specificationList.add(new ValueInSpecification<>(CarEntity_.MODEL, modelsList)));
        Optional.ofNullable(models).ifPresent(makesList -> specificationList.add(new ValueInSpecification<>(CarEntity_.MAKE, makesList)));
        Optional.ofNullable(models).ifPresent(colorsList -> specificationList.add(new ValueInSpecification<>(CarEntity_.COLOR, colorsList)));
        Optional.ofNullable(models).ifPresent(transmissionsList -> specificationList.add(new ValueInSpecification<>(CarEntity_.TRANSMISSION, transmissionsList)));
        Optional.ofNullable(models).ifPresent(drivesList -> specificationList.add(new ValueInSpecification<>(CarEntity_.DRIVE, drivesList)));
        Optional.ofNullable(models).ifPresent(bodiesList -> specificationList.add(new ValueInSpecification<>(CarEntity_.BODY, bodiesList)));
        Optional.ofNullable(models).ifPresent(isTurbo -> specificationList.add(new BooleanSpecification<>(CarEntity_.TURBOCHARGER)));
        specificationList.add(new BetweenSpecification<>(CarEntity_.CAPACITY, Pair.of(capacityStart, capacityEnd)));
        specificationList.add(new BetweenSpecification<>(CarEntity_.VOLUME, Pair.of(volumeStart, volumeEnd)));
        specificationList.add(new BetweenSpecification<>(CarEntity_.ISSUE_YEAR, Pair.of(issueYearStart, issueYearEnd)));
        specificationList.add(new BetweenSpecification<>(CarEntity_.ISSUE_MONTH, Pair.of(issueMonthStart, issueMonthEnd)));

        return specificationList.stream()
                .reduce(Specification::and)
                .map(carEntitySpecification -> {
                    Pageable pageable = getPageable(pageNumber, pageSize, sortValues);
                    return carService.getCars(carEntitySpecification, pageable);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new ApiModelCars()));
    }

}
