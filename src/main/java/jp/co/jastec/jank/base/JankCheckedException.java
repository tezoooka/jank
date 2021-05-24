package jp.co.jastec.jank.base;

public abstract class JankCheckedException extends Exception {

    private JankMessage message;

    public JankCheckedException(Throwable th) {
        super(th);
    }

    public JankCheckedException(JankMessage msg) {
        super(msg.toString());
        this.message = msg;
    }
    public JankCheckedException(JankMessage msg, String detailMessage) {
        super(msg.toString() + "(" + detailMessage + ")" );
        this.message = msg;
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
