package salesSavvy.dto;

public class CartData {
	private String username;
    private Long productId;
    private Integer quantity;
	public CartData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CartData(String username, Long productId, Integer quantity) {
		super();
		this.username = username;
		this.productId = productId;
		this.quantity = quantity;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Integer getQuantity() {
	    return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "CartData [username=" + username + ", productId=" + productId + ", quantity=" + quantity + "]";
	}
	
	
}