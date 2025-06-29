//
//  StartStripeUseCaseNativeProvider.swift
//  iosApp
//
//  Created by Jose Agustin Reinoso on 6/29/25.
//

import Foundation
import UIKit
import Stripe
import StripePaymentSheet
import ComposeApp

class StartStripeUseCaseNativeProvider:StripeNativeBridge {
    static var shared = StartStripeUseCaseNativeProvider()

    public func configure(
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
    
    public func presentSheet() {
        guard let window = UIApplication.shared.connectedScenes
            .compactMap({ $0 as? UIWindowScene })
            .first?.windows.first,
              let rootVC = window.rootViewController else {
            return
        }

        StripeWrapper.shared.presentPaymentSheetFrom(rootVC)
    }
}
