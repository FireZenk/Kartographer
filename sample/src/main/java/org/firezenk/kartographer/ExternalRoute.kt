package org.firezenk.kartographer

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import org.firezenk.kartographer.library.ExternalRoutable
import java.util.*

class ExternalRoute : ExternalRoutable {

    companion object {
        // Google play http market address
        private val MARKET_GOOGLE_PLAY = "market://details?id="
        // Google play http address
        private val URL_HTTP_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id="
    }

    override fun route(context: Any, uuid: UUID, parameters: Any) {
        if (context is Context) {
            val packageName = context.packageName
            try {
                launchUri(context, Uri.parse(MARKET_GOOGLE_PLAY + packageName))
            } catch (_: ActivityNotFoundException) {
                launchUri(context, Uri.parse(URL_HTTP_GOOGLE_PLAY + packageName))
            }
        }
    }

    private fun launchUri(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (context !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}