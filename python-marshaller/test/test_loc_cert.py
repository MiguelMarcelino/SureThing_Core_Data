
'''

    Location Certificate test suit


'''

import unittest
from surethingcore.loc_cert_pb2 import LocationVerification
from surethingcore.wi_fi_pb2 import WiFiNetworksEvidence
from google.protobuf.any_pb2 import Any
from google.protobuf.timestamp_pb2 import Timestamp
import datetime

class TestLocationCertificate(unittest.TestCase):

    def test_location_certificate(self):
        # test the creation of a location certificate


        #### create a verification
        verification = LocationVerification()

        # verifierId
        verification.verifierId = "1"

        # proverId
        verification.claimId  = "1"

        # endorsementIds
        verification.endorsementIds.append("1")

        # time - oneof
        now = datetime.datetime.now()
        verification.time.timestamp.FromDatetime(now)

        # evidence type
        verification.evidenceType = "eu.surething_project.core.wi_fi.WiFiNetworksEvidence"

        # evidence - Any
        wifi_evidence = WiFiNetworksEvidence()
        wifi_evidence.id = "GHI"
        ap = wifi_evidence.aps.add()
        ap.ssid = "ssid-C"
        ap.rssi = "-91"

        evidence_any = Any()
        evidence_any.Pack(wifi_evidence)  # pack Any

        verification.evidence.CopyFrom(evidence_any)

        #### serialize the certificate
        verification_serialized = verification.SerializeToString()

        # deserialize the certificate
        verification_deserialized = LocationVerification()
        verification_deserialized.ParseFromString(verification_serialized)

        evidence = Any()
        evidence.CopyFrom(verification_deserialized.evidence)
        wifi_evidence = WiFiNetworksEvidence()
        evidence.Unpack(wifi_evidence)  # unpack Any
        ap = wifi_evidence.aps[0]

        timestamp = Timestamp()
        timestamp.FromDatetime(now)
        seconds = timestamp.seconds
        nanos = timestamp.nanos

        #### assert all fields
        assert (verification_deserialized.verifierId,
                verification_deserialized.claimId,
                verification_deserialized.endorsementIds[0],
                verification_deserialized.time.timestamp.seconds,
                verification_deserialized.time.timestamp.nanos,
                verification_deserialized.evidenceType,
                wifi_evidence.id,
                ap.ssid,
                ap.rssi
                ) == ("1", "1",
                      "1",
                      seconds,
                      nanos,
                      "eu.surething_project.core.wi_fi.WiFiNetworksEvidence",
                      "GHI", "ssid-C", "-91"
                      )

        pass

if __name__ == '__main__':
    unittest.main()
