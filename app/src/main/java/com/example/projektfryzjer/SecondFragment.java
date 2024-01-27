package com.example.projektfryzjer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projektfryzjer.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private ImageButton buttonFb;
    private ImageButton buttonIg;
    private ImageButton buttonTt;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonFb = getView().findViewById(R.id.imageButtonFb);
        buttonFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewAppWithLink("https://www.facebook.com/groups/1761231424107019/?locale=pl_PL");
            }
        });
        buttonIg = getView().findViewById(R.id.imageButtonIg);
        buttonIg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewAppWithLink("https://www.instagram.com/explore/tags/fryzjer/");
            }
        });
        buttonTt = getView().findViewById(R.id.imageButtonTt);
        buttonTt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewAppWithLink("https://www.tiktok.com/tag/fryzjer");
            }
        });
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openNewAppWithLink(String link){
        Uri uri = Uri.parse(link);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}