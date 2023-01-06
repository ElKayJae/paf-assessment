package vttp2022.paf.assessment.eshop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepo;

    public Optional<Customer> findCustomerByName(String name){
        return customerRepo.findCustomerByName(name);
    }

    public Optional<JsonObject> findAllOrderStatusByName(String name){
        return customerRepo.findAllCustomerOrderStatus(name);
    }
}
