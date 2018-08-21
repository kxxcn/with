package dev.kxxcn.app_with.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2018-08-21.
 */
public class DialogUtils {
	public static void showAlertDialog(Context context, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(context.getString(R.string.dialog_yes), positiveListener)
				.setNegativeButton(context.getString(R.string.dialog_no), negativeListener).show();
	}
}
