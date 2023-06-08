package PizzaApp.api.entity.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import PizzaApp.api.validation.constraints.DoubleLengthNullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
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

	@OneToOne
	@MapsId
	@JsonBackReference
	private Order order;

	public OrderDetails() {
	}

	public OrderDetails(Long id, String deliveryHour, String paymentType, Double changeRequested, Double paymentChange,
						String deliveryComment, Order order) {
		this.id = id;
		this.deliveryHour = deliveryHour;
		this.paymentType = paymentType;
		this.changeRequested = changeRequested;
		this.paymentChange = paymentChange;
		this.deliveryComment = deliveryComment;
		this.order = order;
	}

	public OrderDetails(String deliveryHour, String paymentType, Double changeRequested, double paymentChange,
						String deliveryComment, Order order) {
		this.deliveryHour = deliveryHour;
		this.paymentType = paymentType;
		this.changeRequested = changeRequested;
		this.paymentChange = paymentChange;
		this.deliveryComment = deliveryComment;
		this.order = order;
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
}
