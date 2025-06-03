package com.agusteam.caribeando.caribeando.core.data

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AuthenticationServices.ASAuthorization
import platform.AuthenticationServices.ASAuthorizationAppleIDCredential
import platform.AuthenticationServices.ASAuthorizationAppleIDProvider
import platform.AuthenticationServices.ASAuthorizationController
import platform.AuthenticationServices.ASAuthorizationControllerDelegateProtocol
import platform.AuthenticationServices.ASAuthorizationControllerPresentationContextProvidingProtocol
import platform.AuthenticationServices.ASAuthorizationScopeEmail
import platform.AuthenticationServices.ASAuthorizationScopeFullName
import platform.AuthenticationServices.ASPresentationAnchor
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AppleAuthProvider {

    suspend fun signIn(): String = suspendCancellableCoroutine { cont ->
        val request = ASAuthorizationAppleIDProvider().createRequest()
        request.requestedScopes = listOf(ASAuthorizationScopeFullName, ASAuthorizationScopeEmail)

        val controller = ASAuthorizationController(listOf(request))

        val delegate = object : NSObject(), ASAuthorizationControllerDelegateProtocol {
            override fun authorizationController(
                controller: ASAuthorizationController,
                didCompleteWithAuthorization: ASAuthorization
            ) {
                val credential = didCompleteWithAuthorization.credential as? ASAuthorizationAppleIDCredential
                val identityToken = credential?.identityToken
                val token = identityToken?.let {
                    NSString.create(it, NSUTF8StringEncoding) as String?
                }

                if (token != null) cont.resume(token)
                else cont.resumeWithException(Exception("Apple Sign-In token is null"))
            }

            override fun authorizationController(
                controller: ASAuthorizationController,
                didCompleteWithError: NSError
            ) {
                cont.resumeWithException(Exception("Apple Sign-In failed: ${didCompleteWithError.localizedDescription}"))
            }
        }

        controller.delegate = delegate
        controller.presentationContextProvider = object : NSObject(), ASAuthorizationControllerPresentationContextProvidingProtocol {
            override fun presentationAnchorForAuthorizationController(controller: ASAuthorizationController): ASPresentationAnchor {
                return UIApplication.sharedApplication.keyWindow!!
            }
        }

        controller.performRequests()
    }
}
