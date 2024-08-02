package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ContractRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.exception.contract.ContractNotFoundException;
import com.leultewolde.budgeteer_backend.exception.contract.ContractSaveException;
import com.leultewolde.budgeteer_backend.mapper.ContractMapper;
import com.leultewolde.budgeteer_backend.model.Contract;
import com.leultewolde.budgeteer_backend.repository.ContractRepository;
import com.leultewolde.budgeteer_backend.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    @Override
    public Optional<ContractResponseDTO> addContract(ContractRequestDTO contractRequestDTO) {
        try {
            Contract newContract = contractMapper.toEntity(contractRequestDTO);
            Contract savedContract = contractRepository.save(newContract);
            return Optional.of(contractMapper.toDto(savedContract));
        } catch (DataAccessException e) {
            throw new ContractSaveException("Failed to save contract: " + e.getMessage());
        }
    }

    @Override
    public List<ContractResponseDTO> getContracts() {
        List<Contract> contracts = contractRepository.findAll();
        return contractMapper.toDtos(contracts);
    }

    @Override
    public Optional<ContractResponseDTO> getContract(Long contractId) {
        Optional<Contract> optionalContract = contractRepository.findById(contractId);
        return optionalContract.map(contractMapper::toDto);
    }

    @Override
    public Optional<ContractResponseDTO> updateContract(Long contractId, ContractRequestDTO contractRequestDTO) {
        Optional<Contract> optionalContract = contractRepository.findById(contractId);
        if (optionalContract.isEmpty()) {
            throw new ContractNotFoundException(contractId);
        }

        Contract foundContract = optionalContract.get();

        // Set the new values from the DTO
        foundContract.setLengthInMonths(contractRequestDTO.getLengthInMonths());
        foundContract.setSalaryPerHour(contractRequestDTO.getSalaryPerHour());
        foundContract.setMaxHours(contractRequestDTO.getMaxHours());
        foundContract.setStatus(contractRequestDTO.getStatus());

        try {
            Contract updatedContract = contractRepository.save(foundContract);
            return Optional.of(contractMapper.toDto(updatedContract));
        } catch (DataAccessException e) {
            throw new ContractSaveException("Failed to update contract: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteContract(Long contractId) {
        try {
            if (contractRepository.existsById(contractId)) {
                contractRepository.deleteById(contractId);
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            throw new ContractSaveException("Failed to delete contract: " + e.getMessage());
        }
    }
}
