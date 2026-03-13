package fr.angel.dynamicisland.island

import androidx.test.core.app.ApplicationProvider
import fr.angel.dynamicisland.model.AUTO_HIDE_OPENED_AFTER
import fr.angel.dynamicisland.model.CORNER_RADIUS
import fr.angel.dynamicisland.model.ENABLED_APPS
import fr.angel.dynamicisland.model.GRAVITY
import fr.angel.dynamicisland.model.POSITION_X
import fr.angel.dynamicisland.model.POSITION_Y
import fr.angel.dynamicisland.model.SETTINGS_KEY
import fr.angel.dynamicisland.model.SHOW_BORDER
import fr.angel.dynamicisland.model.SHOW_IN_LANDSCAPE
import fr.angel.dynamicisland.model.SHOW_ON_LOCK_SCREEN
import fr.angel.dynamicisland.model.SIZE_X
import fr.angel.dynamicisland.model.SIZE_Y
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IslandSettingsTest {

	@Test
	fun applySettings_persistsCurrentSnapshot() {
		val context = ApplicationProvider.getApplicationContext<android.content.Context>()
		val settings = IslandSettings()

		settings.positionX = 12
		settings.positionY = 34
		settings.width = 222
		settings.height = 111
		settings.cornerRadius = 28
		settings.enabledApps.clear()
		settings.enabledApps.addAll(listOf("app.one", "app.two"))
		settings.showOnLockScreen = true
		settings.showInLandscape = true
		settings.showBorders = true
		settings.autoHideOpenedAfter = 3200f
		settings.gravity = IslandGravity.Right

		settings.applySettings(context)

		val prefs = context.getSharedPreferences(SETTINGS_KEY, android.content.Context.MODE_PRIVATE)
		assertEquals(12, prefs.getInt(POSITION_X, -1))
		assertEquals(34, prefs.getInt(POSITION_Y, -1))
		assertEquals(222, prefs.getInt(SIZE_X, -1))
		assertEquals(111, prefs.getInt(SIZE_Y, -1))
		assertEquals(28, prefs.getInt(CORNER_RADIUS, -1))
		assertEquals(setOf("app.one", "app.two"), prefs.getStringSet(ENABLED_APPS, emptySet()))
		assertTrue(prefs.getBoolean(SHOW_ON_LOCK_SCREEN, false))
		assertTrue(prefs.getBoolean(SHOW_IN_LANDSCAPE, false))
		assertTrue(prefs.getBoolean(SHOW_BORDER, false))
		assertEquals(3200f, prefs.getFloat(AUTO_HIDE_OPENED_AFTER, 0f))
		assertEquals(IslandGravity.Right.name, prefs.getString(GRAVITY, null))
	}

	@Test
	fun loadSettings_restoresPersistedSnapshot() {
		val context = ApplicationProvider.getApplicationContext<android.content.Context>()
		context.getSharedPreferences(SETTINGS_KEY, android.content.Context.MODE_PRIVATE)
			.edit()
			.clear()
			.putInt(POSITION_X, 90)
			.putInt(POSITION_Y, 14)
			.putInt(SIZE_X, 190)
			.putInt(SIZE_Y, 70)
			.putInt(CORNER_RADIUS, 16)
			.putStringSet(ENABLED_APPS, setOf("alpha"))
			.putBoolean(SHOW_ON_LOCK_SCREEN, true)
			.putBoolean(SHOW_IN_LANDSCAPE, false)
			.putFloat(AUTO_HIDE_OPENED_AFTER, 1500f)
			.putBoolean(SHOW_BORDER, true)
			.putString(GRAVITY, IslandGravity.Left.name)
			.apply()

		val settings = IslandSettings()

		settings.loadSettings(context)

		assertEquals(90, settings.positionX)
		assertEquals(14, settings.positionY)
		assertEquals(190, settings.width)
		assertEquals(70, settings.height)
		assertEquals(16, settings.cornerRadius)
		assertEquals(listOf("alpha"), settings.enabledApps.toList())
		assertTrue(settings.showOnLockScreen)
		assertFalse(settings.showInLandscape)
		assertEquals(1500f, settings.autoHideOpenedAfter)
		assertTrue(settings.showBorders)
		assertEquals(IslandGravity.Left, settings.gravity)
	}
}
