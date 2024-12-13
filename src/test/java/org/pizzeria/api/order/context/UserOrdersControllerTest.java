package org.pizzeria.api.order.context;

import org.pizzeria.api.web.dto.order.dto.NewUserOrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tests/user/{userId}/order")
@Validated
public class UserOrdersControllerTest {

	private final OrderServiceImplTest orderServiceImplTest;

	public UserOrdersControllerTest(OrderServiceImplTest orderServiceImplTest) {
		this.orderServiceImplTest = orderServiceImplTest;
	}

	@PostMapping(params = "minusMin")
	public ResponseEntity<Long> createOrderTestSubjects(@RequestBody NewUserOrderDTO order, @PathVariable Long userId, @RequestParam Long minusMin) {
		LocalDateTime createdOn = LocalDateTime.now().minusMinutes(minusMin);
		return ResponseEntity.status(HttpStatus.OK).body(orderServiceImplTest.createOrderTestSubjects(order, userId, createdOn));
	}
}