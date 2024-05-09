package com.example.appconsultorio.ui.relatorioconsulta;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentRelatorioConsultaBinding;

public class RelatorioConsultaFragment extends Fragment {
    private FragmentRelatorioConsultaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRelatorioConsultaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}