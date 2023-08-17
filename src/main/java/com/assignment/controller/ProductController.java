package com.assignment.controller;

import com.assignment.entities.Product;
import com.assignment.enums.ApprovalStatus;
import com.assignment.enums.ProductStatus;
import com.assignment.model.ProductSearchCriteria;
import com.assignment.request.ProductRequest;
import com.assignment.response.ApiResponse;
import com.assignment.response.ProductResponse;
import com.assignment.service.ApprovalService;
import com.assignment.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    private ApprovalService approvalService;

    @Autowired
    public ProductController(ProductService productService, ApprovalService approvalService){
        this.productService = productService;
        this.approvalService = approvalService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllActiveProducts() {
        List<Product> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest product) {
            return new ResponseEntity<>(productService.createProduct(product), HttpStatus.OK);
    }
    @GetMapping("/approval-queue")
    public ResponseEntity<List<ProductResponse>> getAllProductInApprovalQueue(){
        return new ResponseEntity<>(approvalService.getAllProduct(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "minPostedDate", required = false) LocalDateTime minPostedDate,
            @RequestParam(name = "maxPostedDate", required = false) LocalDateTime maxPostedDate,
            @RequestParam(name = "status", required = false) ProductStatus status,
            @RequestParam(name = "approvalStatus", required = false) ApprovalStatus approvalStatus
    ) {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setProductName(productName);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setMinPostedDate(minPostedDate);
        criteria.setMaxPostedDate(maxPostedDate);
        criteria.setStatus(status);
        criteria.setApprovalStatus(approvalStatus);

        return productService.searchProducts(criteria);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@RequestBody ProductRequest product, @PathVariable Long id){
        return productService.updateProduct(product, id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteProduct(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

    @GetMapping("/approval-queue/{approvalId}/approve")
    public ProductResponse approveProduct(@PathVariable Long approvalId) {
        return approvalService.approveProduct(approvalId);
    }

    @GetMapping("/approval-queue/{approvalId}/reject")
    public ProductResponse rejectProduct(@PathVariable Long approvalId) {
        return approvalService.rejectProduct(approvalId);
    }


}
