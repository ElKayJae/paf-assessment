package vttp2022.paf.assessment.eshop.services;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
		System.out.println(builder.build().toString());
		RestTemplate template = new RestTemplate();
	
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JsonObject> request = new HttpEntity<>(builder.build(), headers);
		ResponseEntity<String> resp = template.postForEntity(requestUrl, request, String.class);
		System.out.println(resp.getBody());
		System.out.println("TEST TEST");
		String payload = resp.getBody();
		InputStream is = new ByteArrayInputStream(payload.getBytes());
		JsonReader r = Json.createReader(is);
		JsonObject jObject = r.readObject();
		String deliveryId = jObject.getString("deliveryId");
		String status = "dispatched";
		if (null == deliveryId){
			status = "pending";
		}
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setDeliveryId(deliveryId);
		orderStatus.setStatus(status);
		orderStatus.setOrderId(jObject.getString("orderId"));
		orderStatusRepo.insertOrderStatus(orderStatus);
		
		return null;

	}
}
