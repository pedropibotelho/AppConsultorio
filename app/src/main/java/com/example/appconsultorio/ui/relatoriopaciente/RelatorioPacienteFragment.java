package com.example.appconsultorio.ui.relatoriopaciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentRelatorioPacienteBinding;


public class RelatorioPacienteFragment extends Fragment {
    private FragmentRelatorioPacienteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRelatorioPacienteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}