package exceptions;

import java.io.IOException;

public class ActionException extends IOException {

    public ActionException(String message) {
        super(message);
    }
    public ActionException(Throwable reason){
        super(reason);
    }
}
