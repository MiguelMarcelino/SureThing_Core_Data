# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: locationendorsement.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import timestamp_pb2 as google_dot_protobuf_dot_timestamp__pb2
from google.protobuf import any_pb2 as google_dot_protobuf_dot_any__pb2
from . import locationclaim_pb2 as locationclaim__pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='locationendorsement.proto',
  package='pt.ulisboa.tecnico.surethingcore',
  syntax='proto3',
  serialized_options=b'\n4pt.ulisboa.tecnico.surethingcore.locationendorsementB$SureThingLocationEndorsementEntitiesP\001',
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n\x19locationendorsement.proto\x12 pt.ulisboa.tecnico.surethingcore\x1a\x1fgoogle/protobuf/timestamp.proto\x1a\x19google/protobuf/any.proto\x1a\x13locationclaim.proto\"\xb9\x02\n\x13LocationEndorsement\x12\x11\n\twitnessId\x18\x01 \x01(\x03\x12?\n\x06\x63laims\x18\x02 \x03(\x0b\x32/.pt.ulisboa.tecnico.surethingcore.LocationClaim\x12\x13\n\x0b\x65ndorsement\x18\x03 \x01(\t\x12\x44\n\x0c\x65videnceType\x18\x04 \x01(\x0e\x32..pt.ulisboa.tecnico.surethingcore.EvidenceType\x12\x31\n\x13\x65ndorsementEvidence\x18\x05 \x03(\x0b\x32\x14.google.protobuf.Any\x12-\n\ttimestamp\x18\x06 \x01(\x0b\x32\x1a.google.protobuf.Timestamp\x12\x11\n\tsignature\x18\x07 \x01(\x0c\x42^\n4pt.ulisboa.tecnico.surethingcore.locationendorsementB$SureThingLocationEndorsementEntitiesP\x01\x62\x06proto3'
  ,
  dependencies=[google_dot_protobuf_dot_timestamp__pb2.DESCRIPTOR,google_dot_protobuf_dot_any__pb2.DESCRIPTOR,locationclaim__pb2.DESCRIPTOR,])




_LOCATIONENDORSEMENT = _descriptor.Descriptor(
  name='LocationEndorsement',
  full_name='pt.ulisboa.tecnico.surethingcore.LocationEndorsement',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='witnessId', full_name='pt.ulisboa.tecnico.surethingcore.LocationEndorsement.witnessId', index=0,
      number=1, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='claims', full_name='pt.ulisboa.tecnico.surethingcore.LocationEndorsement.claims', index=1,
      number=2, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='endorsement', full_name='pt.ulisboa.tecnico.surethingcore.LocationEndorsement.endorsement', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='evidenceType', full_name='pt.ulisboa.tecnico.surethingcore.LocationEndorsement.evidenceType', index=3,
      number=4, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='endorsementEvidence', full_name='pt.ulisboa.tecnico.surethingcore.LocationEndorsement.endorsementEvidence', index=4,
      number=5, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='pt.ulisboa.tecnico.surethingcore.LocationEndorsement.timestamp', index=5,
      number=6, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='signature', full_name='pt.ulisboa.tecnico.surethingcore.LocationEndorsement.signature', index=6,
      number=7, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=b"",
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
  serialized_start=145,
  serialized_end=458,
)

_LOCATIONENDORSEMENT.fields_by_name['claims'].message_type = locationclaim__pb2._LOCATIONCLAIM
_LOCATIONENDORSEMENT.fields_by_name['evidenceType'].enum_type = locationclaim__pb2._EVIDENCETYPE
_LOCATIONENDORSEMENT.fields_by_name['endorsementEvidence'].message_type = google_dot_protobuf_dot_any__pb2._ANY
_LOCATIONENDORSEMENT.fields_by_name['timestamp'].message_type = google_dot_protobuf_dot_timestamp__pb2._TIMESTAMP
DESCRIPTOR.message_types_by_name['LocationEndorsement'] = _LOCATIONENDORSEMENT
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

LocationEndorsement = _reflection.GeneratedProtocolMessageType('LocationEndorsement', (_message.Message,), {
  'DESCRIPTOR' : _LOCATIONENDORSEMENT,
  '__module__' : 'locationendorsement_pb2'
  # @@protoc_insertion_point(class_scope:pt.ulisboa.tecnico.surethingcore.LocationEndorsement)
  })
_sym_db.RegisterMessage(LocationEndorsement)


DESCRIPTOR._options = None
# @@protoc_insertion_point(module_scope)