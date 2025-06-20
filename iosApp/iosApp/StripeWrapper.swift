import Foundation
import UIKit
import Stripe
import StripePaymentSheet

@objc public enum StripeResult: Int {
    case completed
    case canceled
    case failed
}

@objc public protocol StripeWrapperDelegate {
    func onPaymentResult(result: StripeResult, errorMessage: String?)
}

@objc public class StripeWrapper: NSObject {

    @objc public static let shared = StripeWrapper()

    private var paymentSheet: PaymentSheet?
    private var customerConfig: PaymentSheet.CustomerConfiguration?
    private var paymentIntentClientSecret: String = ""

    @objc public var delegate: StripeWrapperDelegate?

    // MARK: - Configuration method
    @objc public func configureWithPublishableKey(
        publishableKey: String,
        customerId: String,
        ephemeralKey: String,
        paymentIntent: String
    ) {
        STPAPIClient.shared.publishableKey = publishableKey
        customerConfig = PaymentSheet.CustomerConfiguration(
            id: customerId, // 🔧 Correct label
            ephemeralKeySecret: ephemeralKey
        )
        paymentIntentClientSecret = paymentIntent
    }

    // MARK: - Present Sheet method
    @objc public func presentPaymentSheetFrom(_ viewController: UIViewController) {
        guard let customerConfig = customerConfig else {
            delegate?.onPaymentResult(result: .failed, errorMessage: "Customer config missing")
            return
        }

        var config = PaymentSheet.Configuration()
        config.merchantDisplayName = "Caribeando"
        config.customer = customerConfig
        config.allowsDelayedPaymentMethods = true

        self.paymentSheet = PaymentSheet(
            paymentIntentClientSecret: paymentIntentClientSecret,
            configuration: config
        )

        self.paymentSheet?.present(from: viewController) { [weak self] result in
            switch result {
            case .completed:
                self?.delegate?.onPaymentResult(result: .completed, errorMessage: nil)
            case .canceled:
                self?.delegate?.onPaymentResult(result: .canceled, errorMessage: "User canceled")
            case .failed(let error):
                self?.delegate?.onPaymentResult(result: .failed, errorMessage: error.localizedDescription)
            }
        }
    }
}
