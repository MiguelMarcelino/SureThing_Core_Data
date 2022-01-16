// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: loc_time.proto

package eu.surething_project.core.grpc;

public interface PoIOrBuilder extends
    // @@protoc_insertion_point(interface_extends:eu.surething_project.core.PoI)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * unique identifier
   * </pre>
   *
   * <code>string id = 1;</code>
   */
  java.lang.String getId();
  /**
   * <pre>
   * unique identifier
   * </pre>
   *
   * <code>string id = 1;</code>
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <pre>
   * name of point-of-interest
   * </pre>
   *
   * <code>string name = 2;</code>
   */
  java.lang.String getName();
  /**
   * <pre>
   * name of point-of-interest
   * </pre>
   *
   * <code>string name = 2;</code>
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <pre>
   * name in different locales
   * </pre>
   *
   * <code>repeated .google.type.LocalizedText localizedName = 3;</code>
   */
  java.util.List<eu.surething_project.core.grpc.google.type.LocalizedText> 
      getLocalizedNameList();
  /**
   * <pre>
   * name in different locales
   * </pre>
   *
   * <code>repeated .google.type.LocalizedText localizedName = 3;</code>
   */
  eu.surething_project.core.grpc.google.type.LocalizedText getLocalizedName(int index);
  /**
   * <pre>
   * name in different locales
   * </pre>
   *
   * <code>repeated .google.type.LocalizedText localizedName = 3;</code>
   */
  int getLocalizedNameCount();
  /**
   * <pre>
   * name in different locales
   * </pre>
   *
   * <code>repeated .google.type.LocalizedText localizedName = 3;</code>
   */
  java.util.List<? extends eu.surething_project.core.grpc.google.type.LocalizedTextOrBuilder> 
      getLocalizedNameOrBuilderList();
  /**
   * <pre>
   * name in different locales
   * </pre>
   *
   * <code>repeated .google.type.LocalizedText localizedName = 3;</code>
   */
  eu.surething_project.core.grpc.google.type.LocalizedTextOrBuilder getLocalizedNameOrBuilder(
      int index);
}
