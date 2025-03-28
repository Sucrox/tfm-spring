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
@Table(name = "cds_user")
public class User {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true, nullable = false)
    private String phone;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String familyName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String dni;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
