package ciris.decoders

private[ciris] trait ConfigDecoders
    extends CirisConfigDecoders
    with DerivedConfigDecoders
    with DurationConfigDecoders
    with JavaIoConfigDecoders
    with JavaNetConfigDecoders
    with JavaNioCharsetConfigDecoders
    with JavaNioFileConfigDecoders
    with JavaUtilConfigDecoders
    with MathConfigDecoders
    with PrimitiveConfigDecoders
