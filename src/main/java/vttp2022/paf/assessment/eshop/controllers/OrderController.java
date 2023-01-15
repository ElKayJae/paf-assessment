package vttp2022.paf.assessment.eshop.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;
import vttp2022.paf.assessment.eshop.services.CustomerService;
import vttp2022.paf.assessment.eshop.services.OrderStatusService;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

@RestController
public class OrderController {

	@Autowired
	CustomerService customerService;

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	WarehouseService warehouseService;

	@Autowired
	OrderStatusService orderStatusService;

	//TODO: Task 3
	@PostMapping (path = "/api/order", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createOrder(@RequestBody String req){

			InputStream is = new ByteArrayInputStream(req.getBytes());
			JsonReader r = Json.createReader(is);
			JsonObject reqObject = r.readObject();

			System.out.println(reqObject.toString());

			String name = reqObject.getString("name");
			Optional<Customer> opt = customerService.findCustomerByName(name);

		if (opt.isEmpty()){
			JsonObject o = Json.createObjectBuilder().add("error", "Customer %s not found".formatted(name)).build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(o.toString());
		}

		Order order = new Order();
		order.setCustomer(opt.get());
		order.setOrderId(UUID.randomUUID().toString().substring(0,8));

		JsonArray lineItems = reqObject.getJsonArray("lineItems");
		for (int i = 0; i < lineItems.size(); i++) {
			LineItem lineItem = new LineItem();
			JsonObject lineObject = lineItems.getJsonObject(i);
			lineItem.setItem(lineObject.getString("item"));
			lineItem.setQuantity(lineObject.getInt("quantity"));
			order.addLineItem(lineItem);
			System.out.println(lineItem.getItem());
		}
		try {
			orderRepo.insertOrder(order);
		} catch (Exception e) {
			JsonObject o = Json.createObjectBuilder().add("error", e.getMessage()).build();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(o.toString());
		}

		OrderStatus orderstatus = warehouseService.dispatch(order);


		if (orderstatus.getDeliveryId() == null){
			JsonObject o = Json.createObjectBuilder()
				.add("orderId", orderstatus.getOrderId())
				.add("status", orderstatus.getStatus())
				.build();

				return ResponseEntity.status(HttpStatus.OK).body(o.toString());

		}

		JsonObject o = Json.createObjectBuilder()
		.add("orderId", orderstatus.getOrderId())
		.add("deliveryId", orderstatus.getDeliveryId())
		.add("status", orderstatus.getStatus())
		.build();

		return ResponseEntity.status(HttpStatus.OK).body(o.toString());
	}

	@GetMapping(path = "/api/order/{name}/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getCustomerOrders(@PathVariable String name){

		Optional<JsonObject> opt = customerService.findAllOrderStatusByName(name);

		if (opt.isEmpty()){
			JsonObject o = Json.createObjectBuilder().add("error", "No orders found for %s".formatted(name)).build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(o.toString());
		}

		JsonObject o = opt.get();

		return ResponseEntity.ok().body(o.toString());
	}
}
