package machineDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class machineAuditDAOImplementation implements machineAuditDAO{

    public static final String AUDIT_FILE = "audit_log.txt";

    @Override
    public void writeAuditEntry(String entry) throws InventoryNotFoundException {
        PrintWriter recorder;

        try {
            recorder = new PrintWriter((new FileWriter(AUDIT_FILE, true)));
        } catch (IOException e) {
            throw new InventoryNotFoundException("Could not get audit log.");
        }

        LocalDateTime timeStamp = LocalDateTime.now();

        recorder.println(timeStamp.toString() + " : " + entry);
        recorder.flush();
    }
}
