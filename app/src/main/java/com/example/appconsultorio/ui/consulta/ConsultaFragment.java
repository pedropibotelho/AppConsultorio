package com.example.appconsultorio.ui.consulta;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentConsultaBinding;

public class ConsultaFragment extends Fragment {
    private FragmentConsultaBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConsultaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}