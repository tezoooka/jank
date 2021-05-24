package jp.co.jastec.jank.base.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文字列のの形式チェックを行う。（長さと文字種のチェックのみで、複雑な検証はできない）
 */
/// modelクラスようのJankValidator系のクラスとは無関係
public class StringFormatChecker {

    private Integer minLength ;
    private Integer maxLength ;
    private char[] allowableChars;

    private boolean nullable = false ;

    private List<CharValidation> charValidations = new ArrayList<>();

    public StringFormatChecker allowNullable() {
        this.nullable = true ;
        return this;
    }

    public StringFormatChecker allowNumeric() {
        this.charValidations.add(numericChar);
        return this;
    }

    public StringFormatChecker allowUpperAlpha() {
        this.charValidations.add(upperAplhaChar);
        return this;
    }

    public StringFormatChecker allowLowerAlpha() {
        this.charValidations.add(lowerAplhaChar);
        return this;
    }

    public StringFormatChecker allowCharsIn(String allowChars) {
        this.allowableChars = allowChars.toCharArray();
        this.charValidations.add(charsIn) ;
        return this;
    }

    public StringFormatChecker allowCustomValidation(CharValidation charValidator) {
        this.charValidations.add(charValidator);
        return this;
    }

    public StringFormatChecker minLength(int len) {
        this.minLength = len ;
        return this;
    }

    public StringFormatChecker maxLength(int len) {
        this.maxLength = len ;
        return this;
    }

    public StringFormatChecker fixLength(int len) {
        this.minLength = len ;
        this.maxLength = len ;
        return this;
    }

    public boolean isValidFormat(String toBeChecked) {

        if ( this.nullable && toBeChecked == null) {
            return true ;
        }

        if ( isNotNull(minLength) && !( toBeChecked.length() >= this.minLength) ) {
            return false;
        }

        if ( isNotNull(maxLength) && !( toBeChecked.length() <= this.maxLength )) {
            return false;
        }

        char[] charArray = toBeChecked.toCharArray();
        boolean charAllowed = false;
        for ( int i = 0 ; i < charArray.length ; i++ ) {
            for ( CharValidation cv : this.charValidations ) {
                charAllowed |= cv.isAllowedChar(charArray[i], i); 
            }
        }

        return charAllowed ;
    
    }


    public interface CharValidation {
        public boolean isAllowedChar(char c, int i) ;
    }

    protected CharValidation numericChar = new CharValidation()  {
        public boolean isAllowedChar(char c, int i) {
            return '0' <=c && c <= '9' ;
        }
    };

    protected CharValidation upperAplhaChar = new CharValidation() {
        public boolean isAllowedChar(char c, int i) {
            return 'A' <=c && c <= 'Z' ;
        }
    };

    protected CharValidation lowerAplhaChar = new CharValidation()  {
            public boolean isAllowedChar(char c, int i) {
            return 'a' <=c && c <= 'z' ;
        }
    };

    protected CharValidation ignoreCaseAplha = new CharValidation()  {
        public boolean isAllowedChar(char c, int i) {
        return 'a' <=c && c <= 'Z' ;
       }
    };

    protected CharValidation charsIn = new CharValidation()  {
        public boolean isAllowedChar(char c, int i) {
            for ( char ac : allowableChars) {
                if ( ac == c) {
                    return true;
                }
            }
            return false;
        }
    };

    private static boolean isNotNull(Object o) {
        return ! Objects.isNull(o);
    }

}