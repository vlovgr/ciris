package ciris.squants.decoders

import ciris.ConfigDecoder
import squants.{Dimension, Quantity}

trait SquantsConfigDecoders {
  private def dimensionConfigDecoder[A <: Quantity[A]](
    dimension: Dimension[A]
  ): ConfigDecoder[String, A] =
    ConfigDecoder.fromTry(dimension.name)(dimension.parseString)

  import squants.electro._

  implicit val areaElectricChargeDensityConfigDecoder: ConfigDecoder[String, AreaElectricChargeDensity] =
    dimensionConfigDecoder(AreaElectricChargeDensity)

  implicit val capacitanceConfigDecoder: ConfigDecoder[String, Capacitance] =
    dimensionConfigDecoder(Capacitance)

  implicit val conductivityConfigDecoder: ConfigDecoder[String, Conductivity] =
    dimensionConfigDecoder(Conductivity)

  implicit val electricalConductanceConfigDecoder: ConfigDecoder[String, ElectricalConductance] =
    dimensionConfigDecoder(ElectricalConductance)

  implicit val electricalResistanceConfigDecoder: ConfigDecoder[String, ElectricalResistance] =
    dimensionConfigDecoder(ElectricalResistance)

  implicit val electricChargeConfigDecoder: ConfigDecoder[String, ElectricCharge] =
    dimensionConfigDecoder(ElectricCharge)

  implicit val electricChargeDensity: ConfigDecoder[String, ElectricChargeDensity] =
    dimensionConfigDecoder(ElectricChargeDensity)

  implicit val electricChargeMassRatio: ConfigDecoder[String, ElectricChargeMassRatio] =
    dimensionConfigDecoder(ElectricChargeMassRatio)

  implicit val electricCurrentConfigDecoder: ConfigDecoder[String, ElectricCurrent] =
    dimensionConfigDecoder(ElectricCurrent)

  implicit val electricCurrentDensity: ConfigDecoder[String, ElectricCurrentDensity] =
    dimensionConfigDecoder(ElectricCurrentDensity)

  implicit val electricFieldStrength: ConfigDecoder[String, ElectricFieldStrength] =
    dimensionConfigDecoder(ElectricFieldStrength)

  implicit val electricPotentialConfigDecoder: ConfigDecoder[String, ElectricPotential] =
    dimensionConfigDecoder(ElectricPotential)

  implicit val inductanceConfigDecoder: ConfigDecoder[String, Inductance] =
    dimensionConfigDecoder(Inductance)

  implicit val linearElectricChargeDensityConfigDecoder: ConfigDecoder[String, LinearElectricChargeDensity] =
    dimensionConfigDecoder(LinearElectricChargeDensity)

  implicit val magneticFieldStrengthConfigDecoder: ConfigDecoder[String, MagneticFieldStrength] =
    dimensionConfigDecoder(MagneticFieldStrength)

  implicit val magneticFluxConfigDecoder: ConfigDecoder[String, MagneticFlux] =
    dimensionConfigDecoder(MagneticFlux)

  implicit val magneticFluxDensityConfigDecoder: ConfigDecoder[String, MagneticFluxDensity] =
    dimensionConfigDecoder(MagneticFluxDensity)

  implicit val permeabilityConfigDecoder: ConfigDecoder[String, Permeability] =
    dimensionConfigDecoder(Permeability)

  implicit val permittivityConfigDecoder: ConfigDecoder[String, Permittivity] =
    dimensionConfigDecoder(Permittivity)

  implicit val resistivityConfigDecoder: ConfigDecoder[String, Resistivity] =
    dimensionConfigDecoder(Resistivity)

  import squants.energy._

  implicit val energyConfigDecoder: ConfigDecoder[String, Energy] =
    dimensionConfigDecoder(Energy)

  implicit val energyDensityConfigDecoder: ConfigDecoder[String, EnergyDensity] =
    dimensionConfigDecoder(EnergyDensity)

  implicit val molarEnergyConfigDecoder: ConfigDecoder[String, MolarEnergy] =
    dimensionConfigDecoder(MolarEnergy)

  implicit val powerConfigDecoder: ConfigDecoder[String, Power] =
    dimensionConfigDecoder(Power)

  implicit val powerDensityConfigDecoder: ConfigDecoder[String, PowerDensity] =
    dimensionConfigDecoder(PowerDensity)

  implicit val powerRampConfigDecoder: ConfigDecoder[String, PowerRamp] =
    dimensionConfigDecoder(PowerRamp)

  implicit val specificEnergyConfigDecoder: ConfigDecoder[String, SpecificEnergy] =
    dimensionConfigDecoder(SpecificEnergy)

  import squants.information._

  implicit val dataRateConfigDecoder: ConfigDecoder[String, DataRate] =
    dimensionConfigDecoder(DataRate)

  implicit val informationConfigDecoder: ConfigDecoder[String, Information] =
    dimensionConfigDecoder(Information)

  import squants.market._

  // https://github.com/typelevel/squants/issues/322
  implicit val moneyDensityConfigDecoder: ConfigDecoder[String, Money] =
    ConfigDecoder.fromTry("Money")(Money.apply)

  import squants.mass._

  implicit val areaDensityConfigDecoder: ConfigDecoder[String, AreaDensity] =
    dimensionConfigDecoder(AreaDensity)

  implicit val chemicalAmountConfigDecoder: ConfigDecoder[String, ChemicalAmount] =
    dimensionConfigDecoder(ChemicalAmount)

