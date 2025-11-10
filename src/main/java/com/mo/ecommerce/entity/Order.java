package com.mo.ecommerce.entity;

import com.mo.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @ManyToOne
    @JoinColumn(name="user_id" , nullable= false)
    private User user;

    @OneToMany(mappedBy="order" ,  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Double totalPrice;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;


    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updated;
    private String trackingNumber;
    private String shippingAddress;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;





}
