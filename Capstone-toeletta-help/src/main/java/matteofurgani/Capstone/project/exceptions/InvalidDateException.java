package matteofurgani.Capstone.project.exceptions;

public class InvalidDateException extends RuntimeException {
	
	 public InvalidDateException(String message) {
	        super(message);
	    }

	    public InvalidDateException() {
	        super("La data deve essere nel futuro");
	    }

}
