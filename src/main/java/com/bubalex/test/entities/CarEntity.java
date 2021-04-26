package com.bubalex.test.entities;

import com.bubalex.test.web.model.ApiModelBody;
import com.bubalex.test.web.model.ApiModelDrive;
import com.bubalex.test.web.model.ApiModelTransmission;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "car")
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class CarEntity extends BaseEntity{

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "issue_year")
    private Integer issueYear;

    @Column(name = "issue_month")
    private Integer issueMonth;

    @Column(name = "color")
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission")
    private ApiModelTransmission transmission;

    @Enumerated(EnumType.STRING)
    @Column(name = "drive")
    private ApiModelDrive drive;

    @Enumerated(EnumType.STRING)
    @Column(name = "body")
    private ApiModelBody body;

    @Column(name = "turbocharger")
    private Boolean turbocharger;

}
