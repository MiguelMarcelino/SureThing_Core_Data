package eu.surething_project.core.location_simulation;

import eu.surething_project.core.config.AddressValidator;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    private List<Entity> entities;

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
            if(!file.exists() || !file.isFile()) {
                file.createNewFile();
                System.out.println("No entity File found. Continuing setup.");
                return;
            }
            fis = new FileInputStream(file);
            buf = new BufferedReader(new InputStreamReader(fis));

            // read file contents
            String line = null;
            while ((line = buf.readLine()) != null) {
                String[] splitInfo = line.split(" ");
                String entityId = splitInfo[0];
                String address = splitInfo[1];
                AddressValidator.validateAddress(address);
                String[] splitAddr = address.split(":");
                double latitude = Double.parseDouble(splitInfo[2]);
                double longitude = Double.parseDouble(splitInfo[3]);
                Entity entity = new Entity(entityId, splitAddr[0], Integer.parseInt(splitAddr[1]),
                        new LatLongPair(latitude, longitude));
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
                if(fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                throw new EntityException(ErrorMessage.ERROR_CLOSING_ENTITIES_FILE, e);
            }
        }
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

    public void updateEntityLocation() {
        // TODO: Randomly update the location of entities
    }
}
