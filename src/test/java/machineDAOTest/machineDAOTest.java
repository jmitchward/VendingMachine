package machineDAOTest;

import machineDAO.machineDAO;
import machineDTO.machineItem;
import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class machineDAOTest {
    machineDAO testDAO;

    public machineDAOTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDAO = context.getBean("dao", machineDAO.class);
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws Exception {
//        String testFile = "testInventory.txt";
//        testDAO = new machineDAOImplementation(testFile);
    }

    @AfterEach
    public void tearDown() {
    }

    // Test to see if adding an item to the inventory works properly.
    @Test
    public void testAddItem() {
        // Arrange
        String name = "Test Object";
        BigDecimal cost = new BigDecimal("10.55");
        int stock = 1;
        // Define the other parameters
        // Act
        machineItem newItem = new machineItem(testDAO.newId(), name, cost, stock);
        // Create the new item.
        testDAO.addItem(newItem);
        // Add the item to the test inventory
        machineItem retrievedItem = testDAO.getItem(newItem.getItemId());
        // Try to retrieve the item we just added.
        // Assert
        assertEquals(newItem.getItemId(), retrievedItem.getItemId(), "These IDs are the same.");
        assertEquals(newItem.getName(), retrievedItem.getName(), "These names are the same.");
        assertEquals(newItem.getCost(), retrievedItem.getCost(), "These costs are the same.");
        assertEquals(newItem.getStock(), retrievedItem.getStock(), "These stocks are the same.");
        // If these tests pass, the object was correctly added to the test inventory
    }

    // We will add two items at 1 stock, then purchase one to ensure the stock goes down.
    @Test
    public void testRemoveStock() {
        // Arrange
        // Add an item that is in stock.
        String name = "Test Object";
        BigDecimal cost = new BigDecimal("10.55");
        int stock = 2;
        // Define the other parameters
        machineItem newItem = new machineItem(testDAO.newId(), name, cost, stock);
        // Create second item for display
        name = "Test Object Two";
        cost = new BigDecimal("10.55");
        stock = 1;
        // Define the other parameters
        machineItem secondItem = new machineItem(testDAO.newId(), name, cost, stock);

        // Act
        // Add the items to the test inventory
        testDAO.addItem(newItem);
        testDAO.addItem(secondItem);
        // Assert
        assertEquals(newItem.getStock(), 2, "The first added item has 2 in stock.");
        assertEquals(secondItem.getStock(), 1, "The second added item has 0 in stock.");
        // Make sure the items have the correct amount in stock.
        assertEquals(newItem.getStock(), testDAO.checkItem(newItem.getItemId()));
        assertEquals(secondItem.getStock(), testDAO.checkItem(secondItem.getItemId()));
        // Check that the check item stock method in the DAO works as well.

        // Act
        // Simulate a purchase on the DAO layer
        testDAO.removeStock(newItem.getItemId());
        // Simulate a purchase on the second item.
        testDAO.removeStock(secondItem.getItemId());
        // Assert
        assertEquals(newItem.getStock(), 1, "The first added item has 1 in stock.");
        assertEquals(secondItem.getStock(), 0, "The second added item has 0 in stock.");
    }

    @Test
    public void testGetAll() {
        // Arrange
        // Add items to the stock
        String name = "Test Object";
        BigDecimal cost = new BigDecimal("10.55");
        int stock = 2;
        // Define the other parameters
        machineItem newItem = new machineItem(testDAO.newId(), name, cost, stock);
        // Create second item for display
        name = "Test Object Two";
        cost = new BigDecimal("10.55");
        stock = 1;
        // Define the other parameters
        machineItem secondItem = new machineItem(testDAO.newId(), name, cost, stock);

        // Act
        // Add the items to the test inventory
        testDAO.addItem(newItem);
        testDAO.addItem(secondItem);
        List<machineItem> allItems = testDAO.getAllItems();
        // Get the items back in a list

        // Assert
        assertNotNull(allItems, "There's stuff in there!");
        assertTrue(allItems.contains(newItem), "One of them is the first item!");
        assertTrue(allItems.contains(secondItem), "Also the second one.");
    }

}
