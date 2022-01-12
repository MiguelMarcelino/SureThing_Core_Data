// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: loc_time.proto

package eu.surething_project.core.grpc;

public interface PoIProximityOrBuilder extends
    // @@protoc_insertion_point(interface_extends:eu.surething_project.core.PoIProximity)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * unique identifier for point-of-interest
   * </pre>
   *
   * <code>string poiId = 1;</code>
   */
  java.lang.String getPoiId();
  /**
   * <pre>
   * unique identifier for point-of-interest
   * </pre>
   *
   * <code>string poiId = 1;</code>
   */
  com.google.protobuf.ByteString
      getPoiIdBytes();

  /**
   * <pre>
   * distance to PoI, &gt;= 0
   * </pre>
   *
   * <code>uint64 distanceValue = 2;</code>
   */
  long getDistanceValue();

  /**
   * <pre>
   * unit of distance (e.g. cm)
   * </pre>
   *
   * <code>string distanceUnit = 3;</code>
   */
  java.lang.String getDistanceUnit();
  /**
   * <pre>
   * unit of distance (e.g. cm)
   * </pre>
   *
   * <code>string distanceUnit = 3;</code>
   */
  com.google.protobuf.ByteString
      getDistanceUnitBytes();
}
