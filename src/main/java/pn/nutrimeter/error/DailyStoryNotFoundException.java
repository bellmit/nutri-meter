package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such daily story found!")
public class DailyStoryNotFoundException extends RuntimeException {

    public DailyStoryNotFoundException(String message) { super(message); }
}
