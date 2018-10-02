package br.elois.maktaba.fabreveal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import br.elois.maktaba.R;

/**
 * Created by Gordon Wong on 7/13/2015.
 *
 * Layout that draws a dimmed overlay.
 */
public class FabRevealBackground extends FrameLayout {

	public FabRevealBackground(Context context) {
        super(context);
		init();
	}

	public FabRevealBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FabRevealBackground(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.fab_dim_overlay, this);
	}
}