package jp.co.jastec.jank.input;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.datetime.JankTime;
import jp.co.jastec.jank.base.datetime.JankTimeRange;
import jp.co.jastec.jank.cli.InputRejectException;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.header.AbsenceTime;
import jp.co.jastec.jank.model.header.AbsenceTime.Reason;
import jp.co.jastec.jank.model.header.AbsenceTime.Type;
import jp.co.jastec.jank.model.header.DayOffType;
import jp.co.jastec.jank.model.header.WorkingDateTime;

/** 不就業内訳の入力クラス */
public class AbsenceTimeInput extends InteractiveInput {

    private final JankTime startTime;
    private final JankTime finishTime;
    private final AbsenceTime absence;
    private final DayOffType dayOffType;

    private AbsenceTime.Type cuurentInput ;

    boolean interruptDone = false ;

    public AbsenceTimeInput(JankModel model) {
        super(model);
        WorkingDateTime wdt = this.model.getWorkingDateTime();
        this.startTime = wdt.getStartTime();
        this.finishTime = wdt.getFinishTime();
        this.absence = wdt.getAbsenceTime();
        this.dayOffType = wdt.getDayOffType();
    }   

    @Override
    public String prompt() {

        String messageBody ;
        if ( isNeedLateInput() ) {
            cuurentInput = AbsenceTime.Type.LATE;
            messageBody =  "遅刻しましたね。不就業コードを入力してください。" ;
        
        } else if  ( isNeedEarlyInput()) {
            cuurentInput = AbsenceTime.Type.EARLY;
            messageBody =  "早退しましたね。不就業コードを入力してください。" ;
        
        } else {
            cuurentInput = AbsenceTime.Type.INTERRUPT;
            messageBody = "中抜があれば、不就業コードとFrom/To時間を入力してください。 \n 中抜ない場合は 空Enter" ;

        }
        return messageBody + "\n 不就業コードの一覧をみるには /h ";

    }

    private boolean isNeedLateInput() {
        JankTime properStartTime = this.dayOffType == DayOffType.AM_ABSENCE ? 
                            JankTime.HALF_HOLIDAY_POINT : JankTime.STD_WORKSTART ;

        return  ( this.absence.getTypeOf(Type.LATE).isEmpty() ) && 
                ( this.startTime.value() > properStartTime.value() );
    }

    private boolean isNeedEarlyInput() {
        JankTime properFinishTime = this.dayOffType == DayOffType.PM_ABSENCE ? 
                            JankTime.HALF_HOLIDAY_POINT : JankTime.STD_WORKFINISH ;

        return  ( this.absence.getTypeOf(Type.EARLY).isEmpty()  ) && 
                ( this.finishTime.value() < properFinishTime.value() );
    }

    @Override
    protected void updateModel(String line) {

        JankTime from = null ;
        JankTime to = null;
        String code = null;

        switch (this.cuurentInput) {
        case LATE:
            from = JankTime.STD_WORKSTART;
            to = this.startTime;
            code = line.trim();
            break ;

        case EARLY:
            from = this.finishTime;
            to = JankTime.STD_WORKFINISH;
            code = line.trim();
            break;
    
        case INTERRUPT:
            if ( this.interruptDone ) {
                return ;
            }
            String[] params = line.split(" ") ;
            code = params[0];
            JankTimeRange fromTo = JankTimeRange.of(params[1]);
            from = fromTo.lower();
            to = fromTo.higher();

            break;
        default:
            // Never reach here
        }

        Reason reason = Reason.choice(cuurentInput, code);
        AbsenceTime.Detail det = AbsenceTime.Detail.create()
            .from(from)
            .to(to)
            .type(this.cuurentInput)
            .reason(reason)
            .done();

        this.absence.add(det);

    }

    @Override
    protected String checkInputFormat(String line) throws InputRejectException {

        String code ;
        
        if (this.cuurentInput == Type.INTERRUPT ) {
            if ( line.isEmpty() ) {
                this.interruptDone = true ;
                return "";
            }
            String[] params = line.split(" ") ;
            if (params.length !=2 ) {
                reject(JankMessage.INVALID_ABSENT_REASON, line);
            }
            code = params[0];

            if ( ! JankTimeRange.isValidFormat(params[1]) ) {
                reject(JankMessage.DATETIME_ILLEGALFORMAT, params[1]);
            }


        }else {
            code = line.trim();
        }

        if ( null == Reason.choice(cuurentInput, code) ) {
            reject(JankMessage.INVALID_ABSENT_REASON, line) ;
        }

        return line;
    }



    @Override 
    protected Interactive getNext() {
        if ( this.interruptDone ) {
            return new RestCheckSelect(this.model);
        } else { 
            return this;
        }
    }

    @Override
    public String helpText() {
        return this.cuurentInput.getHelp();
    }
}
