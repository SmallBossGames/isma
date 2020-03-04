package ru.nstu.grin.view.simple

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import javafx.event.EventType
import javafx.scene.input.PickResult
import javafx.scene.input.ScrollEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.nstu.grin.model.view.SimpleCanvasViewModel

internal class ScrollableHandlerTest {
    private val mockedChainDrawer = mock<SimpleChainDrawer>().apply {
        doNothing().`when`(this).draw()
    }

    @Test
    fun `should increase step`() {
        val model = SimpleCanvasViewModel()
        val scrollableHandler = ScrollableHandler(
            model,
            mockedChainDrawer
        )
        val event = createDownEvent()
        for (i in 0 until 5) {
            scrollableHandler.handle(event)
        }
        with(model.settings) {
            assertEquals(120.0, pixelCost)
            assertEquals(2.0, step)
        }
    }

    @Test
    fun `should increase step twice`() {
        val model = SimpleCanvasViewModel()
        val scrollableHandler = ScrollableHandler(
            model,
            mockedChainDrawer
        )

        val event = createDownEvent()
        for (i in 0 until 10) {
            scrollableHandler.handle(event)
        }

        with(model.settings) {
            assertEquals(120.0, pixelCost)
            assertEquals(4.0, step)
        }
    }

    @Test
    fun `should increase step 5 times`() {
        val model = SimpleCanvasViewModel()
        val scrollableHandler = ScrollableHandler(
            model,
            mockedChainDrawer
        )

        val event = createDownEvent()
        for (i in 0 until 25) {
            scrollableHandler.handle(event)
        }

        with(model.settings) {
            assertEquals(120.0, pixelCost)
            assertEquals(32.0, step)
        }
    }

    @Test
    fun `should decrease step`() {
        val model = SimpleCanvasViewModel()
        val scrollableHandler = ScrollableHandler(
            model,
            mockedChainDrawer
        )

        val event = createUpEvent()
        for (i in 0 until 5) {
            scrollableHandler.handle(event)
        }

        with(model.settings) {
            assertEquals(120.0, pixelCost)
            assertEquals(0.5, step)
        }
    }

    private fun createDownEvent(): ScrollEvent {
        return ScrollEvent(
            ScrollEvent.SCROLL,
            0.0, 0.0, 0.0, 0.0, false, false, false, false,
            false, false, 1.0, 0.0,
            0.0, 0.0, ScrollEvent.HorizontalTextScrollUnits.CHARACTERS, 0.0,
            ScrollEvent.VerticalTextScrollUnits.LINES, 0.0, 0, null
        )
    }

    private fun createUpEvent(): ScrollEvent {
        return ScrollEvent(
            ScrollEvent.SCROLL,
            0.0, 0.0, 0.0, 0.0, false, false, false, false,
            false, false, 0.0, 1.0,
            0.0, 0.0, ScrollEvent.HorizontalTextScrollUnits.CHARACTERS, 0.0,
            ScrollEvent.VerticalTextScrollUnits.LINES, 0.0, 0, null
        )
    }
}