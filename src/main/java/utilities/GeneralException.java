package utilities;

import lombok.Getter;

/**
 * A general exception that will alert staff through discord when created.
 * Created by Kihz on 2020-10-26
 */
@Getter
public class GeneralException extends RuntimeException{
    private String alert;
    private Exception exception;

    public GeneralException(String alert) {
        this(null, alert);
    }

    public GeneralException(Exception e, String alert) {
        super(getMessage(alert, e), e);
        this.alert = alert;
        this.exception = e;
    }

    /**
     * Get the displayed error message.
     * @param alert Alert for this exception.
     * @param e     Exception
     * @return message
     */
    private static String getMessage(String alert, Exception e) {
        return e != null && e.getLocalizedMessage() != null ?
                (e instanceof GeneralException ? e.getLocalizedMessage() : alert + " (" + e.getLocalizedMessage() + ")")
                : alert;
    }
}
