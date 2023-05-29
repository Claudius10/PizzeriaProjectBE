package PizzaApp.api.entity.dto.order;

import java.time.LocalDateTime;

public class OrderCreatedOnDTO {

	private Long id;

	private LocalDateTime createdOn;

	public OrderCreatedOnDTO(Long id, LocalDateTime createdOn) {
		this.id = id;
		this.createdOn = createdOn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
}
