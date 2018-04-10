package quicktfs.utilities;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
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

    /**
     * Interpretiert einen String mit HTML Inhalt.
     * @param html String mit dem HTML Inhalt.
     * @return Den interpretierten Inhalt des HTML Strings.
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}