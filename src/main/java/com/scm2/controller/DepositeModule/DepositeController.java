package com.scm2.controller.DepositeModule;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scm2.dto.DepositeModule.DepositeDto;
import com.scm2.exception.BlogException;
import com.scm2.service.DepositeModule.DepositeService;

@RestController
@RequestMapping("/deposits")
public class DepositeController {

    private DepositeService depositeService;

    public DepositeController(DepositeService depositeService) {
        this.depositeService = depositeService;
    }

    @PostMapping(value = "/save", consumes = { "multipart/form-data" })
    private ResponseEntity<DepositeDto> addDposite(@ModelAttribute DepositeDto depositeDto,
            @RequestParam(value = "depositeattach") MultipartFile file) {
        DepositeDto deposite = depositeService.addDepositeDto(depositeDto, file);
        return new ResponseEntity<>(deposite, HttpStatus.ACCEPTED);

    }

    @PatchMapping(value = "{id}/status")
    private ResponseEntity<DepositeDto> updateDepositeDtoStatus(@PathVariable(name = "id") Integer id,
            @RequestBody Map<String, String> status) {

        // Extract "status" field from the request body
        String value = status.get("status");

        if (value == null || value.isEmpty()) {
            throw new BlogException("Status cannot be null or empty. Allowed values are PENDING, APPROVED, REJECTED.",
                    HttpStatus.BAD_REQUEST);
        }

        // Update the status in the database and return the updated DepositDto
        DepositeDto deposite = depositeService.updateDepositeDtoStatus(value, id);

        return new ResponseEntity<>(deposite, HttpStatus.ACCEPTED);

    }

    @PostMapping("{id}/updateDeposite")
    public ResponseEntity<DepositeDto> updateDepositeAmount(@PathVariable(value = "id") Integer id,
            @RequestBody Map<String, Double> value) {
        double amount = value.get("depositeamount");

        DepositeDto deposite = depositeService.updateDepositeAmount(id, amount);
        return new ResponseEntity<>(deposite, HttpStatus.ACCEPTED);

    }

}
