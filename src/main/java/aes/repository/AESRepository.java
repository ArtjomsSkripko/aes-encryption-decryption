package aes.repository;

import java.util.List;

import javax.sql.DataSource;

import aes.model.Customer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class AESRepository {

    protected static final String CLN_CUSTOMER_ID = "customer_id";
    protected static final String CLN_USERNAME = "username";
    protected static final String CLN_PASSWORD = "password";
    protected static final String CLN_NAME = "name";
    protected static final String CLN_SURNAME = "surname";
    protected static final String CLN_ROLE = "role";

    private static final String FIND_USER_SQL =
        "SELECT customer_id, username, password, name, surname, role " +
            "FROM emsdb.customer " +
            "WHERE username=:username";

    private static final String UPDATE_PASSWORD_SQL = "UPDATE emsdb.customer " +
    "SET password=:password WHERE username=:username";

    private final NamedParameterJdbcTemplate parameterTemplate;
    private final SimpleJdbcInsert insertCustomerIml;
    private final CustomerRowExtractor customerRowExtractor = new CustomerRowExtractor();

    @Autowired
    public AESRepository(NamedParameterJdbcTemplate parameterTemplate, DataSource dataSource) {
        this.parameterTemplate = parameterTemplate;
        this.insertCustomerIml = new SimpleJdbcInsert(dataSource).withTableName("emsdb.customer")
            .usingColumns(CLN_USERNAME, CLN_PASSWORD, CLN_NAME, CLN_ROLE, CLN_SURNAME)
            .usingGeneratedKeyColumns(CLN_CUSTOMER_ID);
        insertCustomerIml.withoutTableColumnMetaDataAccess();

    }

    public void addNewCustomer(Customer customer) {
        if (findCustomer(customer.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                String.format("User with [%s] username already exists", customer.getUsername()));
        }

        MapSqlParameterSource passengerParams = new MapSqlParameterSource();
        passengerParams.addValue(CLN_NAME, customer.getName());
        passengerParams.addValue(CLN_PASSWORD, customer.getPassword());
        passengerParams.addValue(CLN_ROLE, customer.getRole());
        passengerParams.addValue(CLN_USERNAME, customer.getUsername());
        passengerParams.addValue(CLN_SURNAME, customer.getSurname());

        insertCustomerIml.executeAndReturnKey(passengerParams).longValue();
    }

    public void updatePassword(String userName, String password) {
        MapSqlParameterSource passengerParams = new MapSqlParameterSource();
        passengerParams.addValue(CLN_USERNAME, userName);
        passengerParams.addValue(CLN_PASSWORD, password);

        parameterTemplate.update(UPDATE_PASSWORD_SQL, passengerParams);
    }

    public Customer findCustomer(String userName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CLN_USERNAME, userName);
        List<Customer> foundCustomers = parameterTemplate.query(FIND_USER_SQL, params, customerRowExtractor);

        if (CollectionUtils.isNotEmpty(foundCustomers)) {
            return foundCustomers.get(0);
        }
        return null;
    }
}
