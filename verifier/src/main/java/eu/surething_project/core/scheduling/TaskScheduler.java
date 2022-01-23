package eu.surething_project.core.scheduling;

import eu.surething_project.core.database.AsyncDatabaseWriter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {

    private static final int poolSize = 1;

    private static final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(poolSize);

    private AsyncDatabaseWriter asyncDBWriter;

    public TaskScheduler(AsyncDatabaseWriter asyncDBWriter) {
        this.asyncDBWriter = asyncDBWriter;
    }

    public void scheduleTasks() {
        scheduler.scheduleWithFixedDelay(runScalingPolicy, 5, 5,
                TimeUnit.SECONDS);
    }

    final Runnable runScalingPolicy = new Runnable() {
        public void run() {
            asyncDBWriter.run();
        }
    };

}
