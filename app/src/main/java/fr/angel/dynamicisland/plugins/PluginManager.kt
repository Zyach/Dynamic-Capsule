package fr.angel.dynamicisland.plugins

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import fr.angel.dynamicisland.plugins.battery.BatteryPlugin
import fr.angel.dynamicisland.plugins.media.MediaSessionPlugin
import fr.angel.dynamicisland.plugins.notification.NotificationPlugin

class PluginManager(
    private val context: Context,
    private val host: PluginHost,
    private val allPlugins: List<BasePlugin> = defaultPlugins(),
) {
    val activePlugins = mutableStateListOf<BasePlugin>()

    fun initialize() {
        allPlugins.forEach { plugin ->
            plugin.enabled.value = plugin.isPluginEnabled(context)
            if (plugin.active) {
                plugin.onCreate(host)
            }
        }
    }

    fun onDestroy() {
        allPlugins.forEach { it.onDestroy() }
    }

    fun requestDisplay(plugin: BasePlugin) {
        if (!activePlugins.contains(plugin)) {
            val insertAt = calculateInsertIndex(activePlugins, plugin, allPlugins)
            activePlugins.add(insertAt, plugin)
        }
    }

    fun requestDismiss(plugin: BasePlugin) {
        activePlugins.remove(plugin)
    }

    companion object {
        fun defaultPlugins(): List<BasePlugin> = listOf(
            NotificationPlugin(),
            MediaSessionPlugin(),
            BatteryPlugin()
        )

        internal fun calculateInsertIndex(
            activePlugins: List<BasePlugin>,
            plugin: BasePlugin,
            allPlugins: List<BasePlugin>
        ): Int {
            val pluginIndex = allPlugins.indexOfFirst { it.id == plugin.id }
            if (pluginIndex == -1) return activePlugins.size

            var insertAt = activePlugins.size
            for (i in activePlugins.indices) {
                val activeIndex = allPlugins.indexOfFirst { it.id == activePlugins[i].id }
                if (activeIndex == -1 || pluginIndex < activeIndex) {
                    insertAt = i
                    break
                }
            }
            return insertAt
        }
    }
}
