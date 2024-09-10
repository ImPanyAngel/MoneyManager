package com.example.moneymanager;

public class BankObjectDTO {
    private String name;
    private String money;
    private String currency;
    private boolean included;

    // Constructor
    public BankObjectDTO(String name, String money, String currency, boolean included) {
        setName(name);
        setMoney(money);
        setCurrency(currency);
        setIncluded(included);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }
}

