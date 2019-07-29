package transactions;

public class Transaction {
	
 private String transactionId;
 private Carrier carrier;
 private Request request;
 private Offer offer;
 private int score;
 
public Transaction(String transactionId, Carrier carrier, Request request, Offer offer) {
	this.transactionId = transactionId;
	this.carrier = carrier;
	this.request = request;
	this.offer = offer;
}

public String getTransactionId() {
	return transactionId;
}

public Carrier getCarrier() {
	return carrier;
}

public Request getRequest() {
	return request;
}

public Offer getOffer() {
	return offer;
}

public void setScore(int score) {
	this.score = score;
}

public int getScore() {
	return score;
}
 
 
}
