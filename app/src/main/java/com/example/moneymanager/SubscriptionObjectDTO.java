package com.example.moneymanager;

public class SubscriptionObjectDTO {
    private String name;
    private String money;
    private String bank;
    private String date;

    public SubscriptionObjectDTO(String name, String money, String bank, String date) {
        setName(name);
        setMoney(money);
        setBank(bank);
        setDate(date);
    }

    // Setters and getters
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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
