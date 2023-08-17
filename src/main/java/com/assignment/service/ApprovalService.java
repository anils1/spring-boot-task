package com.assignment.service;

import com.assignment.entities.Product;
import com.assignment.response.ProductResponse;

import java.util.List;

public interface ApprovalService {
    public List<ProductResponse> getAllProduct();
    ProductResponse approveProduct(Long approvalId);

    ProductResponse rejectProduct(Long approvalId);
}
