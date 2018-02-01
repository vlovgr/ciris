package ciris.decoders

private[ciris] trait ConfigDecoders
    extends CirisConfigDecoders
    with DerivedConfigDecoders
    with DurationConfigDecoders
    with JavaNioCharsetConfigDecoders
    with JavaUtilConfigDecoders
    with MathConfigDecoders
    with PrimitiveConfigDecoders
