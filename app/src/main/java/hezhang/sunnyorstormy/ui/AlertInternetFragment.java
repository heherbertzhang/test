package hezhang.sunnyorstormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import hezhang.sunnyorstormy.R;

/**
 * Created by herbert on 15-04-28.
 */
public class AlertInternetFragment extends DialogFragment{
    @Override
    //overwrite the old one
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.errorTitle))
                .setMessage(context.getString(R.string.noInternet))
                .setPositiveButton(context.getString(R.string.ok_button), null);//null just close the dialog
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
