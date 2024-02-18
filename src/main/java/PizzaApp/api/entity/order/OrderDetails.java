package PizzaApp.api.entity.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import PizzaApp.api.exceptions.constraints.DoubleLengthNullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity(name = "OrderDetails")
@Table(name = "order_details")
public class OrderDetails {

	@Id
	private Long id;

	@Column(name = "delivery_hour")
	@NotBlank(message = "Hora de entrega: el valor no puede faltar. ")
	private String deliveryHour;

	@Column(name = "payment_type")
	@NotBlank(message = "Forma de pago: el valor no puede faltar. ")
	private String paymentType;

	@Column(name = "change_requested")
	@DoubleLengthNullable(min = 0, max = 5, message = "Cambio de efectivo: mín 0, máx 5 digitos; ejemplo: 25.55 ")
	private Double changeRequested;

	@Column(name = "payment_change")
	private Double paymentChange;

	@Column(name = "delivery_comment")
	@Pattern(regexp = "^[a-zA-Z0-9!¡¿?.,\s]{0,150}$", message = "Observación: máximo 150 valores. Solo letras (sin tildes), dígitos, !¡ ?¿ . , : ; se aceptan.")
	private String deliveryComment;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JsonBackReference
	private Order order;

	public OrderDetails() {
	}

	private OrderDetails(Builder builder) {
		this.id = builder.id;
		this.deliveryHour = builder.deliveryHour;
		this.paymentType = builder.paymentType;
		this.changeRequested = builder.changeRequested;
		this.paymentChange = builder.paymentChange;
		this.deliveryComment = builder.deliveryComment;
		this.order = null;
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
