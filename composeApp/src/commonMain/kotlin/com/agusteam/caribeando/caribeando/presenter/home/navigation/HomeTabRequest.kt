package com.agusteam.caribeando.presenter.home.navigation

/**
 * 2026-10-09 (design handoff, screen 16): one-shot "open this bottom-nav tab when Home appears"
 * request. The success screen ("Listo") lives deep inside the nested checkout NavHost
 * (`ShoppingFlowNavigation` -> `MainNavigationFlow`), which has no route parameter to say which
 * tab Home should land on. Rather than threading new state through that whole chain, the success
 * screen records the wanted tab here right before it exits, and `HomeScreen` consumes it once on
 * its first composition. Single-threaded UI use only.
 */
object HomeTabRequest {
    private var pendingRoute: String? = null

    fun request(route: NavigationRoutes) {
        pendingRoute = route.route
    }

    /** Returns the requested route (if any) and clears it so it is honored exactly once. */
    fun consume(): String? = pendingRoute.also { pendingRoute = null }
}
