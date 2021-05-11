# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: locationclaim.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import timestamp_pb2 as google_dot_protobuf_dot_timestamp__pb2
from google.protobuf import any_pb2 as google_dot_protobuf_dot_any__pb2
from . import latlng_pb2 as latlng__pb2
from . import localized_text_pb2 as localized__text__pb2
from . import signature_pb2 as signature__pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='locationclaim.proto',
  package='eu.surething_project.core',
  syntax='proto3',
  serialized_options=b'\n\031eu.surething_project.coreB\021SignatureEntitiesP\001',
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n\x13locationclaim.proto\x12\x19\x65u.surething_project.core\x1a\x1fgoogle/protobuf/timestamp.proto\x1a\x19google/protobuf/any.proto\x1a\x0clatlng.proto\x1a\x14localized_text.proto\x1a\x0fsignature.proto\"\xde\x01\n\x08Location\x12%\n\x06latLng\x18\x01 \x01(\x0b\x32\x13.google.type.LatLngH\x00\x12-\n\x03poi\x18\x02 \x01(\x0b\x32\x1e.eu.surething_project.core.PoIH\x00\x12\x41\n\x0eproximityToPoI\x18\x03 \x01(\x0b\x32\'.eu.surething_project.core.PoIProximityH\x00\x12-\n\x03olc\x18\x04 \x01(\x0b\x32\x1e.eu.surething_project.core.OLCH\x00\x42\n\n\x08location\"R\n\x03PoI\x12\n\n\x02id\x18\x01 \x01(\t\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\x31\n\rlocalizedName\x18\x03 \x03(\x0b\x32\x1a.google.type.LocalizedText\"J\n\x0cPoIProximity\x12\r\n\x05poiId\x18\x01 \x01(\t\x12\x15\n\rdistanceValue\x18\x02 \x01(\x04\x12\x14\n\x0c\x64istanceUnit\x18\x03 \x01(\t\"q\n\x03OLC\x12\x15\n\rsouthLatitude\x18\x01 \x01(\x01\x12\x15\n\rwestLongitude\x18\x02 \x01(\x01\x12\x15\n\rnorthLatitude\x18\x03 \x01(\x01\x12\x15\n\reastLongitude\x18\x04 \x01(\x01\x12\x0e\n\x06length\x18\x05 \x01(\x05\"\xbd\x01\n\x04Time\x12/\n\ttimestamp\x18\x01 \x01(\x0b\x32\x1a.google.protobuf.TimestampH\x00\x12;\n\x08interval\x18\x02 \x01(\x0b\x32\'.eu.surething_project.core.TimeIntervalH\x00\x12?\n\x0frelativeToEpoch\x18\x03 \x01(\x0b\x32$.eu.surething_project.core.EpochTimeH\x00\x42\x06\n\x04time\"b\n\x0cTimeInterval\x12)\n\x05\x62\x65gin\x18\x01 \x01(\x0b\x32\x1a.google.protobuf.Timestamp\x12\'\n\x03\x65nd\x18\x02 \x01(\x0b\x32\x1a.google.protobuf.Timestamp\"A\n\tEpochTime\x12\x0f\n\x07\x65pochId\x18\x01 \x01(\t\x12\x11\n\ttimeValue\x18\x02 \x01(\x03\x12\x10\n\x08timeUnit\x18\x03 \x01(\t\"\xd6\x01\n\rLocationClaim\x12\x0f\n\x07\x63laimId\x18\x01 \x01(\t\x12\x10\n\x08proverId\x18\x02 \x01(\t\x12\x35\n\x08location\x18\x03 \x01(\x0b\x32#.eu.surething_project.core.Location\x12-\n\x04time\x18\x04 \x01(\x0b\x32\x1f.eu.surething_project.core.Time\x12\x14\n\x0c\x65videnceType\x18\x05 \x01(\t\x12&\n\x08\x65vidence\x18\x06 \x01(\x0b\x32\x14.google.protobuf.Any\"\x8d\x01\n\x13SignedLocationClaim\x12\x37\n\x05\x63laim\x18\x01 \x01(\x0b\x32(.eu.surething_project.core.LocationClaim\x12=\n\x0fproverSignature\x18\x02 \x01(\x0b\x32$.eu.surething_project.core.SignatureB0\n\x19\x65u.surething_project.coreB\x11SignatureEntitiesP\x01\x62\x06proto3'
  ,
  dependencies=[google_dot_protobuf_dot_timestamp__pb2.DESCRIPTOR,google_dot_protobuf_dot_any__pb2.DESCRIPTOR,latlng__pb2.DESCRIPTOR,localized__text__pb2.DESCRIPTOR,signature__pb2.DESCRIPTOR,])




