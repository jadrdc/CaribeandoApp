import Foundation
import FirebaseCrashlytics

@objc class IOSNativeLogger: NSObject {
    @objc static let shared = IOSNativeLogger()

    @objc func log(_ message: String) {
        Crashlytics.crashlytics().log(message)
    }

    @objc func recordException(_ message: String) {
        let model = ExceptionModel(name: "KotlinException",reason: message)
        Crashlytics.crashlytics().record(exceptionModel: model)
    }

    @objc func forceCrash() {
        Crashlytics.crashlytics().log("🔥 Forced crash")
        fatalError("🔥 Test crash")
    }
}
