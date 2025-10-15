package com.joel4848.visibleghosts.client;

import net.minecraft.client.render.VertexConsumer;

public class VertexConsumerWrapper implements VertexConsumer {

    private final VertexConsumer delegate;
    private final float alpha;

    public VertexConsumerWrapper(VertexConsumer delegate, float alpha) {
        this.delegate = delegate;
        this.alpha = alpha;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alphaOriginal) {
        int newAlpha = Math.round(alphaOriginal * alpha);
        delegate.color(red, green, blue, newAlpha);
        return this;
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
        return delegate.vertex(x, y, z);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        return delegate.texture(u, v);
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        return delegate.overlay(u, v);
    }

    @Override
    public VertexConsumer light(int u, int v) {
        return delegate.light(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return delegate.normal(x, y, z);
    }
}
