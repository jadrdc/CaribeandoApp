import UIKit
import SwiftUI
import MapKit
import ComposeApp

// Embed Jetpack Compose in SwiftUI
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(    nativeViewFactory: IOSNativeViewFactory.shared)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}


// Combine Compose and Apple Map
struct ContentView: View {
    var body: some View {
        VStack(spacing: 0) {
          //  AppleMapView()
            ComposeView()
                .ignoresSafeArea(.keyboard) // Compose handles keyboard
        }
    }
}
