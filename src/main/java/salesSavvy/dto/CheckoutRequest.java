package salesSavvy.dto;

public class CheckoutRequest {
    private String username;
    private String address;
    private String paymentMode; // "COD" or "ONLINE_SIM"
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}  
}
