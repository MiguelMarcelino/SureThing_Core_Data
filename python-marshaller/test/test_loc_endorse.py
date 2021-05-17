
'''

    Location Endorsement test suit


'''

import unittest
from surethingcore.loc_endorse_pb2 import LocationEndorsement
from surethingcore.wi_fi_pb2 import WiFiNetworksEvidence
from google.protobuf.any_pb2 import Any
from google.protobuf.timestamp_pb2 import Timestamp
import datetime

class TestLocationEndorsement(unittest.TestCase):

    def test_location_endorsement(self):
        # test creation of a location endorsement

        #### create an endorsement
        endorsement = LocationEndorsement()

        # witnessId
        endorsement.witnessId = "1"

        # claimId
        endorsement.claimId = "1"

        # time - oneof
        now = datetime.datetime.now()
        endorsement.time.timestamp.FromDatetime(now)

        # evidence type
        endorsement.evidenceType = "eu.surething_project.core.wi_fi.WiFiNetworksEvidence"

        # evidence - Any
        wifi_evidence = WiFiNetworksEvidence()
        wifi_evidence.id = "DEF"
        ap = wifi_evidence.aps.add()
        ap.ssid = "ssid-B"
        ap.rssi = "-90"

        evidence_any = Any()
        evidence_any.Pack(wifi_evidence)  # pack Any

        endorsement.evidence.CopyFrom(evidence_any)

        #### serialize the endorsement
        endorsement_serialized = endorsement.SerializeToString()

        #### deserialize the endorsement
        endorsement_deserialized = LocationEndorsement()
        endorsement_deserialized.ParseFromString(endorsement_serialized)

        evidence = Any()
        evidence.CopyFrom(endorsement_deserialized.evidence)
        wifi_evidence = WiFiNetworksEvidence()
        evidence.Unpack(wifi_evidence)  # unpack Any
        ap = wifi_evidence.aps[0]

        timestamp = Timestamp()
        timestamp.FromDatetime(now)
        seconds = timestamp.seconds
        nanos = timestamp.nanos

        #### assert all fields
        assert (endorsement_deserialized.witnessId,
                endorsement_deserialized.claimId,
                endorsement_deserialized.time.timestamp.seconds,
                endorsement_deserialized.time.timestamp.nanos,
                endorsement_deserialized.evidenceType,
                wifi_evidence.id,
                ap.ssid,
                ap.rssi
                ) == ("1", "1",
                      seconds,
                      nanos,
                      "eu.surething_project.core.wi_fi.WiFiNetworksEvidence",
                      "DEF", "ssid-B", "-90"
                      )

if __name__ == '__main__':
    unittest.main()
