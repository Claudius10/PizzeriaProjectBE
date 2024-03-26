package PizzaApp.api.order;

import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.repos.order.projections.CreatedOnOnly;
import PizzaApp.api.validation.order.OrderValidationResult;
import PizzaApp.api.validation.order.OrderValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDeleteValidationTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderValidatorImpl orderValidator;

	@Test
	public void givenOrderDeleteRequest_whenDeleteWindowPassed_thenReturnInvalidResult() {
		when(orderRepository.findCreatedOnById(1L)).thenReturn(new CreatedOnOnly(LocalDateTime.now().minusMinutes(20)));

		OrderValidationResult isDeleteRequestValid = orderValidator.validateDelete(1L);

		assertAll(() -> {
			assertFalse(isDeleteRequestValid.isValid());
			assertEquals("El tiempo límite para anular el pedido (20 minutos) ha finalizado.", isDeleteRequestValid.getMessage());
		});

		verify(orderRepository, times(1)).findCreatedOnById(1L);
	}

	@Test
	public void givenOrderDeleteRequest_whenDeleteWindowDidNotPass_thenReturnValidResult() {
		when(orderRepository.findCreatedOnById(1L)).thenReturn(new CreatedOnOnly(LocalDateTime.now().minusMinutes(10)));

		OrderValidationResult isDeleteRequestValid = orderValidator.validateDelete(1L);

		assertAll(() -> {
			assertTrue(isDeleteRequestValid.isValid());
			assertNull(isDeleteRequestValid.getMessage());
		});

		verify(orderRepository, times(1)).findCreatedOnById(1L);
	}
}


