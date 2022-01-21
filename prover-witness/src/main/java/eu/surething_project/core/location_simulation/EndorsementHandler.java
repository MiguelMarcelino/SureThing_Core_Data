package eu.surething_project.core.location_simulation;

import eu.surething_project.core.grpc.SignedLocationEndorsement;

import java.util.ArrayList;
import java.util.List;

public class EndorsementHandler {

    private List<SignedLocationEndorsement> endorsementList;

    public EndorsementHandler() {
        this.endorsementList = new ArrayList<>();
    }

    public void addLocationEndorsement(SignedLocationEndorsement locationEndorsement) {
        this.endorsementList.add(locationEndorsement);
    }

    public List<SignedLocationEndorsement> getEndorsementList() {
        return this.endorsementList;
    }

    public void cleanEndorsementList() {
        if(!this.endorsementList.isEmpty())
            this.endorsementList.clear();
    }
}
