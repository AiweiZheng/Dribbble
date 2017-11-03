package com.zheng.project.android.dribbble.utils;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class HtmlUtils {

    public static void setHtmlText(TextView view, String text, boolean trimTrailingWhitespace) {

        String htmlText = Html.fromHtml(text == null ? "" : text).toString();
        if (trimTrailingWhitespace) {
            htmlText = htmlText.trim();
        }
        view.setText(htmlText);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
