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
 *  Test suite of SureThing Core data-types
 */
public class CoreDataTest {


	long timeInMillis = System.currentTimeMillis(); // current time in milliseconds

	/** 
			test Location Claim creation, serialization , deserilization , and check content
	 */
	@Test
	public void testLocationClaim() {

		//	create and serialize
		LocationClaim locationClaim = LocationClaim.newBuilder()
						.setClaimId("1")
						.setProverId("1")
						.setLocation(Location.newBuilder()
										.setLatLng(LatLng.newBuilder()
											.setLatitude(53.3)
											.setLongitude(85.3)
											.build())
										.build())
						.setTime(Time.newBuilder()
										.setTimestamp(fromMillis(timeInMillis))
										.build())
						.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
						.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
										.setId("ABC")
										.addAps(WiFiNetworksEvidence.AP.newBuilder()
											.setSsid("ssid-A")
											.setRssi("-89")			
											.build())
										.build()))
						.build();

		
		//	deserialize
		
		String claimId = locationClaim.getClaimId();  // claimId
		String proverId = locationClaim.getProverId(); // proverId

		// Location - oneof (LATLNG, POI, PROXIMITYTOPOI, OLC)
		Location location = locationClaim.getLocation();
		Location.LocationCase locationCase = location.getLocationCase();
		LatLng latLng= null;
		switch(locationCase){
			case LATLNG:{
				latLng = location.getLatLng();
			}
			break;
			case POI:location.getPoi(); break;
			case PROXIMITYTOPOI: location.getProximityToPoI(); break;
			case OLC:location.getOlc(); break;
			case LOCATION_NOT_SET:break;
		}

		// Time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
		Time time = locationClaim.getTime();
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

		String evidenceType = locationClaim.getEvidenceType(); // Evidence Type - WiFi

		// WiFi Networks Evidence details - Any - unpack evidence content
		Any evidence = locationClaim.getEvidence();
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
		final double lat = latLng.getLatitude();  // Location latLng - Latitude
		final double lng = latLng.getLongitude(); // Location latLng - Longitude
		final Timestamp t = timestamp; // timestamp of location claim
			
		// Check content
		Assertions.assertAll("Check Location Claim Content",
		()-> assertEquals(claimId, "1"),
		()-> assertEquals(proverId, "1"),
		()-> assertEquals(lat, 53.3),
		()-> assertEquals(lng, 85.3),
		()-> assertEquals(compare(t, fromMillis(timeInMillis) ), 0 ),
		()-> assertEquals(evidenceType, "eu.surething_project.core.wi_fi.WiFiNetworksEvidence"),
		()-> assertEquals(evidenceId, "ABC"),
		()-> assertEquals(ap.getSsid(), "ssid-A"),
		()-> assertEquals(ap.getRssi(), "-89")
		
		);

	}

	/** 
			test Location endorsement creation, serialization , deserilization , and check content
	 */
	
	@Test
	public void testLocationEndorsement() {
		
		//	create and serialize
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

		//	deserialize
		
		String witnessId = locationEndorsement.getWitnessId();  // witenessId
		String claimId = locationEndorsement.getClaimId(); // claimId

		// Time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
		Time time = locationEndorsement.getTime();
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

		String evidenceType = locationEndorsement.getEvidenceType(); // Evidence Type - WiFi

		// WiFi Networks Evidence details - Any - unpack evidence content
		Any evidence = locationEndorsement.getEvidence();
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
			
		// Check content
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
	

	/** 
			test Location verification (certificate) creation, serialization , deserilization , and check content
	 */
	@Test
	public void testLocationVerification() {
		
		//	create and serialize
		LocationVerification  locationVerification  = LocationVerification .newBuilder()
						.setVerifierId ("1")
						.setClaimId ("1")
						.addEndorsementIds("1")
						.setTime(Time.newBuilder()
										.setTimestamp(fromMillis(timeInMillis))
										.build())
						.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
						.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
										.setId("GHI")
										.addAps(WiFiNetworksEvidence.AP.newBuilder()
											.setSsid("ssid-C")
											.setRssi("-70")			
											.build())
										.build()))
						.build();

		//	deserialize
		
		String verifierId = locationVerification.getVerifierId();  // witenessId
		String claimId = locationVerification.getClaimId(); // claimId


		List<String> endorsementIds  = new ArrayList();
		endorsementIds = locationVerification.getEndorsementIdsList(); // List of endorsements
		String endorsementId = endorsementIds.get(0);  // First endorsement


		// Time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
		Time time = locationVerification.getTime();
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

		String evidenceType = locationVerification.getEvidenceType(); // Evidence Type - WiFi

		// WiFi Networks Evidence details - Any - unpack evidence content
		Any evidence = locationVerification.getEvidence();
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
		final Timestamp t = timestamp; // timestamp of certificate
			
		// Check content
		Assertions.assertAll("Check Location Verification Content",
		()-> assertEquals(verifierId, "1"),
		()-> assertEquals(claimId, "1"),
		()-> assertEquals(endorsementId, "1"),
		()-> assertEquals(compare(t, fromMillis(timeInMillis) ), 0 ),
		()-> assertEquals(evidenceType, "eu.surething_project.core.wi_fi.WiFiNetworksEvidence"),
		()-> assertEquals(evidenceId, "GHI"),
		()-> assertEquals(ap.getSsid(), "ssid-C"),
		()-> assertEquals(ap.getRssi(), "-70")
		
		);

	}
	
}