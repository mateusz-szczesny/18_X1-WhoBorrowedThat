package pl.lodz.p.whoborrowedthat.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.adapter.LentRecyclerViewAdapter;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.viewmodel.LentViewModel;

import static pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper.getUserFormSP;

public class LentListFragment extends Fragment {

    private LentViewModel lentViewModel;

    public LentListFragment() {
    }

    public static LentListFragment newInstance() {
        return new LentListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lent_item_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.listOfBorrowedStuff);

        setWelcome(view);

        final LentRecyclerViewAdapter lentRecyclerViewAdapter = new LentRecyclerViewAdapter(context);
        recyclerView.setAdapter(lentRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        lentViewModel = ViewModelProviders.of(this).get(LentViewModel.class);
        lentRecyclerViewAdapter.setVM(lentViewModel);
        lentViewModel.getAllLents().observe(this, new Observer<List<Stuff>>() {
            @Override
            public void onChanged(@Nullable List<Stuff> stuffs) {
                lentRecyclerViewAdapter.setLents(stuffs);
            }
        });

        return view;
    }

    private void setWelcome(View view) {
        TextView title = view.findViewById(R.id.title);
        String email = getUserFormSP(getActivity().getApplication()).getEmail();
        title.setText(email + " - Lent List");
    }
}
