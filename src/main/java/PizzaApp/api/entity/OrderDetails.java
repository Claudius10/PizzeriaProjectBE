package PizzaApp.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_order_details")
public class OrderDetails {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_date")
	private String orderDate;

	@Column(name = "delivery_hour")
	private String deliveryHour;
	
	@Column(name = "total_quantity")
	private int totalQuantity;

	@Column(name = "total_cost")
	private double totalCost;
	
	@Column(name = "total_cost_offers")
	private double totalCostOffers;
	
	@Column(name = "payment_type")
	private String paymentType;
	
	@Column(name = "change_requested")
	private double changeRequested;

	@Column(name = "payment_change")
	private double paymentChange;

	@Column(name = "delivery_comment")
	private String deliveryComment;

	public OrderDetails() {
	}

	public OrderDetails(Long id, String orderDate, String deliveryHour, int totalQuantity, double totalCost,
			double totalCostOffers, String paymentType, double changeRequested, double paymentChange,
			String deliveryComment) {
		this.id = id;
		this.orderDate = orderDate;
		this.deliveryHour = deliveryHour;
		this.totalQuantity = totalQuantity;
		this.totalCost = totalCost;
		this.totalCostOffers = totalCostOffers;
		this.paymentType = paymentType;
		this.changeRequested = changeRequested;
		this.paymentChange = paymentChange;
		this.deliveryComment = deliveryComment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryHour() {
		return deliveryHour;
	}

	public void setDeliveryHour(String deliveryHour) {
		this.deliveryHour = deliveryHour;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getTotalCostOffers() {
		return totalCostOffers;
	}

	public void setTotalCostOffers(double totalCostOffers) {
		this.totalCostOffers = totalCostOffers;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public double getChangeRequested() {
		return changeRequested;
	}

	public void setChangeRequested(double changeRequested) {
		this.changeRequested = changeRequested;
	}

	public double getPaymentChange() {
		return paymentChange;
	}

	public void setPaymentChange(double paymentChange) {
		this.paymentChange = paymentChange;
	}

	public String getDeliveryComment() {
		return deliveryComment;
	}

	public void setDeliveryComment(String deliveryComment) {
		this.deliveryComment = deliveryComment;
	}
}
