package net.cyclestreets;

import java.util.List;

import net.cyclestreets.api.Photo;
import android.util.Log;

import com.nutiteq.BasicMapComponent;
import com.nutiteq.android.MapView;
import com.nutiteq.components.Place;
import com.nutiteq.components.PlaceIcon;
import com.nutiteq.components.WgsBoundingBox;
import com.nutiteq.components.WgsPoint;
import com.nutiteq.utils.Utils;

public class CycleStreetsMapView extends MapView {
	protected BasicMapComponent mapComponent;
	
	CycleStreetsMapView(android.content.Context context, BasicMapComponent component) {
		super(context, component);
		mapComponent = component;
	}

	public void mapMoved() {
		super.mapMoved();		

		WgsBoundingBox bounds = mapComponent.getBoundingBox();
        WgsPoint center = bounds.getBoundingBoxCenter();
        int zoom = mapComponent.getZoom();
        WgsPoint sw = bounds.getWgsMin();
        WgsPoint ne = bounds.getWgsMax();
        double n = ne.getLat();
        double s = sw.getLat();
        double e = ne.getLon();
        double w = sw.getLon();
        Log.d(getClass().getSimpleName(), "north: " + n);
        Log.d(getClass().getSimpleName(), "south: " + s);
        Log.d(getClass().getSimpleName(), "east: " + e);
        Log.d(getClass().getSimpleName(), "west: " + w);

        try {
        	List<Photo> photos = HelloNutiteq.apiClient.getPhotos(center, zoom, n, s, e, w);
        	Log.d(getClass().getSimpleName(), "got photos: " + photos.size());
        	Log.d(getClass().getSimpleName()	, photos.get(0).caption);
        	PlaceIcon icon = new PlaceIcon(Utils
                    .createImage("/res/drawable-mdpi/icon.png"));
        	for (Photo photo: photos) {
        		mapComponent.addPlace(new Place(photo.id, photo.caption, icon, new WgsPoint(photo.longitude, photo.latitude)));
        	}
        }
        catch (Exception ex) {
        	throw new RuntimeException(ex);
        }
	}
}
