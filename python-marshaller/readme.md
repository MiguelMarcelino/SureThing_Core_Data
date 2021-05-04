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
2- fix nested import in proto files

```
audit_pb_2.py

from . import location_proof_pb2 as location__proof__pb2
from . import ledger_pb2 as ledger__pb2


ledger_pb2.py

from . import location_proof_pb2 as location__proof__pb2

```

[How To Package Your Python Code](https://python-packaging.readthedocs.io/en/latest/).