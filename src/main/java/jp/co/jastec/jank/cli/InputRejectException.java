package jp.co.jastec.jank.cli;

import jp.co.jastec.jank.base.JankCheckedException;
import jp.co.jastec.jank.base.JankMessage;

public class InputRejectException extends JankCheckedException {

    String rejectedVaue = null;
    public InputRejectException(JankMessage msg) {
        super(msg);
    }

    public InputRejectException(JankMessage msg, String rejectedValue ) {
        super(msg);
        this.rejectedVaue = rejectedValue;
    }

    @Override
    public String getMessage() {
        String jm = super.getJankMessage().getMessageText();
        if ( rejectedVaue != null ) {
            return jm + " -> " + rejectedVaue ;
        } else {
            return jm ;
        }
   }

}
