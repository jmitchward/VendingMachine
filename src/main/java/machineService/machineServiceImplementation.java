package machineService;

import machineDAO.machineDAO;
import machineDAO.machineAuditDAO;
import machineDTO.machineItem;
import machineDAO.InventoryNotFoundException;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class machineServiceImplementation implements machineService{

    machineDAO dao;
    machineAuditDAO auditor;
    BigDecimal userTotal = new BigDecimal("0.00");
    private final BigDecimal quarter = new BigDecimal("0.25");
    private final BigDecimal dime = new BigDecimal("0.10");
    private final BigDecimal nickel = new BigDecimal("0.05");
    private final BigDecimal penny = new BigDecimal("0.01");

    public machineServiceImplementation(machineDAO dao, machineAuditDAO auditor) throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.dao = dao;
        this.auditor = auditor;
        this.dao.sqlConnect();
    }
    @Override
    public machineItem purchaseItem(String itemId) throws InsufficientFundsException, NoItemInventoryException, IOException, InventoryNotFoundException {

        // First, we must find out if they have any money. Second, we must see if its enough.
        if (userTotal == null || userTotal.compareTo(dao.getItem(itemId).getCost()) == -1) {
//            return new machineItem("", "", new BigDecimal("0"), 0);
            throw new InsufficientFundsException("User does not have enough money.");
        }
        else if (dao.getItem(itemId).getStock() == 0) {
            throw new NoItemInventoryException("There are no more of those items.");
        }
        else {
            machineItem itemPurchased = dao.removeStock(itemId);
            auditor.writeAuditEntry("ITEM PURCHASED: " + dao.getItem(itemId));
            // Store the purchased item.
            userTotal = userTotal.subtract(dao.getItem(itemId).getCost());
            // Subtract the cost of the item from the amount the user has.
            return itemPurchased;
        }
    }

    @Override
    public void addItem(machineItem item) throws BlankValueException, RepeatIdException, IOException, InventoryNotFoundException {
        if (dao.getItem(item.getItemId()) instanceof machineItem) {
            throw new RepeatIdException("This item already exists");

        }
        validateAdminAdd(item);
        dao.addItem(item);
        auditor.writeAuditEntry("ITEM ADDED: " + item.getItemId());
    }

    @Override
    public String requestId() {
        String itemId = dao.newId();
        return itemId;
    }

    public void validateAdminAdd(machineItem item) throws BlankValueException {
        if (item.getName() == null
        || item.getStock() == 0
        || item.getCost() == null
        || item.getItemId() == null) {
            throw new BlankValueException("Field left blank.");
        }
    }

    @Override
    public List<machineItem> displayItems() {
        List<machineItem> itemsInStock = dao.getAllItems().stream()
                .filter((item -> item.getStock() > 0))
                .collect(Collectors.toList());
        return itemsInStock;
    }

    @Override
    public machineItem getItem(String itemId) {
        return dao.getItem(itemId);
    }

    @Override
    public BigDecimal checkTotal() {
            return userTotal;
    }

    @Override
    public void setTotal(BigDecimal total) {
        if (userTotal == null || userTotal.equals(0)) {
            this.userTotal = total;
        }
        else {
            this.userTotal = userTotal.add(total);
        }

    }

    @Override
    public List<BigDecimal> returnChange() {
        BigDecimal remain;
        BigDecimal zero = new BigDecimal("0");
        List<BigDecimal> change = new ArrayList<>();
        change.add(userTotal.divideToIntegralValue(quarter));
        remain = userTotal.remainder(quarter,  new MathContext(3, RoundingMode.HALF_DOWN));
        if (!remain.equals(0)) {
            change.add((remain.divideToIntegralValue(dime)));
            remain = remain.remainder(dime, new MathContext(3, RoundingMode.HALF_DOWN));
            if (!remain.equals(0)) {
                change.add(remain.divideToIntegralValue(nickel));
                remain = remain.remainder(nickel,  new MathContext(3, RoundingMode.HALF_DOWN));
                if (!remain.equals(0)) {
                    change.add(remain.divideToIntegralValue(penny));
                } else {
                    change.add(zero);
                }
            } else {
                change.add(zero);
            }
        }
        else {
            change.add(zero);
            }
        return change;
    }

    @Override
    public void exitCascade() throws IOException, InventoryNotFoundException, SQLException {
        dao.runInventory();
    }
}
