package br.ufpa.lojadoces.connection.log;

/**
 * Created by leo on 26/10/16.
 */

public interface Printer {

    Printer t(String tag, int methodCount);

    Settings init(String tag);

    Settings getSettings();

    void d(String message, Object... args);

    void d(Object object);

    void e(String message, Object... args);

    void e(Throwable throwable, String message, Object... args);

    void w(String message, Object... args);

    void i(String message, Object... args);

    void v(String message, Object... args);

    void wtf(String message, Object... args);

    boolean json(String json);

    void xml(String xml);

    void log(int priority, String tag, String message, Throwable throwable);

    void resetSettings();

    String getTag();

}
