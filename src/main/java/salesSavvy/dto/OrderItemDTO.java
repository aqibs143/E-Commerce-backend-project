package salesSavvy.dto;

public class OrderItemDTO {
    private Long productId;
    private String name;
    private int quantity;
    private double priceAtPurchase;

    public OrderItemDTO() {}

    public OrderItemDTO(Long productId, String name, int quantity, double priceAtPurchase) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPriceAtPurchase() {
		return priceAtPurchase;
	}

	public void setPriceAtPurchase(double priceAtPurchase) {
		this.priceAtPurchase = priceAtPurchase;
	}

}
