package com.atrilos.socksrest.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Slf4j
@Table(name = "socks")
public class Socks {
    /**
     * Primary key. Generation using sequence for better scalability
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "socks_seq")
    @SequenceGenerator(name = "socks_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Socks color
     */
    @Column(name = "color", nullable = false)
    private String color;

    /**
     * Cotton content in socks
     */
    @Column(name = "cotton_part", nullable = false)
    private Integer cottonPart;

    /**
     * Socks quantity
     */
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Socks socks = (Socks) o;
        return getId() != null && Objects.equals(getId(), socks.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PostPersist
    public void logUserAdded() {
        log.info(
                "Added socks: color={}, cottonPart={}, quantity={}",
                color,
                cottonPart,
                quantity
        );
    }
}