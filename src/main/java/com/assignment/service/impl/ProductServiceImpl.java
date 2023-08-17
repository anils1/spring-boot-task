package com.assignment.service.impl;

import com.assignment.entities.Approval;
import com.assignment.entities.Product;
import com.assignment.enums.ApprovalStatus;
import com.assignment.enums.ProductStatus;
import com.assignment.exceptions.ProductValidationException;
import com.assignment.model.ProductSearchCriteria;
import com.assignment.payload.Payload;
import com.assignment.repository.ApprovalRepository;
import com.assignment.repository.ProductRepository;
import com.assignment.request.ProductRequest;
import com.assignment.response.ApiResponse;
import com.assignment.response.ProductResponse;
import com.assignment.service.ProductService;
import com.assignment.converter.ProductConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ApprovalRepository approvalRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private String topic = "product-topic";

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ApprovalRepository approvalRepository, KafkaTemplate kafkaTemplate) {
        this.productRepository = productRepository;
        this.approvalRepository = approvalRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findByStatusOrderByPostedDateDesc(ProductStatus.ACTIVE);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        if (productRequest.getPrice().compareTo(BigDecimal.valueOf(10000)) > 0) {
            throw new ProductValidationException("Product price must not exceed $10,000.");
        }
        if (productRequest.getPrice().compareTo(BigDecimal.valueOf(5000)) > 0) {
            pushToApprovalQueueAndSave(productRequest);
            return ProductConverter.convertToProductResponse(productRequest);
        } else {
            Product dbProduct = saveProduct(ProductConverter.getProductFromRequest(productRequest, true));
            return ProductConverter.convertToProductResponse(dbProduct);
        }
    }

    @Override
    public List<ProductResponse> searchProducts(ProductSearchCriteria criteria) {
        List<Product> products = productRepository.customSearchProducts(
                criteria.getProductName(),
                criteria.getMinPrice(),
                criteria.getMaxPrice(),
                criteria.getMinPostedDate(),
                criteria.getMaxPostedDate(),
                criteria.getStatus(),
                criteria.getApprovalStatus()
        );
        return products.stream().map(ProductConverter::convertToProductResponse).toList();
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest, Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        BigDecimal previousPrice = existingProduct.getPrice();
        BigDecimal newPrice = productRequest.getPrice();

        // Checking if the new price is more than 50% of the previous price
        boolean requiresApproval = newPrice.compareTo(previousPrice.multiply(new BigDecimal("1.5"))) > 0;

        if (requiresApproval) {
            existingProduct.setApprovalStatus(ApprovalStatus.PENDING);
            Payload payload = convertToPayload(productRequest, id);
            sendEventsToTopic(payload);
        } else {
            existingProduct.setName(productRequest.getName());
            existingProduct.setPrice(newPrice);
            existingProduct.setStatus(productRequest.getStatus());
            existingProduct.setApprovalStatus(ApprovalStatus.APPROVED);
        }
        productRepository.save(existingProduct);
        return ProductConverter.convertToProductResponse(existingProduct);
    }

    @Override
    public ApiResponse deleteProductById(Long id) {
        ApiResponse response = new ApiResponse();
        Product productToDelete = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
         productRepository.delete(productToDelete);
        response.setStatus(true);
        response.setMessage("Product is deleted with id : "+id);
         return response;
    }
    private Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    private Payload convertToPayload(ProductRequest productRequest, Long approvalId) {
        return Payload.builder()
                .name(productRequest.getName())
                .status(productRequest.getStatus())
                .price(productRequest.getPrice())
                .approvalId(approvalId)
                .build();
    }

    private void pushToApprovalQueueAndSave(ProductRequest productRequest) {
        Product product = saveProduct(ProductConverter.getProductFromRequest(productRequest, false));
        Approval dbApproval = getAndSaveApproval(product);
        sendEventsToTopic(convertToPayload(productRequest, dbApproval.getId()));
    }

    private Approval getAndSaveApproval(Product product) {
        Approval approval = new Approval();
        approval.setProduct(product);
        approval.setStatus(ApprovalStatus.PENDING);
        approval.setRequestDate(LocalDateTime.now());
        approval.setId(product.getId());
        return approvalRepository.save(approval);
    }

    private void sendEventsToTopic(Payload payload) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, payload);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[" + payload.toString() +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                log.info("Unable to send message=[" +
                        payload.toString() + "] due to : " + ex.getMessage());
            }
        });
    }
}
