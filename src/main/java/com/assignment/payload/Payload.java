package com.assignment.payload;

import com.assignment.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include. NON_NULL)
@Builder
public class Payload {
    private String name;
    private BigDecimal price;
    private ProductStatus status;
    private Long approvalId;
}
