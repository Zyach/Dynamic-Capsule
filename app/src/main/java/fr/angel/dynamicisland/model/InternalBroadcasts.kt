package fr.angel.dynamicisland.model

import android.content.Context
import android.content.Intent

fun Context.internalBroadcastIntent(action: String): Intent = Intent(action).setPackage(packageName)

fun Context.sendInternalBroadcast(action: String, extras: Intent.() -> Unit = {}) {
	val intent = internalBroadcastIntent(action).apply(extras)
	sendBroadcast(intent)
}
