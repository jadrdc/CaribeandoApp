import SwiftUI
import MapKit
import ComposeApp



class IOSNativeViewFactory: NativeViewFactory {
    static var shared = IOSNativeViewFactory()
 
    public func createMap(lat: Double, lng: Double) -> UIViewController {
        let view = AppleMapContainer(lat: lat, lng: lng)
               return UIHostingController(rootView: view)
        }
    
}



// SwiftUI Map
struct AppleMapContainer: View {
    @State private var region: MKCoordinateRegion

      init(lat: Double, lng: Double) {
          _region = State(initialValue: MKCoordinateRegion(
              center: CLLocationCoordinate2D(latitude: lat, longitude: lng),
              span: MKCoordinateSpan(latitudeDelta: 0.02, longitudeDelta: 0.02) // Equivalent zoom
          ))
      }

      var body: some View {
          Map(coordinateRegion: $region)
              .edgesIgnoringSafeArea(.all)
      }
}

