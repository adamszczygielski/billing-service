package com.school.billingservice.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "PARENTS")
@Entity
@Getter
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;
}
