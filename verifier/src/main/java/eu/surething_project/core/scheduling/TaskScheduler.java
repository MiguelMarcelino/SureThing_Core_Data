package eu.surething_project.core.scheduling;

import eu.surething_project.core.database.AsyncDatabaseAccess;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TaskScheduler {

    private static final int poolSize = 1;

    private static final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(poolSize);

    private AsyncDatabaseAccess asyncDBWriter;

    public TaskScheduler(AsyncDatabaseAccess asyncDBWriter) {
        this.asyncDBWriter = asyncDBWriter;
    }

    public void scheduleTasks() {
//        scheduler.scheduleWithFixedDelay(runUpdateLocation, 0, 3,
//                TimeUnit.SECONDS);
    }

    // TODO
    final Runnable runUpdateLocation = new Runnable() {
        public void run() {

        }
    };

}
