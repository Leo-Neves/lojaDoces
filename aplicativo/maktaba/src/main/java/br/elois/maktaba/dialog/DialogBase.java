package br.elois.maktaba.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leo on 13/04/16.
 */
public class DialogBase extends Dialog implements DialogInterface.OnShowListener {

    public MDRootLayout view;
    private OnShowListener mShowListener;

    protected DialogBase(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public View findViewById(int id) {
        if (view!=null)
            return view.findViewById(id);
        return super.findViewById(id);
    }

    @Override
    public final void setOnShowListener(OnShowListener listener) {
        mShowListener = listener;
    }

    public final void setOnShowListenerInternal() {
        super.setOnShowListener(this);
    }

    public final void setViewInternal(View view) {
        super.setContentView(view);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (mShowListener != null)
            mShowListener.onShow(dialog);
    }

    @Override
    @Deprecated
    public void setContentView(int layoutResID) throws IllegalAccessError {
        throw new IllegalAccessError("setContentView() is not supported in Dialog. Specify a custom view in the Builder instead.");
    }

    @Override
    @Deprecated
    public void setContentView(View view) throws IllegalAccessError {
        throw new IllegalAccessError("setContentView() is not supported in Dialog. Specify a custom view in the Builder instead.");
    }

    @Override
    @Deprecated
    public void setContentView(View view, ViewGroup.LayoutParams params) throws IllegalAccessError {
        throw new IllegalAccessError("setContentView() is not supported in Dialog. Specify a custom view in the Builder instead.");
    }
}
