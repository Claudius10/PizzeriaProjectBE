package PizzaApp.api.validation;
import java.util.ArrayList;
import java.util.List;

public class ApiErrorDTO {

	private String timeStamp;
	
	private int statusCode;
	
	private String path;
	
	private List<String> errors = new ArrayList<>();

	public ApiErrorDTO() {
	}

	public ApiErrorDTO(String timeStamp, int statusCode, String path, List<String> errors) {
		this.timeStamp = timeStamp;
		this.statusCode = statusCode;
		this.path = path;
		this.errors = errors;
	}

	public void addError(String errorMessage) {
		this.errors.add(errorMessage);
	}

	public void addErrors(List<String> errors) {
		this.errors.addAll(errors);
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
