package br.dev.jcp.training.springboot6brewery.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
    @Version
    private Long version;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "beer_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id"))
    private Set<Beer> beers = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;

        return getDescription().equals(category.getDescription());
    }

    @Override
    public int hashCode() {
        return getDescription().hashCode();
    }
}