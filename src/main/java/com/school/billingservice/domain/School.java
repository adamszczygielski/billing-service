package com.school.billingservice.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Table(name = "SCHOOLS")
@Entity
@Getter
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal hourPrice;
}
