package com.assignment.response;

import com.assignment.enums.ApprovalStatus;
import com.assignment.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include. NON_NULL)
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private LocalDateTime postedDate;
    private ProductStatus status;
    private ApprovalStatus approvalStatus;
    private String message;
    private Long approvalId;
}
