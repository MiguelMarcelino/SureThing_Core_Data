package eu.surething_project.core.test;

import eu.surething_project.core.*;
import eu.surething_project.core.wi_fi.*;
import com.google.protobuf.Timestamp;
import com.google.protobuf.Any;
import com.google.type.LatLng;
import com.google.protobuf.InvalidProtocolBufferException;
import static com.google.protobuf.util.Timestamps.compare;
import static com.google.protobuf.util.Timestamps.fromMillis;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.ArrayList;

/**
 *  Locaiton Endorsement Test 

    Testing Location Endorsement Creation, Serialization, Deserialization, Content.

 */
public class LocationEndorsementTest {


	long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
	
	@Test
	public void testLocationEndorsement() {
		
		//	create location endorsement
		LocationEndorsement locationEndorsement = LocationEndorsement.newBuilder()
						.setWitnessId("1")
						.setClaimId ("1")
						.setTime(Time.newBuilder()
										.setTimestamp(fromMillis(timeInMillis))
										.build())
						.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
						.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
										.setId("DEF")
										.addAps(WiFiNetworksEvidence.AP.newBuilder()
											.setSsid("ssid-B")
											.setRssi("-90")			
											.build())
										.build()))
						.build();


		// serialize location endorsement
		byte [] locationEndorsementSerialized = locationEndorsement.toByteArray();
        
        //	deserialize
        LocationEndorsement locationEndorsementDeserialized = null;
		try{
			locationEndorsementDeserialized = locationEndorsement.parseFrom(locationEndorsementSerialized);
		}catch(InvalidProtocolBufferException e){
			e.printStackTrace();
		}
		String witnessId = locationEndorsementDeserialized.getWitnessId();  // witenessId
		String claimId = locationEndorsementDeserialized.getClaimId(); // claimId

		// Time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
		Time time = locationEndorsementDeserialized.getTime();
		Time.TimeCase timeCase = time.getTimeCase();
		Timestamp timestamp = null;
		switch(timeCase){
			case TIMESTAMP:{
				timestamp = time.getTimestamp();	
			}break;
			case INTERVAL: time.getInterval();break;
			case RELATIVETOEPOCH: time.getRelativeToEpoch();break;
			case EMPTY: time.getEmpty(); break;  // check if TIME_NOT_SET will be enough instead of this case
			case TIME_NOT_SET: break;
		}

		String evidenceType = locationEndorsementDeserialized.getEvidenceType(); // Evidence Type - WiFi

		// WiFi Networks Evidence details - Any - unpack evidence content
		Any evidence = locationEndorsementDeserialized.getEvidence();
		WiFiNetworksEvidence wifiNetworksEvidence = null;
		try {
			wifiNetworksEvidence = evidence.unpack(WiFiNetworksEvidence.class);  
		}catch(InvalidProtocolBufferException e){
				e.printStackTrace();
		}

		List<WiFiNetworksEvidence.AP> aps = new ArrayList<>();
		String evidenceId = wifiNetworksEvidence.getId();  // evidenceId
		aps = wifiNetworksEvidence.getApsList();  // list of APs in WiFi evidence
		WiFiNetworksEvidence.AP ap = aps.get(0);  // First AP
		final Timestamp t = timestamp; // timestamp of location endorsement
			
		// Check content of the location endorsement
		Assertions.assertAll("Check Location Endorsement Content",
		()-> assertEquals(witnessId, "1"),
		()-> assertEquals(claimId, "1"),
		()-> assertEquals(compare(t, fromMillis(timeInMillis) ), 0 ),
		()-> assertEquals(evidenceType, "eu.surething_project.core.wi_fi.WiFiNetworksEvidence"),
		()-> assertEquals(evidenceId, "DEF"),
		()-> assertEquals(ap.getSsid(), "ssid-B"),
		()-> assertEquals(ap.getRssi(), "-90")
		
		);

	}
	
	
}