package jp.co.jastec.jank.cli;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.cli.CliView.CliViewable;
import jp.co.jastec.jank.input.Closing;
import jp.co.jastec.jank.model.JankModel;

public abstract class InteractiveInput implements Interactive {

    protected JankModel model;

    JankMessage message = null ;
    String rejectedValue = null ;

    public InteractiveInput(JankModel model) {
        this.model = model;
    }

    @Override
    public Accepted accept(String line) {

        if ( line.isEmpty() ) {
            line = this.defaultInput() ;
        }

        Accepted command = bindCommand(line);
        if (command != null ) {
            return command ;
        }

        this.message = null;
        try {

            final String preProcessed =  preProcess(line);
            final String checked = checkInputFormat(preProcessed);
            updateModel(checked);

        } catch ( InputRejectException e) {
            this.message = e.getJankMessage();
            this.rejectedValue = e.rejectedVaue;
            return Accepted.REJECT;
        }

        return Accepted.DONE;
    }

    /**
     * modelオブジェクトを更新する。
     * ここでエラーをRejectするとModelの更新が中途半端になるので好ましくない。
     */
    protected abstract void updateModel(String line) ;

    /**
     * 入力内容をチェックし、不備があればreject()メソッドを呼び出す。
     * rejectの可能性があるので、modelの更新はしないほうが良い。
     * 入力内容を加工した文字列を返すと、それが updateModel()に渡される。
     */
    protected abstract String checkInputFormat(String line) throws InputRejectException;

    /** 
     * 個別処理に先立って、入力行に一律の加工を施す。
     * デフォルトではアルファベットは大文字変換されるので、したくない場合は、ここをOverride
    */
    protected String preProcess(String line) {
        /// デフォルトではアルファベットは大文字変換される。したくない場合は、ここをOverride
        return line.toUpperCase();
    }

    /** 空Enterが入力された場合に解釈する文字列 */
    protected String defaultInput() {
        return "";
    }

    /// 個々の入力に先立って解釈される組み込みコマンド
    private Accepted bindCommand(String inputString) {

        String line = inputString.trim();

        if (line.equalsIgnoreCase("/q")) {
            return Accepted.END;
        }

        if (line.equalsIgnoreCase("/r")) {
            return Accepted.RESTART;
        }

        if (line.equalsIgnoreCase("/h") || line.equalsIgnoreCase("/?"))  {
            return Accepted.HELP;
        }

        if (line.equalsIgnoreCase("/v") )  {
            return Accepted.VIEW;
        }

        return null;
    }

    @Override
    public String retryMessage() {
        String errcode = this.message.getMessageCode() ;
        String errmsg  = this.message.getMessageText() ;
        String errvalue = this.rejectedValue != null ? " -> " + this.rejectedValue : ""; 
        return errcode + ":" + errmsg + errvalue;
    }

    protected boolean hasInputError() {
        return this.message != null;
    }


    @Override
    public Interactive next() {
        if (this.hasInputError()) {
            return this;
        }
        return getNext();
    }

    @Override
    public CliViewable viewModel() {
        return this.model;
    }

    @Override
    public Interactive closer() {
        return new Closing(this.model, this);
    }


    protected void reject(JankMessage message) throws InputRejectException{
        throw new InputRejectException(message) ;
    }

    protected void reject(JankMessage message, String rejectedValue) throws InputRejectException {
        throw new InputRejectException(message, rejectedValue) ;
    }

    protected abstract Interactive getNext() ; 

}
