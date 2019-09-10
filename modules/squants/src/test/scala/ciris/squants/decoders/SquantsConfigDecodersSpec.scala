package ciris.squants.decoders

import ciris.squants._
import ciris.squants.generators.SquantsGenerators
import ciris.{ConfigDecoder, PropertySpec}
import squants._

final class SquantsConfigDecodersSpec extends PropertySpec with SquantsGenerators {
  "SquantsConfigDecoders" when {
    import squants.electro._

    testDimension[AreaElectricChargeDensity]
    testDimension[Capacitance]
    testDimension[Conductivity]
    testDimension[ElectricalConductance]
    testDimension[ElectricalResistance]
    testDimension[ElectricCharge]
    testDimension[ElectricChargeDensity]
    testDimension[ElectricChargeMassRatio]
    testDimension[ElectricCurrent]
    testDimension[ElectricCurrentDensity]
    testDimension[ElectricFieldStrength]
    testDimension[ElectricPotential]
    testDimension[Inductance]
    testDimension[LinearElectricChargeDensity]
    testDimension[MagneticFieldStrength]
    testDimension[MagneticFlux]
    testDimension[MagneticFluxDensity]
    testDimension[Permeability]
    testDimension[Permittivity]
    testDimension[Resistivity]

    import squants.energy._

    testDimension[Energy]
    testDimension[EnergyDensity]
    testDimension[MolarEnergy]
    testDimension[Power]
    testDimension[PowerDensity]
    testDimension[PowerRamp]
    testDimension[SpecificEnergy]

    import squants.information._

    testDimension[DataRate]
    testDimension[Information]

    import squants.mass._

    testDimension[AreaDensity]
    testDimension[ChemicalAmount]
    testDimension[Density]
    testDimension[Mass]
    testDimension[MomentOfInertia]

    import squants.motion._

    testDimension[Acceleration]
    testDimension[AngularAcceleration]
    testDimension[AngularVelocity]
    testDimension[Force]
    testDimension[Jerk]
    testDimension[MassFlow]
    testDimension[Momentum]
    testDimension[Pressure]
    testDimension[PressureChange]
    testDimension[Torque]
    testDimension[Velocity]
    testDimension[VolumeFlow]
    testDimension[Yank]

    import squants.photo._

    testDimension[Illuminance]
    testDimension[Luminance]
    testDimension[LuminousEnergy]
    testDimension[LuminousExposure]
    testDimension[LuminousFlux]
    testDimension[LuminousIntensity]

    import squants.radio._

    testDimension[Irradiance]
    testDimension[Radiance]
    testDimension[RadiantIntensity]
    testDimension[SpectralIntensity]
    testDimension[SpectralIrradiance]
    testDimension[SpectralPower]

    import squants.space._

    testDimension[Angle]
    testDimension[Area]
    testDimension[Length]
    testDimension[SolidAngle]
    testDimension[Volume]

    import squants.thermal._

    testDimension[Temperature]
    testDimension[ThermalCapacity]

    import squants.time._

    testDimension[Frequency]
    testDimension[Time]
  }

  def testDimension[A <: Quantity[A]](
    implicit dimension: Dimension[A],
    decoder: ConfigDecoder[String, A]
  ): Unit = {
    s"reading a ${dimension.name}" should {
      s"successfully read ${dimension.name} values" in {
        forAll(genQuantity[A]) { quantity =>
          readValue[A](quantity.toString) shouldBe Right(quantity)
        }
      }

      s"successfully read optional ${dimension.name} values" in {
        forAll(genQuantity[A]) { quantity =>
          readValue[Option[A]](quantity.toString) shouldBe Right(Some(quantity))
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(dimension.parseString(string).isFailure) {
            readValue[A](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
