# Help

## General

1. How to turn off vibrations/haptic feedback?

    Go to Settings -> Turn the switch off for disabling Haptic Feedback

## Calculator

1. How do I access Scientific mode?

    Scientific mode can only be viewed if you are in landscape orientation.

2. Some constants/values do not work or yield to 0?

    This happens when precision setting is low, some constants are so small that the current precision setting can't handle it. This does yield to 0.

    To fix this, Go to Settings -> Select Precision and change to either a higher one like 1e-60 or 1e-99.

3. What is Almost Int Rounding?

    Actually this is a feature of mXparser library and enabled by default, as explained from its docs:

        'Almost Integer Rounding – a number is rounded to the nearest integer if it is close enough to an integer. This is a fairly quick procedure, but you lose the ability to work with small numbers.'
    
    Normally this doesn't cause much issues for calculations, but if you are working with really small numbers you can toggle this setting from our app.

4. The screen gets cuts off and the buttons look weird?

    You might have a really small screen to support this app. Minimum 5 or 5.5 inches is required since the buttons, result, progressive result tab take up space. We are extremely sorry. Nowadays, vendors provide devices with larger screen sizes so this is not a concerning issue but still this feels bad.

## Converter

1. Does the converter require an Internet connections?

    An internet connection is only required for currency conversions and nothing else. All other conversions should work fine without it.

2. Currency conversion takes longer than others?

    Yes, this is because when you enter data, an API request is made to [exchangeratehost](https://exchangerate.host/#/). So this depends upon your internet connection speed, the value entered, the currencies selected on both sides.

3. Does the converter remember the last tab and units I used?

    Yes, the converter does remember the last tab you opened if you have enabled 'Use Recent Tab' in Settings. 
    However, the units are not remembered since there are some issues with spinner(combobox/unit selector).

4. Is there a limit to the unit you can type?

    Currently we haven't encountered such a thing.

5. I have an issue where there is 'no result/I can't type in the text input' especially on currency conversion tab.

    This is a very rare issue that is encountered on some devices although everything works fine on other devices running the same Android version, updates, etc.

    Currently there isn't a solution to this.

## Programmer

(no issue encountered thus far)

## Other

1. Is this app free? like really? and open source too?

    Yea it is and will always be that way, here's the repo: https://github.com/Yet-Zio/yetCalc
    Or you can just go to Settings -> About -> Tap View on Github

2. How do I donate?

    Go to https://github.com/Yet-Zio/yetCalc/blob/main/DONATE.md
    or Settings -> Tap Donate
    takes you to the same link anyways.

(you can notify us if an issue is not mentioned here!)
