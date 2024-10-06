package com.Rusya2054.wm.web.models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;

@Entity
@Table(name = "pump_cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PumpCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "field")
    private String field;

    // нужно связать с wellId из indicators
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "well", referencedColumnName = "id")
    private Well well;

    @Column(name = "typeSize")
    private String typeSize;

    @Column(name = "pumpDepth")
    private Float pumpDepth;

    @Column(name = "lastFailureDate")
    private LocalDate lastFailureDate;
}
