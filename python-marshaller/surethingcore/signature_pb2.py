# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: signature.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='signature.proto',
  package='eu.surething_project.core',
  syntax='proto3',
  serialized_options=b'\n\031eu.surething_project.coreB\021SignatureEntitiesP\001',
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n\x0fsignature.proto\x12\x19\x65u.surething_project.core\"=\n\tSignature\x12\r\n\x05value\x18\x01 \x01(\x0c\x12\x12\n\ncryptoAlgo\x18\x02 \x01(\t\x12\r\n\x05nonce\x18\x03 \x01(\x03\x42\x30\n\x19\x65u.surething_project.coreB\x11SignatureEntitiesP\x01\x62\x06proto3'
)




_SIGNATURE = _descriptor.Descriptor(
  name='Signature',
  full_name='eu.surething_project.core.Signature',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='value', full_name='eu.surething_project.core.Signature.value', index=0,
      number=1, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=b"",
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='cryptoAlgo', full_name='eu.surething_project.core.Signature.cryptoAlgo', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='nonce', full_name='eu.surething_project.core.Signature.nonce', index=2,
      number=3, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=46,
  serialized_end=107,
)

DESCRIPTOR.message_types_by_name['Signature'] = _SIGNATURE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

Signature = _reflection.GeneratedProtocolMessageType('Signature', (_message.Message,), {
  'DESCRIPTOR' : _SIGNATURE,
  '__module__' : 'signature_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.Signature)
  })
_sym_db.RegisterMessage(Signature)


DESCRIPTOR._options = None
# @@protoc_insertion_point(module_scope)