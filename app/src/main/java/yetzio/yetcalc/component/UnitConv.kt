package yetzio.yetcalc.component

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.Double

class UnitConv{
    class Length{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> FemtoToMetre(value)
                    1 -> PicoToMetre(value)
                    2 -> NanoToMetre(value)
                    3 -> MicroToMetre(value)
                    4 -> MilliToMetre(value)
                    5 -> CentiToMetre(value)
                    6 -> DeciToMetre(value)
                    7 -> value
                    8 -> DecaToMetre(value)
                    9 -> HectoToMetre(value)
                    10 -> KiloToMetre(value)
                    11 -> InchToMetre(value)
                    12 -> FeetToMetre(value)
                    13 -> YardsToMetre(value)
                    14 -> MilesToMetre(value)
                    15 -> NMilesToMetre(value)
                    16 -> MegaToMetre(value)
                    17 -> GigaToMetre(value)
                    18 -> TeraToMetre(value)
                    19 -> PetaToMetre(value)
                    20 -> FlgToMetre(value)
                    else -> {
                        value
                    }
                }

                temp = when(to) {
                    0 -> MetreToFemto(temp)
                    1 -> MetreToPico(temp)
                    2 -> MetreToNano(temp)
                    3 -> MetreToMicro(temp)
                    4 -> MetreToMilli(temp)
                    5 -> MetreToCenti(temp)
                    6 -> MetreToDeci(temp)
                    8 -> MetreToDeca(temp)
                    9 -> MetreToHecto(temp)
                    10 -> MetreToKilo(temp)
                    11 -> MetreToInch(temp)
                    12 -> MetreToFeet(temp)
                    13 -> MetreToYards(temp)
                    14 -> MetreToMiles(temp)
                    15 -> MetreToNMiles(temp)
                    16 -> MetreToMega(temp)
                    17 -> MetreToGiga(temp)
                    18 -> MetreToTera(temp)
                    19 -> MetreToPeta(temp)
                    20 -> MetreToFlg(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            // Order 1

            fun FemtoToMetre(femto: Double): Double{
                return femto / BigDecimal("1000000000000000").toDouble()
            }

            fun PicoToMetre(pico: Double): Double{
                return pico / BigDecimal("1000000000000").toDouble()
            }

            fun NanoToMetre(nano: Double): Double {
                return nano / BigDecimal("1000000000").toDouble()
            }

            fun MicroToMetre(micro: Double): Double{
                return micro / BigDecimal("1000000").toDouble()
            }

            fun MilliToMetre(milli: Double): Double{
                return milli / BigDecimal("1000").toDouble()
            }

            fun CentiToMetre(centi: Double): Double{
                return centi / BigDecimal("100").toDouble()
            }

            fun DeciToMetre(deci: Double): Double{
                return deci / BigDecimal("10").toDouble()
            }

            fun DecaToMetre(deca: Double): Double{
                return deca * BigDecimal("10").toDouble()
            }

            fun HectoToMetre(hecto: Double): Double{
                return hecto * BigDecimal("100").toDouble()
            }

            fun KiloToMetre(kilo: Double): Double{
                return kilo * BigDecimal("1000").toDouble()
            }

            fun InchToMetre(inch: Double): Double{
                return inch * BigDecimal("0.0254").toDouble()
            }

            fun FeetToMetre(feet: Double): Double{
                return feet * BigDecimal("0.3048").toDouble()
            }

            fun YardsToMetre(yard: Double): Double{
                return yard * BigDecimal("0.9144").toDouble()
            }

            fun MilesToMetre(mile: Double): Double{
                return mile * BigDecimal("1609.344").toDouble()
            }

            fun NMilesToMetre(nmile: Double): Double{
                return nmile * BigDecimal("1852").toDouble()
            }

            fun MegaToMetre(mega: Double): Double{
                return mega * BigDecimal("1000000").toDouble()
            }

            fun GigaToMetre(giga: Double): Double{
                return giga * BigDecimal("1000000000").toDouble()
            }

            fun TeraToMetre(tera: Double): Double{
                return tera * BigDecimal("1000000000000").toDouble()
            }

            fun PetaToMetre(peta: Double): Double{
                return peta * BigDecimal("1000000000000000").toDouble()
            }

            fun FlgToMetre(flg: Double): Double{
                return flg * BigDecimal("201.168").toDouble()
            }

            // Order 2 - Meter to others
            fun MetreToFemto(metre: Double): Double{
                return metre * BigDecimal("1000000000000000").toDouble()
            }

            fun MetreToPico(metre: Double): Double{
                return metre * BigDecimal("1000000000000").toDouble()
            }

            fun MetreToNano(metre: Double): Double{
                return metre * BigDecimal("1000000000").toDouble()
            }

            fun MetreToMicro(metre: Double): Double{
                return metre * BigDecimal("1000000").toDouble()
            }

            fun MetreToMilli(metre: Double): Double{
                return metre * BigDecimal("1000").toDouble()
            }

            fun MetreToCenti(metre: Double): Double{
                return metre * BigDecimal("100").toDouble()
            }

            fun MetreToDeci(metre: Double): Double{
                return metre * BigDecimal("10").toDouble()
            }

            fun MetreToDeca(metre: Double): Double{
                return metre / BigDecimal("10").toDouble()
            }

            fun MetreToHecto(metre: Double): Double{
                return metre / BigDecimal("100").toDouble()
            }

            fun MetreToKilo(metre: Double): Double{
                return metre / BigDecimal("1000").toDouble()
            }

            fun MetreToInch(metre: Double): Double{
                return BigDecimal.valueOf(metre / 0.0254).setScale(4, RoundingMode.HALF_UP).toDouble()
            }

            fun MetreToFeet(metre: Double): Double{
                return BigDecimal.valueOf(metre / 0.3048).setScale(4, RoundingMode.HALF_UP).toDouble()
            }

            fun MetreToYards(metre: Double): Double{
                return BigDecimal.valueOf(metre / 0.9144).setScale(4, RoundingMode.HALF_UP).toDouble()
            }

            fun MetreToMiles(metre: Double): Double{
                return metre / BigDecimal("1609.344").toDouble()
            }

            fun MetreToNMiles(metre: Double): Double{
                return metre / BigDecimal("1852").toDouble()
            }

            fun MetreToMega(metre: Double): Double{
                return metre / BigDecimal("1000000").toDouble()
            }

            fun MetreToGiga(metre: Double): Double{
                return metre / BigDecimal("1000000000").toDouble()
            }

            fun MetreToTera(metre: Double): Double{
                return metre / BigDecimal("1000000000000").toDouble()
            }

            fun MetreToPeta(metre: Double): Double{
                return metre / BigDecimal("1000000000000000").toDouble()
            }

            fun MetreToFlg(metre: Double): Double{
                return metre / BigDecimal("201.168").toDouble()
            }

        }
    }

