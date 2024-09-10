package com.example.moneymanager;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NumberInputDialogFragment extends DialogFragment {

    public interface NumberInputListener {
        void OnNumberEntered(String number);
    }

    private NumberInputListener listener;

    public void setNumberInputListener(NumberInputListener listener) {this.listener = listener;}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_number_input_dialog, null);

        final EditText editTextNumber = dialogView.findViewById(R.id.etNumber);
        Button btnDialogConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnDialogCancel = dialogView.findViewById(R.id.btnCancel);

        // Set the dialog view to the builder
        builder.setView(dialogView);

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Set the dialog window background to transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Set click listener for the confirm button
        btnDialogConfirm.setOnClickListener(v -> {
            if (listener != null) {
                String number = editTextNumber.getText().toString();
                listener.OnNumberEntered(number);
            }
            dialog.dismiss();
        });

        // Set click listener for the cancel button
        btnDialogCancel.setOnClickListener(v -> dialog.cancel());

        return dialog;
    }

}