package com.sweettracker.datalock.distributed_lock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private long quantity;

    @Version
    private long version;

    @Builder
    public Product3(Long id, String name, int quantity, Long version) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.version = version;
    }

    public void decrease(long quantity) {
        this.quantity -= quantity;
    }
}
