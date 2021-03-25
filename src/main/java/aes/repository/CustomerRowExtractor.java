package aes.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import aes.model.Customer;
import org.springframework.jdbc.core.RowMapper;

import static aes.repository.AESRepository.CLN_CUSTOMER_ID;
import static aes.repository.AESRepository.CLN_NAME;
import static aes.repository.AESRepository.CLN_PASSWORD;
import static aes.repository.AESRepository.CLN_ROLE;
import static aes.repository.AESRepository.CLN_SURNAME;
import static aes.repository.AESRepository.CLN_USERNAME;

public class CustomerRowExtractor implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt(CLN_CUSTOMER_ID));
        customer.setName(rs.getString(CLN_NAME));
        customer.setPassword(rs.getString(CLN_PASSWORD));
        customer.setRole(rs.getString(CLN_ROLE));
        customer.setSurname(rs.getString(CLN_SURNAME));
        customer.setUsername(rs.getString(CLN_USERNAME));

        return customer;
    }

}
