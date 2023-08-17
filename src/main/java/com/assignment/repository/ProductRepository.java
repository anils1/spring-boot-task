package com.assignment.repository;

import com.assignment.entities.Product;
import com.assignment.enums.ApprovalStatus;
import com.assignment.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatusOrderByPostedDateDesc(ProductStatus status);

    @Query("SELECT p FROM Product p WHERE " +
            "(:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:minPostedDate IS NULL OR p.postedDate >= :minPostedDate) " +
            "AND (:maxPostedDate IS NULL OR p.postedDate <= :maxPostedDate) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:approvalStatus IS NULL OR p.approvalStatus = :approvalStatus)")
    List<Product> customSearchProducts(
            @Param("productName") String productName,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minPostedDate") LocalDateTime minPostedDate,
            @Param("maxPostedDate") LocalDateTime maxPostedDate,
            @Param("status") ProductStatus status,
            @Param("approvalStatus") ApprovalStatus approvalStatus
    );
}
