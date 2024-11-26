package yetzio.yetcalc.config

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import yetzio.yetcalc.R
import yetzio.yetcalc.views.CalculatorActivity
import yetzio.yetcalc.views.ProgramCalcActivity
import yetzio.yetcalc.views.UnitConvActivity
import android.app.PendingIntent
import android.content.res.Configuration
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs

class ShortcutManager(private val ctx: Context) {
    val shortcutManager = ctx.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
    val sharedPrefs = ctx.getDefSharedPrefs()

    val currentTheme = sharedPrefs.getString(SharedPrefs.THEMEKEY, ctx.getString(R.string.system_theme))
    val currentThemeConfig = when(currentTheme){
        ctx.getString(R.string.system_theme) -> {
            val nightModeFlags: Int = ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    ctx.getString(R.string.dark_theme)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    ctx.getString(R.string.light_theme)
                }
                else -> {
                    ctx.getString(R.string.dark_theme)
                }
            }
        }
        ctx.getString(R.string.dark_theme) -> {
            ctx.getString(R.string.dark_theme)
        }
        ctx.getString(R.string.abyss_theme) -> {
            ctx.getString(R.string.abyss_theme)
        }
        ctx.getString(R.string.light_theme) -> {
            ctx.getString(R.string.light_theme)
        }
        ctx.getString(R.string.materialyou_theme) -> {
            ctx.getString(R.string.materialyou_theme)
        }
        else -> {
            ctx.getString(R.string.dark_theme)
        }
    }

    val calculatorIcon = when(currentThemeConfig){
        ctx.getString(R.string.dark_theme) -> {
            R.drawable.ic_baseline_calculate_24
        }
        ctx.getString(R.string.light_theme) -> {
            R.drawable.round_calculate_24light
        }
        ctx.getString(R.string.abyss_theme) -> {
            R.drawable.round_calculate_24abyss
        }
        ctx.getString(R.string.materialyou_theme) -> {
            R.mipmap.ic_calcshortcut_round
        }
        else -> {
            R.drawable.ic_baseline_calculate_24
        }
    }

    val unitConvIcon = when(currentThemeConfig){
        ctx.getString(R.string.dark_theme) -> {
            R.drawable.ic_baseline_cyclone_24
        }
        ctx.getString(R.string.light_theme) -> {
            R.drawable.round_cyclone_24light
        }
        ctx.getString(R.string.abyss_theme) -> {
            R.drawable.round_cyclone_24abyss
        }
        ctx.getString(R.string.materialyou_theme) -> {
            R.mipmap.ic_unitshortcut_round
        }
        else -> {
            R.drawable.ic_baseline_cyclone_24
        }
    }

    val pgIcon = when(currentThemeConfig){
        ctx.getString(R.string.dark_theme) -> {
            R.drawable.ic_baseline_code_24
        }
        ctx.getString(R.string.light_theme) -> {
            R.drawable.round_code_24light
        }
        ctx.getString(R.string.abyss_theme) -> {
            R.drawable.round_code_24abyss
        }
        ctx.getString(R.string.materialyou_theme) -> {
            R.mipmap.ic_pgshortcut_round
        }
        else -> {
            R.drawable.ic_baseline_code_24
        }
    }

    val calcShortId = "calculator_shortcut"
    val convShortId = "converter_shortcut"
    val pgShortId = "programmer_shortcut"

    fun removeAllShortcuts(){
        shortcutManager.removeAllDynamicShortcuts()
    }

    fun createShortcut(calcView: CalcView) {
        val shortCutId = when(calcView){
            CalcView.CALCULATOR -> calcShortId
            CalcView.CONVERTER -> convShortId
            CalcView.PROGRAMMER -> pgShortId
        }

        val imgRes = when(calcView){
            CalcView.CALCULATOR -> calculatorIcon
            CalcView.CONVERTER -> unitConvIcon
            CalcView.PROGRAMMER -> pgIcon
        }

        val shortText = when(calcView){
            CalcView.CALCULATOR -> ctx.getString(R.string.calculator)
            CalcView.CONVERTER -> ctx.getString(R.string.converter)
            CalcView.PROGRAMMER -> ctx.getString(R.string.programmer)
        }

        if (shortcutManager.isRequestPinShortcutSupported) {
            val shortcutInfo = ShortcutInfo.Builder(ctx, shortCutId)
                .setShortLabel(shortText)
                .setLongLabel(shortText)
                .setIcon(when(currentThemeConfig){
                    ctx.getString(R.string.materialyou_theme) -> {
                        Icon.createWithResource(ctx, imgRes)
                    }
                    else -> {
                        Icon.createWithResource(ctx, imgRes)
                    }
                })
                .setIntent(
                    when(calcView){
                        CalcView.CALCULATOR -> {
                            Intent(ctx, CalculatorActivity::class.java).apply {
                                action = Intent.ACTION_VIEW
                            }
                        }
                        CalcView.CONVERTER -> {
                            Intent(ctx, UnitConvActivity::class.java).apply {
                                action = Intent.ACTION_VIEW
                            }
                        }
                        CalcView.PROGRAMMER -> {
                            Intent(ctx, ProgramCalcActivity::class.java).apply {
                                action = Intent.ACTION_VIEW
                            }
                        }
                    }

                )
                .build()

            val pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(shortcutInfo)
            val successCallback = PendingIntent.getBroadcast(ctx, 0,
                pinnedShortcutCallbackIntent, PendingIntent.FLAG_IMMUTABLE
            )

            shortcutManager.requestPinShortcut(shortcutInfo, successCallback.intentSender)
        }
    }

    fun addAllShortcuts(){
        val calcShortcut = ShortcutInfo.Builder(ctx, calcShortId)
            .setShortLabel(ctx.getString(R.string.calculator))
            .setIcon(when(currentThemeConfig){
                ctx.getString(R.string.materialyou_theme) -> {
                    Icon.createWithResource(ctx, calculatorIcon)
                }
                else -> {
                    Icon.createWithResource(ctx, calculatorIcon)
                }
            })
            .setIntent(Intent(ctx, CalculatorActivity::class.java).setAction(Intent.ACTION_VIEW))
            .build()

        val unitShortcut = ShortcutInfo.Builder(ctx, convShortId)
            .setShortLabel(ctx.getString(R.string.converter))
            .setIcon(when(currentThemeConfig){
                ctx.getString(R.string.materialyou_theme) -> {
                    Icon.createWithResource(ctx, unitConvIcon)
                }
                else -> {
                    Icon.createWithResource(ctx, unitConvIcon)
                }
            })
            .setIntent(Intent(ctx, UnitConvActivity::class.java).setAction(Intent.ACTION_VIEW))
            .build()

        val pgShortcut = ShortcutInfo.Builder(ctx, pgShortId)
            .setShortLabel(ctx.getString(R.string.programmer))
            .setIcon(when(currentThemeConfig){
                ctx.getString(R.string.materialyou_theme) -> {
                    Icon.createWithResource(ctx, pgIcon)
                }
                else -> {
                    Icon.createWithResource(ctx, pgIcon)
                }
            })
            .setIntent(Intent(ctx, ProgramCalcActivity::class.java).setAction(Intent.ACTION_VIEW))
            .build()

        val shortcuts = listOf(
            calcShortcut, unitShortcut, pgShortcut
        )

        shortcutManager.addDynamicShortcuts(shortcuts)
    }
}