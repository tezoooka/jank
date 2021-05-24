package jp.co.jastec.jank.base;

public class JankException extends RuntimeException {

    private JankMessage message;

    public JankException(Throwable th) {
        super(th);
    }

    public JankException(JankMessage message) {
        super(message.toString());
        this.message = message;
    }
    public JankException(JankMessage message, String detailMessage) {
        super(message.toString() + "(" + detailMessage + ")" );
        this.message = message;
    }

    @Override
    public String getMessage() {
        if (this.message != null) {
            return this.message.text;
        } else {
            return super.getMessage();
        }
    }

    public JankMessage getJankMessage() {
        return this.message ;
    }
   
}
