package com.drp52.ui.fixtures;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.drp52.databinding.FragmentFixturesBinding;

public class FixturesFragment extends Fragment {

    private FragmentFixturesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FixturesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(FixturesViewModel.class);

        binding = FragmentFixturesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}