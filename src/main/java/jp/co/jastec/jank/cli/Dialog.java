package jp.co.jastec.jank.cli;

import jp.co.jastec.jank.cli.JankConsole.Color;

public class Dialog {

    public enum AnswerYesNo { Yes, No, Unknown } ;

    private final static JankConsole.Color ASKCOLOR = Color.YELLOW ;


    public static class Answer {

        private AnswerYesNo  yseNo = AnswerYesNo.Unknown;
        private Double numericValue = null ;
        private String stringValue = null;

        private static Answer is(AnswerYesNo yesNo) {
            Answer a = new Answer();
            a.yseNo = yesNo;
            return a;
        }

        private static Answer is(Double numericValue) {
            Answer a = new Answer();
            a.numericValue = numericValue;
            return a;
        }

        private static Answer is(String stringValue) {
            Answer a = new Answer();
            a.stringValue = stringValue;
            return a;
        }

        public boolean isYes() {
            return yseNo == AnswerYesNo.Yes;
        }

        public boolean isNo() {
            return yseNo == AnswerYesNo.No;
        }

        public boolean isUnknown() {
            return yseNo == AnswerYesNo.Unknown;
        }

        public float getNumericValue() {
            assert(numericValue!=null);
            return numericValue.floatValue();
        }

        public String getStringValue() {
            assert(stringValue!=null);
            return stringValue;
        }

    }

    public static Answer askYesNo(String message) {

        promptOneLine(message);
        String a = JankConsole.readLine().trim();
        if ( a.equalsIgnoreCase("yes")||a.equalsIgnoreCase("y")) {
            return Answer.is(AnswerYesNo.Yes);
            
        } else if (a.equalsIgnoreCase("no")||a.equalsIgnoreCase("n")) {
            return Answer.is(AnswerYesNo.No);

        } else {
            return Answer.is(AnswerYesNo.Unknown);
        }

    }

    public static Answer askNumeric(String message) {

        promptOneLine(message);
        Double nVal = null;
        while (nVal == null) {
            String nString = JankConsole.readLine().trim();
            try {
                nVal = Double.parseDouble(nString);
            } catch ( NumberFormatException nfe ) {
                promptOneLine("数値形式ではりません。\n" + message);
            }
        }
        return Answer.is(nVal);
    }

    public static Answer askString(String message) {

        promptOneLine(message);
        String strValue = JankConsole.readLine().trim();
        return Answer.is(strValue);
    }

    private static void promptOneLine(String message) {
        JankConsole.write(ASKCOLOR, "Jank> " + message);
    }
 
}
