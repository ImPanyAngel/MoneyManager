package com.example.moneymanager;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moneymanager.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MyViewModel viewModel;
    private List<BankObject> bankObjectList;

    public HomeFragment() { /* Required empty public constructor */ }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // On clicking the fab
        binding.fabHome.setOnClickListener(v -> showTitleInputDialog());

        // Return the root view from the binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Application application = requireActivity().getApplication();
        MyViewModelFactory factory = new MyViewModelFactory(application);
        viewModel = new ViewModelProvider(this, factory).get(MyViewModel.class);
        bankObjectList = viewModel.getBankObjectList();
        updateUI();

    }

    private void updateUI() {
        binding.bankContainer.removeAllViews();

        List<BankObject> tempList = new ArrayList<>();

        Iterator<BankObject> iterator = bankObjectList.iterator();
        if (iterator.hasNext()) {
            do {
                BankObject bankObject = iterator.next();
                addBankObjectToContainer(bankObject, tempList);
            } while (iterator.hasNext());
        }

        bankObjectList.clear();
        bankObjectList.addAll(tempList);

        viewModel.setBankObjectList(bankObjectList);
        updateTotalMoney();
    }

    private void addBankObjectToContainer(BankObject bankObject, List<BankObject> tempList) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View bankObjectView = inflater.inflate(R.layout.bank_object, binding.bankContainer, false);

        TextView nameTextView = bankObjectView.findViewById(R.id.tvBankTitle);
        TextView valueTextView = bankObjectView.findViewById(R.id.tvBankMoney);
        SwitchCompat includeSwitch = bankObjectView.findViewById(R.id.switchInclude);

        String currency = bankObject.getCurrency();
        nameTextView.setText(bankObject.getName());
        valueTextView.setText(bankObject.getMoney());
        includeSwitch.setChecked(bankObject.isIncluded());

        BankObject bankObjectInstance = new BankObject(bankObjectView,requireContext(),getParentFragmentManager(), currency);
        bankObjectInstance.setMoneyChangeListener(this::updateTotalMoney);
        bankObjectInstance.setDeleteButtonListener(this::removeBankObject);

        tempList.add(bankObjectInstance);
        binding.bankContainer.addView(bankObjectView);
    }

    private void showTitleInputDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        TitleInputDialogFragment dialog = new TitleInputDialogFragment();
        dialog.setTitleInputListener(this::addBankObject);
        dialog.show(fragmentManager, "TitleInputDialog");
    }

    public void addBankObject(String title, String currency) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View bankObject = inflater.inflate(R.layout.bank_object, binding.bankContainer, false);

        TextView tvBankTitle = bankObject.findViewById(R.id.tvBankTitle);
        tvBankTitle.setText(title);

        BankObject bankObjectInstance = new BankObject(bankObject,requireContext(),getParentFragmentManager(), currency);
        bankObjectInstance.setMoneyChangeListener(this::updateTotalMoney);
        bankObjectInstance.setDeleteButtonListener(this::removeBankObject);

        bankObjectList.add(bankObjectInstance);
        viewModel.setBankObjectList(bankObjectList);
        binding.bankContainer.addView(bankObject);
        viewModel.saveBankObjectList();
    }

    public void updateTotalMoney() {
        final float[] totalMoney = {0};
        final int[] pendingCallbacks = {0};

        for (BankObject bankObject : viewModel.getBankObjectList()) {
            if (bankObject.isIncluded()) {
                pendingCallbacks[0]++;
                bankObject.getMoney(money -> {
                    totalMoney[0] += money;
                    pendingCallbacks[0]--;
                    if (pendingCallbacks[0] == 0) {
                        // All callbacks are done
                        binding.tvTotalMoney.setText(String.format(requireContext().getString(R.string.money_format_gbp), totalMoney[0]));
                    }
                });
            }
        }

        // If no callbacks are pending, update immediately
        if (pendingCallbacks[0] == 0) {
            binding.tvTotalMoney.setText(String.format(requireContext().getString(R.string.money_format_gbp), totalMoney[0]));
        }

        viewModel.saveBankObjectList();
    }

    private void removeBankObject(BankObject bankObject) {
        binding.bankContainer.removeView(bankObject.getView());
        bankObjectList.remove(bankObject);
        viewModel.setBankObjectList(bankObjectList);
        updateTotalMoney();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.saveBankObjectList();
        binding.bankContainer.removeAllViews();
        binding = null;
    }
}
