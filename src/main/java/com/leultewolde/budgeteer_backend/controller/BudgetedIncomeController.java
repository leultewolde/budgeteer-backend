package com.leultewolde.budgeteer_backend.controller;

import com.leultewolde.budgeteer_backend.dto.request.BudgetedIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.BudgetedIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.service.BudgetedIncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/budgeted-incomes")
@RequiredArgsConstructor
public class BudgetedIncomeController {

    private final BudgetedIncomeService budgetedIncomeService;

    @PostMapping
    public ResponseEntity<BudgetedIncomeResponseDTO> addBudgetedIncome(@RequestBody BudgetedIncomeRequestDTO requestDTO) {
        Optional<BudgetedIncomeResponseDTO> newIncome = budgetedIncomeService.addBudgetedIncome(requestDTO);

        return newIncome.map(income -> ResponseEntity.status(HttpStatus.CREATED).body(income))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping
    public ResponseEntity<List<BudgetedIncomeResponseDTO>> getBudgetedIncomes() {
        List<BudgetedIncomeResponseDTO> incomes = budgetedIncomeService.getBudgetedIncomes();
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetedIncomeResponseDTO> getBudgetedIncome(@PathVariable Long id) {
        Optional<BudgetedIncomeResponseDTO> income = budgetedIncomeService.getBudgetedIncome(id);
        return income.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetedIncomeResponseDTO> updateBudgetedIncome(@PathVariable Long id, @RequestBody BudgetedIncomeRequestDTO incomeRequestDTO) {
        Optional<BudgetedIncomeResponseDTO> updatedIncome = budgetedIncomeService.updateBudgetedIncome(id, incomeRequestDTO);
        return updatedIncome.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetedIncome(@PathVariable Long id) {
        boolean isDeleted = budgetedIncomeService.deleteBudgetedIncome(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}