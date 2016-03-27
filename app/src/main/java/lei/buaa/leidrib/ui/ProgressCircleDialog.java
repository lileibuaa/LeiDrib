package lei.buaa.leidrib.ui;

import android.app.Dialog;
import android.content.Context;

import lei.buaa.leidrib.R;

/**
 * Created by lei on 3/25/16.
 * email: lileibh@gmail.com
 */
public class ProgressCircleDialog extends Dialog {
    public ProgressCircleDialog(Context context) {
        super(context, R.style.ProgressDialogStyle);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.dialog_progress);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    protected ProgressCircleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public ProgressCircleDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }
}
