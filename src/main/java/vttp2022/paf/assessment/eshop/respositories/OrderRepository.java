package vttp2022.paf.assessment.eshop.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp2022.paf.assessment.eshop.models.Order;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class OrderRepository {

	// TODO: Task 3

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	LineItemRepository lineItemRepo;

	@Transactional
	public void insertOrder(Order o){
		System.out.println("---------------------transaction start--------------------");
		jdbcTemplate.update(SQL_INSERT_ORDER, o.getOrderId(), o.getOrderDate(), o.getName());
		lineItemRepo.insertLineItem(o.getLineItems(), o.getOrderId());
		System.out.println("---------------------transaction end--------------------");
	}
}
