package com.bubalex.test.services.impl;

import com.bubalex.test.entities.CarEntity;
import com.bubalex.test.exception.NotFoundException;
import com.bubalex.test.mappers.CarMapper;
import com.bubalex.test.repositories.CarRepository;
import com.bubalex.test.services.CarService;
import com.bubalex.test.web.model.ApiModelCar;
import com.bubalex.test.web.model.ApiModelCarRequest;
import com.bubalex.test.web.model.ApiModelCars;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public ApiModelCar getCar(UUID id) {
        return carRepository.findById(id)
                .map(CarMapper::toCarModel)
                .orElse(null);
    }

    @Override
    public ApiModelCars getCars(Specification<CarEntity> specification, Pageable pageable) {
        Page<CarEntity> carEntityPage = carRepository.findAll(specification, pageable);

        return CarMapper.toCarModels(carEntityPage);
    }

    @Override
    public void removeCar(UUID id) {
        CarEntity persistedCar = carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user with such id"));
        persistedCar.setDeleted(true);
        carRepository.save(persistedCar);
    }

    @Override
    public ApiModelCar update(UUID id, ApiModelCarRequest carRequest) {
        CarEntity carEntity = carRepository.findById(id).orElseThrow();
        merge(carRequest, carEntity);
        carRepository.save(carEntity);

        return CarMapper.toCarModel(carEntity);
    }

    @Override
    public ApiModelCar create(ApiModelCarRequest carRequest) {
        CarEntity carEntity = CarMapper.toCarEntity(carRequest);
        carEntity = carRepository.save(carEntity);

        return CarMapper.toCarModel(carEntity);
    }

    private void merge(ApiModelCarRequest car, CarEntity persistentEntity) {
        persistentEntity.setBody(car.getBody());
        persistentEntity.setModel(car.getModel());
        persistentEntity.setColor(car.getColor());
        persistentEntity.setCapacity(car.getCapacity());
        persistentEntity.setVolume(car.getVolume());
        persistentEntity.setDrive(car.getDrive());
        persistentEntity.setIssueMonth(car.getIssueMonth());
        persistentEntity.setIssueYear(car.getIssueYear());
        persistentEntity.setTransmission(car.getTransmission());
        persistentEntity.setMake(car.getMake());
    }
}
