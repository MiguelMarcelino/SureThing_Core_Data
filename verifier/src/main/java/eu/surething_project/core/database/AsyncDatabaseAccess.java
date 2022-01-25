package eu.surething_project.core.database;

import eu.surething_project.core.database.data.LocationClaimData;
import eu.surething_project.core.database.data.LocationProofData;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class AsyncDatabaseAccess {
//    private LinkedBlockingQueue<LocationProofData> queue = new LinkedBlockingQueue<>();

    private static final ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    private DatabaseAccessManagement dbAccessMgmt;

    public AsyncDatabaseAccess(DatabaseAccessManagement dbAccessMgmt) {
        this.dbAccessMgmt = dbAccessMgmt;
    }

//    public void run() {
//        try {
//            LocationProofData locationProof;
//            while (!queue.isEmpty()) {
//                locationProof = queue.take();
//                dbAccessMgmt.addProofData(locationProof);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public void writeProofAsync(LocationProofData data) {
//        queue.add(data);
        executor.submit(() -> {
            // Just to check if it is async
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dbAccessMgmt.addProofData(data);
        });
    }

    public LocationClaimData getLocationProofAsync(String proverId) {
        LocationClaimData claimData = null;
        Future future = executor.submit(() -> dbAccessMgmt.getLastClaimByProverId(proverId));
        while (!future.isDone()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            claimData = (LocationClaimData) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return claimData;
    }

}
