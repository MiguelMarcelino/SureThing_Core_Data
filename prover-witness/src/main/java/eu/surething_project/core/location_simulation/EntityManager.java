package eu.surething_project.core.location_simulation;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntityManager {

    private List<Entity> entities;

    public EntityManager() {}

    /**
     * Gets entity data from a file
     */
    public void readEntityFile(String filePath) {

    }

    public List<Entity> getEntities() {
        return entities;
    }


    /**
     * Gets all entities in range of a given entity
     * @param latLon
     * @return
     */
    public List<Entity> getEntitiesInRange(LatLongPair latLon) {
        // TODO: Simulate entity Location
        List<Entity> entitiesInRange = new ArrayList<>();
        return  entitiesInRange;
    }
}
