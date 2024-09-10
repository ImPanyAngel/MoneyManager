package com.example.moneymanager;

import android.view.View;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.moneymanager.databinding.BankObjectBinding;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankObject {

    private final BankObjectBinding binding;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final String currency;
    private final String name;
    private final String money;
    private final boolean included;
    private MoneyChangeListener moneyChangeListener;
    private DeleteButtonListener deleteButtonListener;

    public interface MoneyChangeListener {
        void onMoneyChanged();
    }

    public interface DeleteButtonListener {
        void onDeleteButtonClicked(BankObject bankObject);
    }

    public void setMoneyChangeListener(MoneyChangeListener listener) {
        this.moneyChangeListener = listener;
    }

    public void setDeleteButtonListener(DeleteButtonListener listener) {
        this.deleteButtonListener = listener;
    }

    public BankObject(String name, String money, String currency, boolean included) {
        this.binding = null;
        this.context = null;
        this.fragmentManager = null;
        this.currency = currency;
        this.name = name;
        this.money = money;
        this.included = included;
    }

    public BankObject(View view, Context context, FragmentManager fragmentManager, String currency) {
        binding = BankObjectBinding.bind(view);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.currency = currency;
        this.name = getName();
        this.money = getMoney();
        this.included = isIncluded();

        String currentMoneyStr = binding.tvBankMoney.getText().toString().substring(1);
        float currentMoney = Float.parseFloat(currentMoneyStr);

        switch (this.currency) {
            case "GBP":
                binding.tvBankMoney.setText(String.format(context.getString(R.string.money_format_gbp), currentMoney));
                break;
            case "EUR":
                binding.tvBankMoney.setText(String.format(context.getString(R.string.money_format_eur), currentMoney));
                break;
        }

        binding.btnDelete.setOnClickListener(v ->
        {
            if (deleteButtonListener != null) {
                deleteButtonListener.onDeleteButtonClicked(this);
            }
        });
        binding.fabAdd.setOnClickListener(v -> showNumberInputDialog(this::increaseMoney));
        binding.fabMinus.setOnClickListener(v -> showNumberInputDialog(this::decreaseMoney));
        binding.switchInclude.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (moneyChangeListener != null) {
                moneyChangeListener.onMoneyChanged();
            }
        });
    }

    private void showNumberInputDialog(NumberInputDialogFragment.NumberInputListener listener) {
        NumberInputDialogFragment dialog = new NumberInputDialogFragment();
        dialog.setNumberInputListener(listener);
        dialog.show(fragmentManager, "TitleInputDialog");
    }

    private void increaseMoney(String number) {
        String currentMoneyStr = binding.tvBankMoney.getText().toString().substring(1);

        float currentMoney = Float.parseFloat(currentMoneyStr);
        currentMoney += Float.parseFloat(number);

        switch (this.currency) {
            case "GBP":
                binding.tvBankMoney.setText(String.format(context.getString(R.string.money_format_gbp), currentMoney));
                break;
            case "EUR":
                binding.tvBankMoney.setText(String.format(context.getString(R.string.money_format_eur), currentMoney));
                break;
        }

        if (moneyChangeListener != null) {
            moneyChangeListener.onMoneyChanged();
        }
    }

    private void decreaseMoney(String number) {
        String currentMoneyStr = binding.tvBankMoney.getText().toString().substring(1);

        float currentMoney = Float.parseFloat(currentMoneyStr);
        currentMoney -= Float.parseFloat(number);

        switch (this.currency) {
            case "GBP":
                binding.tvBankMoney.setText(String.format(context.getString(R.string.money_format_gbp), currentMoney));
                break;
            case "EUR":
                binding.tvBankMoney.setText(String.format(context.getString(R.string.money_format_eur), currentMoney));
                break;
        }

        if (moneyChangeListener != null) {
            moneyChangeListener.onMoneyChanged();
        }
    }

    public void getMoney(final MoneyCallback callback) {
        String currentMoneyStr = binding.tvBankMoney.getText().toString().substring(1);
        float money = Float.parseFloat(currentMoneyStr);

        if (this.currency.equals("EUR")) {
            convertMoney(money, callback);
        } else {
            callback.onMoneyFetched(money);
        }
    }

    private void convertMoney(final float money, final MoneyCallback callback) {
        ExchangeRateApi apiService = ApiClient.getRetrofitInstance().create(ExchangeRateApi.class);

        String apiKey = BuildConfig.API_KEY;
        Call<JsonObject> call = apiService.getExchangeRates(apiKey, currency);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonResponse = response.body();

                    assert jsonResponse != null;
                    JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");
                    float exchangeRate = rates.get("GBP").getAsFloat();

                    float convertedMoney = money * exchangeRate;
                    callback.onMoneyFetched(convertedMoney);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(context, "Request failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getName() {
        try {
            return binding.tvBankTitle.getText().toString();
        } catch (NullPointerException e) {
            return this.name;
        }
    }

    public View getView() {
        return binding.getRoot();
    }

    public String getMoney() {
        try {
            return binding.tvBankMoney.getText().toString();
        } catch (NullPointerException e) {
            return this.money;
        }
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isIncluded() {
        try {
            return binding.switchInclude.isChecked();

        } catch (NullPointerException e) {
            return this.included;
        }
    }
}
