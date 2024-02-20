package com.school.billingservice.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Table(name = "ATTENDANCES")
@Entity
@Getter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime entryDate;

    @Column(nullable = false)
    private LocalDateTime exitDate;

    @Column(nullable = false)
    private Long childId;
}
