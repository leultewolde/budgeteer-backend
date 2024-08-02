package com.leultewolde.budgeteer_backend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum ExpenseCategory {
    NEEDS(
            Set.of(
                    ExpenseSubCategory.RENT,
                    ExpenseSubCategory.UTILITIES,
                    ExpenseSubCategory.GROCERIES,
                    ExpenseSubCategory.MIN_DEBT_PAYMENTS,
                    ExpenseSubCategory.OTHERS
            ),
            50
    ),
    WANTS(
            Set.of(
                    ExpenseSubCategory.DINING_OUT,
                    ExpenseSubCategory.HOBBIES,
                    ExpenseSubCategory.ENTERTAINMENT,
                    ExpenseSubCategory.OTHERS
            ),
            30
    ),
    SAVINGS(
            Set.of(
                    ExpenseSubCategory.EMERGENCY_FUND,
                    ExpenseSubCategory.INVESTING,
                    ExpenseSubCategory.ADDITIONAL_DEBT_PAYMENTS,
                    ExpenseSubCategory.OTHERS
            ),
            20
    );

    private final Set<ExpenseSubCategory> subCategories;
    private final Integer percentageOfIncome;
}
