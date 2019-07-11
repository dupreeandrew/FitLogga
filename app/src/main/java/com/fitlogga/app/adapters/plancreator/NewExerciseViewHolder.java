package com.fitlogga.app.adapters.plancreator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.collapsible.CollapsibleViewHolder;
import com.fitlogga.app.models.exercises.Exercise;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class NewExerciseViewHolder extends CollapsibleViewHolder  {

    interface SaveListener {
        void onSave(Exercise exercise);
        void onFail();
    }

    private View view;
    private List<TextInputLayout> errorTextInputLayouts = new ArrayList<>();

    NewExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public abstract void manifest(Exercise exercise);

    public final void setTitle(String title) {
        TextView titleView = view.findViewById(R.id.tv_exercise_title);
        titleView.setText(title);
    }

    final void setSubtitle(String subtitle) {
        TextView subtitleView = view.findViewById(R.id.tv_subtitle);
        subtitleView.setText(subtitle);
    }

    protected abstract void tryToSave(SaveListener listener);

    final void setDeleteButtonClickListener(View.OnClickListener listener) {
        Button deleteButton = view.findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(listener);
    }

    final void setCloseButtonClickListener(View.OnClickListener listener) {
        Button closeButton = view.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(listener);
    }


    @SuppressLint("ClickableViewAccessibility")
    final void setDragHandleTouchListener(View.OnTouchListener listener) {
        ImageView dragHandleView = view.findViewById(R.id.iv_drag);
        dragHandleView.setOnTouchListener(listener);
    }

    final void applyRequiredError(int textInputLayoutResId) {
        applyError(textInputLayoutResId, "* Required");
    }

    final void applyBadNumberError(int textInputLayoutResId) {
        applyError(textInputLayoutResId, "Impossible");
    }

    final void applyNameTooLongError(int textInputLayoutResId) {
        String errorMessage = "Must be less than " + GlobalSettings.MAX_EXERCISE_NAME_LENGTH
                + " characters";
        applyError(textInputLayoutResId, errorMessage);
    }

    final void applyErrorBackground(int textInputLayoutResId) {
        applyError(textInputLayoutResId, " ");
    }

    private void applyError(int textInputLayoutResId, String error) {
        TextInputLayout textInputLayout = view.findViewById(textInputLayoutResId);
        textInputLayout.setError(error);
        errorTextInputLayouts.add(textInputLayout);
    }

    final void resetRequiredErrors() {

        for (TextInputLayout textInputLayout : errorTextInputLayouts) {
            textInputLayout.setErrorEnabled(false);
        }

        errorTextInputLayouts.clear();
    }

    final boolean isUnacceptableNumber(String parsableInt) {
        try {
            int parsedInt = Integer.parseInt(parsableInt);
            return (parsedInt < 1);
        }
        catch (NumberFormatException ex) {
            return true;
        }
    }

    final boolean isNameTooLong(String exerciseName) {
        return exerciseName.length() > GlobalSettings.MAX_EXERCISE_NAME_LENGTH;
    }

    final void removeAnyFocuses() {
        view.clearFocus();

        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }





}
