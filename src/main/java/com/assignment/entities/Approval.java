package com.assignment.entities;

import com.assignment.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {
    @Id
    private Long id;
    @OneToOne
    private Product product;
    @Enumerated
    private ApprovalStatus status;
    private LocalDateTime requestDate;
}
