package PizzaApp.api.entity.order;

import PizzaApp.api.exceptions.constraints.DoubleLengthNullable;
import PizzaApp.api.utils.globals.ValidationResponses;
import PizzaApp.api.utils.globals.ValidationRules;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity(name = "OrderDetails")
@Table(name = "order_details")
public class OrderDetails {

	@Id
	private Long id;

	@Column
	@NotBlank(message = ValidationResponses.ORDER_DETAILS_DELIVERY_HOUR)
	private String deliveryHour;

	@Column
	@NotBlank(message = ValidationResponses.ORDER_DETAILS_PAYMENT)
	private String paymentType;

	@Column
	@DoubleLengthNullable(min = 0, max = 5, message = ValidationResponses.ORDER_DETAILS_CHANGE_REQUESTED_LENGTH)
	private Double changeRequested;

	@Column
	private Double paymentChange;

	@Column
	@Pattern(regexp = ValidationRules.ORDER_DETAILS_COMMENT, message = ValidationResponses.ORDER_DETAILS_COMMENT)
	private String deliveryComment;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JsonBackReference
	private Order order;

	private OrderDetails(Builder builder) {
		this.id = builder.id;
		this.deliveryHour = builder.deliveryHour;
		this.paymentType = builder.paymentType;
		this.changeRequested = builder.changeRequested;
		this.paymentChange = builder.paymentChange;
		this.deliveryComment = builder.deliveryComment;
		this.order = null;
	}

	public OrderDetails() {
		// The JPA specification requires all Entity classes to have a default no-arg constructor.
	}

	public static class Builder {
		private Long id;
		private String deliveryHour;
		private String paymentType;
		private Double changeRequested;
		private Double paymentChange;
		private String deliveryComment;

		public Builder() {
		}

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withDeliveryHour(String deliveryHour) {
			this.deliveryHour = deliveryHour;
			return this;
		}

		public Builder withPaymentType(String paymentType) {
			this.paymentType = paymentType;
			return this;
		}

		public Builder withChangeRequested(Double changeRequested) {
			this.changeRequested = changeRequested;
			return this;
		}

		public Builder withPaymentChange(Double paymentChange) {
			this.paymentChange = paymentChange;
			return this;
		}

		public Builder withDeliveryComment(String deliveryComment) {
			this.deliveryComment = deliveryComment;
			return this;
		}

		public OrderDetails build() {
			return new OrderDetails(this);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeliveryHour() {
		return deliveryHour;
	}

	public void setDeliveryHour(String deliveryHour) {
		this.deliveryHour = deliveryHour;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getChangeRequested() {
		return changeRequested;
	}

	public void setChangeRequested(Double changeRequested) {
		this.changeRequested = changeRequested;
	}

	public Double getPaymentChange() {
		return paymentChange;
	}

	public void setPaymentChange(Double paymentChange) {
		this.paymentChange = paymentChange;
	}

	public String getDeliveryComment() {
		return deliveryComment;
	}

	public void setDeliveryComment(String deliveryComment) {
		this.deliveryComment = deliveryComment;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderDetails [id=" + id + ", deliveryHour=" + deliveryHour + ", paymentType=" + paymentType
				+ ", changeRequested=" + changeRequested + ", paymentChange=" + paymentChange + ", deliveryComment="
				+ deliveryComment + "]";
	}

	public boolean contentEquals(Object o) {
		OrderDetails that = (OrderDetails) o;
		return Objects.equals(deliveryHour, that.deliveryHour)
				&& Objects.equals(paymentType, that.paymentType)
				&& Objects.equals(changeRequested, that.changeRequested)
				&& Objects.equals(deliveryComment, that.deliveryComment);
	}
}