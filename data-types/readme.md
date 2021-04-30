# SureThing data types

The data structures and its fields are described using comments in the `.proto` files.  
The types are designed to be extended.  
The **protoc** tool (Protocol Buffers compiler) can be used to generate source code in multiple programming languages.

Whenever possible, existing types are used, including the *Well-Known Types* (defined in the package `google.protobuf` -- directly supported by compiler and runtime) and also common types (defined in the package `google.type` -- need to be imported and are defined in [public repositories](https://github.com/googleapis/googleapis/tree/master/google/type)).

The `.proto` files follows the [Protobuf style guide](https://developers.google.com/protocol-buffers/docs/style).
