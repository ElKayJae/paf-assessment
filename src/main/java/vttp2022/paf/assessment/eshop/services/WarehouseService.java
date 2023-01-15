package vttp2022.paf.assessment.eshop.services;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.OrderStatusRepository;

@Service
public class WarehouseService {

	@Autowired
	OrderStatusRepository orderStatusRepo;

	private static final String URL = "http://paf.chuklee.com/dispatch/{order_id}";
	// You cannot change the method's signature
	// You may add one or more checked exceptions
	public OrderStatus dispatch(Order order) {
		
		// TODO: Task 4
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("orderId", order.getOrderId());
		builder.add("name", order.getName());
		builder.add("address", order.getAddress());
		builder.add("email", order.getEmail());

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for (LineItem i : order.getLineItems()){
			JsonObject itemObject = Json.createObjectBuilder()
				.add("item", i.getItem())
				.add("quantity", i.getQuantity()).build();

			arrayBuilder.add(itemObject);
		}

		builder.add("lineItems", arrayBuilder.build());
		builder.add("createdBy", "Low Ke Jun");

		Map<String, String> urlParams = new HashMap<>();
		urlParams.put("order_id", order.getOrderId());
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(URL);
		String requestUrl = uriBuilder.buildAndExpand(urlParams).toUriString();
		System.out.println(requestUrl);
		JsonObject o  = builder.build();
		System.out.println(o.toString());
		OrderStatus orderStatus = new OrderStatus();

		try {
			
			RestTemplate template = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<>(o.toString(), headers);
			ResponseEntity<String> resp = template.postForEntity(requestUrl, request, String.class);
			System.out.println(resp.getBody());
			String payload = resp.getBody();
			InputStream is = new ByteArrayInputStream(payload.getBytes());
			JsonReader r = Json.createReader(is);
			JsonObject jObject = r.readObject();
			String deliveryId = jObject.getString("deliveryId");
			orderStatus.setDeliveryId(deliveryId);
			orderStatus.setStatus("dispatched");
			orderStatus.setOrderId(jObject.getString("orderId"));

		} catch (RestClientException e) {
			System.out.println(e.getMessage());

			orderStatus.setOrderId(order.getOrderId());
			orderStatus.setStatus("pending");
		}
		orderStatusRepo.insertOrderStatus(orderStatus);
		System.out.println(orderStatus.getDeliveryId());
		
		return orderStatus;
	}
}
