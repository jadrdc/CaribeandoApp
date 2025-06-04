import SwiftUI
import FirebaseCore
import FirebaseCrashlytics

@main
struct iOSApp: App {
    init() {
          FirebaseApp.configure()
      }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
