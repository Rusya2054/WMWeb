package com.Rusya2054.wm.web.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "indicators")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Indicator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade =  CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "well", referencedColumnName = "id")
    private Well well;

    @Column(name = "rotationDirection")
    private String rotationDirection;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Column(name = "frequency")
    private Float frequency;

    @Column(name = "intakePressure")
    private Float intakePressure;

    @Column(name = "curPhaseA")
    private Float curPhaseA;

    @Column(name = "curPhaseB")
    private Float curPhaseB;

    @Column(name = "curPhaseC")
    private Float curPhaseC;

    @Column(name = "currentImbalance")
    private Integer currentImbalance;

    @Column(name = "lineCurrent")
    private Float lineCurrent;

    @Column(name = "lineVoltage")
    private Float lineVoltage;

    @Column(name = "activePower")
    private Float activePower;

    @Column(name = "totalPower")
    private Float totalPower;

    @Column(name = "powerFactor")
    private Float powerFactor;

    @Column(name = "engineLoad")
    private Float engineLoad;

    @Column(name = "inputVoltageAB")
    private Float inputVoltageAB;

    @Column(name = "inputVoltageBC")
    private Float inputVoltageBC;

    @Column(name = "inputVoltageCA")
    private Float inputVoltageCA;

    @Column(name = "engineTemp")
    private Float engineTemp;

    @Column(name = "liquidTemp")
    private Float liquidTemp;

    @Column(name = "vibrationAccRadial")
    private Float vibrationAccRadial;

    @Column(name = "vibrationAccAxial")
    private Float vibrationAccAxial;

    @Column(name = "liquidFlowRatio")
    private Float liquidFlowRatio;

    @Column(name = "isolationResistance")
    private Float isolationResistance;
}
