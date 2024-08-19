package br.dev.jcp.training.springboot6brewery.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(name = "beer_order")
public class BeerOrder {

    public BeerOrder(String id, LocalDateTime createdDate, String customerRef, LocalDateTime lastModifiedDate, Long version, Customer customer, Set<BeerOrderLine> beerOrderLines) {
        this.id = id;
        this.createdDate = createdDate;
        this.customerRef = customerRef;
        this.lastModifiedDate = lastModifiedDate;
        this.version = version;
        this.setCustomer(customer);
        this.beerOrderLines = beerOrderLines;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Size(max = 36)
    @Column(name = "id", length = 36, columnDefinition = "varchar", nullable = false)
    private String id;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Size(max = 255)
    @Column(name = "customer_ref")
    private String customerRef;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "version")
    private Long version;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "beerOrder")
    private Set<BeerOrderLine> beerOrderLines;

    public boolean isNew() {
        return id == null;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getBeerOrders().add(this);
    }
}