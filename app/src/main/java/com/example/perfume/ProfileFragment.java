package com.example.perfume;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment {
    private LinearLayout userOrder;
    public ProfileFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userOrder=view.findViewById(R.id.userOrder);
        userOrder.setOnClickListener(v -> {
            Fragment orderFragment = new OrderFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            transaction.setCustomAnimations(
                    R.anim.zoom_in, // enter
                  0, // exit
                    R.anim.pop_zoom_in, // popEnter (khi quay lại)
                   0  // popExit
            );

            transaction.replace(R.id.fragment_container, orderFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
