package br.ufpa.lojadoces.connection.log;

/**
 * Created by leo on 26/10/16.
 */

public interface LogAdapter {
    void d(String tag, String message);

    void e(String tag, String message);

    void w(String tag, String message);

    void i(String tag, String message);

    void v(String tag, String message);

    void wtf(String tag, String message);
}
