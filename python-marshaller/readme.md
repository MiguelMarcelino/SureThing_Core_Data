# python-marshaller


## Surething Core Marshallers

package surethingcorepython contains the generated modules (marshallers) from .proto files in ../data-types/proto/*.proto


## install

```
-> pip install surethingcorepython
```

## Example : KIOSK

```
import surethingcorepython

```

## protoc
### To generate fresh marshallers from .proto files:


1- generate files
```
-> protoc --proto_path=../data-types/proto --python_out=./surethingcorepython ../data-types/proto/*.proto

```
2- fix nested imports in proto files

```
audit_pb_2.py

from . import location_proof_pb2 as location__proof__pb2
from . import ledger_pb2 as ledger__pb2


ledger_pb2.py

from . import location_proof_pb2 as location__proof__pb2


locationclaim_pb2.py

from . import latlng_pb2 as latlng__pb2
from . import localized_text_pb2 as localized__text__pb2
from . import signature_pb2 as signature__pb2


locationendorsement_pb2.py

from . import locationclaim_pb2 as locationclaim__pb2
from . import signature_pb2 as signature__pb2


locationcertificate_pb2.py

from . import locationclaim_pb2 as locationclaim__pb2
from . import signature_pb2 as signature__pb2


```


[How To Package Your Python Code](https://python-packaging.readthedocs.io/en/latest/).