# python-marshaller


## Marshallers

package marshallers contains the generated modules from the files in ../data-types/proto/*.proto


## Python protobuf

```
-> pip install protobuf
```

## Example : LedgerApp

```
import marshallers

```

## protoc
### To generate fresh marshallers from .proto files:


1- generate files
```
-> protoc --proto_path=../data-types/proto --python_out=./marshallers ../data-types/proto/*.proto

```
2- fix nested import in proto files

```
audit_pb_2.py

from . import location_proof_pb2 as location__proof__pb2
from . import ledger_pb2 as ledger__pb2


ledger_pb2.py

from . import location_proof_pb2 as location__proof__pb2

```
