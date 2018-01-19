package ciris.squants.decoders

import ciris.ConfigDecoder
import ciris.ConfigDecoder.fromTry

import scala.util.Try

trait SquantsConfigDecoders {
  import squants.electro._

  implicit val capacitanceConfigDecoder: ConfigDecoder[Capacitance] =
    fromTry("Capacitance")(Capacitance.apply)

  implicit val conductivityConfigDecoder: ConfigDecoder[Conductivity] =
    fromTry("Conductivity")(Conductivity.apply)

  implicit val electricalConductanceConfigDecoder: ConfigDecoder[ElectricalConductance] =
    fromTry("ElectricalConductance")(ElectricalConductance.apply)

  implicit val electricalResistanceConfigDecoder: ConfigDecoder[ElectricalResistance] =
    fromTry("ElectricalResistance")(ElectricalResistance.apply)

  implicit val electricChargeConfigDecoder: ConfigDecoder[ElectricCharge] =
    fromTry("ElectricCharge")(ElectricCharge.apply)

  implicit val electricCurrentConfigDecoder: ConfigDecoder[ElectricCurrent] =
    fromTry("ElectricCurrent")(ElectricCurrent.apply)

  implicit val electricPotentialConfigDecoder: ConfigDecoder[ElectricPotential] =
    fromTry("ElectricPotential")(ElectricPotential.apply)

  implicit val inductanceConfigDecoder: ConfigDecoder[Inductance] =
    fromTry("Inductance")(Inductance.apply)

  implicit val magneticFluxConfigDecoder: ConfigDecoder[MagneticFlux] =
    fromTry("MagneticFlux")(MagneticFlux.apply)

  implicit val magneticFluxDensityConfigDecoder: ConfigDecoder[MagneticFluxDensity] =
    fromTry("MagneticFluxDensity")(MagneticFluxDensity.apply)

  implicit val resistivityConfigDecoder: ConfigDecoder[Resistivity] =
    fromTry("Resistivity")(Resistivity.apply)

  import squants.energy._

  implicit val energyConfigDecoder: ConfigDecoder[Energy] =
    fromTry("Energy")(Energy.apply)

  implicit val energyDensityConfigDecoder: ConfigDecoder[EnergyDensity] =
    fromTry("EnergyDensity")(EnergyDensity.apply)

  implicit val powerConfigDecoder: ConfigDecoder[Power] =
    fromTry("Power")(Power.apply)

  implicit val powerRampConfigDecoder: ConfigDecoder[PowerRamp] =
    fromTry("PowerRamp")(PowerRamp.apply)

  implicit val specificEnergyConfigDecoder: ConfigDecoder[SpecificEnergy] =
    fromTry("SpecificEnergy")(SpecificEnergy.apply)

  import squants.information._

  implicit val dataRateConfigDecoder: ConfigDecoder[DataRate] =
    fromTry("DataRate")(DataRate.apply)

  implicit val informationConfigDecoder: ConfigDecoder[Information] =
    fromTry("Information")(Information.apply)

  import squants.market._

  implicit val moneyDensityConfigDecoder: ConfigDecoder[Money] =
    fromTry("Money")(Money.apply)

  import squants.mass._

  implicit val areaDensityConfigDecoder: ConfigDecoder[AreaDensity] =
    fromTry("AreaDensity")(AreaDensity.apply)

  implicit val chemicalAmountConfigDecoder: ConfigDecoder[ChemicalAmount] =
    fromTry("ChemicalAmount")(ChemicalAmount.apply)

  implicit val densityConfigDecoder: ConfigDecoder[Density] =
    fromTry("Density")(Density.apply)

  implicit val massConfigDecoder: ConfigDecoder[Mass] =
    fromTry("Mass")(Mass.apply)

  implicit val momentOfInertiaConfigDecoder: ConfigDecoder[MomentOfInertia] =
    fromTry("MomentOfInertia")(MomentOfInertia.apply)

  import squants.motion._

  implicit val accelerationConfigDecoder: ConfigDecoder[Acceleration] =
    fromTry("Acceleration")(Acceleration.apply)

  implicit val angularAccelerationConfigDecoder: ConfigDecoder[AngularAcceleration] =
    fromTry("AngularAcceleration")(AngularAcceleration.apply)

  implicit val angularVelocityConfigDecoder: ConfigDecoder[AngularVelocity] =
    fromTry("AngularVelocity")(AngularVelocity.apply)

  implicit val forceConfigDecoder: ConfigDecoder[Force] =
    fromTry("Force")(Force.apply)

