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
    
4. Can't get right answers from 0.1 + 0.2/there is a precision problem/I get answers like 0.30000000000000004/What is Canonical Rounding? Why is it disabled by default?
    
    Issues like 0.1 + 0.2 or 0.1 + 0.1 + 0.1 might equal 0.30000000000000004 instead of 0.3. This can be solved by enabling canonical rounding 
    in Settings -> Canonical Rounding, but there is a risk of using it as well. Calculations including very small numbers, for example 
    0.00000000003 * 0.00000000004 might yield 0 even on a higher precision setting. Therefore canonical rounding is disabled by default. 
    This is a setting taken directly from mXparser and as the docs say:
    
        It solves a huge part of the problem, but comes at a cost in computing performance.

5. Percentages do not work properly?

   Percentages in calculator work in a more traditional and mathematical way. By default, percentage is calculated in 1. Some users have this
   doubt because they come from using different calculators. To fix this for an example like 1000-90%, convert to this: 1000-90%*1000. As you can see,
   you need to multiply it with the required number to get the right percentage result.
   
   **NOTE: This is required only for additions/subtractions. While multiplying or dividing, the usual way will provide the correct result. For example: 150/3% or 5*150%**

6. The screen gets cuts off and the buttons look weird?

    You might have a really small screen to support this app. Minimum 5 or 5.5 inches is required since the buttons, result, progressive result tab take up space. We are extremely sorry. Nowadays, vendors provide devices with larger screen sizes so this is not a concerning issue but still this feels bad.
    
    NOTE: On taller screens and foldables, only the buttons may look weird(like crushed or oval shaped). We promise to provide a layout with support for larger screens/resolutions.

## Converter

1. Does the converter require an Internet connections?

    An internet connection is only required for currency conversions and nothing else. All other conversions should work fine without it.

2. Currency conversion takes longer than others?

    Yes, this is because when you enter data, an API request is made to [fawazahmed0/currency-api](https://github.com/fawazahmed0/currency-api) So this depends upon your internet connection speed, the value entered, the currencies selected on both sides.

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
    
3. Why did you create this? There's already so many good calculators.
    
    Well, there are actually. But maybe most of them are not open source or provide a necessary feature.
    Or one might provide a feature the other doesn't.
    
    Just so you know, this is going to be long...:)
    
    To be honest, I had Google's Calculator by default on my phone. It's pretty good but not that great either.
    It doesn't provide progressive calculations(on scientific mode), more scientific functions, constants and features. It's like a simple enough calculator and don't tell me Google can't make a better calculator than       that, oh wait took them years to add a screen recording feature in Android, can't create their own terminal emulator, well there's Termux, so why need anyway right. Aha, back to the point.
    Either way the UI is pretty great tho, that's what yetCalc's design is based on.
    
    Very well, why didn't I use some other vendor's calculator, like Samsung or Xiaomi. Yea Samsung's Calculator is pretty good and Xiaomi's one is even better. Samsung's one provides progressive calculations, simple        and scientific features, history, a good converter etc. But it suffers from the same things Google's does, more effort has not been put into adding more scientific features like more functions, constants. As a     
    company they could have added a graphing, programmer modes as well. Well, its not my calculator right?
    
    Talking about Xiaomi's one, this one's a pretty good one too, provides even more converters. But on the other side, the same mistake to not add more scientific features like the one's I mentioned for Samsung's and       Google's one. Just look at this [calculator](https://play.google.com/store/apps/details?id=cz.hipercalc) and see how many features it contains, even has a pro version or look at this [one](https://play.google.com/store/apps/details?id=org.mathparser.scalar.lite&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1). Now that's what I call a good calculator, it might not have everything but         atleast some effort is put into it. You can argue it doesn't have a user/beginner-friendly UI for someone coming from a different calculator, and that's a valid point too.
    
    Now if you want to look at a good calculator which is user-friendly and provides a ton of features, look at Microsoft's new [Windows Calculator](https://apps.microsoft.com/store/detail/windows-calculator/9WZDNCRFHVN5). It even has date calculation and graphing, some which I didn't even add to yetCalc(at the time of writing). It's even open source on [github](https://github.com/Microsoft/calculator)
    
    In the end, I created this because I needed a calculator with a simple UI like Google's one with features that are usually and should be provided, more features that are typically not on Android or others. It didn't     become so simple or advanced but it was worth it. It's free, it's open source and it's yet another calculator.

(you can notify us if an issue is not mentioned here!)
