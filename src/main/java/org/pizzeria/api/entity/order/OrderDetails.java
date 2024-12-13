package org.pizzeria.api.entity.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.pizzeria.api.web.exceptions.constraints.annotation.DoubleLengthNullable;
import org.pizzeria.api.web.globals.ValidationResponses;
import org.pizzeria.api.web.globals.ValidationRules;

import java.util.Objects;

@Entity(name = "OrderDetails")
@Table(name = "order_details")
public class OrderDetails {

	@Id
	private Long id;

	@Column
	@NotBlank(message = ValidationResponses.ORDER_DETAILS_DELIVERY_HOUR)
	private String deliveryTime;

	@Column
	@NotBlank(message = ValidationResponses.ORDER_DETAILS_PAYMENT)
	private String paymentMethod;

	@Column
	@DoubleLengthNullable(min = 0, max = 5, message = ValidationResponses.ORDER_DETAILS_CHANGE_REQUESTED_LENGTH)
	private Double billToChange;

	@Column
	private Double changeToGive;

	@Column
	@Pattern(regexp = ValidationRules.ORDER_DETAILS_COMMENT, message = ValidationResponses.ORDER_DETAILS_COMMENT)
	private String comment;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JsonBackReference
	private Order order;

	private OrderDetails(Builder builder) {
		this.id = builder.id;
		this.deliveryTime = builder.deliveryHour;
		this.paymentMethod = builder.paymentType;
		this.billToChange = builder.changeRequested;
		this.changeToGive = builder.paymentChange;
		this.comment = builder.deliveryComment;
		this.order = null;
	}

	public OrderDetails() {
		// The JPA specification requires all Entity classes to have a default no-arg constructor.
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Double getBillToChange() {
		return billToChange;
	}

	public void setBillToChange(Double billToChange) {
		this.billToChange = billToChange;
	}

	public Double getChangeToGive() {
		return changeToGive;
	}

	public void setChangeToGive(Double changeToGive) {
		this.changeToGive = changeToGive;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderDetails [id=" + id + ", deliveryTime=" + deliveryTime + ", paymentMethod=" + paymentMethod
				+ ", billToChange=" + billToChange + ", changeToGive=" + changeToGive + ", comment="
				+ comment + "]";
	}

	public boolean contentEquals(Object o) {
		OrderDetails that = (OrderDetails) o;
		return Objects.equals(deliveryTime, that.deliveryTime)
				&& Objects.equals(paymentMethod, that.paymentMethod)
				&& Objects.equals(billToChange, that.billToChange)
				&& Objects.equals(comment, that.comment);
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
}