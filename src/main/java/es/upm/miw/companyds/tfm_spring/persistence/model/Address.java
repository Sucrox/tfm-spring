package es.upm.miw.companyds.tfm_spring.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data //@ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String number;
    private String floor;
    private String door;
    @Column(nullable = false)
    private String postalCode;
    private String city;

    @ManyToOne
    @JoinColumn(name = "user_phone", referencedColumnName = "phone", nullable = false)
    private User user;
}
