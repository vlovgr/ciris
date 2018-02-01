package ciris.squants.decoders

import ciris.ConfigDecoder

import scala.util.Try

trait SquantsConfigDecoders {
  import squants.electro._

  implicit val capacitanceConfigDecoder: ConfigDecoder[String, Capacitance] =
    ConfigDecoder.fromTry("Capacitance")(Capacitance.apply)

  implicit val conductivityConfigDecoder: ConfigDecoder[String, Conductivity] =
    ConfigDecoder.fromTry("Conductivity")(Conductivity.apply)

  implicit val electricalConductanceConfigDecoder: ConfigDecoder[String, ElectricalConductance] =
    ConfigDecoder.fromTry("ElectricalConductance")(ElectricalConductance.apply)

  implicit val electricalResistanceConfigDecoder: ConfigDecoder[String, ElectricalResistance] =
    ConfigDecoder.fromTry("ElectricalResistance")(ElectricalResistance.apply)

  implicit val electricChargeConfigDecoder: ConfigDecoder[String, ElectricCharge] =
    ConfigDecoder.fromTry("ElectricCharge")(ElectricCharge.apply)

  implicit val electricCurrentConfigDecoder: ConfigDecoder[String, ElectricCurrent] =
    ConfigDecoder.fromTry("ElectricCurrent")(ElectricCurrent.apply)

  implicit val electricPotentialConfigDecoder: ConfigDecoder[String, ElectricPotential] =
    ConfigDecoder.fromTry("ElectricPotential")(ElectricPotential.apply)

  implicit val inductanceConfigDecoder: ConfigDecoder[String, Inductance] =
    ConfigDecoder.fromTry("Inductance")(Inductance.apply)

  implicit val magneticFluxConfigDecoder: ConfigDecoder[String, MagneticFlux] =
    ConfigDecoder.fromTry("MagneticFlux")(MagneticFlux.apply)

  implicit val magneticFluxDensityConfigDecoder: ConfigDecoder[String, MagneticFluxDensity] =
    ConfigDecoder.fromTry("MagneticFluxDensity")(MagneticFluxDensity.apply)

  implicit val resistivityConfigDecoder: ConfigDecoder[String, Resistivity] =
    ConfigDecoder.fromTry("Resistivity")(Resistivity.apply)

  import squants.energy._

  implicit val energyConfigDecoder: ConfigDecoder[String, Energy] =
    ConfigDecoder.fromTry("Energy")(Energy.apply)

  implicit val energyDensityConfigDecoder: ConfigDecoder[String, EnergyDensity] =
    ConfigDecoder.fromTry("EnergyDensity")(EnergyDensity.apply)

  implicit val powerConfigDecoder: ConfigDecoder[String, Power] =
    ConfigDecoder.fromTry("Power")(Power.apply)

  implicit val powerRampConfigDecoder: ConfigDecoder[String, PowerRamp] =
    ConfigDecoder.fromTry("PowerRamp")(PowerRamp.apply)

  implicit val specificEnergyConfigDecoder: ConfigDecoder[String, SpecificEnergy] =
    ConfigDecoder.fromTry("SpecificEnergy")(SpecificEnergy.apply)

  import squants.information._

  implicit val dataRateConfigDecoder: ConfigDecoder[String, DataRate] =
    ConfigDecoder.fromTry("DataRate")(DataRate.apply)

  implicit val informationConfigDecoder: ConfigDecoder[String, Information] =
    ConfigDecoder.fromTry("Information")(Information.apply)

  import squants.market._

  implicit val moneyDensityConfigDecoder: ConfigDecoder[String, Money] =
    ConfigDecoder.fromTry("Money")(Money.apply)

  import squants.mass._

  implicit val areaDensityConfigDecoder: ConfigDecoder[String, AreaDensity] =
    ConfigDecoder.fromTry("AreaDensity")(AreaDensity.apply)

  implicit val chemicalAmountConfigDecoder: ConfigDecoder[String, ChemicalAmount] =
    ConfigDecoder.fromTry("ChemicalAmount")(ChemicalAmount.apply)

  implicit val densityConfigDecoder: ConfigDecoder[String, Density] =
    ConfigDecoder.fromTry("Density")(Density.apply)

  implicit val massConfigDecoder: ConfigDecoder[String, Mass] =
    ConfigDecoder.fromTry("Mass")(Mass.apply)

  implicit val momentOfInertiaConfigDecoder: ConfigDecoder[String, MomentOfInertia] =
    ConfigDecoder.fromTry("MomentOfInertia")(MomentOfInertia.apply)

  import squants.motion._

  implicit val accelerationConfigDecoder: ConfigDecoder[String, Acceleration] =
    ConfigDecoder.fromTry("Acceleration")(Acceleration.apply)

  implicit val angularAccelerationConfigDecoder: ConfigDecoder[String, AngularAcceleration] =
    ConfigDecoder.fromTry("AngularAcceleration")(AngularAcceleration.apply)

  implicit val angularVelocityConfigDecoder: ConfigDecoder[String, AngularVelocity] =
    ConfigDecoder.fromTry("AngularVelocity")(AngularVelocity.apply)

  implicit val forceConfigDecoder: ConfigDecoder[String, Force] =
    ConfigDecoder.fromTry("Force")(Force.apply)

