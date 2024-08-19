package br.dev.jcp.training.springboot6brewery.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "beer_order_line")
public class BeerOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Size(max = 36)
    @Column(name = "id", columnDefinition = "varchar(36)", updatable = false, nullable = false, length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "beer_id")
    private Beer beer;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "order_quantity")
    private Integer orderQuantity = 0;

    @Column(name = "quantity_allocated")
    private Integer quantityAllocated = 0;

    @Column(name = "version")
    private Long version;

    @ManyToOne
    @JoinColumn(name = "beer_order_id")
    private BeerOrder beerOrder;

    public boolean isNew() {
        return id == null;
    }
}