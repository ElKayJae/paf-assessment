package vttp2022.paf.assessment.eshop.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.LineItem;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

import java.util.List;

@Repository
public class LineItemRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertLineItem(List<LineItem> itemList, String order_id){
        List<Object[]> data = itemList.stream().map(i ->{
            Object[] o = new Object[]{i.getItem(), i.getQuantity(), order_id};
            return o;
        }).toList();
        jdbcTemplate.batchUpdate(SQL_INSERT_LINEITEM, data);
    }
    
}
