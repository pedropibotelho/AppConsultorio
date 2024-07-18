package com.example.appconsultorio.ui.financas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentFinancaBinding;

public class FinancaFragment extends Fragment {
    private static final String TAG = "FinancasFrag";
    private FragmentFinancaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflando o layout do fragmento das finanças.");
        return inflater.inflate(R.layout.fragment_financa, container, false);
    }
}