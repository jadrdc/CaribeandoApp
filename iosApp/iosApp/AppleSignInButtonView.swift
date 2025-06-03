import SwiftUI
import AuthenticationServices

struct SignInWithAppleButtonView: View {
    var onToken: (String, String, String) -> Void
    var onError: (String) -> Void

    var body: some View {
        SignInWithAppleButton(.signIn, onRequest: { request in
            request.requestedScopes = [.fullName, .email]
        }, onCompletion: { result in
            switch result {
            case .success(let authResults):
                if let credential = authResults.credential as? ASAuthorizationAppleIDCredential,
                   let tokenData = credential.identityToken,
                   let token = String(data: tokenData, encoding: .utf8) {

                    let firstName = credential.fullName?.givenName ?? ""
                    let lastName = credential.fullName?.familyName ?? ""

                    onToken(token, firstName, lastName)

                } else {
                    onError("No token returned or invalid credential")
                }
            case .failure(let error):
                onError(error.localizedDescription)
            }
        })
        .frame(height: 44)
    }
}
