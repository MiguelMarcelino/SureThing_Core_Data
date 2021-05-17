
'''

    Location Claim test suit


'''

import unittest
from surethingcore.loc_claim_pb2 import LocationClaim
from surethingcore.wi_fi_pb2 import WiFiNetworksEvidence
from google.protobuf.any_pb2 import Any
from google.protobuf.timestamp_pb2 import Timestamp
import datetime


class TestLocationClaim(unittest.TestCase):

    def test_location_claim(self):
        # test creation of a location claim

        #### create a claim
        claim = LocationClaim()

        # claimId
        claim.claimId = "1"

        # proverId
        claim.proverId = "1"

        # location - oneof
        claim.location.latLng.latitude =  56.3
        claim.location.latLng.longitude = 56.3

        #time - oneof
        now = datetime.datetime.now()
        claim.time.timestamp.FromDatetime(now)

        # evidence type
        claim.evidenceType ="eu.surething_project.core.wi_fi.WiFiNetworksEvidence"

        #evidence - Any
        wifi_evidence = WiFiNetworksEvidence()
        wifi_evidence.id = "ABC"
        ap = wifi_evidence.aps.add()
        ap.ssid = "ssid-A"
        ap.rssi = "-89"

        evidence_any = Any()
        evidence_any.Pack(wifi_evidence)   # pack Any

        claim.evidence.CopyFrom(evidence_any)

        #### serialize the claim
        claim_serialized = claim.SerializeToString()

        #### deserialize the claim
        claim_deserialized = LocationClaim()
        claim_deserialized.ParseFromString(claim_serialized)


        evidence = Any()
        evidence.CopyFrom(claim_deserialized.evidence)
        wifi_evidence = WiFiNetworksEvidence()
        evidence.Unpack(wifi_evidence) # unpack Any
        ap = wifi_evidence.aps[0]

        timestamp = Timestamp()
        timestamp.FromDatetime(now)
        seconds = timestamp.seconds
        nanos = timestamp.nanos

        #### assert all fields
        assert(claim_deserialized.claimId,
               claim_deserialized.proverId,
               claim_deserialized.location.latLng.latitude,
               claim_deserialized.location.latLng.longitude,
               claim_deserialized.time.timestamp.seconds,
               claim_deserialized.time.timestamp.nanos,
               claim_deserialized.evidenceType,
               wifi_evidence.id,
               ap.ssid,
               ap.rssi
               ) == ("1", "1",
                     56.3, 56.3,
                     seconds,
                     nanos,
                     "eu.surething_project.core.wi_fi.WiFiNetworksEvidence",
                     "ABC", "ssid-A", "-89"
                     )

if __name__ == '__main__':
    unittest.main()

