package eu.surething_project.core.location_simulation;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import eu.surething_project.core.config.AddressValidator;
import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.LocationEndorsement;
import eu.surething_project.core.grpc.google.type.LatLng;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EntityManager {

    private static final Logger logger = Logger.getLogger(EntityManager.class.getName());

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


            logger.info("Successfully finished reading entity data.");
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

    /**
     * Creates an entity from its string representation
     * @param entityStr - String representation of entity
     * @return
     */
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

            // DEBUG
            if(PropertiesReader.getDebugProperty()) {
                System.out.println("Distance to witness: " + distance);
            }

            if (distance < 0.2) {
                entitiesInRange.add(entity);
            }
        }
        return entitiesInRange;
    }

    /**
     * Simulates entity location updates
     */
    public void updateEntityLocation(LocationEndorsement endorsement) {
        String receivedId = endorsement.getWitnessId();
        LatLng latLng = getLatLngFromEvidence(endorsement.getEvidenceType(), endorsement.getEvidence());
        for (Entity entity : entities) {
            if(entity.getId().equals(receivedId)) {
                LatLngPair latLngPair = new LatLngPair(latLng.getLatitude(), latLng.getLongitude());
                entity.setLatLongPair(latLngPair);
                break;
            }
        }
    }

    /**
     * Simulates updating current entity Location
     */
    public void simulateCurrentEntityLocation() {
        LatLngPair latLngPair = currentEntity.getLatLngPair();
        LatLngPair pair = LocationSimulator.genLatLngCoordinates(latLngPair.getLatitude(),
                latLngPair.getLongitude(), 0.000005);
        currentEntity.setLatLongPair(pair);
    }

    /**
     * Gets LatLng from evidence
     *
     * @param evidenceType
     * @param evidence
     * @return
     */
    private LatLng getLatLngFromEvidence(String evidenceType, Any evidence) {
        LatLng latLng = null;
        String expectedType = "eu.surething_project.core.grpc.google.type.LatLng";
        try {
            if (evidenceType == expectedType) {
                System.err.println("Expected evidence of type LatLng, " +
                        "but provided was: " + evidenceType);
            }
            latLng = evidence.unpack(LatLng.class);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}
