package ciris.squants.decoders

import ciris.ConfigDecoder

import scala.util.Try

trait SquantsConfigDecoders {
  import squants.electro._

  implicit val capacitanceConfigDecoder: ConfigDecoder[String, Capacitance] =
    ConfigDecoder.fromTry[String]("Capacitance")(Capacitance.apply)

  implicit val conductivityConfigDecoder: ConfigDecoder[String, Conductivity] =
    ConfigDecoder.fromTry[String]("Conductivity")(Conductivity.apply)

  implicit val electricalConductanceConfigDecoder: ConfigDecoder[String, ElectricalConductance] =
    ConfigDecoder.fromTry[String]("ElectricalConductance")(ElectricalConductance.apply)

  implicit val electricalResistanceConfigDecoder: ConfigDecoder[String, ElectricalResistance] =
    ConfigDecoder.fromTry[String]("ElectricalResistance")(ElectricalResistance.apply)

  implicit val electricChargeConfigDecoder: ConfigDecoder[String, ElectricCharge] =
    ConfigDecoder.fromTry[String]("ElectricCharge")(ElectricCharge.apply)

  implicit val electricCurrentConfigDecoder: ConfigDecoder[String, ElectricCurrent] =
    ConfigDecoder.fromTry[String]("ElectricCurrent")(ElectricCurrent.apply)

  implicit val electricPotentialConfigDecoder: ConfigDecoder[String, ElectricPotential] =
    ConfigDecoder.fromTry[String]("ElectricPotential")(ElectricPotential.apply)

  implicit val inductanceConfigDecoder: ConfigDecoder[String, Inductance] =
    ConfigDecoder.fromTry[String]("Inductance")(Inductance.apply)

  implicit val magneticFluxConfigDecoder: ConfigDecoder[String, MagneticFlux] =
    ConfigDecoder.fromTry[String]("MagneticFlux")(MagneticFlux.apply)

  implicit val magneticFluxDensityConfigDecoder: ConfigDecoder[String, MagneticFluxDensity] =
    ConfigDecoder.fromTry[String]("MagneticFluxDensity")(MagneticFluxDensity.apply)

  implicit val resistivityConfigDecoder: ConfigDecoder[String, Resistivity] =
    ConfigDecoder.fromTry[String]("Resistivity")(Resistivity.apply)

  import squants.energy._

  implicit val energyConfigDecoder: ConfigDecoder[String, Energy] =
    ConfigDecoder.fromTry[String]("Energy")(Energy.apply)

  implicit val energyDensityConfigDecoder: ConfigDecoder[String, EnergyDensity] =
    ConfigDecoder.fromTry[String]("EnergyDensity")(EnergyDensity.apply)

  implicit val powerConfigDecoder: ConfigDecoder[String, Power] =
    ConfigDecoder.fromTry[String]("Power")(Power.apply)

  implicit val powerRampConfigDecoder: ConfigDecoder[String, PowerRamp] =
    ConfigDecoder.fromTry[String]("PowerRamp")(PowerRamp.apply)

  implicit val specificEnergyConfigDecoder: ConfigDecoder[String, SpecificEnergy] =
    ConfigDecoder.fromTry[String]("SpecificEnergy")(SpecificEnergy.apply)

  import squants.information._

  implicit val dataRateConfigDecoder: ConfigDecoder[String, DataRate] =
    ConfigDecoder.fromTry[String]("DataRate")(DataRate.apply)

  implicit val informationConfigDecoder: ConfigDecoder[String, Information] =
    ConfigDecoder.fromTry[String]("Information")(Information.apply)

  import squants.market._

  implicit val moneyDensityConfigDecoder: ConfigDecoder[String, Money] =
    ConfigDecoder.fromTry[String]("Money")(Money.apply)

  import squants.mass._

  implicit val areaDensityConfigDecoder: ConfigDecoder[String, AreaDensity] =
    ConfigDecoder.fromTry[String]("AreaDensity")(AreaDensity.apply)

  implicit val chemicalAmountConfigDecoder: ConfigDecoder[String, ChemicalAmount] =
    ConfigDecoder.fromTry[String]("ChemicalAmount")(ChemicalAmount.apply)

  implicit val densityConfigDecoder: ConfigDecoder[String, Density] =
    ConfigDecoder.fromTry[String]("Density")(Density.apply)

  implicit val massConfigDecoder: ConfigDecoder[String, Mass] =
    ConfigDecoder.fromTry[String]("Mass")(Mass.apply)

  implicit val momentOfInertiaConfigDecoder: ConfigDecoder[String, MomentOfInertia] =
    ConfigDecoder.fromTry[String]("MomentOfInertia")(MomentOfInertia.apply)

  import squants.motion._

  implicit val accelerationConfigDecoder: ConfigDecoder[String, Acceleration] =
    ConfigDecoder.fromTry[String]("Acceleration")(Acceleration.apply)

  implicit val angularAccelerationConfigDecoder: ConfigDecoder[String, AngularAcceleration] =
    ConfigDecoder.fromTry[String]("AngularAcceleration")(AngularAcceleration.apply)

  implicit val angularVelocityConfigDecoder: ConfigDecoder[String, AngularVelocity] =
    ConfigDecoder.fromTry[String]("AngularVelocity")(AngularVelocity.apply)

  implicit val forceConfigDecoder: ConfigDecoder[String, Force] =
    ConfigDecoder.fromTry[String]("Force")(Force.apply)

