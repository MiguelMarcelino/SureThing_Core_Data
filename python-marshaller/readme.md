# python-marshaller


## Surething Core Marshallers

package surethingcorepython contains the generated modules (marshallers) from .proto files in ../data-types/proto/*.proto


## install

```
-> pip install surethingcore
```

## Example : KIOSK

```
import surethingcore

```

## protoc
### To generate fresh marshallers from .proto files:


1- generate files
```
-> protoc --proto_path=../data-types/proto --python_out=./surethingcore ../data-types/proto/*.proto

```
2- fix nested imports in proto files

```

loc_claim_pb2.py

from . import loc_time_pb2 as loc__time__pb2
from . import signature_pb2 as signature__pb2


loc_endorse_pb2.py 

from . import loc_time_pb2 as loc__time__pb2
from . import signature_pb2 as signature__pb2


loc_cert_pb2.py

from . import loc_time_pb2 as loc__time__pb2
from . import signature_pb2 as signature__pb2

loc_time_pb2.py

from . import latlng_pb2 as latlng__pb2
from . import localized_text_pb2 as localized__text__pb2

```


[How To Package Your Python Code](https://python-packaging.readthedocs.io/en/latest/).