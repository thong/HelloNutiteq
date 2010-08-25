package net.cyclestreets;

import net.cyclestreets.api.ApiClient;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

import com.nutiteq.BasicMapComponent;
import com.nutiteq.android.MapView;
import com.nutiteq.components.OnMapElement;
import com.nutiteq.components.Place;
import com.nutiteq.components.PlaceIcon;
import com.nutiteq.components.WgsPoint;
import com.nutiteq.controls.AndroidKeysHandler;
import com.nutiteq.listeners.OnMapElementListener;
import com.nutiteq.location.LocationMarker;
import com.nutiteq.location.LocationSource;
import com.nutiteq.location.NutiteqLocationMarker;
import com.nutiteq.location.providers.AndroidGPSProvider;
import com.nutiteq.maps.CloudMade;
import com.nutiteq.ui.ThreadDrivenPanning;
import com.nutiteq.utils.Utils;

public class HelloNutiteq extends Activity implements OnMapElementListener {
          private MapView mapView;
          private BasicMapComponent mapComponent;
          private ZoomControls zoomControls;
          private boolean onRetainCalled;

          protected final static WgsPoint CAMBRIDGE = new WgsPoint(-0.74483, 52.2099121);
          protected static ApiClient apiClient = new ApiClient();
          
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    onRetainCalled = false;
    
    mapComponent = new BasicMapComponent("c7e1249ffc03eb9ded908c236bd1996d4c62dbae56a439.28554625", "CycleStreets", "CycleStreets", 1, 1,
                                         CAMBRIDGE, 10);

    mapComponent.setMap(new CloudMade("13ed67dfecf44b5a8d9dc3ec49268ba0", "DEVICE_UID", 64, 1));
    mapComponent.setPanningStrategy(new ThreadDrivenPanning());
    mapComponent.setControlKeysHandler(new AndroidKeysHandler());
    mapComponent.setOnMapElementListener(this);
    mapComponent.startMapping();
    mapView = new MapView(this, mapComponent);
    
	PlaceIcon icon = new PlaceIcon(Utils
            .createImage("/res/drawable-mdpi/icon.png"));
	mapComponent.addPlace(new Place(0, "foo", icon, CAMBRIDGE));


    // add map listener
//    WrapperMapListener wml = new WrapperMapListener(mapView);
//    mapComponent.setMapListener(wml);
//    wml.addListener(new MapAdapter() {
//    	public void mapMoved() {
//            WgsBoundingBox bounds = mapComponent.getBoundingBox();
//            WgsPoint center = bounds.getBoundingBoxCenter();
//            int zoom = mapComponent.getZoom();
//            WgsPoint sw = bounds.getWgsMin();
//            WgsPoint ne = bounds.getWgsMax();
//            double n = ne.getLat();
//            double s = sw.getLat();
//            double e = ne.getLon();
//            double w = sw.getLon();
//            Log.d(getClass().getSimpleName(), "north: " + n);
//            Log.d(getClass().getSimpleName(), "south: " + s);
//            Log.d(getClass().getSimpleName(), "east: " + e);
//            Log.d(getClass().getSimpleName(), "west: " + w);
//
//            try {
//            	List<Photo> photos = apiClient.getPhotos(center, zoom, n, s, e, w);
//            	Log.d(getClass().getSimpleName(), "got photos: " + photos.size());
//            	Log.d(getClass().getSimpleName(), photos.get(0).caption);
//            }
//            catch (Exception ex) {
//            	throw new RuntimeException(ex);
//            }
//    	}
//    });
    
  //Add ZoomControls
    zoomControls = new ZoomControls(this);
    zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
      public void onClick(final View v) {
        mapComponent.zoomIn();
      }
    });
    zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
      public void onClick(final View v) {
    	   mapComponent.zoomOut();
      }
     });

 // GPS Location
    final LocationSource locationSource = new AndroidGPSProvider(
             (LocationManager) getSystemService(Context.LOCATION_SERVICE), 1000L);
   final LocationMarker marker = new NutiteqLocationMarker(new PlaceIcon(Utils
                           .createImage("/res/drawable-mdpi/icon.png"), 5, 17), 3000, true);
   locationSource.setLocationMarker(marker);
   mapComponent.setLocationSource(locationSource);
    
    final RelativeLayout relativeLayout = new RelativeLayout(this);
    setContentView(relativeLayout);
    final RelativeLayout.LayoutParams mapViewLayoutParams = new RelativeLayout.LayoutParams(
          RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
    relativeLayout.addView(mapView, mapViewLayoutParams);

  //Add Zoom controls View to the RelativeLayout
    final RelativeLayout.LayoutParams zoomControlsLayoutParams = new RelativeLayout.LayoutParams(
          RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
       zoomControlsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
       zoomControlsLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
       relativeLayout.addView(zoomControls, zoomControlsLayoutParams);
       
  }
  
  @Override
  public Object onRetainNonConfigurationInstance() {
    onRetainCalled = true;
    return mapComponent;
  }

  @Override
  protected void onDestroy() {
	  super.onDestroy();
	  if (mapView != null) {
	      mapView.clean();
	      mapView = null;
	    }
	  if (!onRetainCalled) {
	      mapComponent.stopMapping();
	      mapComponent = null;
	    }
	}
  
	@Override
	public void elementClicked(OnMapElement arg0) {
		Log.d(getClass().getSimpleName(), "elementClicked " + ((Place) arg0).getLabel());
	}

	@Override
	public void elementEntered(OnMapElement arg0) {
		// TODO: show tool tip
	}

	@Override
	public void elementLeft(OnMapElement arg0) {
		// TODO: hide tool tip
	}
}
