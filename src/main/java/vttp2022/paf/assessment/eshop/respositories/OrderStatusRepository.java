package vttp2022.paf.assessment.eshop.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.OrderStatus;
import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

import java.util.Date;

@Repository
public class OrderStatusRepository {

    @Autowired
    JdbcTemplate template;

    public void insertOrderStatus(OrderStatus orderStatus){
        template.update(SQL_INSERT_ORDER_STATUS, orderStatus.getOrderId(),orderStatus.getDeliveryId(), orderStatus.getStatus(), new Date());
    }

    public SqlRowSet findOrderStatusByOrderId(String id){
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_DELIVERY_BY_ORDER_ID,id);
        rs.next();
        return rs;
    }
}
