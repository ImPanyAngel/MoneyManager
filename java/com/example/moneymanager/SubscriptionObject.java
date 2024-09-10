package com.example.moneymanager;

import android.view.View;

import com.example.moneymanager.databinding.SubscriptionObjectBinding;

public class SubscriptionObject {
    private final SubscriptionObjectBinding binding;
    private DeleteButtonListener deleteButtonListener;

    private final String name;
    private final String money;
    private final String bank;
    private final String date;


    public interface DeleteButtonListener {
        void onDeleteButtonClicked(SubscriptionObject subscriptionObject);
    }

    public void setDeleteButtonListener(DeleteButtonListener listener) {
        this.deleteButtonListener = listener;
    }

    public SubscriptionObject(String name, String money, String bank, String date) {
        this.binding = null;
        this.name = name;
        this.money = money;
        this.bank = bank;
        this.date = date;
    }

    public SubscriptionObject(View view) {
        binding = SubscriptionObjectBinding.bind(view);
        this.name = getName();
        this.money = getMoney();
        this.bank = getBank();
        this.date = getDate();

        binding.btnDelete.setOnClickListener(v ->
        {
            if (deleteButtonListener != null) {
                deleteButtonListener.onDeleteButtonClicked(this);
            }
        });
    }

    public View getView() {
        return binding.getRoot();
    }

    public String getName() {
        try {
            return binding.tvSubTitle.getText().toString();
        } catch (NullPointerException e) {
            return this.name;
        }
    }

    public String getMoney() {
        try {
            return binding.tvBankMoney.getText().toString();
        } catch (NullPointerException e) {
            return this.money;
        }
    }

    public String getBank() {
        try {
            return binding.tvBank.getText().toString();
        } catch (NullPointerException e) {
            return this.bank;
        }
    }

    public String getDate() {
        try {
            return binding.tvDate.getText().toString();
        } catch (NullPointerException e) {
            return this.date;
        }
    }
}