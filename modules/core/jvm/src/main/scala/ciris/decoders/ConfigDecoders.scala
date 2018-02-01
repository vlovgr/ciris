package ciris.decoders

private[ciris] trait ConfigDecoders
    extends CirisConfigDecoders
    with DerivedConfigDecoders
    with DurationConfigDecoders
    with JavaIoConfigDecoders
    with JavaNetConfigDecoders
    with JavaNioCharsetConfigDecoders
    with JavaNioFileConfigDecoders
    with JavaTimeConfigDecoders
    with JavaUtilConfigDecoders
    with MathConfigDecoders
    with PrimitiveConfigDecoders
