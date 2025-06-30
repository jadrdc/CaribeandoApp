import Foundation
import UIKit
import Stripe
import StripePaymentSheet
import ComposeApp // Your KMP-generated module for StripePaymentResult

@objc public class StartStripeUseCaseNativeProvider: NSObject, StripeNativeBridge, StripeWrapperDelegate {
    
    public static let shared = StartStripeUseCaseNativeProvider()

    public var onResult: ((StripePaymentResult) -> Void)? = nil

    override private init() {
        super.init()
        StripeWrapper.shared.delegate = self
    }

    @objc public func configure(
        publishableKey: String,
        customerId: String,
        ephemeralKey: String,
        paymentIntent: String
    ) {
        StripeWrapper.shared.configureWithPublishableKey(
            publishableKey: publishableKey,
            customerId: customerId,
            ephemeralKey: ephemeralKey,
            paymentIntent: paymentIntent
        )
    }

    @objc public func presentSheet() {
        guard let window = UIApplication.shared.connectedScenes
            .compactMap({ $0 as? UIWindowScene }).first?.windows.first,
              let rootVC = window.rootViewController else {
            onResult?(StripePaymentResult.Failed(message: "No UI window available"))
            return
        }

        StripeWrapper.shared.presentPaymentSheetFrom(rootVC)
    }

    // ✅ Required method for StripeWrapperDelegate (must match exactly)
    @objc public func onPaymentResult(result: StripeResult, errorMessage: String?) {
        switch result {
        case .completed:
            onResult?(StripePaymentResult.Completed())
        case .canceled:
            onResult?(StripePaymentResult.Canceled())
        case .failed:
            let msg = errorMessage ?? "Unknown error"
            onResult?(StripePaymentResult.Failed(message: msg))
        @unknown default:
            onResult?(StripePaymentResult.Failed(message: "Unhandled payment state"))
        }
    }
}
