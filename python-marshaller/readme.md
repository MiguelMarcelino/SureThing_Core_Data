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
### to generate fresh marshallers from .proto files

```
-> protoc --proto_path=./data-types/proto --python_out=./marshallers ./data-types/proto/*.proto

```