    class Volume{
        companion object{
            var from = 0
            var to = 0
            var microQt = false

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                if(from == 0)
                    microQt = true
                else
                    microQt = false

                var temp = when(from){
                    0 -> MicroLToCubicMetre(value)
                    1 -> DropToCubicMetre(value)
                    2 -> MilliLToCubicMetre(value)
                    3 -> CCentiToCubicMetre(value)
                    4 -> DeciLToCubicMetre(value)
                    5 -> LitreToCubicMetre(value)
                    6 -> value
                    7 -> CupUSToCubicMetre(value)
                    8 -> CupUKToCubicMetre(value)
                    9 -> PintsUSToCubicMetre(value)
                    10 -> PintsUKToCubicMetre(value)
                    11 -> QuartsUSToCubicMetre(value)
                    12 -> QuartsUKToCubicMetre(value)
                    13 -> GallonsUSToCubicMetre(value)
                    14 -> GallonsUKToCubicMetre(value)
                    15 -> BarrelsDryUSToCubicMetre(value)
                    16 -> BarrelsLiquidUSToCubicMetre(value)
                    17 -> BarrelsFedUSToCubicMetre(value)
                    18 -> BarrelsOilUSToCubicMetre(value)
                    19 -> BarrelsUKToCubicMetre(value)
                    20 -> CInchesToCubicMetre(value)
                    21 -> CFeetToCubicMetre(value)
                    22 -> CYardsToCubicMetre(value)
                    23 -> TeaspoonsUSToCubicMetre(value)
                    24 -> TeaspoonsUKToCubicMetre(value)
                    25 -> TablespoonsUSToCubicMetre(value)
                    26 -> TablespoonsUKToCubicMetre(value)
                    27 -> FluidOuncesUSToCubicMetre(value)
                    28 -> FluidOuncesUKToCubicMetre(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> CubicMetreToMicroL(temp)
                    1 -> CubicMetreToDrop(temp)
                    2 -> CubicMetreToMilliL(temp)
                    3 -> CubicMetreToCCenti(temp)
                    4 -> CubicMetreToDeciL(temp)
                    5 -> CubicMetreToLitre(temp)
                    7 -> CubicMetreToCupUS(temp)
                    8 -> CubicMetreToCupUK(temp)
                    9 -> CubicMetreToPintsUS(temp)
                    10 -> CubicMetreToPintsUK(temp)
                    11 -> CubicMetreToQuartsUS(temp)
                    12 -> CubicMetreToQuartsUK(temp)
                    13 -> CubicMetreToGallonsUS(temp)
                    14 -> CubicMetreToGallonsUK(temp)
                    15 -> CubicMetreToBarrelsDryUS(temp)
                    16 -> CubicMetreToBarrelsLiquidUS(temp)
                    17 -> CubicMetreToBarrelsFedUS(temp)
                    18 -> CubicMetreToBarrelsOilUS(temp)
                    19 -> CubicMetreToBarrelsUK(temp)
                    20 -> CubicMetreToCInches(temp)
                    21 -> CubicMetreToCFeet(temp)
                    22 -> CubicMetreToCYards(temp)
                    23 -> CubicMetreToTeaspoonsUS(temp)
                    24 -> CubicMetreToTeaspoonsUK(temp)
                    25 -> CubicMetreToTablespoonsUS(temp)
                    26 -> CubicMetreToTablespoonsUK(temp)
                    27 -> CubicMetreToFluidOuncesUS(temp)
                    28 -> CubicMetreToFluidOuncesUK(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun MicroLToCubicMetre(micro: Double): Double{
                return micro / BigDecimal("1000000000").toDouble()
            }

            fun DropToCubicMetre(drop: Double): Double{
                return drop / BigDecimal("20000000").toDouble()
            }

            fun MilliLToCubicMetre(milli: Double): Double{
                return milli / BigDecimal("1000000").toDouble()
            }

            fun CCentiToCubicMetre(centi: Double): Double{
                return centi / BigDecimal("1000000").toDouble()
            }

            fun DeciLToCubicMetre(deci: Double): Double{
                return deci / BigDecimal("10000").toDouble()
            }

            fun LitreToCubicMetre(litre: Double): Double{
                return litre / BigDecimal("1000").toDouble()
            }

            fun CupUSToCubicMetre(cup: Double): Double{
                return cup / BigDecimal("4226.8").toDouble()
            }

            fun CupUKToCubicMetre(cup: Double): Double{
                return cup / BigDecimal("3519.5079728").toDouble()
            }

            fun PintsUSToCubicMetre(pint: Double): Double{
                return pint * BigDecimal("0.0004731765").toDouble()
            }

            fun PintsUKToCubicMetre(pint: Double): Double{
                return pint * BigDecimal("0.0005682613").toDouble()
            }

            fun QuartsUSToCubicMetre(quart: Double): Double{
                return quart * BigDecimal("0.0009463529").toDouble()
            }

            fun QuartsUKToCubicMetre(quart: Double): Double{
                return quart * BigDecimal("0.0011365225").toDouble()
            }

            fun GallonsUSToCubicMetre(gallon: Double): Double{
                return gallon * BigDecimal("0.0037854118").toDouble()
            }

            fun GallonsUKToCubicMetre(gallon: Double): Double{
                return gallon * BigDecimal("0.00454609").toDouble()
            }

            fun BarrelsDryUSToCubicMetre(barrel: Double): Double{
                return barrel / BigDecimal("8.6485").toDouble()
            }

            fun BarrelsLiquidUSToCubicMetre(barrel: Double): Double{
                return barrel / BigDecimal("8.3864").toDouble()
            }

            fun BarrelsFedUSToCubicMetre(barrel: Double): Double{
                return barrel / BigDecimal("8.5217").toDouble()
            }

            fun BarrelsOilUSToCubicMetre(barrel: Double): Double{
                return barrel / BigDecimal("6.2898").toDouble()
            }

            fun BarrelsUKToCubicMetre(barrel: Double): Double{
                return barrel / BigDecimal("6.1103").toDouble()
            }

            fun CInchesToCubicMetre(cinch: Double): Double{
                return cinch * BigDecimal("0.0000163871").toDouble()
            }

            fun CFeetToCubicMetre(cfeet: Double): Double{
                return cfeet * BigDecimal("0.0283168466").toDouble()
            }

            fun CYardsToCubicMetre(cyard: Double): Double{
                return cyard * BigDecimal("0.764554858").toDouble()
            }

            fun TeaspoonsUSToCubicMetre(teaspoon: Double): Double{
                return BigDecimal.valueOf(teaspoon / 202884.1361596).setScale(10, RoundingMode.HALF_UP).toDouble()
            }

            fun TeaspoonsUKToCubicMetre(teaspoon: Double): Double{
                return BigDecimal.valueOf(teaspoon / 168936.3827175).setScale(10, RoundingMode.HALF_UP).toDouble()
            }

            fun TablespoonsUSToCubicMetre(tablespoon: Double): Double{
                return BigDecimal.valueOf(tablespoon / 67628.045398).setScale(10, RoundingMode.HALF_UP).toDouble()
            }

            fun TablespoonsUKToCubicMetre(tablespoon: Double): Double{
                return BigDecimal.valueOf(tablespoon / 56312.1275646).setScale(10, RoundingMode.HALF_UP).toDouble()
            }

            fun FluidOuncesUSToCubicMetre(fluidounce: Double): Double{
                return BigDecimal.valueOf(fluidounce / 33814.0227018).setScale(10, RoundingMode.HALF_UP).toDouble()
            }

            fun FluidOuncesUKToCubicMetre(fluidounce: Double): Double{
                return BigDecimal.valueOf(fluidounce / 35195.0797279).setScale(10, RoundingMode.HALF_UP).toDouble()
            }

            // Order 2 - Cubic metre to others

            fun CubicMetreToMicroL(cmetre: Double): Double{
                return cmetre * BigDecimal("1000000000").toDouble()
            }

            fun CubicMetreToDrop(cmetre: Double): Double{
                return cmetre * BigDecimal("20000000").toDouble()
            }

            fun CubicMetreToMilliL(cmetre: Double): Double{
                return cmetre * BigDecimal("1000000").toDouble()
            }

            fun CubicMetreToCCenti(centi: Double): Double{
                return centi * BigDecimal("1000000").toDouble()
            }

            fun CubicMetreToDeciL(cmetre: Double): Double{
                return cmetre * BigDecimal("10000").toDouble()
            }

            fun CubicMetreToLitre(cmetre: Double): Double{
                return cmetre * BigDecimal("1000").toDouble()
            }

            fun CubicMetreToCupUS(cmetre: Double): Double{
                return cmetre * BigDecimal("4226.8").toDouble()
            }

            fun CubicMetreToCupUK(cmetre: Double): Double{
                return cmetre * BigDecimal("3519.5079728").toDouble()
            }

            fun CubicMetreToPintsUS(cmetre: Double): Double{
                return cmetre / BigDecimal("0.0004731765").toDouble()
            }

            fun CubicMetreToPintsUK(cmetre: Double): Double{
                return cmetre / BigDecimal("0.0005682613").toDouble()
            }

            fun CubicMetreToQuartsUS(cmetre: Double): Double{
                return cmetre / BigDecimal("0.0009463529").toDouble()
            }

            fun CubicMetreToQuartsUK(cmetre: Double): Double{
                return cmetre / BigDecimal("0.0011365225").toDouble()
            }

            fun CubicMetreToGallonsUS(cmetre: Double): Double{
                return cmetre / BigDecimal("0.0037854118").toDouble()
            }

            fun CubicMetreToGallonsUK(cmetre: Double): Double{
                return cmetre / BigDecimal("0.00454609").toDouble()
            }

            fun CubicMetreToBarrelsDryUS(cmetre: Double): Double{
                return cmetre * BigDecimal("8.6485").toDouble()
            }

            fun CubicMetreToBarrelsLiquidUS(cmetre: Double): Double{
                return cmetre * BigDecimal("8.3864").toDouble()
            }

            fun CubicMetreToBarrelsFedUS(cmetre: Double): Double{
                return cmetre * BigDecimal("8.5217").toDouble()
            }

            fun CubicMetreToBarrelsOilUS(cmetre: Double): Double{
                return cmetre * BigDecimal("6.2898").toDouble()
            }

            fun CubicMetreToBarrelsUK(cmetre: Double): Double{
                return cmetre * BigDecimal("6.1103").toDouble()
            }

            fun CubicMetreToCInches(cinch: Double): Double{
                return cinch / BigDecimal("0.0000163871").toDouble()
            }

            fun CubicMetreToCFeet(cmetre: Double): Double{
                return cmetre / BigDecimal("0.0283168466").toDouble()
            }

            fun CubicMetreToCYards(cmetre: Double): Double{
                return cmetre / BigDecimal("0.764554858").toDouble()
            }

            fun CubicMetreToTeaspoonsUS(cmetre: Double): Double{
                return if (microQt)
                    BigDecimal.valueOf(cmetre * 202884.1361596).setScale(10, RoundingMode.HALF_UP).toDouble()
                else
                    BigDecimal.valueOf(cmetre * 202884.1361596).setScale(3, RoundingMode.HALF_UP).toDouble()
            }

            fun CubicMetreToTeaspoonsUK(cmetre: Double): Double{
                return if(microQt)
                    BigDecimal.valueOf(cmetre * 168936.3827175).setScale(10, RoundingMode.HALF_UP).toDouble()
                else
                    BigDecimal.valueOf(cmetre * 168936.3827175).setScale(3, RoundingMode.HALF_UP).toDouble()
            }

            fun CubicMetreToTablespoonsUS(cmetre: Double): Double{
                return if(microQt)
                    BigDecimal.valueOf(cmetre * 67628.045398).setScale(10, RoundingMode.HALF_UP).toDouble()
                else
                    BigDecimal.valueOf(cmetre * 67628.045398).setScale(3, RoundingMode.HALF_UP).toDouble()
            }

            fun CubicMetreToTablespoonsUK(cmetre: Double): Double{
                return if(microQt)
                    BigDecimal.valueOf(cmetre * 56312.1275646).setScale(10, RoundingMode.HALF_UP).toDouble()
                else
                    BigDecimal.valueOf(cmetre * 56312.1275646).setScale(3, RoundingMode.HALF_UP).toDouble()
            }

            fun CubicMetreToFluidOuncesUS(cmetre: Double): Double{
                return if(microQt)
                    BigDecimal.valueOf(cmetre * 33814.0227018).setScale(10, RoundingMode.HALF_UP).toDouble()
                else
                    BigDecimal.valueOf(cmetre * 33814.0227018).setScale(3, RoundingMode.HALF_UP).toDouble()
            }

            fun CubicMetreToFluidOuncesUK(cmetre: Double): Double{
                return if(microQt)
                    BigDecimal.valueOf(cmetre * 35195.0797279).setScale(10, RoundingMode.HALF_UP).toDouble()
                else
                    BigDecimal.valueOf(cmetre * 35195.0797279).setScale(3, RoundingMode.HALF_UP).toDouble()
            }
        }
    }

    class Area{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> SqMilliToSqMetre(value)
                    1 -> SqCentiToSqMetre(value)
                    2 -> SqDeciToSqMetre(value)
                    3 -> value
                    4 -> SqDecaToSqMetre(value)
                    5 -> SqHectoToSqMetre(value)
                    6 -> SqKiloToSqMetre(value)
                    7 -> HectaresToSqMetre(value)
                    8 -> SqMilesToSqMetre(value)
                    9 -> AcresToSqMetre(value)
                    10 -> SqInchesToSqMetre(value)
                    11 -> SqFeetToSqMetre(value)
                    12 -> SqYardsToSqMetre(value)
                    13 -> SqNMilesToSqMetre(value)
                    14 -> DunamsToSqMetre(value)
                    15 -> TsubosToSqMetre(value)
                    16 -> PyeongToSqMetre(value)
                    17 -> CuerdasToSqMetre(value)
                    18 -> SqMegaMToSqMetre(value)
                    19 -> SqGigaMToSqMetre(value)
                    20 -> SqTeraMToSqMetre(value)
                    21 -> SqPetaMToSqMetre(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> SqMetreToSqMilli(temp)
                    1 -> SqMetreToSqCenti(temp)
                    2 -> SqMetreToSqDeci(temp)
                    4 -> SqMetreToSqDeca(temp)
                    5 -> SqMetreToSqHecto(temp)
                    6 -> SqMetreToSqKilo(temp)
                    7 -> SqMetreToHectares(temp)
                    8 -> SqMetreToSqMiles(temp)
                    9 -> SqMetreToAcres(temp)
                    10 -> SqMetreToSqInches(temp)
                    11 -> SqMetreToSqFeet(temp)
                    12 -> SqMetreToSqYards(temp)
                    13 -> SqMetreToSqNMiles(temp)
                    14 -> SqMetreToDunams(temp)
                    15 -> SqMetreToTsubos(temp)
                    16 -> SqMetreToPyeong(temp)
                    17 -> SqMetreToCuerdas(temp)
                    18 -> SqMetreToSqMegaM(temp)
                    19 -> SqMetreToSqGigaM(temp)
                    20 -> SqMetreToSqTeraM(temp)
                    21 -> SqMetreToSqPetaM(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun SqMilliToSqMetre(milli: Double): Double{
                return milli / BigDecimal("1000000").toDouble()
            }

            fun SqCentiToSqMetre(centi: Double): Double{
                return centi / BigDecimal("10000").toDouble()
            }

            fun SqDeciToSqMetre(deci: Double): Double{
                return deci / BigDecimal("100").toDouble()
            }

            fun SqDecaToSqMetre(deca: Double): Double{
                return deca * BigDecimal("100").toDouble()
            }

            fun SqHectoToSqMetre(hecto: Double): Double{
                return hecto * BigDecimal("10000").toDouble()
            }

            fun SqKiloToSqMetre(kilo: Double): Double{
                return kilo * BigDecimal("1000000").toDouble()
            }

            fun HectaresToSqMetre(hectare: Double): Double{
                return hectare * BigDecimal("10000").toDouble()
            }

            fun SqMilesToSqMetre(sqmile: Double): Double{
                return sqmile * BigDecimal("2589988.11").toDouble()
            }

            fun AcresToSqMetre(acre: Double): Double{
                return acre * BigDecimal("4047").toDouble()
            }

            fun SqInchesToSqMetre(sqinch: Double): Double{
                return sqinch / BigDecimal("1550").toDouble()
            }

            fun SqFeetToSqMetre(sqfeet: Double): Double{
                return sqfeet / BigDecimal("10.764").toDouble()
            }

            fun SqYardsToSqMetre(sqyard: Double): Double{
                return sqyard / BigDecimal("1.196").toDouble()
            }

            fun SqNMilesToSqMetre(sqnmile: Double): Double{
                return sqnmile * BigDecimal("3430000").toDouble()
            }

            fun DunamsToSqMetre(dunam: Double): Double{
                return dunam * BigDecimal("1000").toDouble()
            }

            fun TsubosToSqMetre(tsubo: Double): Double{
                return tsubo * BigDecimal("3.30579").toDouble()
            }

            fun PyeongToSqMetre(pyeong: Double): Double{
                return pyeong * BigDecimal("3.30579").toDouble()
            }

            fun CuerdasToSqMetre(cuerda: Double): Double{
                return cuerda * BigDecimal("3930.395625").toDouble()
            }

            fun SqMegaMToSqMetre(sqmega: Double): Double{
                return sqmega * BigDecimal("1000000000000").toDouble()
            }

            fun SqGigaMToSqMetre(sqgiga: Double): Double{
                return sqgiga * BigDecimal("1000000000000000000").toDouble()
            }

            fun SqTeraMToSqMetre(sqtera: Double): Double{
                return sqtera * BigDecimal("1000000000000000000000000").toDouble()
            }

            fun SqPetaMToSqMetre(sqpeta: Double): Double{
                return sqpeta * BigDecimal("1000000000000000000000000000000").toDouble()
            }

            // Order 2 Square Metre to others

            fun SqMetreToSqMilli(sqmetre: Double): Double{
                return sqmetre * BigDecimal("1000000").toDouble()
            }

            fun SqMetreToSqCenti(sqmetre: Double): Double{
                return sqmetre * BigDecimal("10000").toDouble()
            }

            fun SqMetreToSqDeci(sqmetre: Double): Double{
                return sqmetre * BigDecimal("100").toDouble()
            }

            fun SqMetreToSqDeca(sqmetre: Double): Double{
                return sqmetre / BigDecimal("100").toDouble()
            }

            fun SqMetreToSqHecto(sqmetre: Double): Double{
                return sqmetre / BigDecimal("10000").toDouble()
            }

            fun SqMetreToSqKilo(sqmetre: Double): Double{
                return sqmetre / BigDecimal("1000000").toDouble()
            }

            fun SqMetreToHectares(sqmetre: Double): Double{
                return sqmetre / BigDecimal("10000").toDouble()
            }

            fun SqMetreToSqMiles(sqmetre: Double): Double{
                return sqmetre / BigDecimal("2589988.11").toDouble()
            }

            fun SqMetreToAcres(sqmetre: Double): Double{
                return sqmetre / BigDecimal("4047").toDouble()
            }

            fun SqMetreToSqInches(sqmetre: Double): Double{
                return sqmetre * BigDecimal("1550").toDouble()
            }

            fun SqMetreToSqFeet(sqmetre: Double): Double{
                return sqmetre * BigDecimal("10.764").toDouble()
            }

            fun SqMetreToSqYards(sqmetre: Double): Double{
                return sqmetre * BigDecimal("1.196").toDouble()
            }

            fun SqMetreToSqNMiles(sqmetre: Double): Double{
                return sqmetre / BigDecimal("3430000").toDouble()
            }

            fun SqMetreToDunams(sqmetre: Double): Double{
                return sqmetre / BigDecimal("1000").toDouble()
            }

            fun SqMetreToTsubos(sqmetre: Double): Double{
                return sqmetre / BigDecimal("3.30579").toDouble()
            }

            fun SqMetreToPyeong(sqmetre: Double): Double{
                return sqmetre / BigDecimal("3.30579").toDouble()
            }

            fun SqMetreToCuerdas(sqmetre: Double): Double{
                return sqmetre / BigDecimal("3930.395625").toDouble()
            }

            fun SqMetreToSqMegaM(sqmetre: Double): Double{
                return sqmetre / BigDecimal("1000000000000").toDouble()
            }

            fun SqMetreToSqGigaM(sqmetre: Double): Double{
                return sqmetre / BigDecimal("1000000000000000000").toDouble()
            }

            fun SqMetreToSqTeraM(sqmetre: Double): Double{
                return sqmetre / BigDecimal("1000000000000000000000000").toDouble()
            }

            fun SqMetreToSqPetaM(sqmetre: Double): Double{
                return sqmetre / BigDecimal("1000000000000000000000000000000").toDouble()
            }
        }
    }

    class WeightORMass{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> FemtoGToKiloG(value)
                    1 -> PicoGToKiloG(value)
                    2 -> NanoGToKiloG(value)
                    3 -> MicroGToKiloG(value)
                    4 -> CaratsToKiloG(value)
                    5 -> MilliGToKiloG(value)
                    6 -> CentiGToKiloG(value)
                    7 -> DeciGToKiloG(value)
                    8 -> GramsToKiloG(value)
                    9 -> DecaGToKiloG(value)
                    10 -> HectoGToKiloG(value)
                    11 -> value
                    12 -> NewtonsEarthToKiloG(value)
                    13 -> TonnesToKiloG(value)
                    14 -> OuncesToKiloG(value)
                    15 -> PoundsToKiloG(value)
                    16 -> StonesToKiloG(value)
                    17 -> TonsUSToKiloG(value)
                    18 -> TonsUKToKiloG(value)
                    19 -> CattiesToKiloG(value)
                    20 -> GrainsTOKiloG(value)
                    21 -> GigaGToKiloG(value)
                    22 -> TeraGToKiloG(value)
                    23 -> PetaGToKiloG(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> KiloGToFemtoG(temp)
                    1 -> KiloGToPicoG(temp)
                    2 -> KiloGToNanoG(temp)
                    3 -> KiloGToMicroG(temp)
                    4 -> KiloGToCarats(temp)
                    5 -> KiloGToMilliG(temp)
                    6 -> KiloGToCentiG(temp)
                    7 -> KiloGToDeciG(temp)
                    8 -> KiloGToGrams(temp)
                    9 -> KiloGToDecaG(temp)
                    10 -> KiloGToHectoG(temp)
                    12 -> KiloGToNewtonsEarth(temp)
                    13 -> KiloGToTonnes(temp)
                    14 -> KiloGToOunces(temp)
                    15 -> KiloGToPounds(temp)
                    16 -> KiloGToStones(temp)
                    17 -> KiloGToTonsUS(temp)
                    18 -> KiloGToTonsUK(temp)
                    19 -> KiloGToCatties(temp)
                    20 -> KiloGToGrains(temp)
                    21 -> KiloGToGigaG(temp)
                    22 -> KiloGToTeraG(temp)
                    23 -> KiloGToPetaG(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun FemtoGToKiloG(femto: Double): Double{
                return femto * BigDecimal("0.0000000000000000010").toDouble()
            }

            fun PicoGToKiloG(pico: Double): Double{
                return pico * BigDecimal("0.0000000000000010").toDouble()
            }

            fun NanoGToKiloG(nano: Double): Double{
                return nano * BigDecimal("0.0000000000010").toDouble()
            }

            fun MicroGToKiloG(micro: Double): Double{
                return micro * BigDecimal("0.0000000010").toDouble()
            }

            fun CaratsToKiloG(carat: Double): Double{
                return carat / BigDecimal("5000").toDouble()
            }

            fun MilliGToKiloG(milli: Double): Double{
                return milli / BigDecimal("1000000").toDouble()
            }

            fun CentiGToKiloG(centi: Double): Double{
                return centi / BigDecimal("100000").toDouble()
            }

            fun DeciGToKiloG(deci: Double): Double{
                return deci / BigDecimal("10000").toDouble()
            }

            fun GramsToKiloG(gram: Double): Double{
                return gram / BigDecimal("1000").toDouble()
            }

            fun DecaGToKiloG(deca: Double): Double{
                return deca / BigDecimal("100").toDouble()
            }

            fun HectoGToKiloG(hecto: Double): Double{
                return hecto / BigDecimal("10").toDouble()
            }

            fun NewtonsEarthToKiloG(newton: Double): Double{
                return newton * BigDecimal("0.1019716").toDouble()
            }

            fun TonnesToKiloG(tonne: Double): Double{
                return tonne * BigDecimal("1000").toDouble()
            }

            fun OuncesToKiloG(ounce: Double): Double{
                return ounce / BigDecimal("35.274").toDouble()
            }

            fun PoundsToKiloG(pound: Double): Double{
                return pound / BigDecimal("2.205").toDouble()
            }

            fun StonesToKiloG(stone: Double): Double{
                return stone * BigDecimal("6.35").toDouble()
            }

            fun TonsUSToKiloG(ton: Double): Double{
                return ton * BigDecimal("907.2").toDouble()
            }

            fun TonsUKToKiloG(ton: Double): Double{
                return ton * BigDecimal("1016").toDouble()
            }

            fun CattiesToKiloG(catty: Double): Double{
                return catty / BigDecimal("1.667").toDouble()
            }

            fun GrainsTOKiloG(grain: Double): Double{
                return grain / BigDecimal("15430").toDouble()
            }

            fun GigaGToKiloG(giga: Double): Double{
                return giga * BigDecimal("1000000").toDouble()
            }

            fun TeraGToKiloG(tera: Double): Double{
                return tera * BigDecimal("1000000000").toDouble()
            }

            fun PetaGToKiloG(peta: Double): Double{
                return peta * BigDecimal("1000000000000").toDouble()
            }

            // Order 2 - Kilogram to others

            fun KiloGToFemtoG(kilog: Double): Double{
                return kilog / BigDecimal("0.0000000000000000010").toDouble()
            }

            fun KiloGToPicoG(kilog: Double): Double{
                return kilog / BigDecimal("0.0000000000000010").toDouble()
            }

            fun KiloGToNanoG(kilog: Double): Double{
                return kilog / BigDecimal("0.0000000000010").toDouble()
            }

            fun KiloGToMicroG(kilog: Double): Double{
                return kilog / BigDecimal("0.0000000010").toDouble()
            }

            fun KiloGToCarats(kilog: Double): Double{
                return kilog * BigDecimal("5000").toDouble()
            }

            fun KiloGToMilliG(kilog: Double): Double{
                return kilog * BigDecimal("1000000").toDouble()
            }

            fun KiloGToCentiG(kilog: Double): Double{
                return kilog * BigDecimal("100000").toDouble()
            }

            fun KiloGToDeciG(kilog: Double): Double{
                return kilog * BigDecimal("10000").toDouble()
            }

            fun KiloGToGrams(kilog: Double): Double{
                return kilog * BigDecimal("1000").toDouble()
            }

            fun KiloGToDecaG(kilog: Double): Double{
                return kilog * BigDecimal("100").toDouble()
            }

            fun KiloGToHectoG(kilog: Double): Double{
                return kilog * BigDecimal("10").toDouble()
            }

            fun KiloGToNewtonsEarth(kilog: Double): Double{
                return kilog / BigDecimal("0.1019716").toDouble()
            }

            fun KiloGToTonnes(kilog: Double): Double{
                return kilog / BigDecimal("1000").toDouble()
            }

            fun KiloGToOunces(kilog: Double): Double{
                return kilog * BigDecimal("35.274").toDouble()
            }

            fun KiloGToPounds(kilog: Double): Double{
                return kilog * BigDecimal("2.205").toDouble()
            }

            fun KiloGToStones(kilog: Double): Double{
                return kilog / BigDecimal("6.35").toDouble()
            }

            fun KiloGToTonsUS(kilog: Double): Double{
                return kilog / BigDecimal("907.2").toDouble()
            }

            fun KiloGToTonsUK(kilog: Double): Double{
                return kilog / BigDecimal("1016").toDouble()
            }

            fun KiloGToCatties(kilog: Double): Double{
                return kilog * BigDecimal("1.667").toDouble()
            }

            fun KiloGToGrains(kilog: Double): Double{
                return kilog * BigDecimal("15430").toDouble()
            }

            fun KiloGToGigaG(kilog: Double): Double{
                return kilog / BigDecimal("1000000").toDouble()
            }

            fun KiloGToTeraG(kilog: Double): Double{
                return kilog / BigDecimal("1000000000").toDouble()
            }

            fun KiloGToPetaG(kilog: Double): Double{
                return kilog / BigDecimal("1000000000000").toDouble()
            }
        }
    }

    class Temperature {
        companion object{
            var from = 0
            var to = 0
            val abs_zero: Double = 273.15

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> CelsToKel(value)
                    1 -> FahrenToKel(value)
                    2 -> value
                    3 -> RankineToKel(value)
                    4 -> DelisleToKel(value)
                    5 -> NewtonToKel(value)
                    6 -> ReaumurToKel(value)
                    7 -> RomerToKel(value)
                    8 -> YoctoKelToKel(value)
                    9 -> AttoKelToKel(value)
                    10 -> FemtoKelToKel(value)
                    11 -> PicoKelToKel(value)
                    12 -> NanoKelToKel(value)
                    13 -> MicroKelToKel(value)
                    14 -> MilliKelToKel(value)
                    15 -> KiloKelToKel(value)
                    16 -> MegaKelToKel(value)
                    17 -> GigaKelToKel(value)
                    18 -> TeraKelToKel(value)
                    19 -> PetaKelToKel(value)
                    20 -> ExaKelToKel(value)
                    21 -> ZetaKelToKel(value)
                    22 -> YottaKelToKel(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> KelToCels(temp)
                    1 -> KelToFahren(temp)
                    3 -> KelToRankine(temp)
                    4 -> KelToDelisle(temp)
                    5 -> KelToNewton(temp)
                    6 -> KelToReaumur(temp)
                    7 -> KelToRomer(temp)
                    8 -> KelToYoctoKel(temp)
                    9 -> KelToAttoKel(temp)
                    10 -> KelToFemtoKel(temp)
                    11 -> KelToPicoKel(temp)
                    12 -> KelToNanoKel(temp)
                    13 -> KelToMicroKel(temp)
                    14 -> KelToMilliKel(temp)
                    15 -> KelToKiloKel(temp)
                    16 -> KelToMegaKel(temp)
                    17 -> KelToGigaKel(temp)
                    18 -> KelToTeraKel(temp)
                    19 -> KelToPetaKel(temp)
                    20 -> KelToExaKel(temp)
                    21 -> KelToZetaKel(temp)
                    22 -> KelToYottaKel(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun CelsToKel(celsius: Double): Double{
                return celsius + abs_zero
            }

            fun FahrenToKel(fahrenheit: Double): Double{
                return (fahrenheit - 32) * 5/9 + abs_zero
            }

            fun RankineToKel(rankine: Double): Double{
                return rankine * 5/9
            }

            fun DelisleToKel(delisle: Double): Double{
                return (delisle + 100)/1.5000 + abs_zero
            }

            fun NewtonToKel(newton: Double): Double{
                return (newton/0.33000) + abs_zero
            }

            fun ReaumurToKel(reaumur: Double): Double{
                return (reaumur/0.80000) + abs_zero
            }

            fun RomerToKel(romer: Double): Double{
                return (romer - 7.5)/0.52500 + abs_zero
            }

            fun YoctoKelToKel(yocto: Double): Double{
                return yocto / BigDecimal("1000000000000000000000000").toDouble()
            }

            fun AttoKelToKel(atto: Double): Double{
                return atto / BigDecimal("1000000000000000000").toDouble()
            }

            fun FemtoKelToKel(femto: Double): Double{
                return femto / BigDecimal("1000000000000000").toDouble()
            }

            fun PicoKelToKel(pico: Double): Double{
                return pico / BigDecimal("1000000000000").toDouble()
            }

            fun NanoKelToKel(nano: Double): Double{
                return nano / BigDecimal("1000000000").toDouble()
            }

            fun MicroKelToKel(micro: Double): Double{
                return micro / BigDecimal("1000000").toDouble()
            }

            fun MilliKelToKel(milli: Double): Double{
                return milli / BigDecimal("1000").toDouble()
            }

            fun KiloKelToKel(kilo: Double): Double{
                return kilo * BigDecimal("1000").toDouble()
            }

            fun MegaKelToKel(mega: Double): Double{
                return mega * BigDecimal("1000000").toDouble()
            }

            fun GigaKelToKel(giga: Double): Double{
                return giga * BigDecimal("1000000000").toDouble()
            }

            fun TeraKelToKel(tera: Double): Double{
                return tera * BigDecimal("1000000000000").toDouble()
            }

            fun PetaKelToKel(peta: Double): Double{
                return peta * BigDecimal("1000000000000000").toDouble()
            }

            fun ExaKelToKel(exa: Double): Double{
                return exa * BigDecimal("1000000000000000000").toDouble()
            }

            fun ZetaKelToKel(zeta: Double): Double{
                return zeta * BigDecimal("1000000000000000000000").toDouble()
            }

            fun YottaKelToKel(yotta: Double): Double{
                return yotta * BigDecimal("1000000000000000000000000").toDouble()
            }

            // Order 2 - Kelvin To Others

            fun KelToCels(kelvin: Double): Double{
                return kelvin - abs_zero
            }

            fun KelToFahren(kelvin: Double): Double{
                return (kelvin - abs_zero) * 9/5 + 32
            }

            fun KelToRankine(kelvin: Double): Double{
                return kelvin / 5/9
            }

            fun KelToDelisle(kelvin: Double): Double{
                return (kelvin - 100)*1.5000 - abs_zero
            }

            fun KelToNewton(kelvin: Double): Double{
                return (kelvin*0.33000) - abs_zero
            }

            fun KelToReaumur(kelvin: Double): Double{
                return (kelvin*0.80000) - abs_zero
            }

            fun KelToRomer(kelvin: Double): Double{
                return (kelvin + 7.5)*0.52500 - abs_zero
            }

            fun KelToYoctoKel(kelvin: Double): Double{
                return kelvin * BigDecimal("1000000000000000000000000").toDouble()
            }

            fun KelToAttoKel(kelvin: Double): Double{
                return kelvin * BigDecimal("1000000000000000000").toDouble()
            }

            fun KelToFemtoKel(kelvin: Double): Double{
                return kelvin * BigDecimal("1000000000000000").toDouble()
            }

            fun KelToPicoKel(kelvin: Double): Double{
                return kelvin * BigDecimal("1000000000000").toDouble()
            }

            fun KelToNanoKel(kelvin: Double): Double{
                return kelvin * BigDecimal("1000000000").toDouble()
            }

            fun KelToMicroKel(kelvin: Double): Double{
                return kelvin * BigDecimal("1000000").toDouble()
            }

            fun KelToMilliKel(kelvin: Double): Double{
                return kelvin * BigDecimal("1000").toDouble()
            }

            fun KelToKiloKel(kelvin: Double): Double{
                return kelvin / BigDecimal("1000").toDouble()
            }

            fun KelToMegaKel(kelvin: Double): Double{
                return kelvin / BigDecimal("1000000").toDouble()
            }

            fun KelToGigaKel(kelvin: Double): Double{
                return kelvin / BigDecimal("1000000000").toDouble()
            }

            fun KelToTeraKel(kelvin: Double): Double{
                return kelvin / BigDecimal("1000000000000").toDouble()
            }

            fun KelToPetaKel(kelvin: Double): Double{
                return kelvin / BigDecimal("1000000000000000").toDouble()
            }

            fun KelToExaKel(kelvin: Double): Double{
                return kelvin / BigDecimal("1000000000000000000").toDouble()
            }

            fun KelToZetaKel(kelvin: Double): Double{
                return kelvin / BigDecimal("1000000000000000000000").toDouble()
            }

            fun KelToYottaKel(kelvin: Double): Double{
                return kelvin / BigDecimal("1000000000000000000000000").toDouble()
            }
        }
    }

    class Speed{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> CentiMperSecToMperSec(value)
                    1 -> value
                    2 -> KiloMperHrToMperSec(value)
                    3 -> MilesperHrToMperSec(value)
                    4 -> KnotsToMperSec(value)
                    5 -> FeetperSecToMperSec(value)
                    6 -> FeetperMToMperSec(value)
                    7 -> InchperSecToMperSec(value)
                    8 -> MachToMperSec(value)
                    9 -> FurlongsperFort(value)
                    10 -> BubnoffToMperSec(value)
                    11 -> NaturalUnitToMperSec(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> MetreperSecToCentiMperSec(temp)
                    2 -> MetreperSecToKiloMperHr(temp)
                    3 -> MetreperSecToMilesperHr(temp)
                    4 -> MetreperSecToKnots(temp)
                    5 -> MetreperSecToFeetperSec(temp)
                    6 -> MetreperSecToFeetperM(temp)
                    7 -> MetreperSecToInchperSec(temp)
                    8 -> MetreperSecToMach(temp)
                    9 -> MetreperSecToFurlon(temp)
                    10 -> MetreperSecToBubnoff(temp)
                    11 -> MetreperSecToNaturalUnit(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun CentiMperSecToMperSec(centimpersec: Double): Double{
                return centimpersec / 100
            }

            fun KiloMperHrToMperSec(kilomperhr: Double): Double{
                return kilomperhr / 3.6
            }

            fun MilesperHrToMperSec(milesperhr: Double): Double{
                return milesperhr / 2.237
            }

            fun KnotsToMperSec(knot: Double): Double{
                return knot / 1.944
            }

            fun FeetperSecToMperSec(feetpersec: Double): Double{
                return feetpersec / 3.281
            }

            fun FeetperMToMperSec(feetperm: Double): Double{
                return feetperm / 196.9
            }

            fun InchperSecToMperSec(inchpersec: Double): Double{
                return inchpersec / 39.37
            }

            fun MachToMperSec(mach: Double): Double{
                return mach * 343
            }

            fun FurlongsperFort(furlongs: Double): Double{
                return furlongs / 6013
            }

            fun BubnoffToMperSec(bubnoff: Double): Double{
                return bubnoff / BigDecimal("31540000000000").toDouble()
            }

            fun NaturalUnitToMperSec(nat: Double): Double{
                return nat * BigDecimal("299792458").toDouble()
            }

            // Order 2 - Metre per second to Others

            fun MetreperSecToCentiMperSec(metre: Double): Double{
                return metre * 100
            }

            fun MetreperSecToKiloMperHr(metre: Double): Double{
                return metre * 3.6
            }

            fun MetreperSecToMilesperHr(metre: Double): Double{
                return metre * 2.237
            }

            fun MetreperSecToKnots(metre: Double): Double{
                return metre * 1.944
            }

            fun MetreperSecToFeetperSec(metre: Double): Double{
                return metre * 3.281
            }

            fun MetreperSecToFeetperM(metre: Double): Double{
                return metre * 196.9
            }

            fun MetreperSecToInchperSec(metre: Double): Double{
                return metre * 39.37
            }

            fun MetreperSecToMach(metre: Double): Double{
                return metre / 343
            }

            fun MetreperSecToFurlon(metre: Double): Double{
                return metre * 6013
            }

            fun MetreperSecToBubnoff(metre: Double): Double{
                return metre * BigDecimal("31540000000000").toDouble()
            }

            fun MetreperSecToNaturalUnit(metre: Double): Double{
                return metre / BigDecimal("299792458").toDouble()
            }
        }
    }

    class Power{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> value
                    1 -> KiloWtToWt(value)
                    2 -> HorsepowerToWt(value)
                    3 -> ErgsperSecToWt(value)
                    4 -> FootPoundsPerMToWt(value)
                    5 -> dBMToWt(value)
                    6 -> CalperHrToWt(value)
                    7 -> BTUsToWt(value)
                    8 -> TonRefriToWt(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    1 -> WtToKiloWt(temp)
                    2 -> WtToHorsepower(temp)
                    3 -> WtToErgsperSec(temp)
                    4 -> WtToFootPoundsPerM(temp)
                    5 -> WtTodBM(temp)
                    6 -> WtToCalperHr(temp)
                    7 -> WtToBTUs(temp)
                    8 -> WtToTonRefri(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun KiloWtToWt(kilo: Double): Double{
                return kilo * 1000
            }

            fun HorsepowerToWt(horsepower: Double): Double{
                return horsepower * 745.7
            }

            fun ErgsperSecToWt(ergs: Double): Double{
                return ergs * BigDecimal("0.00000010").toDouble()
            }

            fun FootPoundsPerMToWt(footpound: Double): Double{
                return footpound * BigDecimal("0.0225969658").toDouble()
            }

            fun dBMToWt(decibel: Double): Double{
                return decibel * BigDecimal("0.0012589").toDouble()
            }

            fun CalperHrToWt(cal: Double): Double{
                return cal * BigDecimal("0.00116222222").toDouble()
            }

            fun BTUsToWt(btu: Double): Double{
                return btu * BigDecimal("0.29307107").toDouble()
            }

            fun TonRefriToWt(tonref: Double): Double{
                return tonref * BigDecimal("3516.85").toDouble()
            }

            // Order 2 - Watts to others

            fun WtToKiloWt(watt: Double): Double{
                return watt / 1000
            }

            fun WtToHorsepower(watt: Double): Double{
                return watt / 745.7
            }

            fun WtToErgsperSec(watt: Double): Double{
                return watt / BigDecimal("0.00000010").toDouble()
            }

            fun WtToFootPoundsPerM(watt: Double): Double{
                return watt / BigDecimal("0.0225969658").toDouble()
            }

            fun WtTodBM(watt: Double): Double{
                return watt / BigDecimal("0.0012589").toDouble()
            }

            fun WtToCalperHr(watt: Double): Double{
                return watt / BigDecimal("0.00116222222").toDouble()
            }

            fun WtToBTUs(watt: Double): Double{
                return watt / BigDecimal("0.29307107").toDouble()
            }

            fun WtToTonRefri(watt: Double): Double{
                return watt / BigDecimal("3516.85").toDouble()
            }
        }
    }

    class Energy{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> ElecVoltToJoules(value)
                    1 -> value
                    2 -> KiloJoulesToJoules(value)
                    3 -> ErgsToJoules(value)
                    4 -> CalToJoules(value)
                    5 -> BTUsToJoules(value)
                    6 -> KiloWtHrToJoules(value)
                    7 -> KiloCalToJoules(value)
                    8 -> FootPDToJoules(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> JoulesToElecVolt(temp)
                    2 -> JoulesToKiloJoules(temp)
                    3 -> JoulesToErgs(temp)
                    4 -> JoulesToCal(temp)
                    5 -> JoulesToBTUs(temp)
                    6 -> JoulesToKiloWtHr(temp)
                    7 -> JoulesToKiloCal(temp)
                    8 -> JoulesToFootPD(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun ElecVoltToJoules(elecvlt: Double): Double{
                return elecvlt * BigDecimal("0.000000000000000000160218").toDouble()
            }

            fun KiloJoulesToJoules(kilo: Double): Double{
                return kilo * 1000
            }

            fun ErgsToJoules(erg: Double): Double{
                return erg * BigDecimal("0.0000001").toDouble()
            }

            fun CalToJoules(cal: Double): Double{
                return cal * 4.184
            }

            fun BTUsToJoules(btu: Double): Double{
                return btu * BigDecimal("1055.06").toDouble()
            }

            fun KiloWtHrToJoules(kilowthr: Double): Double{
                return kilowthr * BigDecimal("3600000").toDouble()
            }

            fun KiloCalToJoules(kilocal: Double): Double{
                return kilocal * 4184
            }

            fun FootPDToJoules(footpound: Double): Double{
                return footpound * 1.35582
            }

            // Order 2 - Joules to others

            fun JoulesToElecVolt(joule: Double): Double{
                return joule / BigDecimal("0.000000000000000000160218").toDouble()
            }

            fun JoulesToKiloJoules(joule: Double): Double{
                return joule / 1000
            }

            fun JoulesToErgs(joule: Double): Double{
                return joule / BigDecimal("0.0000001").toDouble()
            }

            fun JoulesToCal(joule: Double): Double{
                return joule / 4.184
            }

            fun JoulesToBTUs(joule: Double): Double{
                return joule / BigDecimal("1055.06").toDouble()
            }

            fun JoulesToKiloWtHr(joule: Double): Double{
                return joule / BigDecimal("3600000").toDouble()
            }

            fun JoulesToKiloCal(joule: Double): Double{
                return joule / 4184
            }

            fun JoulesToFootPD(joule: Double): Double{
                return joule / 1.35582
            }
        }
    }

    class Pressure{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> AtmosToPascal(value)
                    1 -> TorrToPascal(value)
                    2 -> value
                    3 -> KiloPsToPascal(value)
                    4 -> KipsPerSqInchToPascal(value)
                    5 -> PoundsPerSqInchToPascal(value)
                    6 -> FeetSeaWaterToPascal(value)
                    7 -> BarsToPascal(value)
                    8 -> KGperSqCentiMToPascal(value)
                    9 -> BaryesToPascal(value)
                    10 -> SthenesToPascal(value)
                    11 -> MilliMToPascal(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> PascalToAtmos(temp)
                    1 -> PascalToTorr(temp)
                    3 -> PascalToKiloPs(temp)
                    4 -> PascalToKipsPerSqInch(temp)
                    5 -> PascalToPoundsPerSqInch(temp)
                    6 -> PascalToFeetSeaWater(temp)
                    7 -> PascalToBars(temp)
                    8 -> PascalToKGperSqCentiM(temp)
                    9 -> PascalToBaryes(temp)
                    10 -> PascalToSthenes(temp)
                    11 -> PascalToMilliM(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun AtmosToPascal(atmos: Double): Double{
                return atmos * 101325
            }

            fun TorrToPascal(torr: Double): Double{
                return torr * 133.322
            }

            fun KiloPsToPascal(kilops: Double): Double{
                return kilops * 1000
            }

            fun KipsPerSqInchToPascal(kipsqinch: Double): Double{
                return kipsqinch * BigDecimal("6894757.2931783").toDouble()
            }

            fun PoundsPerSqInchToPascal(pdsqinch: Double): Double{
                return pdsqinch * 6894.76
            }

            fun FeetSeaWaterToPascal(ftswtr: Double): Double{
                return ftswtr * 3064.306
            }

            fun BarsToPascal(bar: Double): Double{
                return bar * BigDecimal("100000").toDouble()
            }

            fun KGperSqCentiMToPascal(kgcenti: Double): Double{
                return kgcenti * BigDecimal("98066.5").toDouble()
            }

            fun BaryesToPascal(barye: Double): Double{
                return barye / 10
            }

            fun SthenesToPascal(sthene: Double): Double{
                return sthene * 1000
            }

            fun MilliMToPascal(millim: Double): Double{
                return millim * 133.322
            }

            // Order 2 - Pascal to others

            fun PascalToAtmos(pascal: Double): Double{
                return pascal / 101325
            }

            fun PascalToTorr(pascal: Double): Double{
                return pascal / 133.322
            }

            fun PascalToKiloPs(pascal: Double): Double{
                return pascal / 1000
            }

            fun PascalToKipsPerSqInch(pascal: Double): Double{
                return pascal / BigDecimal("6894757.2931783").toDouble()
            }

            fun PascalToPoundsPerSqInch(pascal: Double): Double{
                return pascal / 6894.76
            }

            fun PascalToFeetSeaWater(pascal: Double): Double{
                return pascal / 3064.306
            }

            fun PascalToBars(pascal: Double): Double{
                return pascal / BigDecimal("100000").toDouble()
            }

            fun PascalToKGperSqCentiM(pascal: Double): Double{
                return pascal / BigDecimal("98066.5").toDouble()
            }

            fun PascalToBaryes(pascal: Double): Double{
                return pascal * 10
            }

            fun PascalToSthenes(pascal: Double): Double{
                return pascal / 1000
            }

            fun PascalToMilliM(pascal: Double): Double{
                return pascal / 133.322
            }
        }
    }

    class Time{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> NanoSecToSec(value)
                    1 -> MicroSecToSec(value)
                    2 -> MilliSecToSec(value)
                    3 -> JiffyToSec(value)
                    4 -> value
                    5 -> MinToSec(value)
                    6 -> HourToSec(value)
                    7 -> DayToSec(value)
                    8 -> WeekToSec(value)
                    9 -> FortNghtToSec(value)
                    10 -> MonToSec(value)
                    11 -> YearToSec(value)
                    12 -> DecToSec(value)
                    13 -> CenToSec(value)
                    14 -> MillToSec(value)
                    15 -> GalYearToSec(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> SecToNanoSec(temp)
                    1 -> SecToMicroSec(temp)
                    2 -> SecToMilliSec(temp)
                    3 -> SecToJiffy(temp)
                    5 -> SecToMin(temp)
                    6 -> SecToHour(temp)
                    7 -> SecToDay(temp)
                    8 -> SecToWeek(temp)
                    9 -> SecToFortNght(temp)
                    10 -> SecToMon(temp)
                    11 -> SecToYear(temp)
                    12 -> SecToDec(temp)
                    13 -> SecToCen(temp)
                    14 -> SecToMill(temp)
                    15 -> SecToGalYear(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun NanoSecToSec(nano: Double): Double{
                return nano * BigDecimal("0.000000001").toDouble()
            }

            fun MicroSecToSec(micro: Double): Double{
                return micro * BigDecimal("0.000001").toDouble()
            }

            fun MilliSecToSec(milli: Double): Double{
                return milli * BigDecimal("0.001").toDouble()
            }

            fun JiffyToSec(jiff: Double): Double{
                return jiff * 0.01
            }

            fun MinToSec(min: Double): Double{
                return min * 60
            }

            fun HourToSec(hr: Double): Double{
                return hr * 3600
            }

            fun DayToSec(day: Double): Double{
                return day * BigDecimal("86400").toDouble()
            }

            fun WeekToSec(week: Double): Double{
                return week * BigDecimal("604800").toDouble()
            }

            fun FortNghtToSec(fort: Double): Double{
                return fort * BigDecimal("1210000").toDouble()
            }

            fun MonToSec(month: Double): Double{
                return month * BigDecimal("2628000").toDouble()
            }

            fun YearToSec(yr: Double): Double{
                return yr * BigDecimal("31540000").toDouble()
            }

            fun DecToSec(dc: Double): Double{
                return dc * BigDecimal("315400000").toDouble()
            }

            fun CenToSec(cen: Double): Double{
                return cen * BigDecimal("3154000000").toDouble()
            }

            fun MillToSec(mill: Double): Double{
                return mill * BigDecimal("31540000000").toDouble()
            }

            fun GalYearToSec(gal: Double): Double{
                return gal * BigDecimal("7253280000000000").toDouble()
            }

            // Order 2 - Seconds to others

            fun SecToNanoSec(sec: Double): Double{
                return sec / BigDecimal("0.000000001").toDouble()
            }

            fun SecToMicroSec(sec: Double): Double{
                return sec / BigDecimal("0.000001").toDouble()
            }

            fun SecToMilliSec(sec: Double): Double{
                return sec / BigDecimal("0.001").toDouble()
            }

            fun SecToJiffy(sec: Double): Double{
                return sec / 0.01
            }

            fun SecToMin(sec: Double): Double{
                return sec / 60
            }

            fun SecToHour(sec: Double): Double{
                return sec / 3600
            }

            fun SecToDay(sec: Double): Double{
                return sec / BigDecimal("86400").toDouble()
            }

            fun SecToWeek(sec: Double): Double{
                return sec / BigDecimal("604800").toDouble()
            }

            fun SecToFortNght(sec: Double): Double{
                return sec / BigDecimal("1210000").toDouble()
            }

            fun SecToMon(sec: Double): Double{
                return sec / BigDecimal("2628000").toDouble()
            }

            fun SecToYear(sec: Double): Double{
                return sec / BigDecimal("31540000").toDouble()
            }

            fun SecToDec(sec: Double): Double{
                return sec / BigDecimal("315400000").toDouble()
            }

            fun SecToCen(sec: Double): Double{
                return sec / BigDecimal("3154000000").toDouble()
            }

            fun SecToMill(sec: Double): Double{
                return sec / BigDecimal("31540000000").toDouble()
            }

            fun SecToGalYear(sec: Double): Double{
                return sec / BigDecimal("7253280000000000").toDouble()
            }
        }
    }

    class Angle{
        companion object{
            var from = 0
            var to = 0
            val pi = 3.14

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> DegreesToRadian(value)
                    1 -> value
                    2 -> GradiansToRadian(value)
                    3 -> TurnsToRadian(value)
                    4 -> MilliRadToRadian(value)
                    5 -> MinuteOfArcToRadian(value)
                    6 -> SecondOfArcToRadian(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> RadianToDegrees(temp)
                    2 -> RadianToGradians(temp)
                    3 -> RadianToTurns(temp)
                    4 -> RadianToMilliRad(temp)
                    5 -> RadianToMinuteOfArc(temp)
                    6 -> RadianToSecondOfArc(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun DegreesToRadian(degree: Double): Double{
                return degree * pi/180
            }

            fun GradiansToRadian(grad: Double): Double{
                return grad * pi/200
            }

            fun TurnsToRadian(turn: Double): Double{
                return turn * 2 * pi
            }

            fun MilliRadToRadian(milli: Double): Double{
                return milli * 0.001
            }

            fun MinuteOfArcToRadian(minarc: Double): Double{
                return minarc * pi/(60 * 180)
            }

            fun SecondOfArcToRadian(secarc: Double): Double{
                return secarc * pi/(180 * 3600)
            }

            // Order 2 - Radians to others

            fun RadianToDegrees(radian: Double): Double{
                return radian * 180/pi
            }

            fun RadianToGradians(radian: Double): Double{
                return radian * 200/pi
            }

            fun RadianToTurns(radian: Double): Double{
                return radian / (2 * pi)
            }

            fun RadianToMilliRad(radian: Double): Double{
                return radian * 1000
            }

            fun RadianToMinuteOfArc(radian: Double): Double{
                return radian * (60 * 180)/pi
            }

            fun RadianToSecondOfArc(radian: Double): Double{
                return radian * (3600 * 180)/pi
            }
        }
    }

    class Data{
        companion object{
            var from = 0
            var to = 0

            fun convert(from: Int, to: Int, value: Double): Double{
                this.from = from
                this.to = to

                return if(from == to){
                    value
                } else{
                    convHandler(value)
                }
            }

            private fun convHandler(value: Double): Double{
                var temp = when(from){
                    0 -> BitToKiloBytes(value)
                    1 -> NibbleToKiloBytes(value)
                    2 -> CrumbToKiloBytes(value)
                    3 -> BytesToKiloBytes(value)
                    4 -> KiloBitToKiloBytes(value)
                    5 -> KibiBitToKiloBytes(value)
                    6 -> value
                    7 -> KibiByteToKiloBytes(value)
                    8 -> MegaBitToKiloBytes(value)
                    9 -> MebiBitToKiloBytes(value)
                    10 -> MegaByteToKiloBytes(value)
                    11 -> MebiBytesToKiloBytes(value)
                    12 -> GigaBitToKiloBytes(value)
                    13 -> GibiBitToKiloBytes(value)
                    14 -> GigaByteToKiloBytes(value)
                    15 -> GibiByteToKiloBytes(value)
                    16 -> TeraBitToKiloBytes(value)
                    17 -> TebiBitToKiloBytes(value)
                    18 -> TeraByteToKiloBytes(value)
                    19 -> TebiByteToKiloBytes(value)
                    20 -> PetaBitToKiloBytes(value)
                    21 -> PebiBitToKiloBytes(value)
                    22 -> PetaByteToKiloBytes(value)
                    23 -> PebiByteToKiloBytes(value)
                    24 -> ExaBitToKiloBytes(value)
                    25 -> ExbiBitToKiloBytes(value)
                    26 -> ExaByteToKiloBytes(value)
                    27 -> ExbiByteToKiloBytes(value)
                    28 -> ZetaBitToKiloBytes(value)
                    29 -> ZebiBitToKiloBytes(value)
                    30 -> ZetaByteToKiloBytes(value)
                    31 -> ZebiByteToKiloBytes(value)
                    32 -> YottaBitToKiloBytes(value)
                    33 -> YobiBitToKiloBytes(value)
                    34 -> YottaByteToKiloBytes(value)
                    35 -> YobiByteToKiloBytes(value)
                    else -> {
                        value
                    }
                }

                temp = when(to){
                    0 -> KiloBytesToBit(temp)
                    1 -> KiloBytesToNibble(temp)
                    2 -> KiloBytesToCrumb(temp)
                    3 -> KiloBytesToBytes(temp)
                    4 -> KiloBytesToKiloBit(temp)
                    5 -> KiloBytesToKibiBit(temp)
                    7 -> KiloBytesToKibiByte(temp)
                    8 -> KiloBytesToMegaBit(temp)
                    9 -> KiloBytesToMebiBit(temp)
                    10 -> KiloBytesToMegaByte(temp)
                    11 -> KiloBytesToMebiBytes(temp)
                    12 -> KiloBytesToGigaBit(temp)
                    13 -> KiloBytesToGibiBit(temp)
                    14 -> KiloBytesToGigaByte(temp)
                    15 -> KiloBytesToGibiByte(temp)
                    16 -> KiloBytesToTeraBit(temp)
                    17 -> KiloBytesToTebiBit(temp)
                    18 -> KiloBytesToTeraByte(temp)
                    19 -> KiloBytesToTebiByte(temp)
                    20 -> KiloBytesToPetaBit(temp)
                    21 -> KiloBytesToPebiBit(temp)
                    22 -> KiloBytesToPetaByte(temp)
                    23 -> KiloBytesToPebiByte(temp)
                    24 -> KiloBytesToExaBit(temp)
                    25 -> KiloBytesToExbiBit(temp)
                    26 -> KiloBytesToExaByte(temp)
                    27 -> KiloBytesToExbiByte(temp)
                    28 -> KiloBytesToZetaBit(temp)
                    29 -> KiloBytesToZebiBit(temp)
                    30 -> KiloBytesToZetaByte(temp)
                    31 -> KiloBytesToZebiByte(temp)
                    32 -> KiloBytesToYottaBit(temp)
                    33 -> KiloBytesToYobiBit(temp)
                    34 -> KiloBytesToYottaByte(temp)
                    35 -> KiloBytesToYobiByte(temp)
                    else -> {
                        temp
                    }
                }

                return temp
            }

            fun BitToKiloBytes(data: Double): Double{
                return data * 0.000125
            }

            fun NibbleToKiloBytes(data: Double): Double{
                return data * 0.0005
            }

            fun CrumbToKiloBytes(data: Double): Double{
                return data * 0.00025
            }

            fun BytesToKiloBytes(data: Double): Double{
                return data * 0.001
            }

            // k

            fun KiloBitToKiloBytes(data: Double): Double{
                return data * 0.125
            }

            fun KibiBitToKiloBytes(data: Double): Double{
                return data * 0.128
            }

            fun KibiByteToKiloBytes(data: Double): Double{
                return data * 1.024
            }

            // m

            fun MegaBitToKiloBytes(data: Double): Double{
                return data * 125
            }

            fun MebiBitToKiloBytes(data: Double): Double{
                return data * 131.072
            }

            fun MegaByteToKiloBytes(data: Double): Double{
                return data * 1000
            }

            fun MebiBytesToKiloBytes(data: Double): Double{
                return data * 1048.58
            }

            // g

            fun GigaBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("125000").toDouble()
            }

            fun GibiBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("134218").toDouble()
            }

            fun GigaByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1000000").toDouble()
            }

            fun GibiByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1074000").toDouble()
            }

            // t

            fun TeraBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("125000000").toDouble()
            }

            fun TebiBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("137400000").toDouble()
            }

            fun TeraByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1000000000").toDouble()
            }

            fun TebiByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1100000000").toDouble()
            }

            // p
            fun PetaBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("125000000000").toDouble()
            }

            fun PebiBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("140700000000").toDouble()
            }

            fun PetaByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1000000000000").toDouble()
            }

            fun PebiByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1126000000000").toDouble()
            }

            // e
            fun ExaBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("125000000000000").toDouble()
            }

            fun ExbiBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("144115188075856").toDouble()
            }

            fun ExaByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1000000000000000").toDouble()
            }