  implicit val jerkConfigDecoder: ConfigDecoder[String, Jerk] =
    ConfigDecoder.fromTry[String]("Jerk")(Jerk.apply)

  implicit val massFlowConfigDecoder: ConfigDecoder[String, MassFlow] =
    ConfigDecoder.fromTry[String]("MassFlow")(MassFlow.apply)

  implicit val momentumConfigDecoder: ConfigDecoder[String, Momentum] =
    ConfigDecoder.fromTry[String]("Momentum")(Momentum.apply)

  implicit val pressureConfigDecoder: ConfigDecoder[String, Pressure] =
    ConfigDecoder.fromTry[String]("Pressure")(Pressure.apply)

  implicit val pressureChangeConfigDecoder: ConfigDecoder[String, PressureChange] =
    ConfigDecoder.fromTry[String]("PressureChange")(PressureChange.apply)

  implicit val torqueConfigDecoder: ConfigDecoder[String, Torque] =
    ConfigDecoder.fromTry[String]("Torque")(Torque.apply)

  implicit val velocityConfigDecoder: ConfigDecoder[String, Velocity] =
    ConfigDecoder.fromTry[String]("Velocity")(Velocity.apply)

  implicit val volumeFlowConfigDecoder: ConfigDecoder[String, VolumeFlow] =
    ConfigDecoder.fromTry[String]("VolumeFlow")(VolumeFlow.apply)

  implicit val yankConfigDecoder: ConfigDecoder[String, Yank] =
    ConfigDecoder.fromTry[String]("Yank")(Yank.apply)

  import squants.photo._

  implicit val illuminanceConfigDecoder: ConfigDecoder[String, Illuminance] =
    ConfigDecoder.fromTry[String]("Illuminance")(Illuminance.apply)

  implicit val luminanceConfigDecoder: ConfigDecoder[String, Luminance] =
    ConfigDecoder.fromTry[String]("Luminance")(Luminance.apply)

  implicit val luminousEnergyConfigDecoder: ConfigDecoder[String, LuminousEnergy] =
    ConfigDecoder.fromTry[String]("LuminousEnergy")(LuminousEnergy.apply)

  implicit val luminousExposureConfigDecoder: ConfigDecoder[String, LuminousExposure] =
    ConfigDecoder.fromTry[String]("LuminousExposure")(LuminousExposure.apply)

  implicit val luminousFluxConfigDecoder: ConfigDecoder[String, LuminousFlux] =
    ConfigDecoder.fromTry[String]("LuminousFlux")(LuminousFlux.apply)

  implicit val luminousIntensityConfigDecoder: ConfigDecoder[String, LuminousIntensity] =
    ConfigDecoder.fromTry[String]("LuminousIntensity")(LuminousIntensity.apply)

  import squants.radio._

  implicit val irradianceConfigDecoder: ConfigDecoder[String, Irradiance] =
    ConfigDecoder.fromTry[String]("Irradiance")(Irradiance.apply)

  implicit val radianceConfigDecoder: ConfigDecoder[String, Radiance] =
    ConfigDecoder.fromTry[String]("Radiance")(Radiance.apply)

  implicit val radiantIntensityConfigDecoder: ConfigDecoder[String, RadiantIntensity] =
    ConfigDecoder.fromTry[String]("RadiantIntensity")(RadiantIntensity.apply)

  implicit val spectralIntensityConfigDecoder: ConfigDecoder[String, SpectralIntensity] =
    ConfigDecoder.fromTry[String]("SpectralIntensity")(SpectralIntensity.apply)

  implicit val spectralIrradianceConfigDecoder: ConfigDecoder[String, SpectralIrradiance] =
    ConfigDecoder.fromTry[String]("SpectralIrradiance")(SpectralIrradiance.apply)

  implicit val spectralPowerConfigDecoder: ConfigDecoder[String, SpectralPower] =
    ConfigDecoder.fromTry[String]("SpectralPower")(SpectralPower.apply)

  import squants.space._

  implicit val angleConfigDecoder: ConfigDecoder[String, Angle] =
    ConfigDecoder.fromTry[String]("Angle")(Angle.apply)

  implicit val areaConfigDecoder: ConfigDecoder[String, Area] =
    ConfigDecoder.fromTry[String]("Area")(Area.apply)

  implicit val lengthConfigDecoder: ConfigDecoder[String, Length] =
    ConfigDecoder.fromTry[String]("Length")(Length.apply)

  implicit val solidAngleConfigDecoder: ConfigDecoder[String, SolidAngle] =
    ConfigDecoder.fromTry[String]("SolidAngle")(SolidAngle.apply)

  implicit val volumeConfigDecoder: ConfigDecoder[String, Volume] =
    ConfigDecoder.fromTry[String]("Volume")(Volume.apply)

  import squants.thermal._

  // https://github.com/typelevel/squants/issues/261
  implicit val temperatureConfigDecoder: ConfigDecoder[String, Temperature] =
    ConfigDecoder.fromTry[String]("Temperature")(value => Try(Temperature(value)).flatten)

  implicit val thermalCapacityConfigDecoder: ConfigDecoder[String, ThermalCapacity] =
    ConfigDecoder.fromTry[String]("ThermalCapacity")(ThermalCapacity.apply)

  import squants.time._

  implicit val frequencyConfigDecoder: ConfigDecoder[String, Frequency] =
    ConfigDecoder.fromTry[String]("Frequency")(Frequency.apply)

  implicit val timeConfigDecoder: ConfigDecoder[String, Time] =
    ConfigDecoder.fromTry[String]("Time")(Time.apply)
}
