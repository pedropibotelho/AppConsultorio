package com.example.appconsultorio.ui.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentPacienteBinding;

public class PacienteFragment extends Fragment {
    private FragmentPacienteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPacienteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}