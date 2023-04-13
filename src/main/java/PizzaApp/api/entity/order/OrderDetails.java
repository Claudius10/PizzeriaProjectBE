package PizzaApp.api.entity.order;

import PizzaApp.api.exceptions.constraints.DoubleLengthNullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "customer_order_details")
public class OrderDetails {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// orderDate field doesn't need validation
	// as value is being set in orderRepository
	// when persisting to DB
	@Column(name = "order_date")
	private String orderDate;

	@Column(name = "delivery_hour")
	@NotBlank(message = "Hora de entrega: el valor no puede faltar. ")
	private String deliveryHour;

	@Column(name = "payment_type")
	@NotBlank(message = "Forma de pago: el valor no puede faltar. ")
	private String paymentType;

	// isChangeRequestedValid() validates changeRequested field against
	// totalCost / totalCostOffers in OrderRepository
	// before persisting Order to Db
	@Column(name = "change_requested")
	@DoubleLengthNullable(min = 0, max = 5, message = "Cambio de efectivo: mín 0, máx 5 digitos; ejemplo: 25.55 ")
	private Double changeRequested;

	// this field's value is the result of an internal
	// calculation in OrderRepository by calculatePaymentChange()
	// so it doesn't need validation
	@Column(name = "payment_change")
	private double paymentChange;

	@Column(name = "delivery_comment")
	@Pattern(regexp = "^[a-zA-Z0-9!¡¿?.,\s]{0,150}$", message = "Observación: máximo 150 valores. Solo letras (sin tildes), dígitos, !¡ ?¿ . , : ; se aceptan.")
	private String deliveryComment;

	public OrderDetails() {
	}

	public OrderDetails(Long id, String orderDate, String deliveryHour, String paymentType, Double changeRequested,
			double paymentChange, String deliveryComment) {
		this.id = id;
		this.orderDate = orderDate;
		this.deliveryHour = deliveryHour;
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
