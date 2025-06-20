//
//  StripeBridge.swift
//  iosApp
//
//  Created by Jose Agustin Reinoso on 6/19/25.
//

import Foundation
import UIKit

@objc public class StripeBridge: NSObject {
    @objc public static let shared = StripeBridge()

    private override init() {}

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
            .compactMap({ $0 as? UIWindowScene })
            .first?.windows.first,
              let rootVC = window.rootViewController else {
            return
        }

        StripeWrapper.shared.presentPaymentSheetFrom(rootVC)
    }
}
