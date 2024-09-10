package com.example.moneymanager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TitleInputDialogFragment extends DialogFragment {

    public interface TitleInputListener {
        void onTitleEntered(String title, String currency);
    }

    private TitleInputListener listener;

    public void setTitleInputListener(TitleInputListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_title_input_dialog, null);

        final EditText editTextTitle = dialogView.findViewById(R.id.etTitle);
        Spinner spinner = dialogView.findViewById(R.id.spinnerCurrency);
        Button btnDialogConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnDialogCancel = dialogView.findViewById(R.id.btnCancel);

        // Setup the currency spinner
        setupCurrencySpinner(spinner);

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
                String title = editTextTitle.getText().toString();
                String currency = spinner.getSelectedItem().toString();
                listener.onTitleEntered(title, currency);
            }
            dialog.dismiss();
        });

        // Set click listener for the cancel button
        btnDialogCancel.setOnClickListener(v -> dialog.cancel());

        return dialog;
    }

    private void setupCurrencySpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
