package machineDAO;

import machineDTO.machineItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface machineDAO {

    machineItem addItem(machineItem item);
    // Create a new item.
    String newId();
    machineItem removeStock(String itemId);
    // Remove item. This will be used to remove items from STOCK, not the item itself.
    machineItem getItem(String itemId);
    // Returns the values of an item.
    List<machineItem> getAllItems();
    // Returns the inventory.
    int checkItem(String itemId);
    // Check the current stock of an item.
    void saveInventory() throws SQLException;
    // Called at program exit
    void loadInventory() throws InventoryNotFoundException, SQLException;
    // Called at program start
    void runInventory() throws IOException, InventoryNotFoundException, SQLException;
    void sqlConnect() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException;

}
