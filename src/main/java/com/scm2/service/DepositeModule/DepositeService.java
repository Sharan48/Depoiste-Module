package com.scm2.service.DepositeModule;

import org.springframework.web.multipart.MultipartFile;

import com.scm2.dto.DepositeModule.DepositeDto;

public interface DepositeService {

    DepositeDto addDepositeDto(DepositeDto depositeDto, MultipartFile file);

    DepositeDto updateDepositeDtoStatus(String status, Integer id);

    DepositeDto updateDepositeAmount(Integer id, double DepositeAmount);

}
