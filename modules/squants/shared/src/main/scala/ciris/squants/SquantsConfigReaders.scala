package ciris.squants

import ciris.ConfigReader
import ciris.ConfigReader.fromTry

trait SquantsConfigReaders {
  import _root_.squants.electro._

  implicit val capacitanceConfigReader: ConfigReader[Capacitance] =
    fromTry("Capacitance")(Capacitance.apply)

  implicit val conductivityConfigReader: ConfigReader[Conductivity] =
    fromTry("Conductivity")(Conductivity.apply)

  implicit val electricalConductanceConfigReader: ConfigReader[ElectricalConductance] =
    fromTry("ElectricalConductance")(ElectricalConductance.apply)

  implicit val electricalResistanceConfigReader: ConfigReader[ElectricalResistance] =
    fromTry("ElectricalResistance")(ElectricalResistance.apply)

  implicit val electricChargeConfigReader: ConfigReader[ElectricCharge] =
    fromTry("ElectricCharge")(ElectricCharge.apply)

  implicit val electricCurrentConfigReader: ConfigReader[ElectricCurrent] =
    fromTry("ElectricCurrent")(ElectricCurrent.apply)

  implicit val electricPotentialConfigReader: ConfigReader[ElectricPotential] =
    fromTry("ElectricPotential")(ElectricPotential.apply)

  implicit val inductanceConfigReader: ConfigReader[Inductance] =
    fromTry("Inductance")(Inductance.apply)

  implicit val magneticFluxConfigReader: ConfigReader[MagneticFlux] =
    fromTry("MagneticFlux")(MagneticFlux.apply)

  implicit val magneticFluxDensityConfigReader: ConfigReader[MagneticFluxDensity] =
    fromTry("MagneticFluxDensity")(MagneticFluxDensity.apply)

  implicit val resistivityConfigReader: ConfigReader[Resistivity] =
    fromTry("Resistivity")(Resistivity.apply)

  import _root_.squants.energy._

  implicit val energyConfigReader: ConfigReader[Energy] =
    fromTry("Energy")(Energy.apply)

  implicit val energyDensityConfigReader: ConfigReader[EnergyDensity] =
    fromTry("EnergyDensity")(EnergyDensity.apply)

  implicit val powerConfigReader: ConfigReader[Power] =
    fromTry("Power")(Power.apply)

  implicit val powerRampConfigReader: ConfigReader[PowerRamp] =
    fromTry("PowerRamp")(PowerRamp.apply)

  implicit val SpecificEnergyConfigReader: ConfigReader[SpecificEnergy] =
    fromTry("SpecificEnergy")(SpecificEnergy.apply)

  import _root_.squants.information._

  implicit val dataRateConfigReader: ConfigReader[DataRate] =
    fromTry("DataRate")(DataRate.apply)

  implicit val informationConfigReader: ConfigReader[Information] =
    fromTry("Information")(Information.apply)

  import _root_.squants.market._

  implicit val moneyDensityConfigReader: ConfigReader[Money] =
    fromTry("Money")(Money.apply)

  import _root_.squants.mass._

  implicit val areaDensityConfigReader: ConfigReader[AreaDensity] =
    fromTry("AreaDensity")(AreaDensity.apply)

  implicit val chemicalAmountConfigReader: ConfigReader[ChemicalAmount] =
    fromTry("ChemicalAmount")(ChemicalAmount.apply)

  implicit val densityConfigReader: ConfigReader[Density] =
    fromTry("Density")(Density.apply)

  implicit val massConfigReader: ConfigReader[Mass] =
    fromTry("Mass")(Mass.apply)

  import _root_.squants.motion._

  implicit val accelerationConfigReader: ConfigReader[Acceleration] =
    fromTry("Acceleration")(Acceleration.apply)

  implicit val angularVelocityConfigReader: ConfigReader[AngularVelocity] =
    fromTry("AngularVelocity")(AngularVelocity.apply)

  implicit val forceConfigReader: ConfigReader[Force] =
    fromTry("Force")(Force.apply)

