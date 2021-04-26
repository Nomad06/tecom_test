package com.bubalex.test.services;

import com.bubalex.test.entities.CarEntity;
import com.bubalex.test.web.model.ApiModelCar;
import com.bubalex.test.web.model.ApiModelCarRequest;
import com.bubalex.test.web.model.ApiModelCars;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface CarService {

    ApiModelCar getCar(UUID id);

    ApiModelCars getCars(Specification<CarEntity> specification, Pageable pageable);

    void removeCar(UUID id);

    ApiModelCar update(UUID id, ApiModelCarRequest carRequest);

    ApiModelCar create(ApiModelCarRequest carRequest);

}
