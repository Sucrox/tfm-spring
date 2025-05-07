package es.upm.miw.companyds.tfm_spring.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.companyds.tfm_spring.persistence.model.Address;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {

    private int id;
    @NotBlank
    private String street;
    @NotBlank
    private String number;
    private String floor;
    private String door;
    @NotBlank
    private String postalCode;
    private String city;

    @NotBlank
    private String userPhone;

    public static AddressDto ofAddress(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .number(address.getNumber())
                .floor(address.getFloor())
                .door(address.getDoor())
                .postalCode(address.getPostalCode())
                .city(address.getCity())
                .userPhone(address.getUser().getPhone())
                .build();
    }

    public Address toAddress() {
        Address address = new Address();
        BeanUtils.copyProperties(this, address);
        return address;
    }
}
