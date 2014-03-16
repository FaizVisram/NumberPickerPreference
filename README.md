NumberPickerPreference
======================

An Android Preference to pick an integer value via the Number Picker widget.


Usage
-----

NumberPickerPreference has the following parameters:

* bindSummary - Boolean. Binds the summary to the current value. Default true.
* max - Integer. Maximum possible value. Default 10.
* min - Integer. Minimum value. Default 0.
* step - Integer. Step between values. Default 1.


Example
-------

``` xml
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">

    <com.faizvisram.android.preference.NumberPickerPreference
        android:title="NumberPicker Example"
        android:key="example_key"
        bindSummary="true"
        max="100"
        min="0"
        step="10" />

</PreferenceScreen>
```