_LOCATION = _descriptor.Descriptor(
  name='Location',
  full_name='eu.surething_project.core.Location',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='latLng', full_name='eu.surething_project.core.Location.latLng', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='poi', full_name='eu.surething_project.core.Location.poi', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='proximityToPoI', full_name='eu.surething_project.core.Location.proximityToPoI', index=2,
      number=3, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='olc', full_name='eu.surething_project.core.Location.olc', index=3,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
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
    _descriptor.OneofDescriptor(
      name='location', full_name='eu.surething_project.core.Location.location',
      index=0, containing_type=None,
      create_key=_descriptor._internal_create_key,
    fields=[]),
  ],
  serialized_start=164,
  serialized_end=386,
)


_POI = _descriptor.Descriptor(
  name='PoI',
  full_name='eu.surething_project.core.PoI',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='eu.surething_project.core.PoI.id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='name', full_name='eu.surething_project.core.PoI.name', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='localizedName', full_name='eu.surething_project.core.PoI.localizedName', index=2,
      number=3, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
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
  serialized_start=388,
  serialized_end=470,
)


_POIPROXIMITY = _descriptor.Descriptor(
  name='PoIProximity',
  full_name='eu.surething_project.core.PoIProximity',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='poiId', full_name='eu.surething_project.core.PoIProximity.poiId', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='distanceValue', full_name='eu.surething_project.core.PoIProximity.distanceValue', index=1,
      number=2, type=4, cpp_type=4, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='distanceUnit', full_name='eu.surething_project.core.PoIProximity.distanceUnit', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
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
  serialized_start=472,
  serialized_end=546,
)


_OLC = _descriptor.Descriptor(
  name='OLC',
  full_name='eu.surething_project.core.OLC',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='southLatitude', full_name='eu.surething_project.core.OLC.southLatitude', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='westLongitude', full_name='eu.surething_project.core.OLC.westLongitude', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='northLatitude', full_name='eu.surething_project.core.OLC.northLatitude', index=2,
      number=3, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='eastLongitude', full_name='eu.surething_project.core.OLC.eastLongitude', index=3,
      number=4, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='length', full_name='eu.surething_project.core.OLC.length', index=4,
      number=5, type=5, cpp_type=1, label=1,
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
  serialized_start=548,
  serialized_end=661,
)


_TIME = _descriptor.Descriptor(
  name='Time',
  full_name='eu.surething_project.core.Time',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='eu.surething_project.core.Time.timestamp', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='interval', full_name='eu.surething_project.core.Time.interval', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='relativeToEpoch', full_name='eu.surething_project.core.Time.relativeToEpoch', index=2,
      number=3, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
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
    _descriptor.OneofDescriptor(
      name='time', full_name='eu.surething_project.core.Time.time',
      index=0, containing_type=None,
      create_key=_descriptor._internal_create_key,
    fields=[]),
  ],
  serialized_start=664,
  serialized_end=853,
)


_TIMEINTERVAL = _descriptor.Descriptor(
  name='TimeInterval',
  full_name='eu.surething_project.core.TimeInterval',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='begin', full_name='eu.surething_project.core.TimeInterval.begin', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='end', full_name='eu.surething_project.core.TimeInterval.end', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
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
  serialized_start=855,
  serialized_end=953,
)


