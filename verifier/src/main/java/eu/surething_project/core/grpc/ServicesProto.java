// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: services.proto

package eu.surething_project.core.grpc;

public final class ServicesProto {
  private ServicesProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016services.proto\022\031eu.surething_project.c" +
      "ore\032\017loc_claim.proto\032\021loc_endorse.proto\032" +
      "\016loc_cert.proto2\203\002\n\014EndorseClaim\022z\n\022Send" +
      "ClaimToWitness\022..eu.surething_project.co" +
      "re.SignedLocationClaim\0324.eu.surething_pr" +
      "oject.core.SignedLocationEndorsement\022w\n\033" +
      "SendClaimToWitnessNoSigning\022(.eu.surethi" +
      "ng_project.core.LocationClaim\032..eu.suret" +
      "hing_project.core.LocationEndorsement2\221\002" +
      "\n\014CertifyClaim\022}\n\023SendClaimToVerifier\0224." +
      "eu.surething_project.core.SignedLocation" +
      "Endorsement\032..eu.surething_project.core." +
      "LocationCertificate(\001\022\201\001\n\034SendClaimToVer" +
      "ifierNoSigning\022..eu.surething_project.co" +
      "re.LocationEndorsement\032/.eu.surething_pr" +
      "oject.core.LocationVerification(\001B4\n\036eu." +
      "surething_project.core.grpcB\rServicesPro" +
      "toP\001\210\001\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          eu.surething_project.core.grpc.LocClaimProto.getDescriptor(),
          eu.surething_project.core.grpc.LocEndorseProto.getDescriptor(),
          eu.surething_project.core.grpc.LocCertProto.getDescriptor(),
        }, assigner);
    eu.surething_project.core.grpc.LocClaimProto.getDescriptor();
    eu.surething_project.core.grpc.LocEndorseProto.getDescriptor();
    eu.surething_project.core.grpc.LocCertProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