  implicit val jerkConfigReader: ConfigReader[Jerk] =
    fromTry("Jerk")(Jerk.apply)

  implicit val massFlowConfigReader: ConfigReader[MassFlow] =
    fromTry("MassFlow")(MassFlow.apply)

  implicit val momentumConfigReader: ConfigReader[Momentum] =
    fromTry("Momentum")(Momentum.apply)

  implicit val pressureConfigReader: ConfigReader[Pressure] =
    fromTry("Pressure")(Pressure.apply)

  implicit val pressureChangeConfigReader: ConfigReader[PressureChange] =
    fromTry("PressureChange")(PressureChange.apply)

  implicit val velocityConfigReader: ConfigReader[Velocity] =
    fromTry("Velocity")(Velocity.apply)

  implicit val volumeFlowConfigReader: ConfigReader[VolumeFlow] =
    fromTry("VolumeFlow")(VolumeFlow.apply)

  implicit val yankConfigReader: ConfigReader[Yank] =
    fromTry("Yank")(Yank.apply)

  import _root_.squants.photo._

  implicit val illuminanceConfigReader: ConfigReader[Illuminance] =
    fromTry("Illuminance")(Illuminance.apply)

  implicit val luminanceConfigReader: ConfigReader[Luminance] =
    fromTry("Luminance")(Luminance.apply)

  implicit val luminousEnergyConfigReader: ConfigReader[LuminousEnergy] =
    fromTry("LuminousEnergy")(LuminousEnergy.apply)

  implicit val luminousExposureConfigReader: ConfigReader[LuminousExposure] =
    fromTry("LuminousExposure")(LuminousExposure.apply)

  implicit val luminousFluxConfigReader: ConfigReader[LuminousFlux] =
    fromTry("LuminousFlux")(LuminousFlux.apply)

  implicit val luminousIntensityConfigReader: ConfigReader[LuminousIntensity] =
    fromTry("LuminousIntensity")(LuminousIntensity.apply)

  import _root_.squants.radio._

  implicit val irradianceConfigReader: ConfigReader[Irradiance] =
    fromTry("Irradiance")(Irradiance.apply)

  implicit val radianceConfigReader: ConfigReader[Radiance] =
    fromTry("Radiance")(Radiance.apply)

  implicit val radiantIntensityConfigReader: ConfigReader[RadiantIntensity] =
    fromTry("RadiantIntensity")(RadiantIntensity.apply)

  implicit val spectralIntensityConfigReader: ConfigReader[SpectralIntensity] =
    fromTry("SpectralIntensity")(SpectralIntensity.apply)

  implicit val spectralIrradianceConfigReader: ConfigReader[SpectralIrradiance] =
    fromTry("SpectralIrradiance")(SpectralIrradiance.apply)

  implicit val spectralPowerConfigReader: ConfigReader[SpectralPower] =
    fromTry("SpectralPower")(SpectralPower.apply)

  import _root_.squants.space._

  implicit val angleConfigReader: ConfigReader[Angle] =
    fromTry("Angle")(Angle.apply)

  implicit val areaConfigReader: ConfigReader[Area] =
    fromTry("Area")(Area.apply)

  implicit val lengthConfigReader: ConfigReader[Length] =
    fromTry("Length")(Length.apply)

  implicit val solidAngleConfigReader: ConfigReader[SolidAngle] =
    fromTry("SolidAngle")(SolidAngle.apply)

  implicit val volumeConfigReader: ConfigReader[Volume] =
    fromTry("Volume")(Volume.apply)

  import _root_.squants.thermal._

  implicit val temperatureConfigReader: ConfigReader[Temperature] =
    fromTry("Temperature")(Temperature.apply)

  implicit val thermalCapacityConfigReader: ConfigReader[ThermalCapacity] =
    fromTry("ThermalCapacity")(ThermalCapacity.apply)

  import _root_.squants.time._

  implicit val frequencyConfigReader: ConfigReader[Frequency] =
    fromTry("Frequency")(Frequency.apply)

  implicit val timeConfigReader: ConfigReader[Time] =
    fromTry("Time")(Time.apply)
}
