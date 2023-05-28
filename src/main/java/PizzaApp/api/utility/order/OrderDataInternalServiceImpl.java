package PizzaApp.api.utility.order;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import PizzaApp.api.entity.order.Order;
import PizzaApp.api.services.address.AddressService;
import PizzaApp.api.services.email.EmailService;

@Component
public class OrderDataInternalServiceImpl implements OrderDataInternalService {

	private AddressService addressService;
	private EmailService emailService;
	private Logger logger = Logger.getLogger(getClass().getName());

	public OrderDataInternalServiceImpl(AddressService addressService, EmailService emailService) {
		this.addressService = addressService;
		this.emailService = emailService;
	}

	@Override
	public OrderData findOrderData(Order order) {
		OrderData orderDataDTO = new OrderData();

		if (order.getEmail() != null) {
			logger.info("createOrUpdate order: EMAIL NOT NULL, SEARCHING");
			orderDataDTO.setEmail(emailService.findByAddress(order.getEmail()));
		} else {
			logger.info("createOrUpdate order: NO EMAIL DATA, EMAIL IS NULL");
			orderDataDTO.setEmail(null);
		}

		if (order.getAddress() != null) {
			logger.info("createOrUpdate order: ADDRESS IS NOT NULL, SEARCHING");
			orderDataDTO.setAddress(addressService.findAddress(order.getAddress()));
		} else {
			logger.info("createOrUpdate order: NO ADDRESS DATA, ADDRESS IS NULL");
			orderDataDTO.setAddress(null);
		}

		return orderDataDTO;
	}

	/*
	 * @Override public OrderDataDTO findOrderData(Order order) { OrderDataDTO
	 * orderDataDTO = new OrderDataDTO();
	 * 
	 * if (order.getCustomer() != null) {
	 * logger.info("createOrUpdate order: SEARCHING CUSTOMER IN DB");
	 * orderDataDTO.setCustomer(customerService.find(order.getCustomer()));
	 * 
	 * logger.info("createOrUpdate order: SEARCHING EMAIL IN DB");
	 * orderDataDTO.setEmail(emailService.findByAddress(order.getCustomer().getEmail
	 * ()));
	 * 
	 * logger.info("createOrUpdate order: SEARCHING TEL IN DB");
	 * orderDataDTO.setTelephone(telephoneService.findByNumber(order.getCustomer().
	 * getTel())); } else {
	 * logger.info("createOrUpdate order: NO CUSTOMER DATA, CUSTOMER IS NULL");
	 * orderDataDTO.setCustomer(null); orderDataDTO.setEmail(null);
	 * orderDataDTO.setTelephone(null); }
	 * 
	 * if (order.getAddress() != null) {
	 * logger.info("createOrUpdate order: SEARCHING FOR ADDRESS IN DB");
	 * orderDataDTO.setAddress(addressService.findAddress(order.getAddress())); }
	 * else { logger.info("createOrUpdate order: NO ADDRESS DATA, ADDRESS IS NULL");
	 * orderDataDTO.setAddress(null); }
	 * 
	 * return orderDataDTO; }
	 * 
	 */
}