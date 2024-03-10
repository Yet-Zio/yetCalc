<div align="right">

[![License](https://img.shields.io/badge/License-BSD_3--Clause-yellow.svg)](https://github.com/Yet-Zio/yetCalc/blob/main/LICENSE)
[![GitHub Release](https://img.shields.io/github/release/Yet-Zio/yetCalc.svg?style=flat)]()
[![Donate](https://img.shields.io/badge/$-support-ff69b4.svg?style=flat)](https://www.buymeacoffee.com/yetzio)

</div>
<p align="center">
  <img src="./img/icons/appico.png?raw=true" alt="yetCalc Icon" width="60" height="60"/>
</p>

<h1 align="center">yetCalc</h1>

The free and open source calculator developed for Android 5.0 and above using Kotlin. yetCalc's calculator mode uses the [mXparser](https://mathparser.org/) library. Large calculations are handled without device freezing and other issues, although some calculations might take time. 

![yetCalc About](./img/snaps/calcbrand.png)

## Modes

- (🔢)Basic Calculator
- (🧑‍🔬)Scientific Calculator
- (📏)Unit Converter
- (👨‍💻)Programmer

## Features

- Themes
    - System Theme(default)
    - Dark mode🌃
    - Light mode🏙️


- Calculator

    <p align="center">
        <img src="./img/snaps/snap2.png?raw=true" alt="yetCalc Landscape" width="575" height="279"/>
    </p>
    <p align="center">
        <img src="./img/snaps/lightsnap2.png?raw=true" alt="yetCalc Landscape" width="575" height="279"/>
    </p>

    - Support for all basic operators.
    - Progressive calculations.```(results appear on top and change side by side as you change the expression)```
    - Provides commonly used constants.```(Some constants might result to 0 if precision setting is not high)```
    - Switch between different angle measurements with ease.
    - Scientific Mode that provides various trigonometric, hyperbolic and inverse functions, calculus and iterated operators, much more.
    - Provides variable support.```(only 1 variable)```
    - Precision settings for precise calculations.
    - Operations like copy, cut, paste, delete are handled and results are changed accordingly.
    - Vibrations when clicking buttons?(don't know if I should have mentioned it here)

    <br>

    **DO NOTE**: Inorder to use scientific features, the device should be in landscape mode.

- Unit Converter
    - **Currency**: Conversions are handled through [fawazahmed0/currency-api](https://github.com/fawazahmed0/currency-api) and requires an Internet connection.
    - **Length**: Conversions to and from Femtometres, Picometres, Nanometres, Micrometres, Millimetres, Centimetres, Decimetres, Metres, Decametres, Hectometres, Kilometres, Inches, Feet, Yards, Miles, Nautical Miles, Megametres, Gigametres, Terametres, Petametres, Furlongs
    - **Volume**: Conversions to and from Microlitres, Drops, Millilitres, Cubic centimetres, Decilitres, Litres, Cubic metres, Cups(US), Cups(UK), Pints(US), Pints(UK), Quarts(US), Quarts(UK), Gallons(US), Gallons(UK), Dry, Liquid, Federal and Oil Barrels(US), Barrels(UK), Cubic inches, Cubic feet, Cubic yards, Teaspoons(US), Teaspoons(UK), Tablespoons(US), Tablespoons(UK), Fluid Ounces(US), Fluid Ounces(UK)
    - **Area**: Conversions to and from Square millimetres, Square centimetres, Square decimetres, Square metres, Square decametres, Square hectometres, Square kilometres, Hectares, Square miles, Acres, Square inches, Square feet, Square yards, Square nautical miles, Dunams, Tsubos, Pyeong, Cuerdas, Square megametres, Square gigametres, Square terametres, Square petametres
    - **Weight/Mass**: Conversions to and from Femtograms, Picograms, Nanograms, Micrograms, Carats, Milligrams, Centigrams, Decigrams, Grams, Decagrams, Hectograms, Kilograms, Newtons(Earth), Tonnes, Ounces, Pounds, Stones, Tons(US), Tons(UK), Catties, Grains, Gigagrams, Teragrams, Petagrams
    - **Temperature**: Conversions to and from Celsius, Fahrenheit, Kelvin, Rankine scale, Delisle scale, Newton scale, Réaumur scale, Rømer scale, Yoctokelvin, Attokelvin, Femtokelvin, Picokelvin, Nanokelvin, Microkelvin, Millikelvin, Kilokelvin, Megakelvin, Gigakelvin, Terakelvin, Petakelvin, Exakelvin, Zettakelvin, Yottakelvin
    - **Speed**: Conversions to and from Centimetres per second, Metres per second, Kilometres per hour, Miles per hour, Knots, Feet per second, Feet per minute, Inch per second, Mach, Furlongs per Fortnight, Bubnoff units, Natural units(speed of light)
    - **Power**: Conversions to and from Watts, Kilowatts, Horsepower, Ergs per second, Foot-pounds per minute, Decibel-milliwatts, Calories per hour, BTUs per hour, Tons of refrigeration.
    - **Energy**: Conversions to and from Electron volts, Joules, Kilojoules, Ergs, Calories, British Thermal Units, Kilowatt hours, Kilocalories, Foot-pounds
    - **Pressure**: Conversions to and from Atmospheres, Torrs, Pascals, Kilopascals, Kips per square inch, Pounds per square inch, Feet Sea Water(15° C), Bars, Kilograms per square centimetre, Baryes, Sthenes, Millimetres of mercury
    - **Time**: Conversions to and from Nanoseconds, Microseconds, Milliseconds, Jiffies, Seconds, Minutes, Hours, Days, Weeks, Fortnights, Months, Years, Decades, Centuries, Milleniums, Galactic years
    - **Angle**: Degrees, Radians, Gradians, Turns, Milliradians, Minutes of arc, Seconds of arc
    - **Data**: Bits, Nibbles, Crumbs, Bytes, Kilobits, Kibibits, Megabits, Mebibits, Gigabits, Gibibits, Terabits, Tebibits, Petabits, Pebibits, Exabits, Exbibits, Zetabits, Zebibits, Yottabits, Yobibits, Kilobytes, Kibibytes, Megabytes, Mebibytes, Gigabytes, Gibibytes, Terabytes, Tebibytes, Petabytes, Pebibytes, Exabytes, Exbibytes, Zetabytes, Zebibytes, Yottabytes, Yobibytes

- Programmer(Extended from this [implementation](https://github.com/agam23/programmers-calculator-in-kotlin))
    - Provides all basic operators like +, -, ×, ÷
    - Provides logical operators like AND, OR, NOT, NAND, NOR, XOR
    - Provides operations like left shift(<<) and right shift(>>)
    - Support for commonly used number systems: Binary, Octal, Decimal and Hexadecimal
    

## Install

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/yetzio.yetcalc/)


Or download the app from the [releases page](https://github.com/Yet-Zio/yetCalc/releases/latest).

Note: Requires Android 5.0 and above.

Also, only download the app from sources provided by the developer.

## Donations

To donate, check out [Donate.md](./DONATE.md)
