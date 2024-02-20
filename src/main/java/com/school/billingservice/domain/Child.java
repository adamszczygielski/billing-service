package com.school.billingservice.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "CHILDREN")
@Entity
@Getter
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Long schoolId;

    @Column(nullable = false)
    private Long parentId;
}
