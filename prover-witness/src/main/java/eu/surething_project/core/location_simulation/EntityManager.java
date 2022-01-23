package eu.surething_project.core.location_simulation;

import eu.surething_project.core.config.AddressValidator;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    private List<Entity> entities;
    private Entity currentEntity;

    public EntityManager() {
        this.entities = new ArrayList<>();
    }

    /**
     * Gets entity data from a file
     */
    public void readEntityFile(String filePath, String fileName) {
        FileInputStream fis = null;
        BufferedReader buf = null;
        try {
            File file = new File(filePath, fileName);
            if (!file.exists() || !file.isFile()) {
                file.createNewFile();
                System.out.println("No entity File found. Continuing setup.");
                return;
            }
            fis = new FileInputStream(file);
            buf = new BufferedReader(new InputStreamReader(fis));

            // read file contents
            String line = buf.readLine();
            if (line != null) {
                // Get current entity
                currentEntity = createEntityFromStr(line);
            }
            while ((line = buf.readLine()) != null) {
                Entity entity = createEntityFromStr(line);
                entities.add(entity);
            }

            System.out.println("Successfully finished reading entity data.");
        } catch (FileNotFoundException e) {
            throw new EntityException(ErrorMessage.ERROR_READING_ENTITIES_FILE, e);
        } catch (IOException e) {
            throw new EntityException(ErrorMessage.ERROR_READING_ENTITIES_FILE, e);
        } finally {
            try {
                if (buf != null) {
                    buf.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                throw new EntityException(ErrorMessage.ERROR_CLOSING_ENTITIES_FILE, e);
            }
        }
    }

    private Entity createEntityFromStr(String entityStr) {
        String[] splitInfo = entityStr.split(" ");
        String entityId = splitInfo[0];
        String address = splitInfo[1];
        AddressValidator.validateAddress(address);
        String[] splitAddr = address.split(":");
        double latitude = Double.parseDouble(splitInfo[2]);
        double longitude = Double.parseDouble(splitInfo[3]);
        Entity entity = new Entity(entityId, splitAddr[0], Integer.parseInt(splitAddr[1]),
                new LatLngPair(latitude, longitude));
        return entity;
    }

    /**
     * Gets all entities
     * @return
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Gets current entity. It is always the first in the list
     *
     * @return
     */
    public Entity getCurrentEntity() {
        return currentEntity;
    }


    /**
     * Gets all entities in range of a given entity
     *
     * @param latLngCurr - Latitude and Longitude of a given entity
     * @return
     */
    public List<Entity> getEntitiesInRange(LatLngPair latLngCurr) {
        List<Entity> entitiesInRange = new ArrayList<>();
        for (Entity entity : entities) {
            LatLngPair latLngEntity = entity.getLatLngPair();
            double distance = DistanceCalculator.haversineFormula(latLngCurr.getLatitude(),
                    latLngCurr.getLongitude(), latLngEntity.getLatitude(),
                    latLngEntity.getLongitude());
            if (distance < 5) { // 5km is too much (decrease later)
                entitiesInRange.add(entity);
            }
        }
        return entitiesInRange;
    }

    /**
     * Simulates entity location updates
     */
    public void updateEntityLocation() {
        for (Entity entity : entities) {
            LatLngPair latLngPair = entity.getLatLngPair();
            LocationSimulator.genLatLngCoordinates(latLngPair.getLatitude(),
                    latLngPair.getLongitude(), 0.00005);
            entity.setLatLongPair(latLngPair);
        }
    }
}