            fun ExbiByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1152922000000000").toDouble()
            }

            //z
            fun ZetaBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("125000000000000000").toDouble()
            }

            fun ZebiBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("147574000000000000").toDouble()
            }

            fun ZetaByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1000000000000000000").toDouble()
            }

            fun ZebiByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1180592000000000000").toDouble()
            }

            //y
            fun YottaBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("125000000000000000000").toDouble()
            }

            fun YobiBitToKiloBytes(data: Double): Double{
                return data * BigDecimal("151115700000000000000").toDouble()
            }

            fun YottaByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1000000000000000000000").toDouble()
            }

            fun YobiByteToKiloBytes(data: Double): Double{
                return data * BigDecimal("1208926000000000000000").toDouble()
            }

            // Order 2 Kilobytes to others

            fun KiloBytesToBit(data: Double): Double{
                return data / 0.000125
            }

            fun KiloBytesToNibble(data: Double): Double{
                return data / 0.0005
            }

            fun KiloBytesToCrumb(data: Double): Double{
                return data / 0.00025
            }

            fun KiloBytesToBytes(data: Double): Double{
                return data / 0.001
            }

            // k

            fun KiloBytesToKiloBit(data: Double): Double{
                return data / 0.125
            }

            fun KiloBytesToKibiBit(data: Double): Double{
                return data / 0.128
            }

            fun KiloBytesToKibiByte(data: Double): Double{
                return data / 1.024
            }

            // m

            fun KiloBytesToMegaBit(data: Double): Double{
                return data / 125
            }

            fun KiloBytesToMebiBit(data: Double): Double{
                return data / 131.072
            }

            fun KiloBytesToMegaByte(data: Double): Double{
                return data / 1000
            }

            fun KiloBytesToMebiBytes(data: Double): Double{
                return data / 1048.58
            }

            // g

            fun KiloBytesToGigaBit(data: Double): Double{
                return data / BigDecimal("125000").toDouble()
            }

            fun KiloBytesToGibiBit(data: Double): Double{
                return data / BigDecimal("134218").toDouble()
            }

            fun KiloBytesToGigaByte(data: Double): Double{
                return data / BigDecimal("1000000").toDouble()
            }

            fun KiloBytesToGibiByte(data: Double): Double{
                return data / BigDecimal("1074000").toDouble()
            }

            // t

            fun KiloBytesToTeraBit(data: Double): Double{
                return data / BigDecimal("125000000").toDouble()
            }

            fun KiloBytesToTebiBit(data: Double): Double{
                return data / BigDecimal("137400000").toDouble()
            }

            fun KiloBytesToTeraByte(data: Double): Double{
                return data / BigDecimal("1000000000").toDouble()
            }

            fun KiloBytesToTebiByte(data: Double): Double{
                return data / BigDecimal("1100000000").toDouble()
            }

            // p
            fun KiloBytesToPetaBit(data: Double): Double{
                return data / BigDecimal("125000000000").toDouble()
            }

            fun KiloBytesToPebiBit(data: Double): Double{
                return data / BigDecimal("140700000000").toDouble()
            }

            fun KiloBytesToPetaByte(data: Double): Double{
                return data / BigDecimal("1000000000000").toDouble()
            }

            fun KiloBytesToPebiByte(data: Double): Double{
                return data / BigDecimal("1126000000000").toDouble()
            }

            // e
            fun KiloBytesToExaBit(data: Double): Double{
                return data / BigDecimal("125000000000000").toDouble()
            }

            fun KiloBytesToExbiBit(data: Double): Double{
                return data / BigDecimal("144115188075856").toDouble()
            }

            fun KiloBytesToExaByte(data: Double): Double{
                return data / BigDecimal("1000000000000000").toDouble()
            }

            fun KiloBytesToExbiByte(data: Double): Double{
                return data / BigDecimal("1152922000000000").toDouble()
            }

            //z
            fun KiloBytesToZetaBit(data: Double): Double{
                return data / BigDecimal("125000000000000000").toDouble()
            }

            fun KiloBytesToZebiBit(data: Double): Double{
                return data / BigDecimal("147574000000000000").toDouble()
            }

            fun KiloBytesToZetaByte(data: Double): Double{
                return data / BigDecimal("1000000000000000000").toDouble()
            }

            fun KiloBytesToZebiByte(data: Double): Double{
                return data / BigDecimal("1180592000000000000").toDouble()
            }

            //y
            fun KiloBytesToYottaBit(data: Double): Double{
                return data / BigDecimal("125000000000000000000").toDouble()
            }

            fun KiloBytesToYobiBit(data: Double): Double{
                return data / BigDecimal("151115700000000000000").toDouble()
            }

            fun KiloBytesToYottaByte(data: Double): Double{
                return data / BigDecimal("1000000000000000000000").toDouble()
            }

            fun KiloBytesToYobiByte(data: Double): Double{
                return data / BigDecimal("1208926000000000000000").toDouble()
            }
        }
    }
}