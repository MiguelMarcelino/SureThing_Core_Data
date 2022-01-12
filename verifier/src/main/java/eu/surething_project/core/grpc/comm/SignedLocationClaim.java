// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: loc_claim.proto

package eu.surething_project.core.grpc.comm;

/**
 * <pre>
 * signed location claim 
 * </pre>
 *
 * Protobuf type {@code eu.surething_project.core.grpc.SignedLocationClaim}
 */
public  final class SignedLocationClaim extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:eu.surething_project.core.grpc.SignedLocationClaim)
    SignedLocationClaimOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SignedLocationClaim.newBuilder() to construct.
  private SignedLocationClaim(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SignedLocationClaim() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private SignedLocationClaim(
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
            eu.surething_project.core.grpc.comm.LocationClaim.Builder subBuilder = null;
            if (claim_ != null) {
              subBuilder = claim_.toBuilder();
            }
            claim_ = input.readMessage(eu.surething_project.core.grpc.comm.LocationClaim.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(claim_);
              claim_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            eu.surething_project.core.grpc.signature.Signature.Builder subBuilder = null;
            if (proverSignature_ != null) {
              subBuilder = proverSignature_.toBuilder();
            }
            proverSignature_ = input.readMessage(eu.surething_project.core.grpc.signature.Signature.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(proverSignature_);
              proverSignature_ = subBuilder.buildPartial();
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
    return eu.surething_project.core.grpc.comm.LocClaimProto.internal_static_eu_surething_project_core_grpc_SignedLocationClaim_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return eu.surething_project.core.grpc.comm.LocClaimProto.internal_static_eu_surething_project_core_grpc_SignedLocationClaim_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            eu.surething_project.core.grpc.comm.SignedLocationClaim.class, eu.surething_project.core.grpc.comm.SignedLocationClaim.Builder.class);
  }

  public static final int CLAIM_FIELD_NUMBER = 1;
  private eu.surething_project.core.grpc.comm.LocationClaim claim_;
  /**
   * <pre>
   * claim
   * </pre>
   *
   * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
   */
  public boolean hasClaim() {
    return claim_ != null;
  }
  /**
   * <pre>
   * claim
   * </pre>
   *
   * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
   */
  public eu.surething_project.core.grpc.comm.LocationClaim getClaim() {
    return claim_ == null ? eu.surething_project.core.grpc.comm.LocationClaim.getDefaultInstance() : claim_;
  }
  /**
   * <pre>
   * claim
   * </pre>
   *
   * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
   */
  public eu.surething_project.core.grpc.comm.LocationClaimOrBuilder getClaimOrBuilder() {
    return getClaim();
  }

  public static final int PROVERSIGNATURE_FIELD_NUMBER = 2;
  private eu.surething_project.core.grpc.signature.Signature proverSignature_;
  /**
   * <pre>
   * prover's signature
   * </pre>
   *
   * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
   */
  public boolean hasProverSignature() {
    return proverSignature_ != null;
  }
  /**
   * <pre>
   * prover's signature
   * </pre>
   *
   * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
   */
  public eu.surething_project.core.grpc.signature.Signature getProverSignature() {
    return proverSignature_ == null ? eu.surething_project.core.grpc.signature.Signature.getDefaultInstance() : proverSignature_;
  }
  /**
   * <pre>
   * prover's signature
   * </pre>
   *
   * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
   */
  public eu.surething_project.core.grpc.signature.SignatureOrBuilder getProverSignatureOrBuilder() {
    return getProverSignature();
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
    if (claim_ != null) {
      output.writeMessage(1, getClaim());
    }
    if (proverSignature_ != null) {
      output.writeMessage(2, getProverSignature());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (claim_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getClaim());
    }
    if (proverSignature_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getProverSignature());
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
    if (!(obj instanceof eu.surething_project.core.grpc.comm.SignedLocationClaim)) {
      return super.equals(obj);
    }
    eu.surething_project.core.grpc.comm.SignedLocationClaim other = (eu.surething_project.core.grpc.comm.SignedLocationClaim) obj;

    boolean result = true;
    result = result && (hasClaim() == other.hasClaim());
    if (hasClaim()) {
      result = result && getClaim()
          .equals(other.getClaim());
    }
    result = result && (hasProverSignature() == other.hasProverSignature());
    if (hasProverSignature()) {
      result = result && getProverSignature()
          .equals(other.getProverSignature());
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
    if (hasClaim()) {
      hash = (37 * hash) + CLAIM_FIELD_NUMBER;
      hash = (53 * hash) + getClaim().hashCode();
    }
    if (hasProverSignature()) {
      hash = (37 * hash) + PROVERSIGNATURE_FIELD_NUMBER;
      hash = (53 * hash) + getProverSignature().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static eu.surething_project.core.grpc.comm.SignedLocationClaim parseFrom(
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
  public static Builder newBuilder(eu.surething_project.core.grpc.comm.SignedLocationClaim prototype) {
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
   * signed location claim 
   * </pre>
   *
   * Protobuf type {@code eu.surething_project.core.grpc.SignedLocationClaim}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:eu.surething_project.core.grpc.SignedLocationClaim)
      eu.surething_project.core.grpc.comm.SignedLocationClaimOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return eu.surething_project.core.grpc.comm.LocClaimProto.internal_static_eu_surething_project_core_grpc_SignedLocationClaim_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return eu.surething_project.core.grpc.comm.LocClaimProto.internal_static_eu_surething_project_core_grpc_SignedLocationClaim_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              eu.surething_project.core.grpc.comm.SignedLocationClaim.class, eu.surething_project.core.grpc.comm.SignedLocationClaim.Builder.class);
    }

    // Construct using eu.surething_project.core.grpc.comm.SignedLocationClaim.newBuilder()
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
      if (claimBuilder_ == null) {
        claim_ = null;
      } else {
        claim_ = null;
        claimBuilder_ = null;
      }
      if (proverSignatureBuilder_ == null) {
        proverSignature_ = null;
      } else {
        proverSignature_ = null;
        proverSignatureBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return eu.surething_project.core.grpc.comm.LocClaimProto.internal_static_eu_surething_project_core_grpc_SignedLocationClaim_descriptor;
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.comm.SignedLocationClaim getDefaultInstanceForType() {
      return eu.surething_project.core.grpc.comm.SignedLocationClaim.getDefaultInstance();
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.comm.SignedLocationClaim build() {
      eu.surething_project.core.grpc.comm.SignedLocationClaim result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public eu.surething_project.core.grpc.comm.SignedLocationClaim buildPartial() {
      eu.surething_project.core.grpc.comm.SignedLocationClaim result = new eu.surething_project.core.grpc.comm.SignedLocationClaim(this);
      if (claimBuilder_ == null) {
        result.claim_ = claim_;
      } else {
        result.claim_ = claimBuilder_.build();
      }
      if (proverSignatureBuilder_ == null) {
        result.proverSignature_ = proverSignature_;
      } else {
        result.proverSignature_ = proverSignatureBuilder_.build();
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
      if (other instanceof eu.surething_project.core.grpc.comm.SignedLocationClaim) {
        return mergeFrom((eu.surething_project.core.grpc.comm.SignedLocationClaim)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(eu.surething_project.core.grpc.comm.SignedLocationClaim other) {
      if (other == eu.surething_project.core.grpc.comm.SignedLocationClaim.getDefaultInstance()) return this;
      if (other.hasClaim()) {
        mergeClaim(other.getClaim());
      }
      if (other.hasProverSignature()) {
        mergeProverSignature(other.getProverSignature());
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
      eu.surething_project.core.grpc.comm.SignedLocationClaim parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (eu.surething_project.core.grpc.comm.SignedLocationClaim) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private eu.surething_project.core.grpc.comm.LocationClaim claim_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        eu.surething_project.core.grpc.comm.LocationClaim, eu.surething_project.core.grpc.comm.LocationClaim.Builder, eu.surething_project.core.grpc.comm.LocationClaimOrBuilder> claimBuilder_;
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    public boolean hasClaim() {
      return claimBuilder_ != null || claim_ != null;
    }
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    public eu.surething_project.core.grpc.comm.LocationClaim getClaim() {
      if (claimBuilder_ == null) {
        return claim_ == null ? eu.surething_project.core.grpc.comm.LocationClaim.getDefaultInstance() : claim_;
      } else {
        return claimBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    public Builder setClaim(eu.surething_project.core.grpc.comm.LocationClaim value) {
      if (claimBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        claim_ = value;
        onChanged();
      } else {
        claimBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    public Builder setClaim(
        eu.surething_project.core.grpc.comm.LocationClaim.Builder builderForValue) {
      if (claimBuilder_ == null) {
        claim_ = builderForValue.build();
        onChanged();
      } else {
        claimBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    public Builder mergeClaim(eu.surething_project.core.grpc.comm.LocationClaim value) {
      if (claimBuilder_ == null) {
        if (claim_ != null) {
          claim_ =
            eu.surething_project.core.grpc.comm.LocationClaim.newBuilder(claim_).mergeFrom(value).buildPartial();
        } else {
          claim_ = value;
        }
        onChanged();
      } else {
        claimBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    public Builder clearClaim() {
      if (claimBuilder_ == null) {
        claim_ = null;
        onChanged();
      } else {
        claim_ = null;
        claimBuilder_ = null;
      }

      return this;
    }
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    public eu.surething_project.core.grpc.comm.LocationClaim.Builder getClaimBuilder() {
      
      onChanged();
      return getClaimFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    public eu.surething_project.core.grpc.comm.LocationClaimOrBuilder getClaimOrBuilder() {
      if (claimBuilder_ != null) {
        return claimBuilder_.getMessageOrBuilder();
      } else {
        return claim_ == null ?
            eu.surething_project.core.grpc.comm.LocationClaim.getDefaultInstance() : claim_;
      }
    }
    /**
     * <pre>
     * claim
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.LocationClaim claim = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        eu.surething_project.core.grpc.comm.LocationClaim, eu.surething_project.core.grpc.comm.LocationClaim.Builder, eu.surething_project.core.grpc.comm.LocationClaimOrBuilder> 
        getClaimFieldBuilder() {
      if (claimBuilder_ == null) {
        claimBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            eu.surething_project.core.grpc.comm.LocationClaim, eu.surething_project.core.grpc.comm.LocationClaim.Builder, eu.surething_project.core.grpc.comm.LocationClaimOrBuilder>(
                getClaim(),
                getParentForChildren(),
                isClean());
        claim_ = null;
      }
      return claimBuilder_;
    }

    private eu.surething_project.core.grpc.signature.Signature proverSignature_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        eu.surething_project.core.grpc.signature.Signature, eu.surething_project.core.grpc.signature.Signature.Builder, eu.surething_project.core.grpc.signature.SignatureOrBuilder> proverSignatureBuilder_;
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    public boolean hasProverSignature() {
      return proverSignatureBuilder_ != null || proverSignature_ != null;
    }
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    public eu.surething_project.core.grpc.signature.Signature getProverSignature() {
      if (proverSignatureBuilder_ == null) {
        return proverSignature_ == null ? eu.surething_project.core.grpc.signature.Signature.getDefaultInstance() : proverSignature_;
      } else {
        return proverSignatureBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    public Builder setProverSignature(eu.surething_project.core.grpc.signature.Signature value) {
      if (proverSignatureBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        proverSignature_ = value;
        onChanged();
      } else {
        proverSignatureBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    public Builder setProverSignature(
        eu.surething_project.core.grpc.signature.Signature.Builder builderForValue) {
      if (proverSignatureBuilder_ == null) {
        proverSignature_ = builderForValue.build();
        onChanged();
      } else {
        proverSignatureBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    public Builder mergeProverSignature(eu.surething_project.core.grpc.signature.Signature value) {
      if (proverSignatureBuilder_ == null) {
        if (proverSignature_ != null) {
          proverSignature_ =
            eu.surething_project.core.grpc.signature.Signature.newBuilder(proverSignature_).mergeFrom(value).buildPartial();
        } else {
          proverSignature_ = value;
        }
        onChanged();
      } else {
        proverSignatureBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    public Builder clearProverSignature() {
      if (proverSignatureBuilder_ == null) {
        proverSignature_ = null;
        onChanged();
      } else {
        proverSignature_ = null;
        proverSignatureBuilder_ = null;
      }

      return this;
    }
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    public eu.surething_project.core.grpc.signature.Signature.Builder getProverSignatureBuilder() {
      
      onChanged();
      return getProverSignatureFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    public eu.surething_project.core.grpc.signature.SignatureOrBuilder getProverSignatureOrBuilder() {
      if (proverSignatureBuilder_ != null) {
        return proverSignatureBuilder_.getMessageOrBuilder();
      } else {
        return proverSignature_ == null ?
            eu.surething_project.core.grpc.signature.Signature.getDefaultInstance() : proverSignature_;
      }
    }
    /**
     * <pre>
     * prover's signature
     * </pre>
     *
     * <code>.eu.surething_project.core.grpc.Signature proverSignature = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        eu.surething_project.core.grpc.signature.Signature, eu.surething_project.core.grpc.signature.Signature.Builder, eu.surething_project.core.grpc.signature.SignatureOrBuilder> 
        getProverSignatureFieldBuilder() {
      if (proverSignatureBuilder_ == null) {
        proverSignatureBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            eu.surething_project.core.grpc.signature.Signature, eu.surething_project.core.grpc.signature.Signature.Builder, eu.surething_project.core.grpc.signature.SignatureOrBuilder>(
                getProverSignature(),
                getParentForChildren(),
                isClean());
        proverSignature_ = null;
      }
      return proverSignatureBuilder_;
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


    // @@protoc_insertion_point(builder_scope:eu.surething_project.core.grpc.SignedLocationClaim)
  }

  // @@protoc_insertion_point(class_scope:eu.surething_project.core.grpc.SignedLocationClaim)
  private static final eu.surething_project.core.grpc.comm.SignedLocationClaim DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new eu.surething_project.core.grpc.comm.SignedLocationClaim();
  }

  public static eu.surething_project.core.grpc.comm.SignedLocationClaim getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SignedLocationClaim>
      PARSER = new com.google.protobuf.AbstractParser<SignedLocationClaim>() {
    @java.lang.Override
    public SignedLocationClaim parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new SignedLocationClaim(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<SignedLocationClaim> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SignedLocationClaim> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public eu.surething_project.core.grpc.comm.SignedLocationClaim getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

