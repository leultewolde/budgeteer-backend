package com.leultewolde.budgeteer_backend.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,  // Ignore unmapped target properties by default
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT  // Return default values for nulls
)
public interface CentralMapperConfig {
    // Any shared custom methods or types can be added here if needed
}
