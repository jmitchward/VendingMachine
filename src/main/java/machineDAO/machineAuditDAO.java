package machineDAO;

import java.io.IOException;

public interface machineAuditDAO {
    void writeAuditEntry(String entry) throws InventoryNotFoundException, IOException;
}
