package com.bubalex.test.mappers;

import com.bubalex.test.entities.CarEntity;
import com.bubalex.test.web.model.*;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class CarMapper {

    public static ApiModelCars toCarModels(Page<CarEntity> carEntities) {
        return new ApiModelCars()
                .items(
                        carEntities.stream()
                        .map(CarMapper::toCarModel)
                        .collect(Collectors.toList())
                )
                .total(carEntities.getTotalElements());
    }

    public static ApiModelCar toCarModel(CarEntity carEntity) {
        return new ApiModelCar()
                .id(carEntity.getId())
                .model(carEntity.getModel())
                .body(carEntity.getBody())
                .capacity(carEntity.getCapacity())
                .color(carEntity.getColor())
                .drive(carEntity.getDrive())
                .make(carEntity.getMake())
                .issueMonth(carEntity.getIssueMonth())
                .issueYear(carEntity.getIssueYear())
                .transmission(carEntity.getTransmission())
                .turbocharger(carEntity.getTurbocharger())
                .volume(carEntity.getVolume());
    }

    public static CarEntity toCarEntity(ApiModelCarRequest carRequest) {
        return CarEntity.builder()
                .body(carRequest.getBody())
                .capacity(carRequest.getCapacity())
                .color(carRequest.getColor())
                .drive(carRequest.getDrive())
                .issueMonth(carRequest.getIssueMonth())
                .issueYear(carRequest.getIssueYear())
                .make(carRequest.getMake())
                .model(carRequest.getModel())
                .transmission(carRequest.getTransmission())
                .turbocharger(carRequest.isTurbocharger())
                .volume(carRequest.getVolume())
                .build();
    }

    public static CarEntity toCarEntity(ApiModelCar carRequest) {
        return CarEntity.builder()
                .id(carRequest.getId())
                .body(carRequest.getBody())
                .capacity(carRequest.getCapacity())
                .color(carRequest.getColor())
                .drive(carRequest.getDrive())
                .issueMonth(carRequest.getIssueMonth())
                .issueYear(carRequest.getIssueYear())
                .make(carRequest.getMake())
                .model(carRequest.getModel())
                .transmission(carRequest.getTransmission())
                .turbocharger(carRequest.isTurbocharger())
                .volume(carRequest.getVolume())
                .build();
    }

}
