package vttp2022.paf.assessment.eshop.respositories;

public class Queries {
    public static final String SQL_FIND_BY_USERNAME = "select * from customers where name=?";
    public static final String SQL_INSERT_ORDER = "insert into orders(order_id, order_date, name) values (?,?,?)";
    public static final String SQL_INSERT_LINEITEM = "insert into line_item(item, quantity, order_id) values (?,?,?)";
    public static final String SQL_INSERT_ORDER_STATUS = "insert into order_status(order_id, delivery_id, status, status_update) values (?,?,?,?)";
    public static final String SQL_FIND_DELIVERY_BY_ORDER_ID = "select * from order_status where order_id=?";
    public static final String SQL_FIND_ALL_CUSTOMER_ORDERS = "select count(orders.order_id) as count, name, status from orders join order_status on orders.order_id = order_status.order_id where name = ? group by status order by status"
    ;

}
