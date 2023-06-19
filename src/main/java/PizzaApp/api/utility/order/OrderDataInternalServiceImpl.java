package PizzaApp.api.utility.order;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import PizzaApp.api.entity.order.Order;
import PizzaApp.api.services.address.AddressService;
import PizzaApp.api.services.email.EmailService;

@Component
public class OrderDataInternalServiceImpl implements OrderDataInternalService {

	private final Logger logger = Logger.getLogger(getClass().getName());
	private final AddressService addressService;

	public OrderDataInternalServiceImpl(AddressService addressService) {
		this.addressService = addressService;
	}

	@Override
	public OrderData findOrderData(Order order) {
		OrderData orderDataDTO = new OrderData();

		/*if (order.getEmail() != null) {
			logger.info("createOrUpdate order: EMAIL NOT NULL, SEARCHING");
			orderDataDTO.setEmail(emailService.findByAddress(order.getEmail()));
		} else {
			logger.info("createOrUpdate order: NO EMAIL DATA, EMAIL IS NULL");
			orderDataDTO.setEmail(null);
		}*/

		if (order.getAddress() != null) {
			logger.info("createOrUpdate order: ADDRESS IS NOT NULL, SEARCHING");
			orderDataDTO.setAddress(addressService.findAddress(order.getAddress()));
		} else {
			logger.info("createOrUpdate order: NO ADDRESS DATA, ADDRESS IS NULL");
			orderDataDTO.setAddress(null);
		}

		return orderDataDTO;
	}
}