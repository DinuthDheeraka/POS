package bo;

import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDetailDAO;
import dao.custom.PlaceOrderDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDetailDAOImpl;
import dao.custom.impl.PlaceOrderDAOImpl;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDTO;
import model.OrderDetailDTO;
import util.IdsGenerator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderBOImpl implements PurchaseOrderBO{

    ItemDAO itemDAOContract = new ItemDAOImpl();
    CustomerDAO customerDAOContract = new CustomerDAOImpl();
    PlaceOrderDAO placeOrderDAOContract = new PlaceOrderDAOImpl();
    OrderDetailDAO orderDetailDAOImplContract = new OrderDetailDAOImpl();
    PurchaseOrderBOImpl purchaseOrderBO = new PurchaseOrderBOImpl();

    public boolean purchaseOrder(String orderId, LocalDate orderDate, String customerId, List<OrderDetailDTO> orderDetails) throws SQLException, ClassNotFoundException {

        /*Transaction*/
        /*if order id already exist*/
        if (placeOrderDAOContract.isExists(orderId)) {
        }
        placeOrderDAOContract.insert(new OrderDTO(orderId,orderDate,customerId));

        for (OrderDetailDTO detail : orderDetails) {
            orderDetailDAOImplContract.insertOrderDetail(new OrderDetailDTO(orderId,detail.getItemCode(),detail.getQty(),
                    detail.getUnitPrice()));

            //Search & Update Item
            ItemDTO item = findItem(detail.getItemCode());
            item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());

            itemDAOContract.update(new ItemDTO(item.getCode(),item.getDescription(),item.getUnitPrice(),item.getQtyOnHand()));
        }
        return true;
    }

    public ItemDTO findItem(String itemCode) {
        return itemDAOContract.get(itemCode);
    }

    public CustomerDTO getCustomer(String id){
        return customerDAOContract.get(id);
    }

    public boolean isExistsItem(String itemCode) throws SQLException, ClassNotFoundException {
        return itemDAOContract.isExists(itemCode);
    }

    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAOContract.isExists(id);
    }

    public String getLastOrderId() throws SQLException, ClassNotFoundException {
        return placeOrderDAOContract.getLastId();
    }

    public ArrayList<String> getAllCustomerIds(){
        return customerDAOContract.getAllIds();
    }

    public ArrayList<String> getAllItemsIds(){
        return itemDAOContract.getAllIds();
    }
}