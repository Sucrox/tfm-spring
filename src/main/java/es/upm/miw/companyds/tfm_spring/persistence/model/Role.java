package es.upm.miw.companyds.tfm_spring.persistence.model;

public enum Role {
    ADMIN, CUSTOMER;

    public static final String PREFIX = "ROLE_";

    public static Role of(String withPrefix) {
        return Role.valueOf(withPrefix.replace(Role.PREFIX, ""));
    }

}
