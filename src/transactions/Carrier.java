package transactions;

import java.util.HashSet;
import java.util.Set;

public class Carrier {

	private String carrierName;
    private Set<Region> regions = new HashSet<>();
	
	public Carrier(String carrierName) {
		this.carrierName = carrierName;
	}
	public void addSuppliedRegions(Region region) {
		regions.add(region);
	}
	
	public String getCarrierName() {
		return carrierName;
	}
	public Set<Region> getRegions() {
		return regions;
	}
	
}
