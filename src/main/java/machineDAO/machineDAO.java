package machineDAO;

import machineDTO.machineItem;

import java.io.IOException;
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
    void saveInventory(Map<String, machineItem> items) throws IOException, InventoryNotFoundException;
    // Called at program exit
    void loadInventory() throws InventoryNotFoundException;
    // Called at program start
    void runInventory() throws IOException, InventoryNotFoundException;

}
