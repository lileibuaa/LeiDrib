package lei.buaa.leidrib.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by lei on 3/26/16.
 * email: lileibh@gmail.com
 */
public class StringUtils {
    public static Spanned formatHtml(String string) {
        return Html.fromHtml(string.replaceAll("<p>", "").replaceAll("</p>", ""));
    }
}
