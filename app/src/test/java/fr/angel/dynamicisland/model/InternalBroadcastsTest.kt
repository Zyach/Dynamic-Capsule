package fr.angel.dynamicisland.model

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class InternalBroadcastsTest {

	@Test
	fun internalBroadcastIntent_targetsCurrentPackage() {
		val context = ApplicationProvider.getApplicationContext<android.content.Context>()

		val intent = context.internalBroadcastIntent(SETTINGS_CHANGED)

		assertEquals(SETTINGS_CHANGED, intent.action)
		assertEquals(context.packageName, intent.`package`)
		assertNull(intent.component)
	}
}
