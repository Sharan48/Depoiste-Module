package com.scm2.servicesimp.DepositeModule;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scm2.dto.DepositeModule.DepositeDto;
import com.scm2.entity.DepositeModule.Deposite;
import com.scm2.enums.DepositeStatus;
import com.scm2.exception.BlogException;
import com.scm2.exception.ResourceNotFoundException;
import com.scm2.repositaries.DepositeModule.DepositeRepository;
import com.scm2.service.ImageService;
import com.scm2.service.DepositeModule.DepositeService;

@Service
public class DepositeServiceImp implements DepositeService {

    @Autowired
    private ImageService imageService;

    private DepositeRepository depositeRepository;
    private ModelMapper modelMapper;

    public DepositeServiceImp(DepositeRepository depositeRepository, ModelMapper modelMapper) {
        this.depositeRepository = depositeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DepositeDto addDepositeDto(DepositeDto depositeDto, MultipartFile file) {

        // Checking file format
        String fileformat = file.getOriginalFilename();
        if (fileformat == null || !fileformat.matches("^.*\\.(jpg|png|jpeg)$")) {
            throw new BlogException("Invalid file format. Only jpg,jpeg, and png are allowed!", HttpStatus.BAD_REQUEST);

        }

        // saving the attachments url
        String fileurl = null;
        if (!file.isEmpty() && file != null) {
            fileurl = imageService.uploadImage(file);
            depositeDto.setAttachmentUrl(fileurl);

        }

        if (depositeDto.getCollectedAmount() <= depositeDto.getDepositedAmount()) {
            throw new BlogException("Deposited amount should be greater than collected amount!",
                    HttpStatus.BAD_REQUEST);
        }

        Deposite deposite = new Deposite();
        deposite.setCollectedAmount(depositeDto.getCollectedAmount());
        deposite.setRemittanceNumber(depositeDto.getRemittanceNumber());
        deposite.setDepositedAmount(depositeDto.getDepositedAmount());
        deposite.setDepositDate(depositeDto.getDepositDate());
        deposite.setStatus(depositeDto.getStatus());
        deposite.setAttachmentUrl(fileurl);
        depositeRepository.save(deposite);

        return modelMapper.map(deposite, DepositeDto.class);

    }

    @Override
    public DepositeDto updateDepositeDtoStatus(String status, Integer id) {

        // Enum type checking
        DepositeStatus newStatus = DepositeStatus.fromValue(status);

        Deposite deposite = depositeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposite", "id", id));

        if (deposite.getStatus() != DepositeStatus.PENDING) {
            throw new BlogException("Only deposits in PENDING status can be updated", HttpStatus.BAD_REQUEST);
        }

        deposite.setStatus(newStatus);

        depositeRepository.save(deposite);

        return modelMapper.map(deposite, DepositeDto.class);
    }

    @Override
    public DepositeDto updateDepositeAmount(Integer id, double DepositeAmount) {
        Deposite deposite = depositeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposite", "id", id));

        if (deposite.getStatus() != DepositeStatus.PENDING) {
            throw new BlogException("Only deposits in PENDING status can be updated", HttpStatus.BAD_REQUEST);

        }

        if (deposite.getCollectedAmount() <= DepositeAmount) {
            throw new BlogException("Deposited amount should be less than or equal to collected amount!",
                    HttpStatus.BAD_REQUEST);
        }

        deposite.setCollectedAmount(deposite.getCollectedAmount() - DepositeAmount);
        deposite.setDepositedAmount(deposite.getDepositedAmount() + DepositeAmount);

        depositeRepository.save(deposite);

        return modelMapper.map(deposite, DepositeDto.class);
    }

}
