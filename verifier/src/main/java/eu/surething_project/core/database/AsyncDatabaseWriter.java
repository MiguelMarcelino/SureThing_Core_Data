package eu.surething_project.core.database;

import eu.surething_project.core.database.data.LocationProofData;

import java.util.concurrent.LinkedBlockingQueue;

public class AsyncDatabaseWriter implements Runnable {
    private LinkedBlockingQueue<LocationProofData> queue = new LinkedBlockingQueue<>();

    private DatabaseAccessManagement dbAccessMgmt;

    public AsyncDatabaseWriter() {
        dbAccessMgmt = new DatabaseAccessManagement();
    }

    public void run() {
        try {
            LocationProofData locationProof;
            while (!queue.isEmpty()) {
                locationProof = queue.take();
                dbAccessMgmt.addProofData(locationProof);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scheduleDBWrite(LocationProofData data) {
        queue.add(data);
    }
}
