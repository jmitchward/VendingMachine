package machineServiceTest;

import machineDAO.InventoryNotFoundException;
import machineDTO.machineItem;
import machineService.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class machineServiceTest {

    private final machineService service;


    public machineServiceTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = context.getBean("service", machineService.class);
    }

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testAddNewItem() {
        // Arrange
        String itemId = service.requestId();
        // This will return 002, the next valid ID
        String itemName = "Test Object";
        BigDecimal itemCost = new BigDecimal("10.00");
        int itemStock = 1;
        // Act

        try {
            service.addItem(new machineItem(itemId, itemName, itemCost, itemStock));
            // With our stub, this item should be completely valid in the DAO.
        } catch (BlankValueException
            | RepeatIdException
            | IOException
            | InventoryNotFoundException e) {
            // Assert
            fail("This id does not exist, no exception expected.");

        }
    }

    @Test
    public void testCreateDuplicateItem() {
        // Arrange
        // For this test, create an item that is the same as the one in our stub.
        // Testing the RepeatIdException
        String itemId = "001";
        String itemName = "Test Item";
        BigDecimal itemCost = new BigDecimal("10.00");
        int itemStock = 2;
        machineItem testItem = new machineItem(itemId, itemName, itemCost, itemStock);
        // Act
        try {
            service.addItem(testItem);
        } catch (BlankValueException
            | InventoryNotFoundException
            | IOException e) {
            // These are not the errors we want to see.
            // Assert
            fail("Wrong error thrown.");
        }
        catch (RepeatIdException e) {
            return;
        }
    }

    @Test
    public void testEmptyItem() {
        // Arrange
        // We want to see if the BlankValueException is thrown
        String itemId = "002";
        String itemName = "";
        BigDecimal itemCost = new BigDecimal("10.00");
        int itemStock = 2;
        // Act
        try {
            service.addItem(new machineItem(itemId, itemName, itemCost, itemStock));
        } catch (InventoryNotFoundException
            | IOException
            | RepeatIdException e) {
            fail("Wrong errors");
        } catch (BlankValueException e) {
            return;
        }
    }

    @Test
    public void testUserTotal() {
        // Arrange
        BigDecimal userTotal = new BigDecimal("10.00");
        // Act
        service.setTotal(userTotal);
        // Assert
        assertEquals(userTotal, service.checkTotal());
    }

    @Test
    public void testUserFundsException() {
        // Arrange
        BigDecimal userTotal = new BigDecimal("5.00");
        service.setTotal(userTotal);
        // Act
        try {
            service.purchaseItem("001");
        } catch (NoItemInventoryException
                | IOException
                | InventoryNotFoundException e) {
            fail("Wrong exception thrown.");
        } catch (InsufficientFundsException e) {
            return;
        }
        fail("No exception throw.");
    }

    @Test
    public void testPurchaseItem() throws InsufficientFundsException, NoItemInventoryException, InventoryNotFoundException, IOException{
        // Assign
        String itemId = "001";
        // Create a new item
        service.setTotal(new BigDecimal("10.00"));
        // Set the user total to the correct amount.
        machineItem retrievedItem = service.purchaseItem(itemId);
        // Assert
        assertEquals(retrievedItem.getItemId(), itemId, "IDs are the same.");
        assertEquals(retrievedItem.getName(), service.getItem(itemId).getName(), "Names are the same.");
        assertEquals(retrievedItem.getCost(), service.getItem(itemId).getCost(), "Cost is the same.");
        assertEquals(service.checkTotal(), new BigDecimal("0.00"));
    }

    @Test
    public void testChangeReturnA() {
        // Act
        service.setTotal(new BigDecimal("1.50"));
        // Set userTotal to 1.50
        List<BigDecimal> change = service.returnChange();
        // Quarters will be in 0, then following to element 3 for pennies.
        // 2.50 should result in all quarters, precisely 6
        // Assert
        assertEquals(change.get(0), new BigDecimal("6"));
        assertEquals(change.get(1), new BigDecimal("0"));
        assertEquals(change.get(2), new BigDecimal("0"));
        assertEquals(change.get(3), new BigDecimal("0"));
    }

    @Test
    public void testChangeReturnB() {
        // Act
        service.setTotal(new BigDecimal("0.53"));
        // Set userTotal to 0.53
        List<BigDecimal> change = service.returnChange();
        // 0.53 should result in 2 quarters, 3 pennies
        // Assert
        assertEquals(change.get(0), new BigDecimal("2"));
        assertEquals(change.get(1), new BigDecimal("0"));
        assertEquals(change.get(2), new BigDecimal("0"));
        assertEquals(change.get(3), new BigDecimal("3"));
    }

    @Test
    public void testChangeReturnC() {
        // Act
        service.setTotal(new BigDecimal("0.46"));
        // Set userTotal to 0.46
        List<BigDecimal> change = service.returnChange();
        // 0.46 should result in 1 quarter, 2 dimes, 1 penny
        assertEquals(change.get(0), new BigDecimal("1"));
        assertEquals(change.get(1), new BigDecimal("2"));
        assertEquals(change.get(2), new BigDecimal("0"));
        assertEquals(change.get(3), new BigDecimal("1"));
    }

    @Test
    public void testChangeReturnD() {
        // Act
        service.setTotal(new BigDecimal("0.30"));
        // Set userTotal to 0.30
        List<BigDecimal> change = service.returnChange();
        // 0.46 should result in 1 quarter, 1 nickel
        assertEquals(change.get(0), new BigDecimal("1"));
        assertEquals(change.get(1), new BigDecimal("0"));
        assertEquals(change.get(2), new BigDecimal("1"));
        assertEquals(change.get(3), new BigDecimal("0"));
    }
}
