package vttp2022.paf.assessment.eshop.respositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp2022.paf.assessment.eshop.models.Customer;
import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class CustomerRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// You cannot change the method's signature
	public Optional<Customer> findCustomerByName(String name) {
		// TODO: Task 3 
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_BY_USERNAME, name);
		if (rs.next()){
			Customer c = Customer.createCustomer(rs);
			return Optional.of(c);
		}

		return Optional.empty();
	}

	public Optional<JsonObject> findAllCustomerOrderStatus(String name){

		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_ALL_CUSTOMER_ORDERS, name);
		
		if (!rs.next()) return Optional.empty();

		JsonObjectBuilder builder = Json.createObjectBuilder()
			.add("name", name);
			
			String status = rs.getString("status");
			Integer count = rs.getInt("count");
			builder.add(status, count);

		while(rs.next()){
			status = rs.getString("status");
			count = rs.getInt("count");
			builder.add(status, count);
		}

		return Optional.of(builder.build());
		}
		
}	

