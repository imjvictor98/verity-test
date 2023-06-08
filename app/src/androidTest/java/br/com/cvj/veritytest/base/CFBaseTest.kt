package br.com.cvj.veritytest.base

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import br.com.cvj.veritytest.util.CustomIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
@LargeTest
abstract class CFBaseTest {

    @Before
    open fun start() {
        IdlingRegistry.getInstance().register(CustomIdlingResource.countingIdlingResource)
        Intents.init()
    }

    @After
    open fun destroy() {
        IdlingRegistry.getInstance().unregister(CustomIdlingResource.countingIdlingResource)
        Intents.release()
    }

}