package jp.co.jastec.jank.model.header;

import java.util.Arrays;
import java.util.List;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.base.datetime.JankTime;
import jp.co.jastec.jank.base.datetime.JankTimeRange;
import jp.co.jastec.jank.base.validation.JankValidator;

public class WorkingDateTimeValidator extends JankValidator<WorkingDateTime> {
 
    public WorkingDateTimeValidator(WorkingDateTime model) {
        super(model);
    }


    @Override
    protected List<JankValidation> getValidations() {

        JankValidation[] validations = {
            startTimeFinishTimeValidation,
            holidayValidation,
            restCheckValdation,
            lessWorkingValidation
        };

        return Arrays.asList(validations);
    }


    private JankValidation startTimeFinishTimeValidation = new JankValidation(){

        @Override
        public JankMessage exec() {
            
            JankTime startTime = model().getStartTime();
            JankTime finishTime = model().getFinishTime(); 
            
            if ( isAnyNull(startTime, finishTime) ) {
                return null;
            }
            if ( startTime.value() < finishTime.value()) {
                return JankMessage.UPSET_START_FNISH_TIME;
            } else {
                return null;
            }
        }
        
    };
    
    private JankValidation holidayValidation = new JankValidation(){

        @Override
        public JankMessage exec() {
            
            JankDate baseDate = model().getBaseDate();
            RestCheck restCheck = model().getRestCheck();

            if ( baseDate != null && baseDate.isHoliday()){
                if ( restCheck == null ) {
                    return JankMessage.HOLIDAY_BUT_NOCHECK;
                }
            }
            return null;
        }
        
    };

    private JankValidation restCheckValdation = new JankValidation(){
        @Override
        public JankMessage exec() {
            
            JankTime startTime = model().getStartTime();
            JankTime finishTime = model().getFinishTime(); 
            RestCheck restCheck = model().getRestCheck();

            if ( ! isAnyNull(startTime, finishTime, restCheck)) {
                JankTimeRange fromtTo = JankTimeRange.of(startTime, finishTime);
                return restCheck.validateWithWorkingTime(fromtTo);
            } else {
                return null;
            }
        }
        
    };

    private JankValidation lessWorkingValidation = new JankValidation(){

        @Override
        public JankMessage exec() {
            // TODO
            return null;
        }

    };

}
