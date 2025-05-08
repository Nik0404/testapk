package com.acroninspector.app.presentation.custom;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.acroninspector.app.R;
import com.acroninspector.app.common.constants.Constants;
import com.acroninspector.app.common.constants.DatabaseConstants;

public class CustomDataBindingAttributes {

    private CustomDataBindingAttributes() {
    }

    @BindingAdapter("android:layout_marginStart")
    public static void setStartMargin(View view, float startMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(Math.round(startMargin), layoutParams.topMargin,
                layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setTopMargin(View view, float topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, Math.round(topMargin),
                layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_marginEnd")
    public static void setEndMargin(View view, float endMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                Math.round(endMargin), layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter({"networkStatus"})
    public static void setNetworkStatusImage(AppCompatImageView imageView, boolean isNetworkEnabled) {
        int circleResource;

        if (isNetworkEnabled)
            circleResource = R.drawable.online_circle;
        else circleResource = R.drawable.offline_circle;

        imageView.setImageDrawable(ContextCompat
                .getDrawable(imageView.getContext(), circleResource));
    }

    @BindingAdapter({"networkStatus"})
    public static void setNetworkStatusText(AppCompatTextView textView, boolean isNetworkEnabled) {
        int networkStatusResourceId;
        int networkStatusColor;

        if (isNetworkEnabled) {
            networkStatusResourceId = R.string.online;
            networkStatusColor = ContextCompat.getColor(textView.getContext(), R.color.colorAccent);
        } else {
            networkStatusResourceId = R.string.offline;
            networkStatusColor = ContextCompat.getColor(textView.getContext(), R.color.colorLightGray);
        }

        textView.setText(networkStatusResourceId);
        textView.setTextColor(networkStatusColor);
    }

    @BindingAdapter({"nfcColor"})
    public static void setNfcStatusColor(AppCompatImageButton imageButton, boolean isNfcEnabled) {
        int color;

        if (isNfcEnabled)
            color = ContextCompat.getColor(imageButton.getContext(), R.color.colorAccent);
        else color = ContextCompat.getColor(imageButton.getContext(), R.color.colorGray);

        imageButton.setColorFilter(color);
    }

    @BindingAdapter({"nfcStatus"})
    public static void setNfcStatusText(AppCompatTextView textView, boolean isNfcEnabled) {
        int nfcStatusResourceId;
        int nfcStatusColor;

        if (isNfcEnabled) {
            nfcStatusResourceId = R.string.nfc_on;
            nfcStatusColor = ContextCompat.getColor(textView.getContext(), R.color.colorAccent);
        } else {
            nfcStatusResourceId = R.string.nfc_off;
            nfcStatusColor = ContextCompat.getColor(textView.getContext(), R.color.colorLightGray);
        }

        textView.setText(nfcStatusResourceId);
        textView.setTextColor(nfcStatusColor);
    }

    @BindingAdapter({"notificationsCount"})
    public static void setNotificationsCount(AppCompatTextView textView, int count) {
        textView.setText(String.valueOf(count));

        if (count > 0) {
            textView.setVisibility(View.VISIBLE);
        } else textView.setVisibility(View.INVISIBLE);
    }

    @BindingAdapter({"mediaFileType"})
    public static void setMediaFileImage(AppCompatImageView imageView, int mediaType) {
        int mediaFileIconResource;

        switch (mediaType) {
            case Constants.MEDIA_TYPE_PHOTO:
                mediaFileIconResource = R.drawable.ic_image;
                break;
            case Constants.MEDIA_TYPE_VIDEO:
                mediaFileIconResource = R.drawable.ic_movie;
                break;
            case Constants.MEDIA_TYPE_AUDIO:
                mediaFileIconResource = R.drawable.ic_mic;
                break;
            default:
                throw new IllegalArgumentException("Unknown media type: " + mediaType);
        }

        imageView.setImageDrawable(ContextCompat
                .getDrawable(imageView.getContext(), mediaFileIconResource));
    }

    @BindingAdapter({"mediaFileType"})
    public static void setMediaFileText(AppCompatTextView textView, int mediaType) {
        int mediaFileTextResource;

        switch (mediaType) {
            case Constants.MEDIA_TYPE_PHOTO:
                mediaFileTextResource = R.string.photo;
                break;
            case Constants.MEDIA_TYPE_VIDEO:
                mediaFileTextResource = R.string.video;
                break;
            case Constants.MEDIA_TYPE_AUDIO:
                mediaFileTextResource = R.string.audio;
                break;
            default:
                throw new IllegalArgumentException("Unknown media type: " + mediaType);
        }

        textView.setText(mediaFileTextResource);
    }

    @BindingAdapter({"taskStatus"})
    public static void setTaskStatusBackgroundColor(View view, int taskStatus) {
        int color;

        switch (taskStatus) {
            case DatabaseConstants.TASK_STATUS_IN_PROGRESS:
                color = R.color.colorPurple;
                break;
            case DatabaseConstants.TASK_STATUS_COMPLETED:
                color = R.color.colorGreen;
                break;
            default:
                color = R.color.colorBlue;

        }
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), color));
    }

    @BindingAdapter({"taskStatus"})
    public static void setTaskStatusBackgroundColor(CardView view, int taskStatus) {
        int color;

        switch (taskStatus) {
            case DatabaseConstants.TASK_STATUS_IN_PROGRESS:
                color = R.color.colorPurple;
                break;
            case DatabaseConstants.TASK_STATUS_COMPLETED:
                color = R.color.colorGreen;
                break;
            default:
                color = R.color.colorBlue;
        }
        view.setCardBackgroundColor(ContextCompat.getColor(view.getContext(), color));
    }

    @BindingAdapter({"taskStatus"})
    public static void setTaskStatusText(AppCompatTextView textView, int taskStatus) {
        textView.setText(getTaskStatusTitle(taskStatus));
    }

    @BindingAdapter({"editTaskStatus"})
    public static void setEditTaskStatusText(AppCompatTextView textView, int taskStatus) {
        textView.setText(getEditTaskStatusTitle(taskStatus));
    }

    private static int getTaskStatusTitle(int taskStatus) {
        int textResourceId;

        switch (taskStatus) {
            case DatabaseConstants.TASK_STATUS_IN_PROGRESS:
                textResourceId = R.string.in_progress;
                break;
            case DatabaseConstants.TASK_STATUS_COMPLETED:
                textResourceId = R.string.completed;
                break;
            default:
                textResourceId = R.string.new_task;
        }

        return textResourceId;
    }

    private static int getEditTaskStatusTitle(int taskStatus) {
        int textResourceId;

        switch (taskStatus) {
            case DatabaseConstants.TASK_STATUS_IN_PROGRESS:
                textResourceId = R.string.in_progress;
                break;
            case DatabaseConstants.TASK_STATUS_COMPLETED:
                textResourceId = R.string.completed;
                break;
            default:
                textResourceId = R.string.new_task_edit;
        }

        return textResourceId;
    }

    @BindingAdapter({"routeStatus"})
    public static void setTaskStatusText(AppCompatButton button, int taskStatus) {
        int textResourceId;
        if (taskStatus == DatabaseConstants.TASK_STATUS_ANOTHER_IN_PROGRESS) {
            button.setVisibility(View.GONE);
            return;
        } else {
            button.setVisibility(View.VISIBLE);
        }
        if (taskStatus == DatabaseConstants.TASK_STATUS_NEW)
            textResourceId = R.string.start_route;
        else if (taskStatus == DatabaseConstants.TASK_STATUS_IN_PROGRESS)
            textResourceId = R.string.end_route;
        else textResourceId = R.string.route_completed;

        if (textResourceId == R.string.route_completed) {
            button.setPadding(0, 0, 0, 0);
            button.setBackground(null);
            button.setEnabled(false);
        } else {
            button.setPadding(convertDpToPixel(25, button.getContext()), 0,
                    convertDpToPixel(25, button.getContext()), 0);
            button.setBackground(ContextCompat.getDrawable(button.getContext(),
                    R.drawable.button_start_end_round_background));
            button.setEnabled(true);
        }
        button.setText(textResourceId);
    }

    @BindingAdapter({"editTaskButtonStatus"})
    public static void setEditTaskButtonStyle(AppCompatButton button, int taskStatus) {
        int textResourceId;

        if (taskStatus == DatabaseConstants.TASK_STATUS_COMPLETED)
            textResourceId = R.string.route_completed;
        else {
            textResourceId = R.string.edit;
        }

        if (textResourceId == R.string.route_completed) {
            button.setPadding(0, 0, 0, 0);
            button.setBackground(null);
            button.setEnabled(false);
        } else {
            button.setPadding(convertDpToPixel(25, button.getContext()), 0,
                    convertDpToPixel(25, button.getContext()), 0);
            button.setBackground(ContextCompat.getDrawable(button.getContext(),
                    R.drawable.button_start_end_round_background));
            button.setEnabled(true);
        }
        button.setText(textResourceId);
    }

    @SuppressWarnings("SameParameterValue")
    private static int convertDpToPixel(float dp, Context context) {
        return ((int) dp) * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @BindingAdapter({"criticality"})
    public static void setCriticality(AppCompatImageButton button, int criticality) {
        int color = getCriticalityColor(criticality);

        if (criticality == Constants.CRITICALITY_NO) {
            button.setVisibility(View.GONE);
        } else button.setVisibility(View.VISIBLE);

        button.setColorFilter(ContextCompat.getColor(button.getContext(), color));
    }

    @BindingAdapter({"questionCriticality"})
    public static void setQuestionCriticality(AppCompatImageButton button, int criticality) {
        int color = getCriticalityColor(criticality);

        if (criticality == Constants.CRITICALITY_NO) {
            button.setVisibility(View.INVISIBLE);
        } else button.setVisibility(View.VISIBLE);

        button.setColorFilter(ContextCompat.getColor(button.getContext(), color));
    }

    @BindingAdapter({"criticality"})
    public static void setCriticality(AppCompatImageView imageView, int criticality) {
        int color = getCriticalityColor(criticality);

        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), color));
    }

    private static int getCriticalityColor(int criticality) {
        int color;

        if (criticality == Constants.CRITICALITY_NORMAL)
            color = R.color.colorCriticalityNormal;
        else if (criticality == Constants.CRITICALITY_TO_STOP)
            color = R.color.colorCriticalityToStop;
        else if (criticality == Constants.CRITICALITY_EMERGENCY)
            color = R.color.colorCriticalityEmergency;
        else color = R.color.colorDarkGray;

        return color;
    }

    @BindingAdapter({"criticality"})
    public static void setCriticality(AppCompatTextView textView, int criticality) {
        int text;

        if (criticality == Constants.CRITICALITY_NORMAL)
            text = R.string.criticality_normal;
        else if (criticality == Constants.CRITICALITY_TO_STOP)
            text = R.string.criticality_to_stop;
        else if (criticality == Constants.CRITICALITY_EMERGENCY)
            text = R.string.criticality_emergency;
        else text = R.string.error;

        textView.setText(textView.getContext().getString(text));
    }
}

