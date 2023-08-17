package com.assignment.converter;

import com.assignment.entities.Approval;
import com.assignment.entities.Product;
import com.assignment.enums.ApprovalStatus;
import com.assignment.payload.Payload;
import com.assignment.request.ProductRequest;
import com.assignment.response.ProductResponse;

import java.time.LocalDateTime;

public interface ProductConverter {

    static ProductResponse convertToProductResponse(Approval approval) {
        Product product = approval.getProduct();
        return convertToProductResponse(product, ApprovalStatus.PENDING);
    }

    static ProductResponse convertToProductResponse(Product product) {
        return convertToProductResponse(product, product.getApprovalStatus());
    }

    static ProductResponse convertToProductResponse(ProductRequest productRequest, String message) {
        return ProductResponse.builder()
                .status(productRequest.getStatus())
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .message(message)
                .build();
    }

    static Payload convertToPayload(ProductRequest productRequest, Long approvalId) {
        return Payload.builder()
                .name(productRequest.getName())
                .status(productRequest.getStatus())
                .price(productRequest.getPrice())
                .approvalId(approvalId)
                .build();
    }

    static ProductResponse convertToProductResponse(Product product, ApprovalStatus approvalStatus) {
        return ProductResponse.builder()
                .postedDate(product.getPostedDate())
                .status(product.getStatus())
                .price(product.getPrice())
                .name(product.getName())
                .id(product.getId())
                .approvalId(product.getId())
                .approvalStatus(approvalStatus)
                .status(product.getStatus())
                .build();
    }

        static ProductResponse convertToProductResponse(Payload payload) {
        return ProductResponse.builder()
                .name(payload.getName())
                .status(payload.getStatus())
                .postedDate(LocalDateTime.now())
                .price(payload.getPrice())
                .approvalStatus(ApprovalStatus.PENDING)
                .id(payload.getApprovalId())
                .build();
    }

    static Product getProductFromRequest(ProductRequest productRequest, boolean isForProduct) {
        ApprovalStatus approvalStatus = isForProduct ? ApprovalStatus.APPROVED : ApprovalStatus.PENDING;
        return Product.builder()
                .name(productRequest.getName())
                .status(productRequest.getStatus())
                .postedDate(LocalDateTime.now())
                .price(productRequest.getPrice())
                .approvalStatus(approvalStatus)
                .build();
    }
    static ProductResponse convertToProductResponse(ProductRequest productRequest) {
        return ProductResponse.builder()
                .status(productRequest.getStatus())
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .message("Product creation failed. It will be created after approval.")
                .build();
    }
}
