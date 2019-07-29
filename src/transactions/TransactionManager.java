package transactions;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionManager {
	
	private Map<String, Region> regions = new HashMap<>();
	private Map<String, Place> places = new HashMap<>();
	private Map<String, Carrier> carriers = new HashMap<>();
	private Map<String,Request> requests = new HashMap<>();
	private Map<String,Offer> offers = new HashMap<>();
	private Map<String,Transaction> transactions = new HashMap<>();
	
	
//R1
	public List<String> addRegion(String regionName, String... placeNames) { 
		Region region = new Region(regionName);
	    for (String p : placeNames) {
			if (!places.containsKey(p)){
				Place place = new Place(p, region);
				region.addPlace(place);
				places.put(p, place);
			}
		}
		regions.put(regionName, region);
		return region.getPlaces().stream().map(Place::getName).sorted().collect(Collectors.toList());
				
	}
	
	public List<String> addCarrier(String carrierName, String... regionNames) { 
          Carrier carrier = new Carrier(carrierName);
          for(String r: regionNames) {
        	  if(regions.containsKey(r)){
        		  Region region = regions.get(r);
        		  carrier.addSuppliedRegions(region); 
        		  region.addCarrier(carrier);
        	  }
          }
          carriers.put(carrierName, carrier);
		
		return carrier.getRegions().stream().map(r->r.getName()).sorted().collect(Collectors.toList());
	}
	

	public List<String> getCarriersForRegion(String regionName) { 
		return regions.get(regionName).getCarriers()
				.stream()
				.map(Carrier::getCarrierName)
				.sorted().collect(Collectors.toList());
	}
	
//R2
	public void addRequest(String requestId, String placeName, String productId) 
			throws TMException {
		if(!places.containsKey(placeName)) throw new TMException();
		if(requests.containsKey(requestId)) throw new TMException();
		Place place = places.get(placeName);
		
		Request request = new Request(requestId, place, productId);
		requests.put(requestId, request);
		
	}
	
	public void addOffer(String offerId, String placeName, String productId) 
			throws TMException {
		if(!places.containsKey(placeName)) throw new TMException();
		if(offers.containsKey(offerId)) throw new TMException();
		
		Place place = places.get(placeName);
		
		Offer offer = new Offer(offerId, place, productId);
		offers.put(offerId, offer);
	}
	

//R3
	public void addTransaction(String transactionId, String carrierName, String requestId, String offerId) 
			throws TMException {
		if(transactions.containsValue(requests.get(requestId).getTransaction()) 
				|| transactions.containsValue(offers.get(offerId).getTransaction())) throw new TMException();
		if(!requests.get(requestId).getProductId().equals(offers.get(offerId).getProductId())) throw new TMException();
		if(!carriers.get(carrierName).getRegions().contains(requests.get(requestId).getRegion()) 
				|| !carriers.get(carrierName).getRegions().contains(offers.get(offerId).getRegion()) ) throw new TMException();
		
		Transaction transaction = new Transaction(transactionId, carriers.get(carrierName), requests.get(requestId), offers.get(offerId));
		transactions.put(transactionId, transaction);
	}
	
	public boolean evaluateTransaction(String transactionId, int score) {
		if(score < 1 || score > 10) return false;
		
		transactions.get(transactionId).setScore(score);
		return true;
	}
	
//R4
	public SortedMap<Long, List<String>> deliveryRegionsPerNT() {
		Map<String, Long> regions = transactions.values().stream()
//		.map(Transaction::getRequest)
//		.map(Request::getRegion)
//		.map(Region::getName)
		.map(t->t.getRequest().getRegion())
		.collect(Collectors.groupingBy(r->r.getName(), Collectors.counting()));
		
		return regions.entrySet().stream().collect(Collectors.groupingBy(e->e.getValue(), 
				()->new TreeMap<Long,List<String>>(Comparator.reverseOrder()), 
				Collectors.mapping(e->e.getKey(), Collectors.toList())));
		
//		return transactions.values().stream()
//				.map(Transaction::getRequest)
//				.map(Request::getRegion)
//				.collect(Collectors.groupingBy(r->r.getName(), Collectors.counting()))
//				.entrySet().stream().collect(Collectors.groupingBy(e->e.getValue(), 
//						()->new TreeMap<Long,List<String>>(Comparator.reverseOrder()), 
//						Collectors.mapping(e->e.getKey(), Collectors.toList())));
	}
	
	public SortedMap<String, Integer> scorePerCarrier(int minimumScore) {
		return transactions.values().stream()
					.filter(t->t.getScore() >= minimumScore)
		            .collect(Collectors.groupingBy(t->t.getCarrier().getCarrierName(), 
		        		   TreeMap::new, Collectors.summingInt(t->t.getScore())));
	}
	
	public SortedMap<String, Long> nTPerProduct() {
		return transactions.values()
				.stream()
				.collect(Collectors.groupingBy(t->t.getRequest().getProductId(),TreeMap::new,Collectors.counting()));
	}
	
	
}

