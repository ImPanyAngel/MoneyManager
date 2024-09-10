package com.example.moneymanager;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends AndroidViewModel {
    private static final String BANK_OBJECT_FILE_NAME = "bank_object_data.txt";
    private static final String SUBSCRIPTION_OBJECT_FILE_NAME = "subscription_object_data.txt";

    private List<BankObject> bankObjectList;
    private List<SubscriptionObject> subscriptionObjectList;
    private final File context;

    public MyViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext().getFilesDir();
        bankObjectList = loadBankObjectList();
        subscriptionObjectList = loadSubscriptionObjectList();
    }

    public List<BankObject> getBankObjectList() {
        return bankObjectList;
    }

    public void setBankObjectList(List<BankObject> bankObjectList) {
        this.bankObjectList = bankObjectList;
    }

    public List<SubscriptionObject> getSubscriptionObjectList() {
        return subscriptionObjectList;
    }

    public void setSubscriptionObjectList(List<SubscriptionObject> subscriptionObjectList) {
        this.subscriptionObjectList = subscriptionObjectList;
    }

    public void saveBankObjectList() {
        List<BankObjectDTO> dtoList = convertBankObjectsToDTO(bankObjectList);
        FileUtils.saveToFile(context, BANK_OBJECT_FILE_NAME, dtoList);
    }

    private List<BankObject> loadBankObjectList() {
        List<BankObjectDTO> dtoList = FileUtils.readFromFile(context, BANK_OBJECT_FILE_NAME, BankObjectDTO.class);
        return convertDTOToBankObjects(dtoList);
    }

    public void saveSubscriptionObjectList() {
        List<SubscriptionObjectDTO> dtoList = convertSubscriptionObjectsToDTO(subscriptionObjectList);
        FileUtils.saveToFile(context, SUBSCRIPTION_OBJECT_FILE_NAME, dtoList);
    }

    private List<SubscriptionObject> loadSubscriptionObjectList() {
        List<SubscriptionObjectDTO> dtoList = FileUtils.readFromFile(context, SUBSCRIPTION_OBJECT_FILE_NAME, SubscriptionObjectDTO.class);
        return convertDTOToSubscriptionObjects(dtoList);
    }

    // Conversion methods for BankObject <-> BankObjectDTO
    private List<BankObjectDTO> convertBankObjectsToDTO(List<BankObject> bankObjects) {
        List<BankObjectDTO> dtoList = new ArrayList<>();
        for (BankObject bankObject : bankObjects) {
            BankObjectDTO dto = new BankObjectDTO(
                    bankObject.getName(),
                    bankObject.getMoney(),
                    bankObject.getCurrency(),
                    bankObject.isIncluded()
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    private List<BankObject> convertDTOToBankObjects(List<BankObjectDTO> dtoList) {
        List<BankObject> bankObjects = new ArrayList<>();
        for (BankObjectDTO dto : dtoList) {
            BankObject bankObject = new BankObject(
                    dto.getName(),
                    dto.getMoney(),
                    dto.getCurrency(),
                    dto.isIncluded()
            );
            bankObjects.add(bankObject);
        }
        return bankObjects;
    }

    // Conversion methods for SubscriptionObject <-> SubscriptionObjectDTO
    private List<SubscriptionObjectDTO> convertSubscriptionObjectsToDTO(List<SubscriptionObject> subscriptionObjects) {
        List<SubscriptionObjectDTO> dtoList = new ArrayList<>();
        for (SubscriptionObject subscriptionObject : subscriptionObjects) {
            SubscriptionObjectDTO dto = new SubscriptionObjectDTO(
                    subscriptionObject.getName(),
                    subscriptionObject.getMoney(),
                    subscriptionObject.getBank(),
                    subscriptionObject.getDate()
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    private List<SubscriptionObject> convertDTOToSubscriptionObjects(List<SubscriptionObjectDTO> dtoList) {
        List<SubscriptionObject> subscriptionObjects = new ArrayList<>();
        for (SubscriptionObjectDTO dto : dtoList) {
            SubscriptionObject subscriptionObject = new SubscriptionObject(
                    dto.getName(),
                    dto.getMoney(),
                    dto.getBank(),
                    dto.getDate()
            );
            subscriptionObjects.add(subscriptionObject);
        }
        return subscriptionObjects;
    }
}
