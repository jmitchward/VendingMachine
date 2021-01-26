package machineDAO;

import machineDTO.machineItem;

import java.io.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class machineDAOImplementation implements machineDAO {

    private final Map<String, machineItem> inventory = new HashMap<>();
    machineSQL sqlConnector = new machineSQL();
    private final String INVENTORY;
    public static final String DELIMITER = "::";
    public String nextId = "000";

    public machineDAOImplementation() throws InventoryNotFoundException, SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        INVENTORY = "inventory_file.txt";
        loadInventory();
        // No parameter constructor, production use.
    }

    public machineDAOImplementation(String newFile) throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        INVENTORY = newFile;
        // Overloaded constructor, testing use.
    }

    @Override
    public machineItem addItem(machineItem item) {
        machineItem newItem = inventory.put(item.getItemId(), item);
        return newItem;
    }

    @Override
    public String newId() {
        String newId = nextId;
        nextId = String.format("%03d", (Integer.parseInt(nextId) + 1));
        return newId;
    }

    @Override
    public machineItem removeStock(String itemId) {
        // Items will not be removed from the inventory, their stock can only be decreased.
        inventory.get(itemId).setStock(inventory.get(itemId).getStock() - 1);
        return inventory.get(itemId);
    }

    @Override
    public machineItem getItem(String itemId) {
        return inventory.get(itemId);
        // Return the desired item from the inventory map.
    }

    @Override
    public List<machineItem> getAllItems() {
        return new ArrayList<machineItem>(inventory.values());
        // Create and return an array list of values in the inventory map.
    }

    @Override
    public int checkItem(String itemId) {
        return inventory.get(itemId).getStock();
        // Return the number of an item remaining.
    }

    @Override
    public void runInventory() throws SQLException {
        saveInventory();
    }

//    @Override
//    public void saveInventory(Map<String, machineItem> items) throws InventoryNotFoundException, IOException {
//        PrintWriter saver;
//        // Declare file writing object.
//
//        try {
//            saver = new PrintWriter(new FileWriter(INVENTORY));
//            // Try to implement file writing object.
//        } catch (FileNotFoundException e) {
//            throw new InventoryNotFoundException("Could not load inventory.");
//            // If the file doesn't exist, throw custom exception.
//        }
//
//        for (Map.Entry<String, machineItem> thisItem : items.entrySet()) {
//            // For each item in the received map object.
//            saver.println(marshallForth(thisItem.getValue()));
//            // Pass item to the marshalling function, formatting it for storage.
//        }
//
//        saver.close();
//        // Close the saver.
//    }

    @Override
    public void saveInventory() throws SQLException {
        for (machineItem item : inventory.values()) {
            String query = sqlConnector.updateItem(item.getItemId(), item.getStock());
            sqlConnector.executeUpdate(query);
        }

    }

//    @Override
//    public void loadInventory() throws InventoryNotFoundException{
//        Scanner loader;
//        // Declare file reading object.
//
//        try {
//            loader = new Scanner(new BufferedReader(new FileReader(INVENTORY)));
//            // Try to implement file reading object.
//        } catch (FileNotFoundException e) {
//            throw new InventoryNotFoundException("Could not load inventory.");
//            // If the file doesn't exist, throw custom exception.
//        }
//
//        String thisLine;
//        // Declare temporary string variable.
//        machineItem thisItem;
//        // Declare temporary object variable.
//
//        while (loader.hasNextLine()) {
//            // While there are lines in the file.
//            thisLine = loader.nextLine();
//            // Put them in the string variable.
//            thisItem = unmarshallItems(thisLine);
//            // Pass the string variable into the unmarshalling function, returning it as a machineItem object.
//            inventory.put(thisItem.getItemId(), thisItem);
//            // Repopulate the Map with machineItem objects from the file.
//        }
//
//        loader.close();
//        // Close the loader.
//    }

    @Override
    public void loadInventory() throws SQLException {
        ResultSet dbInventory = sqlConnector.executeStmt(sqlConnector.getAllItems());
        while (dbInventory.next()) {
            String itemId = dbInventory.getString("itemId");
            String itemName = dbInventory.getString("itemName");
            BigDecimal itemCost = dbInventory.getBigDecimal("itemCost");
            int itemStock = dbInventory.getInt("itemStock");
            machineItem thisItem = new machineItem(itemId, itemName, itemCost, itemStock);
            inventory.put(thisItem.getItemId(), thisItem);
        }
    }

    private String marshallForth(machineItem item) {
        // Receive the machineItem object from the save inventory method.
        String itemString = item.getItemId() + DELIMITER;
        // Declare a string, then store the first item.
        itemString += item.getName() + DELIMITER;
        itemString += item.getCost() + DELIMITER;
        itemString += item.getStock() + DELIMITER;
        // Append the string with each object value.
        return itemString;
        // ItemId::ItemName::ItemCost::ItemStock::
        // Return the object in string form.
    }

    private machineItem unmarshallItems(String itemString) {
        // Receive a string from the load inventory method, retrieved directly from the file.
        String[] itemElements = itemString.split(DELIMITER);
        // Declare a string array, populate it with Strings created by splitting the retrieved string from file
        String itemId = itemElements[0];
        if (Integer.parseInt(itemId) > Integer.parseInt(nextId)) {
            nextId = String.format("%03d", (Integer.parseInt(itemId) + 1));
        }
        // The first element in the string array will always be the item id.
        String itemName = itemElements[1];
        // The second element will be the item name.
        BigDecimal itemCost = BigDecimal.valueOf(Double.parseDouble(itemElements[2]));
        // Third cost of the item.
        int itemStock = Integer.parseInt(itemElements[3]);
        // Fourth the number of items in stock.
        machineItem fileItem = new machineItem(itemId, itemName, itemCost, itemStock);
        // Using the object constructor, create a machineItem object
        return fileItem;
        // Return this object to the load inventory method.
    }

    @Override
    public void sqlConnect() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        sqlConnector.getConnection();
    }
}
