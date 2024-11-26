package yetzio.yetcalc.dialogs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import yetzio.yetcalc.R
import yetzio.yetcalc.utils.getThemeColor

fun showEasterEggDialog(ctx: Context) {
    val dialogView = LayoutInflater.from(ctx).inflate(R.layout.easter_egg, null)

    val dialog = MaterialAlertDialogBuilder(ctx)
        .setView(dialogView)
        .setCancelable(true)
        .create()

    val constLayout: ConstraintLayout = dialogView.findViewById(R.id.easterEggFrameContainer)

    val bitmap = BitmapFactory.decodeStream(ctx.assets.open("eastereggbg.png"))

    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, ctx.resources.displayMetrics.widthPixels, bitmap.height * ctx.resources.displayMetrics.widthPixels / bitmap.width, true)

    val drawable = BitmapDrawable(ctx.resources, scaledBitmap)
    constLayout.background = drawable

    dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    DynamicToast.make(ctx, ctx.getString(R.string.eastereggtoast), ContextCompat.getDrawable(ctx, R.drawable.gigachad), ctx.getThemeColor(R.attr.calcTextDefaultColor), ctx.getThemeColor(R.attr.calcBackgroundDefault), Toast.LENGTH_LONG).show()
    dialog.show()
}


