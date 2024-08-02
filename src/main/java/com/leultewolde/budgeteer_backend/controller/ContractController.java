package com.leultewolde.budgeteer_backend.controller;

import com.leultewolde.budgeteer_backend.dto.request.ContractRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractResponseDTO> addContract(@RequestBody ContractRequestDTO contractRequestDTO) {
        Optional<ContractResponseDTO> newContract = contractService.addContract(contractRequestDTO);

        return newContract.map(contract -> ResponseEntity.status(HttpStatus.CREATED).body(contract))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping
    public ResponseEntity<List<ContractResponseDTO>> getContracts() {
        List<ContractResponseDTO> contracts = contractService.getContracts();
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> getContract(@PathVariable Long id) {
        Optional<ContractResponseDTO> contract = contractService.getContract(id);
        return contract.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> updateContract(@PathVariable Long id, @RequestBody ContractRequestDTO contractRequestDTO) {
        Optional<ContractResponseDTO> updatedContract = contractService.updateContract(id, contractRequestDTO);
        return updatedContract.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        boolean isDeleted = contractService.deleteContract(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }
}
