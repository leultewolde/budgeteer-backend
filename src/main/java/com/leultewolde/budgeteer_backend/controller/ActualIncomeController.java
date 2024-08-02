package com.leultewolde.budgeteer_backend.controller;

import com.leultewolde.budgeteer_backend.dto.request.ActualIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ActualIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.service.ActualIncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/actual-incomes")
@RequiredArgsConstructor
public class ActualIncomeController {

    private final ActualIncomeService actualIncomeService;

    @PostMapping
    public ResponseEntity<ActualIncomeResponseDTO> addActualIncome(@RequestBody ActualIncomeRequestDTO requestDTO) {
        Optional<ActualIncomeResponseDTO> newIncome = actualIncomeService.addActualIncome(requestDTO);

        return newIncome.map(income -> ResponseEntity.status(HttpStatus.CREATED).body(income))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping
    public ResponseEntity<List<ActualIncomeResponseDTO>> getActualIncomes() {
        List<ActualIncomeResponseDTO> incomes = actualIncomeService.getActualIncomes();
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActualIncomeResponseDTO> getActualIncome(@PathVariable Long id) {
        Optional<ActualIncomeResponseDTO> income = actualIncomeService.getActualIncome(id);
        return income.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActualIncomeResponseDTO> updateActualIncome(@PathVariable Long id, @RequestBody ActualIncomeRequestDTO incomeRequestDTO) {
        Optional<ActualIncomeResponseDTO> updatedIncome = actualIncomeService.updateActualIncome(id, incomeRequestDTO);
        return updatedIncome.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActualIncome(@PathVariable Long id) {
        boolean isDeleted = actualIncomeService.deleteActualIncome(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