_EPOCHTIME = _descriptor.Descriptor(
  name='EpochTime',
  full_name='eu.surething_project.core.EpochTime',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='epochId', full_name='eu.surething_project.core.EpochTime.epochId', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='timeValue', full_name='eu.surething_project.core.EpochTime.timeValue', index=1,
      number=2, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='timeUnit', full_name='eu.surething_project.core.EpochTime.timeUnit', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
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
  serialized_start=955,
  serialized_end=1020,
)


_LOCATIONCLAIM = _descriptor.Descriptor(
  name='LocationClaim',
  full_name='eu.surething_project.core.LocationClaim',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='claimId', full_name='eu.surething_project.core.LocationClaim.claimId', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='proverId', full_name='eu.surething_project.core.LocationClaim.proverId', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='location', full_name='eu.surething_project.core.LocationClaim.location', index=2,
      number=3, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='time', full_name='eu.surething_project.core.LocationClaim.time', index=3,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='evidenceType', full_name='eu.surething_project.core.LocationClaim.evidenceType', index=4,
      number=5, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='evidence', full_name='eu.surething_project.core.LocationClaim.evidence', index=5,
      number=6, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
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
  serialized_start=1023,
  serialized_end=1237,
)


_SIGNEDLOCATIONCLAIM = _descriptor.Descriptor(
  name='SignedLocationClaim',
  full_name='eu.surething_project.core.SignedLocationClaim',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='claim', full_name='eu.surething_project.core.SignedLocationClaim.claim', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='proverSignature', full_name='eu.surething_project.core.SignedLocationClaim.proverSignature', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
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
  serialized_start=1240,
  serialized_end=1381,
)

_LOCATION.fields_by_name['latLng'].message_type = latlng__pb2._LATLNG
_LOCATION.fields_by_name['poi'].message_type = _POI
_LOCATION.fields_by_name['proximityToPoI'].message_type = _POIPROXIMITY
_LOCATION.fields_by_name['olc'].message_type = _OLC
_LOCATION.oneofs_by_name['location'].fields.append(
  _LOCATION.fields_by_name['latLng'])
_LOCATION.fields_by_name['latLng'].containing_oneof = _LOCATION.oneofs_by_name['location']
_LOCATION.oneofs_by_name['location'].fields.append(
  _LOCATION.fields_by_name['poi'])
_LOCATION.fields_by_name['poi'].containing_oneof = _LOCATION.oneofs_by_name['location']
_LOCATION.oneofs_by_name['location'].fields.append(
  _LOCATION.fields_by_name['proximityToPoI'])
_LOCATION.fields_by_name['proximityToPoI'].containing_oneof = _LOCATION.oneofs_by_name['location']
_LOCATION.oneofs_by_name['location'].fields.append(
  _LOCATION.fields_by_name['olc'])
_LOCATION.fields_by_name['olc'].containing_oneof = _LOCATION.oneofs_by_name['location']
_POI.fields_by_name['localizedName'].message_type = localized__text__pb2._LOCALIZEDTEXT
_TIME.fields_by_name['timestamp'].message_type = google_dot_protobuf_dot_timestamp__pb2._TIMESTAMP
_TIME.fields_by_name['interval'].message_type = _TIMEINTERVAL
_TIME.fields_by_name['relativeToEpoch'].message_type = _EPOCHTIME
_TIME.oneofs_by_name['time'].fields.append(
  _TIME.fields_by_name['timestamp'])
_TIME.fields_by_name['timestamp'].containing_oneof = _TIME.oneofs_by_name['time']
_TIME.oneofs_by_name['time'].fields.append(
  _TIME.fields_by_name['interval'])
_TIME.fields_by_name['interval'].containing_oneof = _TIME.oneofs_by_name['time']
_TIME.oneofs_by_name['time'].fields.append(
  _TIME.fields_by_name['relativeToEpoch'])
