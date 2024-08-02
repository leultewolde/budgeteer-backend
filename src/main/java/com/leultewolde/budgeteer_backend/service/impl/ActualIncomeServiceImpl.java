package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ActualIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ActualIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.exception.income.IncomeNotFoundException;
import com.leultewolde.budgeteer_backend.exception.income.IncomeSaveException;
import com.leultewolde.budgeteer_backend.mapper.IncomeMapper;
import com.leultewolde.budgeteer_backend.model.ActualIncome;
import com.leultewolde.budgeteer_backend.repository.ActualIncomeRepository;
import com.leultewolde.budgeteer_backend.service.ActualIncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActualIncomeServiceImpl implements ActualIncomeService {

    private final ActualIncomeRepository actualIncomeRepository;
    private final IncomeMapper incomeMapper;

    @Override
    public Optional<ActualIncomeResponseDTO> addActualIncome(ActualIncomeRequestDTO incomeRequestDTO) {
        try {
            ActualIncome newIncome = incomeMapper.toEntity(incomeRequestDTO);
            ActualIncome savedIncome = actualIncomeRepository.save(newIncome);
            return Optional.of(incomeMapper.toDto(savedIncome));
        } catch (DataAccessException e) {
            throw new IncomeSaveException("Failed to save actual income: " + e.getMessage());
        }
    }

    @Override
    public List<ActualIncomeResponseDTO> getActualIncomes() {
        List<ActualIncome> incomes = actualIncomeRepository.findAll();
        return incomeMapper.toActualIncomeDtos(incomes);
    }

    @Override
    public Optional<ActualIncomeResponseDTO> getActualIncome(Long incomeId) {
        Optional<ActualIncome> optionalIncome = actualIncomeRepository.findById(incomeId);
        return optionalIncome.map(incomeMapper::toDto);
    }

    @Override
    public Optional<ActualIncomeResponseDTO> updateActualIncome(Long incomeId, ActualIncomeRequestDTO incomeRequestDTO) {
        Optional<ActualIncome> optionalIncome = actualIncomeRepository.findById(incomeId);
        if (optionalIncome.isEmpty()) {
            throw new IncomeNotFoundException(incomeId);
        }

        ActualIncome foundIncome = optionalIncome.get();
        foundIncome.setHoursWorked(incomeRequestDTO.getHoursWorked());
        foundIncome.setDate(incomeRequestDTO.getDate());
        foundIncome.setPayPeriodStart(incomeRequestDTO.getPayPeriodStart());
        foundIncome.setPayPeriodEnd(incomeRequestDTO.getPayPeriodEnd());

        try {
            ActualIncome updatedIncome = actualIncomeRepository.save(foundIncome);
            return Optional.of(incomeMapper.toDto(updatedIncome));
        } catch (DataAccessException e) {
            throw new IncomeSaveException("Failed to update actual income: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteActualIncome(Long incomeId) {
        try {
            if (actualIncomeRepository.existsById(incomeId)) {
                actualIncomeRepository.deleteById(incomeId);
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            throw new IncomeSaveException("Failed to delete actual income: " + e.getMessage());
        }
    }
}