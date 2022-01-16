package eu.surething_project.core.location_simulation;

import java.util.HashMap;

public class EntityHandler {

    public HashMap<Integer, Entity> entityMap;

    public EntityHandler() {
        this.entityMap = new HashMap<>();
    }

    /**
     * Adds entity to HashMap
     * @param id
     * @param entity
     */
    public void addEntity(int id, Entity entity) {
        this.entityMap.put(id, entity);
    }

    /**
     * Calculates the distance between two entities
     * @param e1
     * @param e2
     */
    public void calculateDistance(Entity e1, Entity e2) {

    }
}
