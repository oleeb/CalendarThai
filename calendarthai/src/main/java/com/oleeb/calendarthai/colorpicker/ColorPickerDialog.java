package com.oleeb.calendarthai.colorpicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.oleeb.calendarthai.R;

public class ColorPickerDialog
        extends
        Dialog
        implements
        ColorPicker.OnColorChangedListener,
        View.OnClickListener {

    private OnColorChangedListener mListener;

    public interface OnColorChangedListener {
        public void onColorChanged(int color);
    }

    public ColorPickerDialog(Context context, int initialColor) {
        super(context);
        init(initialColor);
    }

    private ColorPicker picker;
    private SVBar svBar;
    private OpacityBar opacityBar;
    private Button ok_btn;
    private Button cancel_btn;

    private void init(int color) {
        // To fight color banding.
        getWindow().setFormat(PixelFormat.RGBA_8888);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.dialog_color_picker, null);

        setContentView(layout);

        picker = (ColorPicker) findViewById(R.id.picker);
        svBar = (SVBar) findViewById(R.id.svbar);
        opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);

        //To set the old selected color you can do it like this
        picker.setOldCenterColor(color);

        picker.setOnColorChangedListener(this);
        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

    }

    @Override
    public void onColorChanged(int color) {

    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok_btn) {
            if (mListener != null) {
                mListener.onColorChanged(picker.getColor());
            }
        }
        dismiss();
    }

}
