package transactions;

import java.util.HashSet;
import java.util.Set;

public class Region {
  
	private String name;
	
	private Set<Place> places = new HashSet<>();
	private Set<Carrier> carriers = new HashSet<>();

	public Region(String name) {
		this.name = name;
	}
	
	public void addPlace(Place place) {
		places.add(place);
	}
	
	public String getName() {
		return name;
	}
	
	public Set<Place> getPlaces() {
		return places;
	}
	
	public void addCarrier(Carrier carrier) {
		carriers.add(carrier);
	}
	
	public Set<Carrier> getCarriers() {
		return carriers;
	}
	
} 








