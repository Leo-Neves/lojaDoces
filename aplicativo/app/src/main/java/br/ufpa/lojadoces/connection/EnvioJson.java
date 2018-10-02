package br.ufpa.lojadoces.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import br.ufpa.lojadoces.connection.log.Logger;
import br.ufpa.lojadoces.connection.log.Settings;


/**
 * Created by leo on 16/09/16.
 */
public class EnvioJson {
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);
    private static RequestQueue requestQueue;
    private StringRequest request;
    private int method;
    private EnvioListener envioListener;
    private JSONObject body;
    private Header headers;
    private String url;
    private String resposta;
    public boolean log = true;

    public EnvioJson(int method, String url, JSONObject body, Header headers, EnvioListener envioListener) {
        this.method = method;
        this.envioListener = envioListener;
        this.url = url;
        this.body = body;
        this.headers = headers != null ? headers : new Header();
    }

    public void sync(Context context) {
        configurarRequisicao();
        configurarTimeout();
        logEnvio();
        enviarRequisicao(context);
    }

    public String getResposta() {
        return resposta;
    }

    private void configurarRequisicao() {
        request = new StringRequest(method, url, respostaListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return body.toString().getBytes(PROTOCOL_CHARSET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Logger.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", body, PROTOCOL_CHARSET);
                }
                return null;
            }

            @Override
            public String getBodyContentType() {
                return PROTOCOL_CONTENT_TYPE;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.data.length > 10000) setShouldCache(false);
                String utf8String = null;
                try {
                     utf8String = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return super.parseNetworkResponse(response);
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
    }

    private void configurarTimeout() {
        RetryPolicy defaultRetryPolicy = new DefaultRetryPolicy(15000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(defaultRetryPolicy);
    }

    private void logEnvio() {
        if (!log)
            return;
        int methodCount = Logger.getSettings().getMethodCount();
        boolean threadInfo = Logger.getSettings().isShowThreadInfo();
        String tag = Logger.getTag();
        Logger.init().hideThreadInfo().methodCount(0);
        Logger.i("Envio de: " + url);
        if (request.getMethod() != Request.Method.GET && body != null) Logger.json(body.toString());
        Settings settings = Logger.init(tag).methodCount(methodCount);
        if (!threadInfo)
            settings.hideThreadInfo();
    }

    private void logResposta(String json) {
        if (!log)
            return;
        int methodCount = Logger.getSettings().getMethodCount();
        boolean threadInfo = Logger.getSettings().isShowThreadInfo();
        String tag = Logger.getTag();
        Logger.init().hideThreadInfo().methodCount(0);
        Logger.i("Resposta de " + url);
        Logger.json(json);
        Settings settings = Logger.init(tag).methodCount(methodCount);
        if (!threadInfo)
            settings.hideThreadInfo();
    }

    private void enviarRequisicao(Context context) {
        if (requestQueue==null)
            requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private Response.Listener<String> respostaListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String jsonResponse) {
            resposta = jsonResponse;
            int status = 0;
            try {
                try {
                    logResposta(jsonResponse);
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    onRespostaSuccess(jsonObject);
                } catch (JSONException e) {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        onRespostaSuccess(jsonArray);
                    } catch (JSONException f) {
                        Logger.e(jsonResponse);
                        status = 0;
                        onRespostaError(f, status);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onRespostaError(e, status);
            }
        }


    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            volleyError.printStackTrace();
            int errorCode = 501;
            if (volleyError instanceof NetworkError) {
                errorCode = 404;
            } else if (volleyError instanceof ServerError) {
                errorCode = 500;
            } else if (volleyError instanceof AuthFailureError) {
                errorCode = 401;
            } else if (volleyError instanceof ParseError) {
                errorCode = 500;
            } else if (volleyError instanceof TimeoutError) {
                errorCode = 408;
            }
            if (volleyError.networkResponse != null) {
                resposta = new String(volleyError.networkResponse.data);
                errorCode = volleyError.networkResponse.statusCode;
                Log.e(getClass().getSimpleName(), "Erro ao sincronizar com " + url + resposta);
            } else {
                onRespostaError(volleyError, 0);
            }
            onRespostaError(volleyError, errorCode);
        }
    };

    private void onRespostaSuccess(JSONObject jsonResponse) throws JSONException {
        if (envioListener != null)
            envioListener.onSuccess(jsonResponse);
    }

    private void onRespostaSuccess(JSONArray jsonResponse) throws JSONException {
        if (envioListener != null)
            envioListener.onSuccess(jsonResponse);
    }

    private void onRespostaError(Exception e, int status) {
        Logger.e("Erro " + status + " ao sincronizar com " + url);
        if (envioListener != null)
            envioListener.onError(e, status);
    }

}