  implicit val jerkConfigDecoder: ConfigDecoder[Jerk] =
    fromTry("Jerk")(Jerk.apply)

  implicit val massFlowConfigDecoder: ConfigDecoder[MassFlow] =
    fromTry("MassFlow")(MassFlow.apply)

  implicit val momentumConfigDecoder: ConfigDecoder[Momentum] =
    fromTry("Momentum")(Momentum.apply)

  implicit val pressureConfigDecoder: ConfigDecoder[Pressure] =
    fromTry("Pressure")(Pressure.apply)

  implicit val pressureChangeConfigDecoder: ConfigDecoder[PressureChange] =
    fromTry("PressureChange")(PressureChange.apply)

  implicit val torqueConfigDecoder: ConfigDecoder[Torque] =
    fromTry("Torque")(Torque.apply)

  implicit val velocityConfigDecoder: ConfigDecoder[Velocity] =
    fromTry("Velocity")(Velocity.apply)

  implicit val volumeFlowConfigDecoder: ConfigDecoder[VolumeFlow] =
    fromTry("VolumeFlow")(VolumeFlow.apply)

  implicit val yankConfigDecoder: ConfigDecoder[Yank] =
    fromTry("Yank")(Yank.apply)

  import squants.photo._

  implicit val illuminanceConfigDecoder: ConfigDecoder[Illuminance] =
    fromTry("Illuminance")(Illuminance.apply)

  implicit val luminanceConfigDecoder: ConfigDecoder[Luminance] =
    fromTry("Luminance")(Luminance.apply)

  implicit val luminousEnergyConfigDecoder: ConfigDecoder[LuminousEnergy] =
    fromTry("LuminousEnergy")(LuminousEnergy.apply)

  implicit val luminousExposureConfigDecoder: ConfigDecoder[LuminousExposure] =
    fromTry("LuminousExposure")(LuminousExposure.apply)

  implicit val luminousFluxConfigDecoder: ConfigDecoder[LuminousFlux] =
    fromTry("LuminousFlux")(LuminousFlux.apply)

  implicit val luminousIntensityConfigDecoder: ConfigDecoder[LuminousIntensity] =
    fromTry("LuminousIntensity")(LuminousIntensity.apply)

  import squants.radio._

  implicit val irradianceConfigDecoder: ConfigDecoder[Irradiance] =
    fromTry("Irradiance")(Irradiance.apply)

  implicit val radianceConfigDecoder: ConfigDecoder[Radiance] =
    fromTry("Radiance")(Radiance.apply)

  implicit val radiantIntensityConfigDecoder: ConfigDecoder[RadiantIntensity] =
    fromTry("RadiantIntensity")(RadiantIntensity.apply)

  implicit val spectralIntensityConfigDecoder: ConfigDecoder[SpectralIntensity] =
    fromTry("SpectralIntensity")(SpectralIntensity.apply)

  implicit val spectralIrradianceConfigDecoder: ConfigDecoder[SpectralIrradiance] =
    fromTry("SpectralIrradiance")(SpectralIrradiance.apply)

  implicit val spectralPowerConfigDecoder: ConfigDecoder[SpectralPower] =
    fromTry("SpectralPower")(SpectralPower.apply)

  import squants.space._

  implicit val angleConfigDecoder: ConfigDecoder[Angle] =
    fromTry("Angle")(Angle.apply)

  implicit val areaConfigDecoder: ConfigDecoder[Area] =
    fromTry("Area")(Area.apply)

  implicit val lengthConfigDecoder: ConfigDecoder[Length] =
    fromTry("Length")(Length.apply)

  implicit val solidAngleConfigDecoder: ConfigDecoder[SolidAngle] =
    fromTry("SolidAngle")(SolidAngle.apply)

  implicit val volumeConfigDecoder: ConfigDecoder[Volume] =
    fromTry("Volume")(Volume.apply)

  import squants.thermal._

  // https://github.com/typelevel/squants/issues/261
  implicit val temperatureConfigDecoder: ConfigDecoder[Temperature] =
    fromTry("Temperature")(value => Try(Temperature(value)).flatten)

  implicit val thermalCapacityConfigDecoder: ConfigDecoder[ThermalCapacity] =
    fromTry("ThermalCapacity")(ThermalCapacity.apply)

  import squants.time._

  implicit val frequencyConfigDecoder: ConfigDecoder[Frequency] =
    fromTry("Frequency")(Frequency.apply)

  implicit val timeConfigDecoder: ConfigDecoder[Time] =
    fromTry("Time")(Time.apply)
}
