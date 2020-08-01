package io.rukou.routing;

public final class Service {
  private Service() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:io.rukou.routing.Message)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */
    int getHeaderCount();
    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */
    boolean containsHeader(
        String key);
    /**
     * Use {@link #getHeaderMap()} instead.
     */
    @Deprecated
    java.util.Map<String, String>
    getHeader();
    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */
    java.util.Map<String, String>
    getHeaderMap();
    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */

    String getHeaderOrDefault(
        String key,
        String defaultValue);
    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */

    String getHeaderOrThrow(
        String key);

    /**
     * <code>string body = 3;</code>
     * @return The body.
     */
    String getBody();
    /**
     * <code>string body = 3;</code>
     * @return The bytes for body.
     */
    com.google.protobuf.ByteString
        getBodyBytes();
  }
  /**
   * Protobuf type {@code io.rukou.routing.Message}
   */
  public static final class Message extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:io.rukou.routing.Message)
      MessageOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Message.newBuilder() to construct.
    private Message(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Message() {
      body_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new Message();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Message(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
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
            case 18: {
              if (!((mutable_bitField0_ & 0x00000001) != 0)) {
                header_ = com.google.protobuf.MapField.newMapField(
                    HeaderDefaultEntryHolder.defaultEntry);
                mutable_bitField0_ |= 0x00000001;
              }
              com.google.protobuf.MapEntry<String, String>
              header__ = input.readMessage(
                  HeaderDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
              header_.getMutableMap().put(
                  header__.getKey(), header__.getValue());
              break;
            }
            case 26: {
              String s = input.readStringRequireUtf8();

              body_ = s;
              break;
            }
            default: {
              if (!parseUnknownField(
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
      return io.rukou.routing.Service.internal_static_io_rukou_routing_Message_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    @Override
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 2:
          return internalGetHeader();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return io.rukou.routing.Service.internal_static_io_rukou_routing_Message_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              io.rukou.routing.Service.Message.class, io.rukou.routing.Service.Message.Builder.class);
    }

    public static final int HEADER_FIELD_NUMBER = 2;
    private static final class HeaderDefaultEntryHolder {
      static final com.google.protobuf.MapEntry<
          String, String> defaultEntry =
              com.google.protobuf.MapEntry
              .<String, String>newDefaultInstance(
                  io.rukou.routing.Service.internal_static_io_rukou_routing_Message_HeaderEntry_descriptor, 
                  com.google.protobuf.WireFormat.FieldType.STRING,
                  "",
                  com.google.protobuf.WireFormat.FieldType.STRING,
                  "");
    }
    private com.google.protobuf.MapField<
        String, String> header_;
    private com.google.protobuf.MapField<String, String>
    internalGetHeader() {
      if (header_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            HeaderDefaultEntryHolder.defaultEntry);
      }
      return header_;
    }

    public int getHeaderCount() {
      return internalGetHeader().getMap().size();
    }
    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */

    @Override
    public boolean containsHeader(
        String key) {
      if (key == null) { throw new NullPointerException(); }
      return internalGetHeader().getMap().containsKey(key);
    }
    /**
     * Use {@link #getHeaderMap()} instead.
     */
    @Override
    @Deprecated
    public java.util.Map<String, String> getHeader() {
      return getHeaderMap();
    }
    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */
    @Override

    public java.util.Map<String, String> getHeaderMap() {
      return internalGetHeader().getMap();
    }
    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */
    @Override

    public String getHeaderOrDefault(
        String key,
        String defaultValue) {
      if (key == null) { throw new NullPointerException(); }
      java.util.Map<String, String> map =
          internalGetHeader().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <code>map&lt;string, string&gt; header = 2;</code>
     */
    @Override

    public String getHeaderOrThrow(
        String key) {
      if (key == null) { throw new NullPointerException(); }
      java.util.Map<String, String> map =
          internalGetHeader().getMap();
      if (!map.containsKey(key)) {
        throw new IllegalArgumentException();
      }
      return map.get(key);
    }

    public static final int BODY_FIELD_NUMBER = 3;
    private volatile Object body_;
    /**
     * <code>string body = 3;</code>
     * @return The body.
     */
    @Override
    public String getBody() {
      Object ref = body_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        body_ = s;
        return s;
      }
    }
    /**
     * <code>string body = 3;</code>
     * @return The bytes for body.
     */
    @Override
    public com.google.protobuf.ByteString
        getBodyBytes() {
      Object ref = body_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        body_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      com.google.protobuf.GeneratedMessageV3
        .serializeStringMapTo(
          output,
          internalGetHeader(),
          HeaderDefaultEntryHolder.defaultEntry,
          2);
      if (!getBodyBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, body_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      for (java.util.Map.Entry<String, String> entry
           : internalGetHeader().getMap().entrySet()) {
        com.google.protobuf.MapEntry<String, String>
        header__ = HeaderDefaultEntryHolder.defaultEntry.newBuilderForType()
            .setKey(entry.getKey())
            .setValue(entry.getValue())
            .build();
        size += com.google.protobuf.CodedOutputStream
            .computeMessageSize(2, header__);
      }
      if (!getBodyBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, body_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof io.rukou.routing.Service.Message)) {
        return super.equals(obj);
      }
      io.rukou.routing.Service.Message other = (io.rukou.routing.Service.Message) obj;

      if (!internalGetHeader().equals(
          other.internalGetHeader())) return false;
      if (!getBody()
          .equals(other.getBody())) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (!internalGetHeader().getMap().isEmpty()) {
        hash = (37 * hash) + HEADER_FIELD_NUMBER;
        hash = (53 * hash) + internalGetHeader().hashCode();
      }
      hash = (37 * hash) + BODY_FIELD_NUMBER;
      hash = (53 * hash) + getBody().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static io.rukou.routing.Service.Message parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.rukou.routing.Service.Message parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.rukou.routing.Service.Message parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.rukou.routing.Service.Message parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.rukou.routing.Service.Message parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.rukou.routing.Service.Message parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.rukou.routing.Service.Message parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static io.rukou.routing.Service.Message parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static io.rukou.routing.Service.Message parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static io.rukou.routing.Service.Message parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static io.rukou.routing.Service.Message parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static io.rukou.routing.Service.Message parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(io.rukou.routing.Service.Message prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code io.rukou.routing.Message}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:io.rukou.routing.Message)
        io.rukou.routing.Service.MessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return io.rukou.routing.Service.internal_static_io_rukou_routing_Message_descriptor;
      }

      @SuppressWarnings({"rawtypes"})
      protected com.google.protobuf.MapField internalGetMapField(
          int number) {
        switch (number) {
          case 2:
            return internalGetHeader();
          default:
            throw new RuntimeException(
                "Invalid map field number: " + number);
        }
      }
      @SuppressWarnings({"rawtypes"})
      protected com.google.protobuf.MapField internalGetMutableMapField(
          int number) {
        switch (number) {
          case 2:
            return internalGetMutableHeader();
          default:
            throw new RuntimeException(
                "Invalid map field number: " + number);
        }
      }
      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return io.rukou.routing.Service.internal_static_io_rukou_routing_Message_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                io.rukou.routing.Service.Message.class, io.rukou.routing.Service.Message.Builder.class);
      }

      // Construct using io.rukou.routing.Service.Message.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        internalGetMutableHeader().clear();
        body_ = "";

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return io.rukou.routing.Service.internal_static_io_rukou_routing_Message_descriptor;
      }

      @Override
      public io.rukou.routing.Service.Message getDefaultInstanceForType() {
        return io.rukou.routing.Service.Message.getDefaultInstance();
      }

      @Override
      public io.rukou.routing.Service.Message build() {
        io.rukou.routing.Service.Message result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public io.rukou.routing.Service.Message buildPartial() {
        io.rukou.routing.Service.Message result = new io.rukou.routing.Service.Message(this);
        int from_bitField0_ = bitField0_;
        result.header_ = internalGetHeader();
        result.header_.makeImmutable();
        result.body_ = body_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof io.rukou.routing.Service.Message) {
          return mergeFrom((io.rukou.routing.Service.Message)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(io.rukou.routing.Service.Message other) {
        if (other == io.rukou.routing.Service.Message.getDefaultInstance()) return this;
        internalGetMutableHeader().mergeFrom(
            other.internalGetHeader());
        if (!other.getBody().isEmpty()) {
          body_ = other.body_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        io.rukou.routing.Service.Message parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (io.rukou.routing.Service.Message) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.MapField<
          String, String> header_;
      private com.google.protobuf.MapField<String, String>
      internalGetHeader() {
        if (header_ == null) {
          return com.google.protobuf.MapField.emptyMapField(
              HeaderDefaultEntryHolder.defaultEntry);
        }
        return header_;
      }
      private com.google.protobuf.MapField<String, String>
      internalGetMutableHeader() {
        onChanged();;
        if (header_ == null) {
          header_ = com.google.protobuf.MapField.newMapField(
              HeaderDefaultEntryHolder.defaultEntry);
        }
        if (!header_.isMutable()) {
          header_ = header_.copy();
        }
        return header_;
      }

      public int getHeaderCount() {
        return internalGetHeader().getMap().size();
      }
      /**
       * <code>map&lt;string, string&gt; header = 2;</code>
       */

      @Override
      public boolean containsHeader(
          String key) {
        if (key == null) { throw new NullPointerException(); }
        return internalGetHeader().getMap().containsKey(key);
      }
      /**
       * Use {@link #getHeaderMap()} instead.
       */
      @Override
      @Deprecated
      public java.util.Map<String, String> getHeader() {
        return getHeaderMap();
      }
      /**
       * <code>map&lt;string, string&gt; header = 2;</code>
       */
      @Override

      public java.util.Map<String, String> getHeaderMap() {
        return internalGetHeader().getMap();
      }
      /**
       * <code>map&lt;string, string&gt; header = 2;</code>
       */
      @Override

      public String getHeaderOrDefault(
          String key,
          String defaultValue) {
        if (key == null) { throw new NullPointerException(); }
        java.util.Map<String, String> map =
            internalGetHeader().getMap();
        return map.containsKey(key) ? map.get(key) : defaultValue;
      }
      /**
       * <code>map&lt;string, string&gt; header = 2;</code>
       */
      @Override

      public String getHeaderOrThrow(
          String key) {
        if (key == null) { throw new NullPointerException(); }
        java.util.Map<String, String> map =
            internalGetHeader().getMap();
        if (!map.containsKey(key)) {
          throw new IllegalArgumentException();
        }
        return map.get(key);
      }

      public Builder clearHeader() {
        internalGetMutableHeader().getMutableMap()
            .clear();
        return this;
      }
      /**
       * <code>map&lt;string, string&gt; header = 2;</code>
       */

      public Builder removeHeader(
          String key) {
        if (key == null) { throw new NullPointerException(); }
        internalGetMutableHeader().getMutableMap()
            .remove(key);
        return this;
      }
      /**
       * Use alternate mutation accessors instead.
       */
      @Deprecated
      public java.util.Map<String, String>
      getMutableHeader() {
        return internalGetMutableHeader().getMutableMap();
      }
      /**
       * <code>map&lt;string, string&gt; header = 2;</code>
       */
      public Builder putHeader(
          String key,
          String value) {
        if (key == null) { throw new NullPointerException(); }
        if (value == null) { throw new NullPointerException(); }
        internalGetMutableHeader().getMutableMap()
            .put(key, value);
        return this;
      }
      /**
       * <code>map&lt;string, string&gt; header = 2;</code>
       */

      public Builder putAllHeader(
          java.util.Map<String, String> values) {
        internalGetMutableHeader().getMutableMap()
            .putAll(values);
        return this;
      }

      private Object body_ = "";
      /**
       * <code>string body = 3;</code>
       * @return The body.
       */
      public String getBody() {
        Object ref = body_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          body_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string body = 3;</code>
       * @return The bytes for body.
       */
      public com.google.protobuf.ByteString
          getBodyBytes() {
        Object ref = body_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          body_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string body = 3;</code>
       * @param value The body to set.
       * @return This builder for chaining.
       */
      public Builder setBody(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        body_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string body = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearBody() {
        
        body_ = getDefaultInstance().getBody();
        onChanged();
        return this;
      }
      /**
       * <code>string body = 3;</code>
       * @param value The bytes for body to set.
       * @return This builder for chaining.
       */
      public Builder setBodyBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        body_ = value;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:io.rukou.routing.Message)
    }

    // @@protoc_insertion_point(class_scope:io.rukou.routing.Message)
    private static final io.rukou.routing.Service.Message DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new io.rukou.routing.Service.Message();
    }

    public static io.rukou.routing.Service.Message getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Message>
        PARSER = new com.google.protobuf.AbstractParser<Message>() {
      @Override
      public Message parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Message(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Message> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Message> getParserForType() {
      return PARSER;
    }

    @Override
    public io.rukou.routing.Service.Message getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  public interface AckOrBuilder extends
      // @@protoc_insertion_point(interface_extends:io.rukou.routing.Ack)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string status = 1;</code>
     * @return The status.
     */
    String getStatus();
    /**
     * <code>string status = 1;</code>
     * @return The bytes for status.
     */
    com.google.protobuf.ByteString
        getStatusBytes();
  }
  /**
   * Protobuf type {@code io.rukou.routing.Ack}
   */
  public static final class Ack extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:io.rukou.routing.Ack)
      AckOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Ack.newBuilder() to construct.
    private Ack(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Ack() {
      status_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new Ack();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Ack(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
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
              String s = input.readStringRequireUtf8();

              status_ = s;
              break;
            }
            default: {
              if (!parseUnknownField(
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
      return io.rukou.routing.Service.internal_static_io_rukou_routing_Ack_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return io.rukou.routing.Service.internal_static_io_rukou_routing_Ack_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              io.rukou.routing.Service.Ack.class, io.rukou.routing.Service.Ack.Builder.class);
    }

    public static final int STATUS_FIELD_NUMBER = 1;
    private volatile Object status_;
    /**
     * <code>string status = 1;</code>
     * @return The status.
     */
    @Override
    public String getStatus() {
      Object ref = status_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        status_ = s;
        return s;
      }
    }
    /**
     * <code>string status = 1;</code>
     * @return The bytes for status.
     */
    @Override
    public com.google.protobuf.ByteString
        getStatusBytes() {
      Object ref = status_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        status_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getStatusBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, status_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getStatusBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, status_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof io.rukou.routing.Service.Ack)) {
        return super.equals(obj);
      }
      io.rukou.routing.Service.Ack other = (io.rukou.routing.Service.Ack) obj;

      if (!getStatus()
          .equals(other.getStatus())) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + STATUS_FIELD_NUMBER;
      hash = (53 * hash) + getStatus().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static io.rukou.routing.Service.Ack parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.rukou.routing.Service.Ack parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.rukou.routing.Service.Ack parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.rukou.routing.Service.Ack parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.rukou.routing.Service.Ack parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.rukou.routing.Service.Ack parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.rukou.routing.Service.Ack parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static io.rukou.routing.Service.Ack parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static io.rukou.routing.Service.Ack parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static io.rukou.routing.Service.Ack parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static io.rukou.routing.Service.Ack parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static io.rukou.routing.Service.Ack parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(io.rukou.routing.Service.Ack prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code io.rukou.routing.Ack}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:io.rukou.routing.Ack)
        io.rukou.routing.Service.AckOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return io.rukou.routing.Service.internal_static_io_rukou_routing_Ack_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return io.rukou.routing.Service.internal_static_io_rukou_routing_Ack_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                io.rukou.routing.Service.Ack.class, io.rukou.routing.Service.Ack.Builder.class);
      }

      // Construct using io.rukou.routing.Service.Ack.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        status_ = "";

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return io.rukou.routing.Service.internal_static_io_rukou_routing_Ack_descriptor;
      }

      @Override
      public io.rukou.routing.Service.Ack getDefaultInstanceForType() {
        return io.rukou.routing.Service.Ack.getDefaultInstance();
      }

      @Override
      public io.rukou.routing.Service.Ack build() {
        io.rukou.routing.Service.Ack result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public io.rukou.routing.Service.Ack buildPartial() {
        io.rukou.routing.Service.Ack result = new io.rukou.routing.Service.Ack(this);
        result.status_ = status_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof io.rukou.routing.Service.Ack) {
          return mergeFrom((io.rukou.routing.Service.Ack)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(io.rukou.routing.Service.Ack other) {
        if (other == io.rukou.routing.Service.Ack.getDefaultInstance()) return this;
        if (!other.getStatus().isEmpty()) {
          status_ = other.status_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        io.rukou.routing.Service.Ack parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (io.rukou.routing.Service.Ack) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private Object status_ = "";
      /**
       * <code>string status = 1;</code>
       * @return The status.
       */
      public String getStatus() {
        Object ref = status_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          status_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string status = 1;</code>
       * @return The bytes for status.
       */
      public com.google.protobuf.ByteString
          getStatusBytes() {
        Object ref = status_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          status_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string status = 1;</code>
       * @param value The status to set.
       * @return This builder for chaining.
       */
      public Builder setStatus(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        status_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string status = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearStatus() {
        
        status_ = getDefaultInstance().getStatus();
        onChanged();
        return this;
      }
      /**
       * <code>string status = 1;</code>
       * @param value The bytes for status to set.
       * @return This builder for chaining.
       */
      public Builder setStatusBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        status_ = value;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:io.rukou.routing.Ack)
    }

    // @@protoc_insertion_point(class_scope:io.rukou.routing.Ack)
    private static final io.rukou.routing.Service.Ack DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new io.rukou.routing.Service.Ack();
    }

    public static io.rukou.routing.Service.Ack getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Ack>
        PARSER = new com.google.protobuf.AbstractParser<Ack>() {
      @Override
      public Ack parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Ack(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Ack> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Ack> getParserForType() {
      return PARSER;
    }

    @Override
    public io.rukou.routing.Service.Ack getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_io_rukou_routing_Message_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_io_rukou_routing_Message_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_io_rukou_routing_Message_HeaderEntry_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_io_rukou_routing_Message_HeaderEntry_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_io_rukou_routing_Ack_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_io_rukou_routing_Ack_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\rservice.proto\022\020io.rukou.routing\"}\n\007Mes" +
      "sage\0225\n\006header\030\002 \003(\0132%.io.rukou.routing." +
      "Message.HeaderEntry\022\014\n\004body\030\003 \001(\t\032-\n\013Hea" +
      "derEntry\022\013\n\003key\030\001 \001(\t\022\r\n\005value\030\002 \001(\t:\0028\001" +
      "\"\025\n\003Ack\022\016\n\006status\030\001 \001(\t2\225\001\n\016RequestHandl" +
      "er\022B\n\ngetRequest\022\025.io.rukou.routing.Ack\032" +
      "\031.io.rukou.routing.Message\"\0000\001\022?\n\007respon" +
      "d\022\031.io.rukou.routing.Message\032\025.io.rukou." +
      "routing.Ack\"\000(\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_io_rukou_routing_Message_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_io_rukou_routing_Message_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_io_rukou_routing_Message_descriptor,
        new String[] { "Header", "Body", });
    internal_static_io_rukou_routing_Message_HeaderEntry_descriptor =
      internal_static_io_rukou_routing_Message_descriptor.getNestedTypes().get(0);
    internal_static_io_rukou_routing_Message_HeaderEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_io_rukou_routing_Message_HeaderEntry_descriptor,
        new String[] { "Key", "Value", });
    internal_static_io_rukou_routing_Ack_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_io_rukou_routing_Ack_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_io_rukou_routing_Ack_descriptor,
        new String[] { "Status", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
