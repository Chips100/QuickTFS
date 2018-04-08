package quicktfs.utilities;

import android.widget.EditText;

/**
 * Utility methods for UI operations.
 */
public class UiUtilities {
    /**
     * Moves focus to the first specified EditText that has no value.
     * @param editTexts EditTexts that are candidates to gain focus.
     */
    public static void focusFirstEmptyEditText(EditText... editTexts) {
        for(EditText editText:editTexts) {
            if (StringUtilities.isNullOrEmpty(editText.getText().toString())) {
                editText.requestFocus();
                return;
            }
        }
    }
}