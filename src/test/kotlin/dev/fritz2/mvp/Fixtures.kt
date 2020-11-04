package dev.fritz2.mvp

import dev.fritz2.dom.html.render
import dev.fritz2.elemento.elements
import dev.fritz2.mvp.PresenterState.BIND
import dev.fritz2.mvp.PresenterState.HIDE
import dev.fritz2.mvp.PresenterState.PREPARE_FROM_REQUEST
import dev.fritz2.mvp.PresenterState.SHOW
import kotlinx.browser.document
import org.w3c.dom.Element

// ------------------------------------------------------ constants

const val navigationId = "navigation"
const val mainId = "main"
const val contentId = "content"
const val wait = 100L

// ------------------------------------------------------ html

fun initDocument() {
    document.clear()
    //language=html
    document.write(
        """
            <body>
                <nav id="$navigationId"></nav>
                <main id="$mainId"></main>
            </body>
        """.trimIndent()
    )
}

fun content(): String? = document.getElementById(contentId)?.textContent

// ------------------------------------------------------ mvp

fun initPresenter() {
    Presenter.registry.clear()
    Presenter.instances.clear()
    Presenter.register("apple", ::ApplePresenter)
    Presenter.register("banana", ::BananaPresenter)
    Presenter.register("pineapple", ::PineapplePresenter)
}

fun initPlaceManager(): PlaceManager {
    val placeManager = PlaceManager(PlaceRequest("apple")) {
        render {
            p(id = contentId) { +"💣" }
        }
    }
    placeManager.manage(document.getElementById(mainId))
    return placeManager
}

enum class PresenterState {
    BIND, PREPARE_FROM_REQUEST, SHOW, HIDE
}

abstract class RecordingPresenter<V : View> : Presenter<V> {
    internal val state: MutableList<PresenterState> = mutableListOf()

    override fun bind() {
        state += BIND
    }

    override fun prepareFromRequest(place: PlaceRequest) {
        state += PREPARE_FROM_REQUEST
    }

    override fun show() {
        state += SHOW
    }

    override fun hide() {
        state += HIDE
    }
}

class AppleView : View {
    override val elements: List<Element> = elements {
        p(id = contentId) { +"🍎" }
    }
}

class ApplePresenter : RecordingPresenter<AppleView>() {
    override suspend fun view(): AppleView = AppleView()
}

class BananaView : View {
    override val elements: List<Element> = elements {
        p(id = contentId) { +"🍌" }
    }
}

class BananaPresenter : RecordingPresenter<BananaView>() {
    override suspend fun view(): BananaView = BananaView()
}

class PineappleView : View {
    override val elements: List<Element> = elements {
        p(id = contentId) { +"🍍" }
    }
}

class PineapplePresenter : RecordingPresenter<PineappleView>() {
    override suspend fun view(): PineappleView = PineappleView()
}