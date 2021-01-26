package machineService;

import machineDAO.InventoryNotFoundException;
import machineDTO.machineItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface machineService {

    machineItem purchaseItem(String itemId) throws InsufficientFundsException, NoItemInventoryException, IOException, InventoryNotFoundException;
    // Facilitates the purchase of an item. This is in the service layer as it contains logic
    void addItem(machineItem item) throws BlankValueException, RepeatIdException, IOException, InventoryNotFoundException;
    String requestId();
    List<machineItem> displayItems();
    // This will be used to filter the items that are not in stock, as it is part of the logic to only display
    // items in stock.
    machineItem getItem(String itemId);
    // Retrieve a single item in the machine
    BigDecimal checkTotal();
    void setTotal(BigDecimal total);
    List<BigDecimal> returnChange();
    // Used to calculate the amount of change returned to the customer. This is in the service layer as it contains logic.
    void exitCascade() throws IOException, InventoryNotFoundException, SQLException;
}
