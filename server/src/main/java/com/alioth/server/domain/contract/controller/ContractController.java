package com.alioth.server.domain.contract.controller;

import com.alioth.server.common.response.CommonResponse;


import com.alioth.server.domain.contract.dto.req.ContractCreateDto;
import com.alioth.server.domain.contract.dto.req.ContractUpdateDto;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.service.ContractService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/contract")
public class ContractController {
    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createContract(@RequestBody @Valid ContractCreateDto contractCreateDto) {
        ContractResDto createdContract = contractService.createContract(contractCreateDto);
        return CommonResponse.responseMessage(HttpStatus.CREATED, "Contract created successfully", createdContract);
    }
    @PatchMapping("/update/{contractId}")
    public ResponseEntity<CommonResponse> updateContract(@PathVariable Long contractId, @RequestBody @Valid ContractUpdateDto contractUpdateDto) {
        ContractResDto updatedContract = contractService.updateContract(contractId, contractUpdateDto);
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "계약이 성공적으로 변경되었습니다.",
                updatedContract
        );
    }
    @DeleteMapping("/delete/{contractId}")
    public ResponseEntity<CommonResponse> deleteContract(@PathVariable Long contractId) {
        contractService.deleteContract(contractId);
        return CommonResponse.responseMessage(HttpStatus.OK, "계약이 성공적으로 삭제되었습니다.");
    }
    @GetMapping("/list")
    public ResponseEntity<CommonResponse> listAllContracts() {
        List<ContractResDto> contracts = contractService.listAllContracts();
        return CommonResponse.responseMessage(HttpStatus.OK, "모든 계약 목록을 성공적으로 조회했습니다.", contracts);
    }
}
