package jp.co.jastec.jank.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;

/// 標準入力・標準出力（JavaでいうSystem.outやin）を、本アプリケーション向けに
/// ラッピングして、使いやすいメソッドを追加したクラス。
public class JankConsole {

    private PrintStream writer ;
    private Scanner scanner ;
    
    private static JankConsole me = null ;

    private JankConsole(PrintStream out, InputStream in) {
        this.writer = out;
        this.scanner = new Scanner(in);
    }

    public static void setup(PrintStream out, InputStream in) {
        me = new JankConsole(out, in);
    }

    public static void setup() {
        AnsiConsole.systemInstall();
        me = new JankConsole(System.out, System.in);
    }

    public static void close() {
        me.scanner.close();
    }

    public static boolean isColorSuppoerted() {
        return AnsiConsole.isInstalled();
    }

    public static void write(String line) {
        me.writer.print(line);
    }

    public static void write(Color color, String line) {
        String colord = color.apply(line) ;
        me.writer.print(colord);
    }

    public static void writeLine(String line) {
        me.writer.println(line);
    }

    public static void writeLine(String prompt, Color color, String line) {
        String colord = color.apply(prompt + line) ;
        writeLine(colord);
    }

    public static void writeLine(String prompt, Color color, String[] lines) {
        for ( String line : lines) {
            writeLine(prompt, color, line);
        }
    }

    public static void writeLine(Color color, String line) {
        me.writer.println(color.apply(line));
    }

    public static String readLine() {
        return me.scanner.nextLine();
    }


    // public static String askYesNo(String prompt) {
    //     write(prompt + ":");
    //     return readLine();
    // }


    public enum Color {

        BLACK  ( "\u001b[00;30m" ,"\u001b[00;40m"),
        RED    ( "\u001b[00;31m" ,"\u001b[00;41m"),
        GREEN  ( "\u001b[00;32m" ,"\u001b[00;42m"),
        YELLOW ( "\u001b[00;33m" ,"\u001b[00;43m"),
        PURPLE ( "\u001b[00;34m" ,"\u001b[00;44m"),
        PINK   ( "\u001b[00;35m" ,"\u001b[00;45m"),
        CYAN   ( "\u001b[00;36m" ,"\u001b[00;46m"),
        WHITE  ( "\u001b[00;37m" ,"\u001b[00;47m"),
        
        NONE   ("", ""),
        ;

        private static final String ENDESC = "\u001b[00m";
        private String fore;
        private String back;
        private Color(String fore, String back) {
            this.fore = fore;
            this.back = back;
        }

        public String apply(String s) {
            if ( isColorSuppoerted() ) { 
                return this.fore + s + ENDESC;
            } else {
                return s;
            }
        }

        public String apply(String s, Color back) {
            if ( isColorSuppoerted() && !s.isEmpty()) { 
                return this.fore + back.back + s + ENDESC;
            } else {
                return s;
            }
        }

    }
}
