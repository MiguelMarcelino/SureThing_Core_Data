// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: loc_time.proto

package eu.surething_project.core.grpc;

/**
 * <pre>
 * Time interval
 * </pre>
 *
 * Protobuf type {@code eu.surething_project.core.TimeInterval}
 */
public  final class TimeInterval extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:eu.surething_project.core.TimeInterval)
    TimeIntervalOrBuilder {
private static final long serialVersionUID = 0L;
  // Use TimeInterval.newBuilder() to construct.
  private TimeInterval(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private TimeInterval() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private TimeInterval(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            com.google.protobuf.Timestamp.Builder subBuilder = null;
            if (begin_ != null) {
              subBuilder = begin_.toBuilder();
            }
            begin_ = input.readMessage(com.google.protobuf.Timestamp.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(begin_);
              begin_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            com.google.protobuf.Timestamp.Builder subBuilder = null;
            if (end_ != null) {
              subBuilder = end_.toBuilder();
            }
            end_ = input.readMessage(com.google.protobuf.Timestamp.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(end_);
              end_ = subBuilder.buildPartial();
            }

            break;
          }
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return eu.surething_project.core.grpc.LocTimeProto.internal_static_eu_surething_project_core_TimeInterval_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return eu.surething_project.core.grpc.LocTimeProto.internal_static_eu_surething_project_core_TimeInterval_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            eu.surething_project.core.grpc.TimeInterval.class, eu.surething_project.core.grpc.TimeInterval.Builder.class);
  }

  public static final int BEGIN_FIELD_NUMBER = 1;
  private com.google.protobuf.Timestamp begin_;
  /**
   * <code>.google.protobuf.Timestamp begin = 1;</code>
   */
  public boolean hasBegin() {
    return begin_ != null;
  }
  /**
   * <code>.google.protobuf.Timestamp begin = 1;</code>
   */
  public com.google.protobuf.Timestamp getBegin() {
    return begin_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : begin_;
  }
  /**
   * <code>.google.protobuf.Timestamp begin = 1;</code>
   */
  public com.google.protobuf.TimestampOrBuilder getBeginOrBuilder() {
    return getBegin();
  }

  public static final int END_FIELD_NUMBER = 2;
  private com.google.protobuf.Timestamp end_;
  /**
   * <code>.google.protobuf.Timestamp end = 2;</code>
   */
  public boolean hasEnd() {
    return end_ != null;
  }
  /**
   * <code>.google.protobuf.Timestamp end = 2;</code>
   */
  public com.google.protobuf.Timestamp getEnd() {
    return end_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : end_;
  }
  /**
   * <code>.google.protobuf.Timestamp end = 2;</code>
   */
  public com.google.protobuf.TimestampOrBuilder getEndOrBuilder() {
    return getEnd();
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (begin_ != null) {
      output.writeMessage(1, getBegin());
    }
    if (end_ != null) {
      output.writeMessage(2, getEnd());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (begin_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getBegin());
    }
    if (end_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getEnd());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof eu.surething_project.core.grpc.TimeInterval)) {
      return super.equals(obj);
    }
    eu.surething_project.core.grpc.TimeInterval other = (eu.surething_project.core.grpc.TimeInterval) obj;

    boolean result = true;
    result = result && (hasBegin() == other.hasBegin());
    if (hasBegin()) {
      result = result && getBegin()
          .equals(other.getBegin());
    }
    result = result && (hasEnd() == other.hasEnd());
    if (hasEnd()) {
      result = result && getEnd()
          .equals(other.getEnd());
    }
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasBegin()) {
      hash = (37 * hash) + BEGIN_FIELD_NUMBER;
      hash = (53 * hash) + getBegin().hashCode();
    }
    if (hasEnd()) {
      hash = (37 * hash) + END_FIELD_NUMBER;
      hash = (53 * hash) + getEnd().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static eu.surething_project.core.grpc.TimeInterval parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.TimeInterval parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(eu.surething_project.core.grpc.TimeInterval prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * Time interval
   * </pre>
   *
   * Protobuf type {@code eu.surething_project.core.TimeInterval}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:eu.surething_project.core.TimeInterval)
      eu.surething_project.core.grpc.TimeIntervalOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return eu.surething_project.core.grpc.LocTimeProto.internal_static_eu_surething_project_core_TimeInterval_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return eu.surething_project.core.grpc.LocTimeProto.internal_static_eu_surething_project_core_TimeInterval_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              eu.surething_project.core.grpc.TimeInterval.class, eu.surething_project.core.grpc.TimeInterval.Builder.class);
    }

    // Construct using eu.surething_project.core.grpc.TimeInterval.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (beginBuilder_ == null) {
        begin_ = null;
      } else {
        begin_ = null;
        beginBuilder_ = null;
      }
      if (endBuilder_ == null) {
        end_ = null;
      } else {
        end_ = null;
        endBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return eu.surething_project.core.grpc.LocTimeProto.internal_static_eu_surething_project_core_TimeInterval_descriptor;
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.TimeInterval getDefaultInstanceForType() {
      return eu.surething_project.core.grpc.TimeInterval.getDefaultInstance();
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.TimeInterval build() {
      eu.surething_project.core.grpc.TimeInterval result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.TimeInterval buildPartial() {
      eu.surething_project.core.grpc.TimeInterval result = new eu.surething_project.core.grpc.TimeInterval(this);
      if (beginBuilder_ == null) {
        result.begin_ = begin_;
      } else {
        result.begin_ = beginBuilder_.build();
      }
      if (endBuilder_ == null) {
        result.end_ = end_;
      } else {
        result.end_ = endBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return (Builder) super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof eu.surething_project.core.grpc.TimeInterval) {
        return mergeFrom((eu.surething_project.core.grpc.TimeInterval)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(eu.surething_project.core.grpc.TimeInterval other) {
      if (other == eu.surething_project.core.grpc.TimeInterval.getDefaultInstance()) return this;
      if (other.hasBegin()) {
        mergeBegin(other.getBegin());
      }
      if (other.hasEnd()) {
        mergeEnd(other.getEnd());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      eu.surething_project.core.grpc.TimeInterval parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (eu.surething_project.core.grpc.TimeInterval) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.google.protobuf.Timestamp begin_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder> beginBuilder_;
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    public boolean hasBegin() {
      return beginBuilder_ != null || begin_ != null;
    }
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    public com.google.protobuf.Timestamp getBegin() {
      if (beginBuilder_ == null) {
        return begin_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : begin_;
      } else {
        return beginBuilder_.getMessage();
      }
    }
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    public Builder setBegin(com.google.protobuf.Timestamp value) {
      if (beginBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        begin_ = value;
        onChanged();
      } else {
        beginBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    public Builder setBegin(
        com.google.protobuf.Timestamp.Builder builderForValue) {
      if (beginBuilder_ == null) {
        begin_ = builderForValue.build();
        onChanged();
      } else {
        beginBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    public Builder mergeBegin(com.google.protobuf.Timestamp value) {
      if (beginBuilder_ == null) {
        if (begin_ != null) {
          begin_ =
            com.google.protobuf.Timestamp.newBuilder(begin_).mergeFrom(value).buildPartial();
        } else {
          begin_ = value;
        }
        onChanged();
      } else {
        beginBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    public Builder clearBegin() {
      if (beginBuilder_ == null) {
        begin_ = null;
        onChanged();
      } else {
        begin_ = null;
        beginBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    public com.google.protobuf.Timestamp.Builder getBeginBuilder() {
      
      onChanged();
      return getBeginFieldBuilder().getBuilder();
    }
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    public com.google.protobuf.TimestampOrBuilder getBeginOrBuilder() {
      if (beginBuilder_ != null) {
        return beginBuilder_.getMessageOrBuilder();
      } else {
        return begin_ == null ?
            com.google.protobuf.Timestamp.getDefaultInstance() : begin_;
      }
    }
    /**
     * <code>.google.protobuf.Timestamp begin = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder> 
        getBeginFieldBuilder() {
      if (beginBuilder_ == null) {
        beginBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder>(
                getBegin(),
                getParentForChildren(),
                isClean());
        begin_ = null;
      }
      return beginBuilder_;
    }

    private com.google.protobuf.Timestamp end_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder> endBuilder_;
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    public boolean hasEnd() {
      return endBuilder_ != null || end_ != null;
    }
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    public com.google.protobuf.Timestamp getEnd() {
      if (endBuilder_ == null) {
        return end_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : end_;
      } else {
        return endBuilder_.getMessage();
      }
    }
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    public Builder setEnd(com.google.protobuf.Timestamp value) {
      if (endBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        end_ = value;
        onChanged();
      } else {
        endBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    public Builder setEnd(
        com.google.protobuf.Timestamp.Builder builderForValue) {
      if (endBuilder_ == null) {
        end_ = builderForValue.build();
        onChanged();
      } else {
        endBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    public Builder mergeEnd(com.google.protobuf.Timestamp value) {
      if (endBuilder_ == null) {
        if (end_ != null) {
          end_ =
            com.google.protobuf.Timestamp.newBuilder(end_).mergeFrom(value).buildPartial();
        } else {
          end_ = value;
        }
        onChanged();
      } else {
        endBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    public Builder clearEnd() {
      if (endBuilder_ == null) {
        end_ = null;
        onChanged();
      } else {
        end_ = null;
        endBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    public com.google.protobuf.Timestamp.Builder getEndBuilder() {
      
      onChanged();
      return getEndFieldBuilder().getBuilder();
    }
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    public com.google.protobuf.TimestampOrBuilder getEndOrBuilder() {
      if (endBuilder_ != null) {
        return endBuilder_.getMessageOrBuilder();
      } else {
        return end_ == null ?
            com.google.protobuf.Timestamp.getDefaultInstance() : end_;
      }
    }
    /**
     * <code>.google.protobuf.Timestamp end = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder> 
        getEndFieldBuilder() {
      if (endBuilder_ == null) {
        endBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder>(
                getEnd(),
                getParentForChildren(),
                isClean());
        end_ = null;
      }
      return endBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:eu.surething_project.core.TimeInterval)
  }

  // @@protoc_insertion_point(class_scope:eu.surething_project.core.TimeInterval)
  private static final eu.surething_project.core.grpc.TimeInterval DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new eu.surething_project.core.grpc.TimeInterval();
  }

  public static eu.surething_project.core.grpc.TimeInterval getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<TimeInterval>
      PARSER = new com.google.protobuf.AbstractParser<TimeInterval>() {
    @java.lang.Override
    public TimeInterval parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new TimeInterval(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<TimeInterval> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<TimeInterval> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public eu.surething_project.core.grpc.TimeInterval getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