_TIME.fields_by_name['relativeToEpoch'].containing_oneof = _TIME.oneofs_by_name['time']
_TIMEINTERVAL.fields_by_name['begin'].message_type = google_dot_protobuf_dot_timestamp__pb2._TIMESTAMP
_TIMEINTERVAL.fields_by_name['end'].message_type = google_dot_protobuf_dot_timestamp__pb2._TIMESTAMP
_LOCATIONCLAIM.fields_by_name['location'].message_type = _LOCATION
_LOCATIONCLAIM.fields_by_name['time'].message_type = _TIME
_LOCATIONCLAIM.fields_by_name['evidence'].message_type = google_dot_protobuf_dot_any__pb2._ANY
_SIGNEDLOCATIONCLAIM.fields_by_name['claim'].message_type = _LOCATIONCLAIM
_SIGNEDLOCATIONCLAIM.fields_by_name['proverSignature'].message_type = signature__pb2._SIGNATURE
DESCRIPTOR.message_types_by_name['Location'] = _LOCATION
DESCRIPTOR.message_types_by_name['PoI'] = _POI
DESCRIPTOR.message_types_by_name['PoIProximity'] = _POIPROXIMITY
DESCRIPTOR.message_types_by_name['OLC'] = _OLC
DESCRIPTOR.message_types_by_name['Time'] = _TIME
DESCRIPTOR.message_types_by_name['TimeInterval'] = _TIMEINTERVAL
DESCRIPTOR.message_types_by_name['EpochTime'] = _EPOCHTIME
DESCRIPTOR.message_types_by_name['LocationClaim'] = _LOCATIONCLAIM
DESCRIPTOR.message_types_by_name['SignedLocationClaim'] = _SIGNEDLOCATIONCLAIM
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

Location = _reflection.GeneratedProtocolMessageType('Location', (_message.Message,), {
  'DESCRIPTOR' : _LOCATION,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.Location)
  })
_sym_db.RegisterMessage(Location)

PoI = _reflection.GeneratedProtocolMessageType('PoI', (_message.Message,), {
  'DESCRIPTOR' : _POI,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.PoI)
  })
_sym_db.RegisterMessage(PoI)

PoIProximity = _reflection.GeneratedProtocolMessageType('PoIProximity', (_message.Message,), {
  'DESCRIPTOR' : _POIPROXIMITY,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.PoIProximity)
  })
_sym_db.RegisterMessage(PoIProximity)

OLC = _reflection.GeneratedProtocolMessageType('OLC', (_message.Message,), {
  'DESCRIPTOR' : _OLC,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.OLC)
  })
_sym_db.RegisterMessage(OLC)

Time = _reflection.GeneratedProtocolMessageType('Time', (_message.Message,), {
  'DESCRIPTOR' : _TIME,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.Time)
  })
_sym_db.RegisterMessage(Time)

TimeInterval = _reflection.GeneratedProtocolMessageType('TimeInterval', (_message.Message,), {
  'DESCRIPTOR' : _TIMEINTERVAL,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.TimeInterval)
  })
_sym_db.RegisterMessage(TimeInterval)

EpochTime = _reflection.GeneratedProtocolMessageType('EpochTime', (_message.Message,), {
  'DESCRIPTOR' : _EPOCHTIME,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.EpochTime)
  })
_sym_db.RegisterMessage(EpochTime)

LocationClaim = _reflection.GeneratedProtocolMessageType('LocationClaim', (_message.Message,), {
  'DESCRIPTOR' : _LOCATIONCLAIM,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.LocationClaim)
  })
_sym_db.RegisterMessage(LocationClaim)

SignedLocationClaim = _reflection.GeneratedProtocolMessageType('SignedLocationClaim', (_message.Message,), {
  'DESCRIPTOR' : _SIGNEDLOCATIONCLAIM,
  '__module__' : 'locationclaim_pb2'
  # @@protoc_insertion_point(class_scope:eu.surething_project.core.SignedLocationClaim)
  })
_sym_db.RegisterMessage(SignedLocationClaim)


DESCRIPTOR._options = None
# @@protoc_insertion_point(module_scope)