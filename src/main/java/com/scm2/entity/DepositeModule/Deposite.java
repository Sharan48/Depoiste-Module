package com.scm2.entity.DepositeModule;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.scm2.enums.DepositeStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Deposite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String remittanceNumber;

    @Column(nullable = false)
    private double collectedAmount;

    @Column(nullable = false)
    private double depositedAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate depositDate;

    @Column(length = 1000)
    private String attachmentUrl;

    @Enumerated(EnumType.STRING)
    private DepositeStatus status = DepositeStatus.PENDING;

}
