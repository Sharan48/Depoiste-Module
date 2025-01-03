package com.scm2.dto.DepositeModule;

import java.io.Serializable;
import java.time.LocalDate;

import com.scm2.enums.DepositeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositeDto implements Serializable {

    private Integer id;

    private String remittanceNumber;

    private double collectedAmount;

    private double depositedAmount;

    private LocalDate depositDate;

    private String attachmentUrl;

    private DepositeStatus status = DepositeStatus.PENDING;

}
