package jp.co.jastec.jank.base.datetime;

import jp.co.jastec.jank.base.JankException;
import jp.co.jastec.jank.base.JankMessage;

public class JankDateTimeException extends JankException {

    public JankDateTimeException() {
        super(JankMessage.WRONG_DATE_TIME);
    }

    public JankDateTimeException(JankMessage message) {
        super(message);

    }

    public JankDateTimeException(JankMessage message, String detail) {
        super(message, detail);

    }

}