package machineDAO;

import machineDTO.machineItem;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;
import java.util.Properties;

public class machineSQL {

    BasicDataSource ds = new BasicDataSource();
    String userName = "root";
    String password = "heroes";
    String serverName = "localhost";
    int portNumber = 3306;
    Connection conn = null;

    public machineSQL() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        ds.setUrl("jdbc:mysql://localhost/vendingmachine");
        ds.setUsername(userName);
        ds.setPassword(password);
        getConnection();
    }

    public machineSQL(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public machineSQL(String serverName, int portNumber) {
        this.serverName = serverName;
        this.portNumber = portNumber;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        conn = ds.getConnection();
        return conn;
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

    public ResultSet executeStmt(String query) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.executeQuery();
    }

    public void executeUpdate(String query) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.executeUpdate();
    }

    public String updateItem(String itemId, int itemStock) {
        return ("UPDATE inventory SET itemStock = " + itemStock + " WHERE itemId = " + Integer.parseInt(itemId));
    }

    public String addItem(machineItem item) {
        return ("INSERT INTO inventory (itemName, itemCost, itemStock) VALUES('"
                + item.getName() + "',"
                + item.getCost() + ","
                + item.getStock()+ ")");
    }

    public String removeItem(String itemId) {
        return ("UPDATE inventory SET itemStock = itemStock - 1 WHERE itemId = " + itemId);
    }

    public String getItem(String itemId) {
        return ("SELECT * FROM inventory WHERE itemId = " + itemId);
    }

    public String getAllItems() {
        return ("SELECT * FROM inventory");
    }

    public String checkItem(String itemId) {
        return ("SELECT itemStock FROM inventory WHERE itemId = " + itemId);
    }
}
