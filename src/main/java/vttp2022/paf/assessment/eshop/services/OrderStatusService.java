package vttp2022.paf.assessment.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import vttp2022.paf.assessment.eshop.respositories.OrderStatusRepository;

@Service
public class OrderStatusService {
    @Autowired
    OrderStatusRepository orderStatusRepo;

    public SqlRowSet findOrderStatusByOrderId(String id){
        return orderStatusRepo.findOrderStatusByOrderId(id);
    }
}
