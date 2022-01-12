// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: loc_time.proto

package eu.surething_project.core.grpc;

public interface LocationOrBuilder extends
    // @@protoc_insertion_point(interface_extends:eu.surething_project.core.Location)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * latitude and longitude coordinates
   * </pre>
   *
   * <code>.google.type.LatLng latLng = 1;</code>
   */
  boolean hasLatLng();
  /**
   * <pre>
   * latitude and longitude coordinates
   * </pre>
   *
   * <code>.google.type.LatLng latLng = 1;</code>
   */
  eu.surething_project.core.grpc.google.type.LatLng getLatLng();
  /**
   * <pre>
   * latitude and longitude coordinates
   * </pre>
   *
   * <code>.google.type.LatLng latLng = 1;</code>
   */
  eu.surething_project.core.grpc.google.type.LatLngOrBuilder getLatLngOrBuilder();

  /**
   * <pre>
   * point-of-interest
   * </pre>
   *
   * <code>.eu.surething_project.core.PoI poi = 2;</code>
   */
  boolean hasPoi();
  /**
   * <pre>
   * point-of-interest
   * </pre>
   *
   * <code>.eu.surething_project.core.PoI poi = 2;</code>
   */
  eu.surething_project.core.grpc.PoI getPoi();
  /**
   * <pre>
   * point-of-interest
   * </pre>
   *
   * <code>.eu.surething_project.core.PoI poi = 2;</code>
   */
  eu.surething_project.core.grpc.PoIOrBuilder getPoiOrBuilder();

  /**
   * <pre>
   * proximity to point-of-interest
   * </pre>
   *
   * <code>.eu.surething_project.core.PoIProximity proximityToPoI = 3;</code>
   */
  boolean hasProximityToPoI();
  /**
   * <pre>
   * proximity to point-of-interest
   * </pre>
   *
   * <code>.eu.surething_project.core.PoIProximity proximityToPoI = 3;</code>
   */
  eu.surething_project.core.grpc.PoIProximity getProximityToPoI();
  /**
   * <pre>
   * proximity to point-of-interest
   * </pre>
   *
   * <code>.eu.surething_project.core.PoIProximity proximityToPoI = 3;</code>
   */
  eu.surething_project.core.grpc.PoIProximityOrBuilder getProximityToPoIOrBuilder();

  /**
   * <pre>
   * open location code
   * </pre>
   *
   * <code>.eu.surething_project.core.OLC olc = 4;</code>
   */
  boolean hasOlc();
  /**
   * <pre>
   * open location code
   * </pre>
   *
   * <code>.eu.surething_project.core.OLC olc = 4;</code>
   */
  eu.surething_project.core.grpc.OLC getOlc();
  /**
   * <pre>
   * open location code
   * </pre>
   *
   * <code>.eu.surething_project.core.OLC olc = 4;</code>
   */
  eu.surething_project.core.grpc.OLCOrBuilder getOlcOrBuilder();

  public eu.surething_project.core.grpc.Location.LocationCase getLocationCase();
}