package fr.angel.dynamicisland.plugins

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import org.junit.Assert.assertEquals
import org.junit.Test

class PluginManagerTest {

	private val notification = TestPlugin("notification")
	private val media = TestPlugin("media")
	private val battery = TestPlugin("battery")
	private val catalog = listOf(notification, media, battery)

	@Test
	fun calculateInsertIndex_prioritizesEarlierPlugin() {
		val active = listOf(media, battery)

		val insertAt = PluginManager.calculateInsertIndex(active, notification, catalog)

		assertEquals(0, insertAt)
	}

	@Test
	fun calculateInsertIndex_insertsBetweenHigherAndLowerPriorityPlugins() {
		val active = listOf(notification, battery)

		val insertAt = PluginManager.calculateInsertIndex(active, media, catalog)

		assertEquals(1, insertAt)
	}

	@Test
	fun calculateInsertIndex_appendsUnknownPlugin() {
		val active = listOf(notification, media)

		val insertAt = PluginManager.calculateInsertIndex(active, TestPlugin("unknown"), catalog)

		assertEquals(active.size, insertAt)
	}

	private class TestPlugin(
		override val id: String,
	) : BasePlugin() {
		override val name: String = id
		override val description: String = id
		override val permissions: ArrayList<String> = arrayListOf()
		override var enabled = mutableStateOf(false)
		override var pluginSettings = mutableMapOf<String, PluginSettingsItem>()

		override fun canExpand(): Boolean = false
		override fun onPluginCreate() = Unit

		@OptIn(ExperimentalSharedTransitionApi::class)
		@Composable
		override fun Composable(
			sharedTransitionScope: SharedTransitionScope,
			animatedContentScope: AnimatedContentScope
		) = Unit

		override fun onClick() = Unit
		override fun onDestroy() = Unit

		@Composable
		override fun PermissionsRequired() = Unit

		@OptIn(ExperimentalSharedTransitionApi::class)
		@Composable
		override fun LeftOpenedComposable(
			sharedTransitionScope: SharedTransitionScope,
			animatedContentScope: AnimatedContentScope
		) = Unit

		@OptIn(ExperimentalSharedTransitionApi::class)
		@Composable
		override fun RightOpenedComposable(
			sharedTransitionScope: SharedTransitionScope,
			animatedContentScope: AnimatedContentScope
		) = Unit

		override fun onRightSwipe() = Unit
		override fun onLeftSwipe() = Unit
	}
}
