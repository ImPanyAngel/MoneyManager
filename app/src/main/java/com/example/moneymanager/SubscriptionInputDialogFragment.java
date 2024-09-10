package com.example.moneymanager;


import android.app.Application;
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
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;


public class SubscriptionInputDialogFragment extends DialogFragment {

    private MyViewModel viewModel;

    public interface SubscriptionInputListener {
        void onSubscriptionEntered(String title, String amount, String date, String bank, String currency);
    }

    private SubscriptionInputListener listener;

    public void setSubscriptionInputListener(SubscriptionInputListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_subscription_input_dialog, null);

        Application application = requireActivity().getApplication();
        MyViewModelFactory factory = new MyViewModelFactory(application);
        viewModel = new ViewModelProvider(this, factory).get(MyViewModel.class);

        final EditText editTextTitle = dialogView.findViewById(R.id.etTitle);
        final EditText editTextAmount = dialogView.findViewById(R.id.etAmount);
        final EditText editTextDate = dialogView.findViewById(R.id.etDate);
        Spinner spinnerBank = dialogView.findViewById(R.id.spinnerBank);
        Spinner spinnerCurrency = dialogView.findViewById(R.id.spinnerCurrency);
        Button btnDialogConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnDialogCancel = dialogView.findViewById(R.id.btnCancel);

        setupBankSpinner(spinnerBank);
        setupCurrencySpinner(spinnerCurrency);

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
                String amount = editTextAmount.getText().toString();
                String date = editTextDate.getText().toString();
                String currency = spinnerCurrency.getSelectedItem().toString();
                String bank;
                if (spinnerBank.getSelectedItem() != null) {
                    bank = spinnerBank.getSelectedItem().toString();
                } else {
                    bank = "Unknown";
                }
                listener.onSubscriptionEntered(title, amount, date, bank, currency);
            }
            dialog.dismiss();
        });

        // Set click listener for the cancel button
        btnDialogCancel.setOnClickListener(v -> dialog.cancel());

        return dialog;
    }

    private void setupBankSpinner(Spinner spinner) {
        List<String> bankNames = new ArrayList<>();
        for (BankObject bankObject : viewModel.getBankObjectList()) {
            bankNames.add(bankObject.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, bankNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupCurrencySpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

}