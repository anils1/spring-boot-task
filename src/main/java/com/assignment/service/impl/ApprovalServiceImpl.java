package com.assignment.service.impl;

import com.assignment.entities.Approval;
import com.assignment.entities.Product;
import com.assignment.enums.ApprovalStatus;
import com.assignment.payload.Payload;
import com.assignment.repository.ApprovalRepository;
import com.assignment.repository.ProductRepository;
import com.assignment.response.ProductResponse;
import com.assignment.service.ApprovalService;
import com.assignment.converter.ProductConverter;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    private ApprovalRepository approvalRepository;

    private List<ProductResponse> productList = new ArrayList<>();

    private ProductRepository productRepository;

    @Autowired
    public ApprovalServiceImpl(ApprovalRepository approvalRepository, ProductRepository productRepository) {
        this.approvalRepository = approvalRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<ProductResponse> sortedProdList = productList.stream()
                .map(product -> approvalRepository.findById(product.getId()).orElse(null))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Approval::getRequestDate))
                .map(ProductConverter::convertToProductResponse)
                .collect(Collectors.toList());
        return sortedProdList;
    }

    @KafkaListener(topics = "product-topic", groupId = "test")
    public void kafkaListener(Payload payload) {
        System.out.println("payload : " + payload.toString());
        productList.add(ProductConverter.convertToProductResponse(payload));
    }

    @Override
    public ProductResponse approveProduct(Long approvalId) {
        productList.removeIf(product -> product.getId() == approvalId);
        Product existingProduct =  productRepository.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with approvalId: " + approvalId));
            existingProduct.setApprovalStatus(ApprovalStatus.APPROVED);
            return ProductConverter.convertToProductResponse(productRepository.save(existingProduct));
    }

    @Override
    public ProductResponse rejectProduct(Long approvalId) {
        ProductResponse existingProduct = productList.stream()
                .filter(product -> product.getId() == approvalId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with approvalId: " + approvalId));
        productList.removeIf(product -> product.getId() == approvalId);
        return existingProduct;
    }

}
