// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: signature.proto

package eu.surething_project.core.grpc;

/**
 * <pre>
 * signature definition
 * </pre>
 *
 * Protobuf type {@code eu.surething_project.core.Signature}
 */
public  final class Signature extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:eu.surething_project.core.Signature)
    SignatureOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Signature.newBuilder() to construct.
  private Signature(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Signature() {
    value_ = com.google.protobuf.ByteString.EMPTY;
    cryptoAlgo_ = "";
    nonce_ = 0L;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Signature(
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

            value_ = input.readBytes();
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            cryptoAlgo_ = s;
            break;
          }
          case 24: {

            nonce_ = input.readInt64();
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
    return eu.surething_project.core.grpc.SignatureProto.internal_static_eu_surething_project_core_Signature_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return eu.surething_project.core.grpc.SignatureProto.internal_static_eu_surething_project_core_Signature_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            eu.surething_project.core.grpc.Signature.class, eu.surething_project.core.grpc.Signature.Builder.class);
  }

  public static final int VALUE_FIELD_NUMBER = 1;
  private com.google.protobuf.ByteString value_;
  /**
   * <pre>
   * binary value of signature
   * </pre>
   *
   * <code>bytes value = 1;</code>
   */
  public com.google.protobuf.ByteString getValue() {
    return value_;
  }

  public static final int CRYPTOALGO_FIELD_NUMBER = 2;
  private volatile java.lang.Object cryptoAlgo_;
  /**
   * <pre>
   * identification of cryptographic signature algorithm
   * </pre>
   *
   * <code>string cryptoAlgo = 2;</code>
   */
  public java.lang.String getCryptoAlgo() {
    java.lang.Object ref = cryptoAlgo_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      cryptoAlgo_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * identification of cryptographic signature algorithm
   * </pre>
   *
   * <code>string cryptoAlgo = 2;</code>
   */
  public com.google.protobuf.ByteString
      getCryptoAlgoBytes() {
    java.lang.Object ref = cryptoAlgo_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      cryptoAlgo_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int NONCE_FIELD_NUMBER = 3;
  private long nonce_;
  /**
   * <pre>
   * number used once - to assure message freshness
   * </pre>
   *
   * <code>int64 nonce = 3;</code>
   */
  public long getNonce() {
    return nonce_;
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
    if (!value_.isEmpty()) {
      output.writeBytes(1, value_);
    }
    if (!getCryptoAlgoBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, cryptoAlgo_);
    }
    if (nonce_ != 0L) {
      output.writeInt64(3, nonce_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!value_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(1, value_);
    }
    if (!getCryptoAlgoBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, cryptoAlgo_);
    }
    if (nonce_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, nonce_);
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
    if (!(obj instanceof eu.surething_project.core.grpc.Signature)) {
      return super.equals(obj);
    }
    eu.surething_project.core.grpc.Signature other = (eu.surething_project.core.grpc.Signature) obj;

    boolean result = true;
    result = result && getValue()
        .equals(other.getValue());
    result = result && getCryptoAlgo()
        .equals(other.getCryptoAlgo());
    result = result && (getNonce()
        == other.getNonce());
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
    hash = (37 * hash) + VALUE_FIELD_NUMBER;
    hash = (53 * hash) + getValue().hashCode();
    hash = (37 * hash) + CRYPTOALGO_FIELD_NUMBER;
    hash = (53 * hash) + getCryptoAlgo().hashCode();
    hash = (37 * hash) + NONCE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getNonce());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static eu.surething_project.core.grpc.Signature parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.Signature parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.Signature parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.Signature parseFrom(
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
  public static Builder newBuilder(eu.surething_project.core.grpc.Signature prototype) {
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
   * signature definition
   * </pre>
   *
   * Protobuf type {@code eu.surething_project.core.Signature}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:eu.surething_project.core.Signature)
      eu.surething_project.core.grpc.SignatureOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return eu.surething_project.core.grpc.SignatureProto.internal_static_eu_surething_project_core_Signature_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return eu.surething_project.core.grpc.SignatureProto.internal_static_eu_surething_project_core_Signature_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              eu.surething_project.core.grpc.Signature.class, eu.surething_project.core.grpc.Signature.Builder.class);
    }

    // Construct using eu.surething_project.core.grpc.Signature.newBuilder()
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
      value_ = com.google.protobuf.ByteString.EMPTY;

      cryptoAlgo_ = "";

      nonce_ = 0L;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return eu.surething_project.core.grpc.SignatureProto.internal_static_eu_surething_project_core_Signature_descriptor;
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.Signature getDefaultInstanceForType() {
      return eu.surething_project.core.grpc.Signature.getDefaultInstance();
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.Signature build() {
      eu.surething_project.core.grpc.Signature result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.Signature buildPartial() {
      eu.surething_project.core.grpc.Signature result = new eu.surething_project.core.grpc.Signature(this);
      result.value_ = value_;
      result.cryptoAlgo_ = cryptoAlgo_;
      result.nonce_ = nonce_;
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
      if (other instanceof eu.surething_project.core.grpc.Signature) {
        return mergeFrom((eu.surething_project.core.grpc.Signature)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(eu.surething_project.core.grpc.Signature other) {
      if (other == eu.surething_project.core.grpc.Signature.getDefaultInstance()) return this;
      if (other.getValue() != com.google.protobuf.ByteString.EMPTY) {
        setValue(other.getValue());
      }
      if (!other.getCryptoAlgo().isEmpty()) {
        cryptoAlgo_ = other.cryptoAlgo_;
        onChanged();
      }
      if (other.getNonce() != 0L) {
        setNonce(other.getNonce());
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
      eu.surething_project.core.grpc.Signature parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (eu.surething_project.core.grpc.Signature) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.google.protobuf.ByteString value_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <pre>
     * binary value of signature
     * </pre>
     *
     * <code>bytes value = 1;</code>
     */
    public com.google.protobuf.ByteString getValue() {
      return value_;
    }
    /**
     * <pre>
     * binary value of signature
     * </pre>
     *
     * <code>bytes value = 1;</code>
     */
    public Builder setValue(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      value_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * binary value of signature
     * </pre>
     *
     * <code>bytes value = 1;</code>
     */
    public Builder clearValue() {
      
      value_ = getDefaultInstance().getValue();
      onChanged();
      return this;
    }

    private java.lang.Object cryptoAlgo_ = "";
    /**
     * <pre>
     * identification of cryptographic signature algorithm
     * </pre>
     *
     * <code>string cryptoAlgo = 2;</code>
     */
    public java.lang.String getCryptoAlgo() {
      java.lang.Object ref = cryptoAlgo_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        cryptoAlgo_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * identification of cryptographic signature algorithm
     * </pre>
     *
     * <code>string cryptoAlgo = 2;</code>
     */
    public com.google.protobuf.ByteString
        getCryptoAlgoBytes() {
      java.lang.Object ref = cryptoAlgo_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        cryptoAlgo_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * identification of cryptographic signature algorithm
     * </pre>
     *
     * <code>string cryptoAlgo = 2;</code>
     */
    public Builder setCryptoAlgo(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      cryptoAlgo_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * identification of cryptographic signature algorithm
     * </pre>
     *
     * <code>string cryptoAlgo = 2;</code>
     */
    public Builder clearCryptoAlgo() {
      
      cryptoAlgo_ = getDefaultInstance().getCryptoAlgo();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * identification of cryptographic signature algorithm
     * </pre>
     *
     * <code>string cryptoAlgo = 2;</code>
     */
    public Builder setCryptoAlgoBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      cryptoAlgo_ = value;
      onChanged();
      return this;
    }

    private long nonce_ ;
    /**
     * <pre>
     * number used once - to assure message freshness
     * </pre>
     *
     * <code>int64 nonce = 3;</code>
     */
    public long getNonce() {
      return nonce_;
    }
    /**
     * <pre>
     * number used once - to assure message freshness
     * </pre>
     *
     * <code>int64 nonce = 3;</code>
     */
    public Builder setNonce(long value) {
      
      nonce_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * number used once - to assure message freshness
     * </pre>
     *
     * <code>int64 nonce = 3;</code>
     */
    public Builder clearNonce() {
      
      nonce_ = 0L;
      onChanged();
      return this;
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


    // @@protoc_insertion_point(builder_scope:eu.surething_project.core.Signature)
  }

  // @@protoc_insertion_point(class_scope:eu.surething_project.core.Signature)
  private static final eu.surething_project.core.grpc.Signature DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new eu.surething_project.core.grpc.Signature();
  }

  public static eu.surething_project.core.grpc.Signature getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Signature>
      PARSER = new com.google.protobuf.AbstractParser<Signature>() {
    @java.lang.Override
    public Signature parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Signature(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Signature> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Signature> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public eu.surething_project.core.grpc.Signature getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

