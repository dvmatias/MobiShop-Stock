package com.cmdv.core.helpers

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned

class HtmlHelper {

    companion object {
        fun fromHtml(stringResource: Int?, context: Context): Spanned =
            if (stringResource == null || stringResource == -1 || stringResource == 0) {
                SpannableString.valueOf("")
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    Html.fromHtml(context.getString(stringResource), Html.FROM_HTML_MODE_COMPACT)
                else
                    Html.fromHtml(context.getString(stringResource))
            }

        fun fromHtml(stringText: String?): Spanned =
            if (stringText == null || stringText == "") {
                SpannableString.valueOf("")
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    Html.fromHtml(stringText, Html.FROM_HTML_MODE_COMPACT)
                else
                    Html.fromHtml(stringText)
            }
    }

}