  implicit val jerkConfigDecoder: ConfigDecoder[String, Jerk] =
    ConfigDecoder.fromTry("Jerk")(Jerk.apply)

  implicit val massFlowConfigDecoder: ConfigDecoder[String, MassFlow] =
    ConfigDecoder.fromTry("MassFlow")(MassFlow.apply)

  implicit val momentumConfigDecoder: ConfigDecoder[String, Momentum] =
    ConfigDecoder.fromTry("Momentum")(Momentum.apply)

  implicit val pressureConfigDecoder: ConfigDecoder[String, Pressure] =
    ConfigDecoder.fromTry("Pressure")(Pressure.apply)

  implicit val pressureChangeConfigDecoder: ConfigDecoder[String, PressureChange] =
    ConfigDecoder.fromTry("PressureChange")(PressureChange.apply)

  implicit val torqueConfigDecoder: ConfigDecoder[String, Torque] =
    ConfigDecoder.fromTry("Torque")(Torque.apply)

  implicit val velocityConfigDecoder: ConfigDecoder[String, Velocity] =
    ConfigDecoder.fromTry("Velocity")(Velocity.apply)

  implicit val volumeFlowConfigDecoder: ConfigDecoder[String, VolumeFlow] =
    ConfigDecoder.fromTry("VolumeFlow")(VolumeFlow.apply)

  implicit val yankConfigDecoder: ConfigDecoder[String, Yank] =
    ConfigDecoder.fromTry("Yank")(Yank.apply)

  import squants.photo._

  implicit val illuminanceConfigDecoder: ConfigDecoder[String, Illuminance] =
    ConfigDecoder.fromTry("Illuminance")(Illuminance.apply)

  implicit val luminanceConfigDecoder: ConfigDecoder[String, Luminance] =
    ConfigDecoder.fromTry("Luminance")(Luminance.apply)

  implicit val luminousEnergyConfigDecoder: ConfigDecoder[String, LuminousEnergy] =
    ConfigDecoder.fromTry("LuminousEnergy")(LuminousEnergy.apply)

  implicit val luminousExposureConfigDecoder: ConfigDecoder[String, LuminousExposure] =
    ConfigDecoder.fromTry("LuminousExposure")(LuminousExposure.apply)

  implicit val luminousFluxConfigDecoder: ConfigDecoder[String, LuminousFlux] =
    ConfigDecoder.fromTry("LuminousFlux")(LuminousFlux.apply)

  implicit val luminousIntensityConfigDecoder: ConfigDecoder[String, LuminousIntensity] =
    ConfigDecoder.fromTry("LuminousIntensity")(LuminousIntensity.apply)

  import squants.radio._

  implicit val irradianceConfigDecoder: ConfigDecoder[String, Irradiance] =
    ConfigDecoder.fromTry("Irradiance")(Irradiance.apply)

  implicit val radianceConfigDecoder: ConfigDecoder[String, Radiance] =
    ConfigDecoder.fromTry("Radiance")(Radiance.apply)

  implicit val radiantIntensityConfigDecoder: ConfigDecoder[String, RadiantIntensity] =
    ConfigDecoder.fromTry("RadiantIntensity")(RadiantIntensity.apply)

  implicit val spectralIntensityConfigDecoder: ConfigDecoder[String, SpectralIntensity] =
    ConfigDecoder.fromTry("SpectralIntensity")(SpectralIntensity.apply)

  implicit val spectralIrradianceConfigDecoder: ConfigDecoder[String, SpectralIrradiance] =
    ConfigDecoder.fromTry("SpectralIrradiance")(SpectralIrradiance.apply)

  implicit val spectralPowerConfigDecoder: ConfigDecoder[String, SpectralPower] =
    ConfigDecoder.fromTry("SpectralPower")(SpectralPower.apply)

  import squants.space._

  implicit val angleConfigDecoder: ConfigDecoder[String, Angle] =
    ConfigDecoder.fromTry("Angle")(Angle.apply)

  implicit val areaConfigDecoder: ConfigDecoder[String, Area] =
    ConfigDecoder.fromTry("Area")(Area.apply)

  implicit val lengthConfigDecoder: ConfigDecoder[String, Length] =
    ConfigDecoder.fromTry("Length")(Length.apply)

  implicit val solidAngleConfigDecoder: ConfigDecoder[String, SolidAngle] =
    ConfigDecoder.fromTry("SolidAngle")(SolidAngle.apply)

  implicit val volumeConfigDecoder: ConfigDecoder[String, Volume] =
    ConfigDecoder.fromTry("Volume")(Volume.apply)

  import squants.thermal._

  // https://github.com/typelevel/squants/issues/261
  implicit val temperatureConfigDecoder: ConfigDecoder[String, Temperature] =
    ConfigDecoder.fromTry("Temperature")(value => Try(Temperature(value)).flatten)

  implicit val thermalCapacityConfigDecoder: ConfigDecoder[String, ThermalCapacity] =
    ConfigDecoder.fromTry("ThermalCapacity")(ThermalCapacity.apply)

  import squants.time._

  implicit val frequencyConfigDecoder: ConfigDecoder[String, Frequency] =
    ConfigDecoder.fromTry("Frequency")(Frequency.apply)

  implicit val timeConfigDecoder: ConfigDecoder[String, Time] =
    ConfigDecoder.fromTry("Time")(Time.apply)
}
