package PizzaApp.api.controllers.locked.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@GetMapping
	public String user() {
		return "User access";
	}
}



