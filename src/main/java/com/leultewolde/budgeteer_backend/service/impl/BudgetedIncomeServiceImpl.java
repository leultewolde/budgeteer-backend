package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.BudgetedIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.BudgetedIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.exception.income.IncomeNotFoundException;
import com.leultewolde.budgeteer_backend.exception.income.IncomeSaveException;
import com.leultewolde.budgeteer_backend.mapper.IncomeMapper;
import com.leultewolde.budgeteer_backend.model.BudgetedIncome;
import com.leultewolde.budgeteer_backend.repository.BudgetedIncomeRepository;
import com.leultewolde.budgeteer_backend.service.BudgetedIncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetedIncomeServiceImpl implements BudgetedIncomeService {

    private final BudgetedIncomeRepository budgetedIncomeRepository;
    private final IncomeMapper incomeMapper;

    @Override
    public Optional<BudgetedIncomeResponseDTO> addBudgetedIncome(BudgetedIncomeRequestDTO incomeRequestDTO) {
        try {
            BudgetedIncome newIncome = incomeMapper.toEntity(incomeRequestDTO);
            BudgetedIncome savedIncome = budgetedIncomeRepository.save(newIncome);
            return Optional.of(incomeMapper.toDto(savedIncome));
        } catch (DataAccessException e) {
            throw new IncomeSaveException("Failed to save budgeted income: " + e.getMessage());
        }
    }

    @Override
    public List<BudgetedIncomeResponseDTO> getBudgetedIncomes() {
        List<BudgetedIncome> incomes = budgetedIncomeRepository.findAll();
        return incomeMapper.toBudgetedIncomeDtos(incomes);
    }

    @Override
    public Optional<BudgetedIncomeResponseDTO> getBudgetedIncome(Long incomeId) {
        Optional<BudgetedIncome> optionalIncome = budgetedIncomeRepository.findById(incomeId);
        return optionalIncome.map(incomeMapper::toDto);
    }

    @Override
    public Optional<BudgetedIncomeResponseDTO> updateBudgetedIncome(Long incomeId, BudgetedIncomeRequestDTO incomeRequestDTO) {
        Optional<BudgetedIncome> optionalIncome = budgetedIncomeRepository.findById(incomeId);
        if (optionalIncome.isEmpty()) {
            throw new IncomeNotFoundException(incomeId);
        }

        BudgetedIncome foundIncome = optionalIncome.get();
        foundIncome.setHoursWorked(incomeRequestDTO.getHoursWorked());
        foundIncome.setDate(incomeRequestDTO.getDate());
        foundIncome.setPayPeriodStart(incomeRequestDTO.getPayPeriodStart());
        foundIncome.setPayPeriodEnd(incomeRequestDTO.getPayPeriodEnd());

        try {
            BudgetedIncome updatedIncome = budgetedIncomeRepository.save(foundIncome);
            return Optional.of(incomeMapper.toDto(updatedIncome));
        } catch (DataAccessException e) {
            throw new IncomeSaveException("Failed to update budgeted income: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteBudgetedIncome(Long incomeId) {
        try {
            if (budgetedIncomeRepository.existsById(incomeId)) {
                budgetedIncomeRepository.deleteById(incomeId);
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            throw new IncomeSaveException("Failed to delete budgeted income: " + e.getMessage());
        }
    }
}