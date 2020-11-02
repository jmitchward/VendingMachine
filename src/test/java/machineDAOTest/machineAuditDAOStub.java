package machineDAOTest;

import machineDAO.InventoryNotFoundException;
import machineDAO.machineAuditDAO;

import java.io.IOException;

public class machineAuditDAOStub implements machineAuditDAO {
    @Override
    public void writeAuditEntry(String entry) throws InventoryNotFoundException, IOException {

    }
}
