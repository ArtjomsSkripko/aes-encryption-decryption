package aes.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ResponseDTO {

    private String token;
    private Customer customer;

    public ResponseDTO() {
    }

    public ResponseDTO(String token, Customer customer) {
        this.token = token;
        this.customer = customer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ResponseDTO that = (ResponseDTO) o;

        return new EqualsBuilder()
            .append(token, that.token)
            .append(customer, that.customer)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(token)
            .append(customer)
            .toHashCode();
    }
}
