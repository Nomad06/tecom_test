package com.bubalex.test.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false;

    /**
     * Life-cycle hook for entity insertion (should be extended in subtypes).
     */
    @PrePersist
    void onCreate() {
        this.deleted = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Life-cycle hook for entity modification (should be extended in subtypes).
     */
    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
