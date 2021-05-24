package jp.co.jastec.jank.base.validation;

@Deprecated
public abstract class JankValidatableBase implements JankValidatable {

    protected abstract JankValidator<?> getValidator() ;

    @Override
    public ResultList validate() {
        return getValidator().validate();
    }

}
