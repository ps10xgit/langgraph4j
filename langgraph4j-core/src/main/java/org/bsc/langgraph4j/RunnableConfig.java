package org.bsc.langgraph4j;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * A final class representing configuration for a runnable task.
 * This class holds various parameters such as thread ID, checkpoint ID, next node,
 * and stream mode, providing methods to modify these parameters safely
 * without permanently altering the original configuration.
 */
public final class RunnableConfig implements HasMetadata<RunnableConfig.Builder> {
    private final String threadId;
    private final String checkPointId;
    private final String nextNode;
    private final CompiledGraph.StreamMode streamMode;
    private final Map<String,Object> metadata;

    /**
     * Returns the stream mode of the compiled graph.
     *
     * @return {@code StreamMode} representing the current stream mode.
     */
    public CompiledGraph.StreamMode streamMode() {
        return streamMode;
    }
    /**
     * Returns the thread ID as an {@link Optional}.
     *
     * @return the thread ID wrapped in an {@code Optional}, or an empty {@code Optional} if no thread ID is set.
     */
    public Optional<String> threadId() {
        return ofNullable(threadId);
    }
    /**
     * Returns the current {@code checkPointId} wrapped in an {@link Optional}.
     *
     * @return an {@link Optional} containing the {@code checkPointId}, or {@link Optional#empty()} if it is null.
     */
    public Optional<String> checkPointId() {
        return ofNullable(checkPointId);
    }
    /**
     * Returns an {@code Optional} describing the next node in the sequence,
     * or an empty {@code Optional} if there is no such node.
     *
     * @return an {@code Optional} describing the next node, or an empty {@code Optional}
     */
    public Optional<String> nextNode() {
        return ofNullable(nextNode);
    }

    /**
     * Create a new RunnableConfig with the same attributes as this one
     * but with a different {@link CompiledGraph.StreamMode}.
     * @param streamMode the new stream mode
     * @return a new RunnableConfig with the updated stream mode
     */
    public RunnableConfig withStreamMode(CompiledGraph.StreamMode streamMode) {
        if( this.streamMode == streamMode ) {
            return this;
        }

        return RunnableConfig.builder(this)
                .streamMode(streamMode)
                .build();
    }

    /**
     * Updates the checkpoint ID of the configuration.
     *
     * @param checkPointId The new checkpoint ID to set.
     * @return A new instance of {@code RunnableConfig} with the updated checkpoint ID, 
     *         or the current instance if no change is needed.
     */
    public RunnableConfig withCheckPointId( String checkPointId ) {
        if( Objects.equals(this.checkPointId, checkPointId ) ) {
            return this;
        }
        return RunnableConfig.builder(this)
                .checkPointId(checkPointId)
                .build();

    }

    /**
     * return metadata value for key
     *
     * @param key given metadata key
     * @return metadata value for key if any
     */
    @Override
    public Optional<Object> metadata(String key) {
        if( key == null ) {
            return Optional.empty();
        }
        return ofNullable( metadata ).map( m -> m.get(key) );
    }

    /**
     * Creates a new instance of the {@link Builder} class.
     *
     * @return A new {@code Builder} object.
     */
    public static Builder builder() {
        return new Builder();
    }
    /**
     * Creates a new {@code Builder} instance with the specified {@link RunnableConfig}.
     *
     * @param config The configuration for the {@code Builder}.
     * @return A new {@code Builder} instance.
     */
    public static Builder builder( RunnableConfig config ) { return new Builder(config); }

    /**
     * A builder pattern class for constructing {@link RunnableConfig} objects.
     * This class provides a fluent interface to set various properties of a 
     * {@link RunnableConfig} object and then build the final configuration.
     */
    public static class Builder extends HasMetadata.Builder<Builder> {
        private String threadId;
        private String checkPointId;
        private String nextNode;
        private CompiledGraph.StreamMode streamMode = CompiledGraph.StreamMode.VALUES;

        /**
         * Constructs a new instance of the {@link Builder} with default configuration settings.
         * Initializes a new {@link RunnableConfig} object for configuration purposes.
         */
        Builder() {}

        /** Initializes a new instance of the {@code Builder} class with the specified {@link RunnableConfig}.
         * 
         * @param config The configuration to be used for initialization.
         */
        Builder( RunnableConfig config ) {
            super( requireNonNull(config, "config cannot be null!").metadata );
            this.threadId       = config.threadId;
            this.checkPointId   = config.checkPointId;
            this.nextNode       = config.nextNode;
            this.streamMode     = config.streamMode;
        }
        /**
         * Sets the ID of the thread.
         *
         * @param threadId the ID of the thread to set
         * @return a reference to this {@code Builder} object so that method calls can be chained together
         */
        public Builder threadId(String threadId) {
            this.threadId = threadId;
            return this;
        }
        /**
         * Sets the checkpoint ID for the configuration.
         *
         * @param {@code checkPointId} - the ID of the checkpoint to be set
         * @return {@literal this} - a reference to the current `Builder` instance
         */
        public Builder checkPointId(String checkPointId) {
            this.checkPointId = checkPointId;
            return this;
        }
        /**
         * Sets the next node in the configuration and returns this builder for method chaining.
         *
         * @param nextNode The next node to be set.
         * @return This builder instance, allowing for method chaining.
         */
        public Builder nextNode(String nextNode) {
            this.nextNode = nextNode;
            return this;
        }
        /**
         * Sets the stream mode of the configuration.
         *
         * @param streamMode The {@link CompiledGraph.StreamMode} to set.
         * @return A reference to this builder for method chaining.
         */
        public Builder streamMode(CompiledGraph.StreamMode streamMode) {
            this.streamMode = streamMode;
            return this;
        }
        /**
         * Constructs and returns the configured {@code RunnableConfig} object.
         *
         * @return the configured {@code RunnableConfig} object
         */
        public RunnableConfig build() {
            return new RunnableConfig(this);
        }
    }
    /**
     * Creates a new instance of {@code RunnableConfig} as a copy of the provided {@code config}.
     *
     * @param builder The configuration builder.
     */
    private RunnableConfig( Builder builder ) {
        this.threadId       = builder.threadId;
        this.checkPointId   = builder.checkPointId;
        this.nextNode       = builder.nextNode;
        this.streamMode     = builder.streamMode;
        this.metadata       = ofNullable(builder.metadata())
                                .map( Map::copyOf )
                                .orElse(null);
    }

    @Override
    public String toString() {
        return  format("RunnableConfig{ threadId=%s, checkPointId=%s, nextNode=%s, streamMode=%s }" ,
                threadId,
                checkPointId,
                nextNode,
                streamMode
                );
    }

}