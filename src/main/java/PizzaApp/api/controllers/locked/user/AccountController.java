package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.user.Telephone;
import PizzaApp.api.validation.account.AccountRequestValidator;
import PizzaApp.api.services.user.account.AccountService;
import PizzaApp.api.services.user.address.AddressService;
import PizzaApp.api.services.user.telephone.TelephoneService;
import PizzaApp.api.utility.auth.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@Validated
public class AccountController {

	private final AccountService accountService;

	private final TelephoneService telephoneService;

	private final AddressService addressService;

	private final AccountRequestValidator accountRequestValidator;

	public AccountController(
			AccountService accountService,
			TelephoneService telephoneService,
			AddressService addressService,
			AccountRequestValidator accountRequestValidator) {
		this.accountService = accountService;
		this.telephoneService = telephoneService;
		this.addressService = addressService;
		this.accountRequestValidator = accountRequestValidator;
	}

	@GetMapping("/{id}/data")
	public ResponseEntity<UserDataDTO> findDataById(
			@PathVariable String id,
			HttpServletRequest request,
			HttpServletResponse response,
			CsrfToken csrfToken) {
		accountRequestValidator.validate(id, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.ok().body(accountService.findDataDTOById(Long.valueOf(id)));
	}

	@GetMapping("/{id}/tel")
	public ResponseEntity<List<TelephoneDTO>> findTelListById(
			@PathVariable String id,
			HttpServletRequest request,
			HttpServletResponse response,
			CsrfToken csrfToken) {
		accountRequestValidator.validate(id, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.ok().body(telephoneService.findByUserId(Long.valueOf(id)).orElseThrow());
	}

	@PostMapping("/{id}/tel")
	public ResponseEntity<?> addTel(
			@PathVariable String id,
			@RequestBody @Valid Integer telephone,
			HttpServletRequest request) {
		accountRequestValidator.validate(id, request);
		accountService.addTel(Long.valueOf(id), telephone);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}/tel")
	public ResponseEntity<?> removeTel(
			@PathVariable String id,
			@RequestBody @Valid Integer telephone,
			HttpServletRequest request) {
		accountRequestValidator.validate(id, request);
		accountService.removeTel(Long.valueOf(id), telephone);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}/address")
	public ResponseEntity<List<Address>> findAddressListById(
			@PathVariable String id,
			HttpServletRequest request,
			HttpServletResponse response,
			CsrfToken csrfToken) {
		accountRequestValidator.validate(id, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.ok().body(addressService.findByUserId(Long.valueOf(id)).orElseThrow());
	}

	@PostMapping("/{id}/address")
	public ResponseEntity<?> addAddress(
			@PathVariable String id,
			@RequestBody @Valid Address address,
			HttpServletRequest request) {
		accountRequestValidator.validate(id, request);
		accountService.addAddress(Long.valueOf(id), address);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}/address")
	public ResponseEntity<?> removeAddress(
			@PathVariable String id,
			@RequestBody @Valid Address address,
			HttpServletRequest request) {
		accountRequestValidator.validate(id, request);
		accountService.removeAddress(Long.valueOf(id), address);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/name")
	public ResponseEntity<?> updateName(
			@PathVariable String id,
			@Valid @RequestBody NameChangeDTO nameChangeDTO,
			HttpServletRequest request,
			HttpServletResponse response,
			CsrfToken csrfToken) {
		accountRequestValidator.validate(id, request);
		accountService.updateName(id, nameChangeDTO);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/email")
	public ResponseEntity<?> updateEmail(
			@PathVariable String id,
			@Valid @RequestBody EmailChangeDTO emailChangeDTO,
			HttpServletRequest request,
			HttpServletResponse response,
			CsrfToken csrfToken) {
		accountRequestValidator.validate(id, request);
		accountService.updateEmail(id, emailChangeDTO);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/password")
	public ResponseEntity<?> updatePassword(
			@PathVariable String id,
			@Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
			HttpServletRequest request,
			HttpServletResponse response) {
		accountRequestValidator.validate(id, request);
		accountService.updatePassword(id, passwordChangeDTO);
		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(
			@PathVariable String id,
			@RequestBody PasswordDTO passwordDTO,
			HttpServletRequest request,
			HttpServletResponse response) {
		accountRequestValidator.validate(id, request);
		accountService.delete(id, passwordDTO);
		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/test")
	public ResponseEntity<?> testEndpoint() {
		return ResponseEntity.ok().body("GET User access");
	}

	@PostMapping("/test")
	public ResponseEntity<?> testPostEndpoint() {
		return ResponseEntity.ok().body("POST User access");
	}
}