  implicit val densityConfigDecoder: ConfigDecoder[String, Density] =
    dimensionConfigDecoder(Density)

  implicit val massConfigDecoder: ConfigDecoder[String, Mass] =
    dimensionConfigDecoder(Mass)

  implicit val momentOfInertiaConfigDecoder: ConfigDecoder[String, MomentOfInertia] =
    dimensionConfigDecoder(MomentOfInertia)

  import squants.motion._

  implicit val accelerationConfigDecoder: ConfigDecoder[String, Acceleration] =
    dimensionConfigDecoder(Acceleration)

  implicit val angularAccelerationConfigDecoder: ConfigDecoder[String, AngularAcceleration] =
    dimensionConfigDecoder(AngularAcceleration)

  implicit val angularVelocityConfigDecoder: ConfigDecoder[String, AngularVelocity] =
    dimensionConfigDecoder(AngularVelocity)

  implicit val forceConfigDecoder: ConfigDecoder[String, Force] =
    dimensionConfigDecoder(Force)

  implicit val jerkConfigDecoder: ConfigDecoder[String, Jerk] =
    dimensionConfigDecoder(Jerk)

  implicit val massFlowConfigDecoder: ConfigDecoder[String, MassFlow] =
    dimensionConfigDecoder(MassFlow)

  implicit val momentumConfigDecoder: ConfigDecoder[String, Momentum] =
    dimensionConfigDecoder(Momentum)

  implicit val pressureConfigDecoder: ConfigDecoder[String, Pressure] =
    dimensionConfigDecoder(Pressure)

  implicit val pressureChangeConfigDecoder: ConfigDecoder[String, PressureChange] =
    dimensionConfigDecoder(PressureChange)

  implicit val torqueConfigDecoder: ConfigDecoder[String, Torque] =
    dimensionConfigDecoder(Torque)

  implicit val velocityConfigDecoder: ConfigDecoder[String, Velocity] =
    dimensionConfigDecoder(Velocity)

  implicit val volumeFlowConfigDecoder: ConfigDecoder[String, VolumeFlow] =
    dimensionConfigDecoder(VolumeFlow)

  implicit val yankConfigDecoder: ConfigDecoder[String, Yank] =
    dimensionConfigDecoder(Yank)

  import squants.photo._

  implicit val illuminanceConfigDecoder: ConfigDecoder[String, Illuminance] =
    dimensionConfigDecoder(Illuminance)

  implicit val luminanceConfigDecoder: ConfigDecoder[String, Luminance] =
    dimensionConfigDecoder(Luminance)

  implicit val luminousEnergyConfigDecoder: ConfigDecoder[String, LuminousEnergy] =
    dimensionConfigDecoder(LuminousEnergy)

  implicit val luminousExposureConfigDecoder: ConfigDecoder[String, LuminousExposure] =
    dimensionConfigDecoder(LuminousExposure)

  implicit val luminousFluxConfigDecoder: ConfigDecoder[String, LuminousFlux] =
    dimensionConfigDecoder(LuminousFlux)

  implicit val luminousIntensityConfigDecoder: ConfigDecoder[String, LuminousIntensity] =
    dimensionConfigDecoder(LuminousIntensity)

  import squants.radio._

  implicit val irradianceConfigDecoder: ConfigDecoder[String, Irradiance] =
    dimensionConfigDecoder(Irradiance)

  implicit val radianceConfigDecoder: ConfigDecoder[String, Radiance] =
    dimensionConfigDecoder(Radiance)

  implicit val radiantIntensityConfigDecoder: ConfigDecoder[String, RadiantIntensity] =
    dimensionConfigDecoder(RadiantIntensity)

  implicit val spectralIntensityConfigDecoder: ConfigDecoder[String, SpectralIntensity] =
    dimensionConfigDecoder(SpectralIntensity)

  implicit val spectralIrradianceConfigDecoder: ConfigDecoder[String, SpectralIrradiance] =
    dimensionConfigDecoder(SpectralIrradiance)

  implicit val spectralPowerConfigDecoder: ConfigDecoder[String, SpectralPower] =
    dimensionConfigDecoder(SpectralPower)

  import squants.space._

  implicit val angleConfigDecoder: ConfigDecoder[String, Angle] =
    dimensionConfigDecoder(Angle)

  implicit val areaConfigDecoder: ConfigDecoder[String, Area] =
    dimensionConfigDecoder(Area)

  implicit val lengthConfigDecoder: ConfigDecoder[String, Length] =
    dimensionConfigDecoder(Length)

  implicit val solidAngleConfigDecoder: ConfigDecoder[String, SolidAngle] =
    dimensionConfigDecoder(SolidAngle)

  implicit val volumeConfigDecoder: ConfigDecoder[String, Volume] =
    dimensionConfigDecoder(Volume)

  import squants.thermal._

  implicit val temperatureConfigDecoder: ConfigDecoder[String, Temperature] =
    dimensionConfigDecoder(Temperature)

  implicit val thermalCapacityConfigDecoder: ConfigDecoder[String, ThermalCapacity] =
    dimensionConfigDecoder(ThermalCapacity)

  import squants.time._

  implicit val frequencyConfigDecoder: ConfigDecoder[String, Frequency] =
    dimensionConfigDecoder(Frequency)

  implicit val timeConfigDecoder: ConfigDecoder[String, Time] =
    dimensionConfigDecoder(Time)
}
