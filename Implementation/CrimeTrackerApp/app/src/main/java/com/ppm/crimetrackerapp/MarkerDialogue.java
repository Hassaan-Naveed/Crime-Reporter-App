package com.ppm.crimetrackerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class MarkerDialogue extends AppCompatDialogFragment {

    private EditText markerName;
    private MarkerDialogueListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        View view = inflater.inflate(R.layout.dialogue_addmarker, null);

        builder.setView(view)
                .setTitle("Add Report")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Report", (dialog, which) -> {
                    String name = markerName.getText().toString();
                    listener.applyText(name);
                });

        markerName = view.findViewById(R.id.edit_markername);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (MarkerDialogueListener) context;
    }


    public interface MarkerDialogueListener{
        void applyText(String name);
    }
}
