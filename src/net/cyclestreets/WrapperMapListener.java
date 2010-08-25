package net.cyclestreets;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.nutiteq.components.WgsPoint;
import com.nutiteq.listeners.MapListener;

public class WrapperMapListener implements MapListener {
	protected Set<MapListener> listeners = new CopyOnWriteArraySet<MapListener>();		


	public WrapperMapListener() {
	}
	
	public WrapperMapListener(MapListener ml) {
		addListener(ml);
	}
	
	public void addListener(MapListener ml) {
		listeners.add(ml);
	}

	public MapListener[] getListeners() {
		return listeners.toArray(new MapListener[0]);
	}
	
	public void removeListener(MapListener ml) {
		listeners.remove(ml);
	}
	
	@Override
	public void mapClicked(WgsPoint arg0) {
		for (MapListener ml: listeners) {
			ml.mapClicked(arg0);
		}
	}

	@Override
	public void mapMoved() {
		for (MapListener ml: listeners) {
			ml.mapMoved();
		}
	}

	@Override
	public void needRepaint(boolean arg0) {
		for (MapListener ml: listeners) {
			ml.needRepaint(arg0);
		}
	}
}
