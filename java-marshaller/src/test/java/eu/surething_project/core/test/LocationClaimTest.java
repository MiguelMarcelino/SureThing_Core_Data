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
 *  Location Claim Test suite

    Testing Location Claim Creation, Serialization, Deserialization, Content.


 */
public class LocationClaimTest {


	long timeInMillis = System.currentTimeMillis(); // current time in milliseconds

	@Test
	public void testLocationClaim() {

		//	creation of location claim
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

		// serialize location claim
		byte [] locationClaimSerialized = locationClaim.toByteArray();

        //	deserialize
		LocationClaim locationClaimDeserialized = null;
		try{
			locationClaimDeserialized = LocationClaim.parseFrom(locationClaimSerialized);
		}catch(InvalidProtocolBufferException e){
			e.printStackTrace();
		}
		String claimId = locationClaimDeserialized.getClaimId();  // claimId
		String proverId = locationClaimDeserialized.getProverId(); // proverId

		// Location - oneof (LATLNG, POI, PROXIMITYTOPOI, OLC)
		Location location = locationClaimDeserialized.getLocation();
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
		Time time = locationClaimDeserialized.getTime();
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

		String evidenceType = locationClaimDeserialized.getEvidenceType(); // Evidence Type - WiFi

		// WiFi Networks Evidence details - Any - unpack evidence content
		Any evidence = locationClaimDeserialized.getEvidence();
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
			
		// Check content of the location claim
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
}