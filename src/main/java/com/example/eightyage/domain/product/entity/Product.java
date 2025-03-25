package com.example.eightyage.domain.product.entity;

import com.example.eightyage.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product")
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Enumerated(EnumType.STRING)
    @Setter private Category category;

    @Setter private String content;

    @Setter private Integer price;

    @Enumerated(EnumType.STRING)
    @Setter private SaleState saleState;

    public Product(String name, Category category, String content, Integer price, SaleState saleState) {
        this.name = name;
        this.category = category;
        this.content = content;
        this.price = price;
        this.saleState = saleState;
    }
}
