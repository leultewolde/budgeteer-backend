package com.leultewolde.budgeteer_backend.service;

import com.leultewolde.budgeteer_backend.dto.request.ContractRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ContractService {

    Optional<ContractResponseDTO> addContract(ContractRequestDTO contractRequestDTO);

    List<ContractResponseDTO> getContracts();

    Optional<ContractResponseDTO> getContract(Long contractId);

    Optional<ContractResponseDTO> updateContract(Long contractId, ContractRequestDTO contractRequestDTO);

    boolean deleteContract(Long contractId);
}
