package com.example.inertloginmodule.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.inertloginmodule.R;


public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("forecast")
//                .setMessage(TextUtils.concat(getText(R.string.about_version), "\n\n",
//                        getText(R.string.about_description), "\n\n",
//                        getText(R.string.about_developers), "\n\n",
//                        getText(R.string.about_src), "\n\n",
//                        getText(R.string.about_issues), "\n\n",
//                        getText(R.string.about_data), "\n\n",
//                        getText(R.string.about_icons)))
                .setPositiveButton(R.string.dialog_ok, null)
                .create();
        alertDialog.show();
        ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        return alertDialog;
    }
}
