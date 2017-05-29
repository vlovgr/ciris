package ciris.squants.readers

import ciris.squants._
import ciris.squants.generators.SquantsGenerators
import ciris.{ConfigReader, PropertySpec}
import squants._

import scala.reflect.ClassTag
import scala.util.Try

final class SquantsConfigReadersSpec extends PropertySpec with SquantsGenerators {
  "SquantsConfigReaders" when {
    import squants.electro._

    testDimension(Capacitance, Capacitance.apply)
    testDimension(Conductivity, Conductivity.apply)
    testDimension(ElectricalConductance, ElectricalConductance.apply)
    testDimension(ElectricalResistance, ElectricalResistance.apply)
    testDimension(ElectricCharge, ElectricCharge.apply)
    testDimension(ElectricCurrent, ElectricCurrent.apply)
    testDimension(ElectricPotential, ElectricPotential.apply)
    testDimension(Inductance, Inductance.apply)
    testDimension(MagneticFlux, MagneticFlux.apply)
    testDimension(MagneticFluxDensity, MagneticFluxDensity.apply)
    testDimension(Resistivity, Resistivity.apply)

    import squants.energy._

    testDimension(Energy, Energy.apply)
    testDimension(EnergyDensity, EnergyDensity.apply)
    testDimension(Power, Power.apply)
    testDimension(PowerRamp, PowerRamp.apply)
    testDimension(SpecificEnergy, SpecificEnergy.apply)

    import squants.information._

    testDimension(DataRate, DataRate.apply)
    testDimension(Information, Information.apply)

    // import squants.market._

    testMoney()

    import squants.mass._

    testDimension(AreaDensity, AreaDensity.apply)
    testDimension(ChemicalAmount, ChemicalAmount.apply)
    testDimension(Density, Density.apply)
    testDimension(Mass, Mass.apply)
    testDimension(MomentOfInertia, MomentOfInertia.apply)

    import squants.motion._

    testDimension(Acceleration, Acceleration.apply)
    testDimension(AngularAcceleration, AngularAcceleration.apply)
    testDimension(AngularVelocity, AngularVelocity.apply)
    testDimension(Force, Force.apply)
    testDimension(Jerk, Jerk.apply)
    testDimension(MassFlow, MassFlow.apply)
    testDimension(Momentum, Momentum.apply)
    testDimension(Pressure, Pressure.apply)
    testDimension(PressureChange, PressureChange.apply)
    testDimension(Torque, Torque.apply)
    testDimension(Velocity, Velocity.apply)
    testDimension(VolumeFlow, VolumeFlow.apply)
    testDimension(Yank, Yank.apply)

    import squants.photo._

    testDimension(Illuminance, Illuminance.apply)
    testDimension(Luminance, Luminance.apply)
    testDimension(LuminousEnergy, LuminousEnergy.apply)
    testDimension(LuminousExposure, LuminousExposure.apply)
    testDimension(LuminousFlux, LuminousFlux.apply)
    testDimension(LuminousIntensity, LuminousIntensity.apply)

    import squants.radio._

    testDimension(Irradiance, Irradiance.apply)
    testDimension(Radiance, Radiance.apply)
    testDimension(RadiantIntensity, RadiantIntensity.apply)
    testDimension(SpectralIntensity, SpectralIntensity.apply)
    testDimension(SpectralIrradiance, SpectralIrradiance.apply)
    testDimension(SpectralPower, SpectralPower.apply)

    import squants.space._

    testDimension(Angle, Angle.apply)
    testDimension(Area, Area.apply)
    testDimension(Length, Length.apply)
    testDimension(SolidAngle, SolidAngle.apply)
    testDimension(Volume, Volume.apply)

    import squants.thermal._

    testDimension(Temperature, Temperature.apply)
    testDimension(ThermalCapacity, ThermalCapacity.apply)

    import squants.time._

    testDimension(Frequency, Frequency.apply)
    testDimension(Time, Time.apply)
  }

  def testDimension[A <: Quantity[A]: ConfigReader: ClassTag](
    dimension: Dimension[A],
    apply: String => Try[A]
  ): Unit = {
    val typeName: String = implicitly[ClassTag[A]].runtimeClass.getSimpleName

    s"reading a $typeName" should {
      s"successfully read $typeName values" in {
        forAll(genQuantity(dimension)) { quantity =>
          readValue[A](quantity.toString) shouldBe Right(quantity)
        }
      }

      s"successfully read optional $typeName values" in {
        forAll(genQuantity(dimension)) { quantity =>
          readValue[Option[A]](quantity.toString) shouldBe Right(Some(quantity))
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(apply(string).isFailure) {
            readValue[A](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }

  def testMoney(): Unit = {
    "reading a Money" should {
      "successfully read Money values" in {
        forAll(genMoney) { money =>
          readValue[Money](money.toString) shouldBe Right(money)
        }
      }

      "successfully read optional Money values" in {
        forAll(genMoney) { money =>
          readValue[Option[Money]](money.toString) shouldBe Right(Some(money))
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(market.Money(string).isFailure) {
            readValue[Money](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
