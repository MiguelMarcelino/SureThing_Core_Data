package eu.surething_project.core.scheduling;

import eu.surething_project.core.location_simulation.EntityManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {

    private static final int poolSize = 1;

    private static final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(poolSize);

    private EntityManager entityManager;

    public TaskScheduler(EntityManager manager) {
        this.entityManager = manager;
    }

    public void scheduleTasks() {
        scheduler.scheduleWithFixedDelay(runScalingPolicy, 5, 5,
                TimeUnit.SECONDS);
    }

    final Runnable runScalingPolicy = new Runnable() {
        public void run() {
            entityManager.updateEntityLocation();
        }
    };

}
