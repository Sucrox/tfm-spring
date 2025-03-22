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
@Table(name = "cdsUser") // conflict with user table
public class User {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true, nullable = false)
    private String phone;
    private String firstName;
    private String familyName;
    private String email;
    private String dni;
//TODO
//  private String address;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
