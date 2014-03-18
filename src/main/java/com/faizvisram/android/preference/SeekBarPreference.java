package com.faizvisram.android.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.faizvisram.android.preference.numberpickerpreference.R;

/**
 * Created by Faiz Visram on 2014-03-16.
 */
public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private static final int DEFAULT_MAX = 100;
    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_STEP = 1;

    private SeekBar mSeekBar;
    private int mMax = DEFAULT_MAX;
    private int mMin = DEFAULT_MIN;
    private int mStep = DEFAULT_STEP;
    private int mCurrentValue = mMin;
    private String mTitle;

    /**
     * Perform inflation from XML and apply a class-specific base style. This
     * constructor of Preference allows subclasses to use their own base
     * style when they are inflating. For example, a {@link CheckBoxPreference}
     * constructor calls this version of the super class constructor and
     * supplies {@code android.R.attr.checkBoxPreferenceStyle} for <var>defStyle</var>.
     * This allows the theme's checkbox preference style to modify all of the base
     * preference attributes as well as the {@link CheckBoxPreference} class's
     * attributes.
     *
     * @param context  The Context this is associated with, through which it can
     *                 access the current theme, resources, {@link SharedPreferences},
     *                 etc.
     * @param attrs    The attributes of the XML tag that is inflating the preference.
     * @param defStyle The default style to apply to this preference. If 0, no style
     *                 will be applied (beyond what is included in the theme). This
     *                 may either be an attribute resource, whose value will be
     *                 retrieved from the current theme, or an explicit style
     *                 resource.
     * @see #Preference(android.content.Context, android.util.AttributeSet)
     */
    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * Constructor that is called when inflating a Preference from XML. This is
     * called when a Preference is being constructed from an XML file, supplying
     * attributes that were specified in the XML file. This version uses a
     * default style of 0, so the only attribute values applied are those in the
     * Context's Theme and the given AttributeSet.
     *
     * @param context The Context this is associated with, through which it can
     *                access the current theme, resources, {@link SharedPreferences},
     *                etc.
     * @param attrs   The attributes of the XML tag that is inflating the
     *                preference.
     * @see #Preference(android.content.Context, android.util.AttributeSet, int)
     */
    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Constructor to create a Preference.
     *
     * @param context The Context in which to store Preference values.
     */
    public SeekBarPreference(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            int title = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "title", -1);
            if (title != -1) {
                mTitle = context.getString(title);
            }
            mMin = attrs.getAttributeIntValue(null, "min", DEFAULT_MIN);
            mMax = attrs.getAttributeIntValue(null, "max", DEFAULT_MAX);
            mStep = attrs.getAttributeIntValue(null, "step", DEFAULT_STEP);
            mCurrentValue = mMin;
        }

        if (mMax < mMin) {
            throw new AssertionError("max value must be > min value");
        }
        if (mStep <= 0) {
            throw new AssertionError("step value must be > 0");
        }

    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            mCurrentValue = this.getPersistedInt(mMin);
        } else {
            // Set default state from the XML attribute
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }
        Log.e("NumberPickerPreference", "mCurrentValue: " + mCurrentValue);
        setValue(mCurrentValue);

    }

    protected View onCreateView(ViewGroup parent) {
        View view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_seek_bar_preference, parent, false);

        mSeekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        mSeekBar.setMax((mMax - mMin) / mStep + 1);
        mSeekBar.setOnSeekBarChangeListener(this);
        setValue(mCurrentValue);

        if (mTitle == null) {
            view.findViewById(R.id.txt_title).setVisibility(View.GONE);
        } else {
            ((TextView) view.findViewById(R.id.txt_title)).setText(mTitle);
        }

        return view;
    }

    private int getValue() {
        return mSeekBar.getProgress() * mStep + mMin;
    }

    private void setValue(int value) {
        if (mSeekBar != null) {
            mSeekBar.setProgress((value - mMin) / mStep);
        }
    }

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar  The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range 0..max where max
     *                 was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        persistInt(getValue());
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}