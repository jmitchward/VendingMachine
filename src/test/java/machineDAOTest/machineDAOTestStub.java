package machineDAOTest;

import machineDTO.machineItem;
import machineDAO.machineDAO;
import machineDAO.InventoryNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class machineDAOTestStub implements machineDAO{

    public machineItem onlyItem;
    public String nextId = "002";

    public machineDAOTestStub() {
        String newId = "001";
        String name = "Test Item";
        BigDecimal cost = new BigDecimal("10.00");
        int stock = 2;
        onlyItem = new machineItem(newId, name, cost, stock);
    }

    machineDAOTestStub(machineItem testItem) {
        this.onlyItem = testItem;
    }

    @Override
    public machineItem addItem(machineItem item) {
        if (item.getItemId().equals(onlyItem.getItemId())) {
            // If the service layer bounces back, then the ID already exists
            return onlyItem;
        }
        else {
            // Else...what it returns does not matter.
            return null;
        }
    }

    @Override
    public String newId() {
        String newId = nextId;
        nextId = String.format("%03d", (Integer.parseInt(nextId) + 1));
        return newId;
    }

    @Override
    public machineItem removeStock(String itemId) {
        if (itemId.equals((onlyItem.getItemId()))) {
            return onlyItem;
        }
        else {
            return null;
        }
    }

    @Override
    public machineItem getItem(String itemId) {
        if (itemId.equals(onlyItem.getItemId())) {
            return onlyItem;
        }
        else {
            return null;
        }
    }

    @Override
    public List<machineItem> getAllItems(){
        List<machineItem> itemList = new ArrayList<>();
        itemList.add((onlyItem));
        return itemList;
    }

    @Override
    public int checkItem(String itemId) {
        if (itemId.equals(onlyItem.getItemId())) {
            return onlyItem.getStock();
        }
        else {
            return 0;
        }
    }

    @Override
    public void saveInventory(Map<String, machineItem> items) throws IOException, InventoryNotFoundException {

    }

    @Override
    public void loadInventory() throws InventoryNotFoundException {

    }

    @Override
    public void runInventory() throws IOException, InventoryNotFoundException {

    }
}
