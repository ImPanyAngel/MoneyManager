package com.example.moneymanager;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moneymanager.databinding.FragmentSubscriptionBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubscriptionFragment extends Fragment {

    private FragmentSubscriptionBinding binding;
    private MyViewModel viewModel;
    List<SubscriptionObject> subscriptionObjectList;

    public SubscriptionFragment() { /* Required empty public constructor */ }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize View Binding
        binding = FragmentSubscriptionBinding.inflate(inflater, container, false);

        binding.fabSubscription.setOnClickListener(v -> showSubscriptionInputDialog());

        // Return the root view from the binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Application application = requireActivity().getApplication();
        MyViewModelFactory factory = new MyViewModelFactory(application);
        viewModel = new ViewModelProvider(this, factory).get(MyViewModel.class);
        subscriptionObjectList = viewModel.getSubscriptionObjectList();
        updateUI();
    }

    private void updateUI() {
        binding.subscriptionsContainer.removeAllViews();

        List<SubscriptionObject> tempList = new ArrayList<>();

        Iterator<SubscriptionObject> iterator = subscriptionObjectList.iterator();
        if (iterator.hasNext()) {
            do {
                SubscriptionObject subscriptionObject = iterator.next();
                addSubscriptionObjectToContainer(subscriptionObject, tempList);
            } while (iterator.hasNext());
        }

        subscriptionObjectList.clear();
        subscriptionObjectList.addAll(tempList);
    }

    private void addSubscriptionObjectToContainer(SubscriptionObject subscriptionObject, List<SubscriptionObject> tempList) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View subscriptionObjectView = inflater.inflate(R.layout.subscription_object, binding.subscriptionsContainer, false);

        TextView nameTextView = subscriptionObjectView.findViewById(R.id.tvSubTitle);
        TextView valueTextView = subscriptionObjectView.findViewById(R.id.tvBankMoney);
        TextView dateTextView = subscriptionObjectView.findViewById(R.id.tvDate);
        TextView bankTextView = subscriptionObjectView.findViewById(R.id.tvBank);

        nameTextView.setText(subscriptionObject.getName());
        valueTextView.setText(subscriptionObject.getMoney());
        dateTextView.setText(subscriptionObject.getDate());
        bankTextView.setText(subscriptionObject.getBank());

        SubscriptionObject subscriptionObjectInstance = new SubscriptionObject(subscriptionObjectView);
        subscriptionObjectInstance.setDeleteButtonListener(this::removeSubscription);

        tempList.add(subscriptionObjectInstance);
        viewModel.setSubscriptionObjectList(subscriptionObjectList);
        binding.subscriptionsContainer.addView(subscriptionObjectView);
    }

    private void showSubscriptionInputDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        SubscriptionInputDialogFragment dialog = new SubscriptionInputDialogFragment();
        dialog.setSubscriptionInputListener(this::addSubscription);
        dialog.show(fragmentManager, "SubscriptionInputDialog");
    }

    private void addSubscription(String title, String amount, String date, String bank, String currency) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View subscriptionObject = inflater.inflate(R.layout.subscription_object, binding.subscriptionsContainer, false);

        TextView textView = subscriptionObject.findViewById(R.id.tvSubTitle);
        textView.setText(title);

        textView = subscriptionObject.findViewById(R.id.tvBank);
        textView.setText(bank);

        textView = subscriptionObject.findViewById(R.id.tvDate);
        textView.setText(date);

        textView = subscriptionObject.findViewById(R.id.tvBankMoney);
        switch (currency) {
            case "GBP":
                textView.setText(String.format(requireContext().getString(R.string.money_format_gbp), Float.parseFloat(amount)));
                break;
            case "EUR":
                textView.setText(String.format(requireContext().getString(R.string.money_format_eur), Float.parseFloat(amount)));
                break;
        }

        SubscriptionObject subscriptionObjectInstance = new SubscriptionObject(subscriptionObject);
        subscriptionObjectInstance.setDeleteButtonListener(this::removeSubscription);

        subscriptionObjectList.add(subscriptionObjectInstance);
        viewModel.setSubscriptionObjectList(subscriptionObjectList);
        binding.subscriptionsContainer.addView(subscriptionObject);
        viewModel.saveSubscriptionObjectList();
    }

    private void removeSubscription(SubscriptionObject subscriptionObject) {
        binding.subscriptionsContainer.removeView(subscriptionObject.getView());
        subscriptionObjectList.remove(subscriptionObject);
        viewModel.setSubscriptionObjectList(subscriptionObjectList);
        viewModel.saveSubscriptionObjectList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.saveSubscriptionObjectList();
        binding.subscriptionsContainer.removeAllViews();
        binding = null;
    }
}
