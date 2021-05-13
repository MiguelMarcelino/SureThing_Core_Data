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
 *  Location Certitifcate Test suite

	 Testing Location Certificate Creation, Serialization, Deserialization, Content.

 */
public class LocationCertificateTest {


	long timeInMillis = System.currentTimeMillis(); // current time in milliseconds

	@Test
	public void testLocationVerification() {
		
		//	create location certificate
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

		// serialize location certificate
		byte [] locationCertificateSerialized = locationVerification.toByteArray();

		//	deserialize
		LocationVerification locationCertificateDeserialized = null;
		try{
			locationCertificateDeserialized = locationVerification.parseFrom(locationCertificateSerialized);

		}catch(InvalidProtocolBufferException e){
			e.printStackTrace();
		}

		String verifierId = locationCertificateDeserialized.getVerifierId();  // witenessId
		String claimId = locationCertificateDeserialized.getClaimId(); // claimId
		List<String> endorsementIds  = new ArrayList();
		endorsementIds = locationCertificateDeserialized.getEndorsementIdsList(); // List of endorsements
		String endorsementId = endorsementIds.get(0);  // First endorsement

		// Time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
		Time time = locationCertificateDeserialized.getTime();
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
		
		String evidenceType = locationCertificateDeserialized.getEvidenceType(); // Evidence Type - WiFi

		// WiFi Networks Evidence details - Any - unpack evidence content
		Any evidence = locationCertificateDeserialized.getEvidence();
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
			
		// Check content of the locaiton certificate
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