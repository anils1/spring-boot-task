package com.assignment.model;

import com.assignment.enums.ApprovalStatus;
import com.assignment.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteria {
    private String productName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private LocalDateTime minPostedDate;
    private LocalDateTime maxPostedDate;
    private ProductStatus status;
    private ApprovalStatus approvalStatus;

    // Constructors, getters, and setters
}

