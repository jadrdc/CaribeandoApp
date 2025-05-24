package com.agusteam.caribeando.caribeando.core
import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createMap(
        lat: Double,
        lng: Double
    ): UIViewController
}