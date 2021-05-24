package jp.co.jastec.jank.model.header;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.base.JankException;
import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.datetime.JankTime;
import jp.co.jastec.jank.base.datetime.JankTimeRange;
import jp.co.jastec.jank.cli.CliView;
import jp.co.jastec.jank.cli.CliView.CliViewString;
import jp.co.jastec.jank.cli.CliView.CliViewable;

/**
 * 遅刻・中抜け・早退などの不就業
 */
public class AbsenceTime extends ArrayList<AbsenceTime.Detail> implements CliViewable {

    @Deprecated
    public Detail getLate() {
        return this.stream().filter((p)->p.type == Type.LATE).findFirst().orElse(null);
    }

    @Deprecated
    public Detail getEarly() {
        return this.stream().filter((p)->p.type == Type.EARLY).findFirst().orElse(null);
    }

    @Deprecated
    public java.util.List<Detail> getInterrupts() {
        return this.stream().filter((p)->p.type == Type.INTERRUPT).toList();
    }

    public java.util.List<Detail> getTypeOf(Type absenceType) {
        return this.stream().filter((p)->p.type == absenceType).toList();
    }


    @Override
    public boolean add(Detail adding) {

        boolean dupeError = 
            adding.type.isUniq() &&
            this.stream().anyMatch((p)->p.type == adding.type);

        if ( dupeError ) {
            throw new JankException(JankMessage.DUPE_ABSENCETIME );
        }
        return super.add(adding);
    }

    /** 不就業工数を求める */
    public float getAbsenceHours() {
        return (float) this.stream()
            .mapToDouble((d)->d.getHours())
            .sum();
    }

    /** 控除対象の不就業工数を求める */
    public float getDeducationHours() {
        return (float) this.stream()
            .filter((d)-> d.isDeductive())
            .mapToDouble((d)->d.getHours())
            .sum();
    }

    /** 非控除の不就業工数を求める */
    public float getNonDeducationHours() {
        return (float) this.stream()
            .filter((d)-> ! d.isDeductive())
            .mapToDouble((d)->d.getHours())
            .sum();
    }

    @Override
    public CliView getView() {
        CliView cliView = new CliView();

        int count = this.size() ;
        if ( count == 0 ) {
            cliView.add("不就業: なし");
        
        } else {
            for ( int i = 0 ; i < count ; i++) {
                String pre = ( i==0 ? "不就業: " : "      : " );
                cliView.add(pre + get(i).getViewString());
            }
            String d = String.format(" 控除工数計: %.1fH", this.getDeducationHours());
            cliView.add(d);
        }

        return cliView;
    }


    /**
     * 不就業の明細行
     */
    public static class Detail implements JankTimeRange, CliViewString {

        @Expose private Type type;
        @Expose private Reason reason;
        @Expose private JankTime from ;
        @Expose private JankTime to;

        /** コンストラクタは隠すがBuilderを提供する */
        private Detail() {  }

        public float getHours() {
            return JankTime.hoursBetween(from, to);
        }

        public boolean isDeductive() {
            return this.reason.isDeductive();
        }

        public static Builder create() {
            Builder builder = new Builder();
            Detail detail = new Detail();
            builder.me = detail;
            return builder;
        }

        public static class Builder {
            Detail me ;
            public Builder type(Type type) { me.type = type; return this; }
            public Builder reason(Reason reason) { me.reason = reason; return this; }
            public Builder from(JankTime from) { me.from = from; return this; }
            public Builder to(JankTime to) { me.to = to; return this; }
            public Detail  done() { return me;} ;
        }

        @Override
        public JankTime lower() { return this.from;}

        @Override
        public JankTime higher() { return this.to;} 

        @Override
        public String getViewString() {

            return String.format("%s %s(%s) %s-%s", 
                type.dispString,
                reason.toString(),
                type.code + reason.code,
                from.toDisplayString(),
                to.toDisplayString()
            );
        }

    }


    /** 不就業種別 */
    public enum Type {
        
        LATE("遅刻", "1", true),
        EARLY("早退", "2", true),
        INTERRUPT("中抜", "3",false),
        ;

        private boolean isUniq = true ;
        private String code ;
        private String dispString ;
        private String helpString = null ;

        private Type(String dispString, String code, boolean isUniq) {
            this.dispString = dispString;
            this.code = code ;
            this.isUniq = isUniq;
        }

        public String getCode() {
            return code;
        }

        public String toDispString() {
            return dispString;
        }

        public boolean isUniq() {
            return this.isUniq ;
        }

        public String getHelp() {
            if ( this.helpString == null ) {
                StringBuilder sb = new StringBuilder();
                for (Reason r : Reason.values()) {
                    sb.append(this.dispString)
                    .append("-")
                    .append(r.name())
                    .append(":")
                    .append(this.code + r.code)
                    .append("\n");
                }
                this.helpString = sb.toString();
            }
            return helpString;
        }

    }


    /** 不就業事由：これも意味不明仕様の代表格 */
    public enum Reason {
        私事都合１_非(false, "11") ,
        私事都合２_非(false, "12") ,
        業務都合１_非(false, "13") ,
        業務都合２_非(false, "14") ,
        その他(false, "31") ,
        私事都合１_控(true, "21") ,
        私事都合２_控(true, "22") ,
        業務都合１_控(true, "23") ,
        その他１_組合(true, "24") ,
        その他３_役員(true, "32") ,
        ;
        private boolean isDeductive;
        private String code ;
        private Reason(boolean deductive, String code) {
            this.isDeductive = deductive;
            this.code = code;
        }
        public boolean isDeductive() {
            return this.isDeductive;
        }

        public static Reason choice(Type type, String hint) {

            String code ;

            if ( hint.length() == 2 ) {
                code = hint;
            } else if ( hint.length() == 3 ) {
                code = hint.substring(1,3) ;
            } else {
                return null;
            }

            for ( Reason r : Reason.values()) {
                if (r.code.equals(code)) {
                    return r;
                }
            }

            return null;

        }
        
    }

}
