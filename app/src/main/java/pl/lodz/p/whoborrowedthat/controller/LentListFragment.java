package pl.lodz.p.whoborrowedthat.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.adapter.LentRecyclerViewAdapter;
import pl.lodz.p.whoborrowedthat.command.SearchCommand;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.viewmodel.LentViewModel;

public class LentListFragment extends Fragment implements SearchCommand{

    private LentViewModel lentViewModel;
    private FloatingActionButton addButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;


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
    public void onResume() {
        super.onResume();
        lentViewModel.refreshData(getActivity().getApplication());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lent_item_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.listOfBorrowedStuff);

        final LentRecyclerViewAdapter lentRecyclerViewAdapter = new LentRecyclerViewAdapter(getActivity().getApplication());
        recyclerView.setAdapter(lentRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        lentViewModel = ViewModelProviders.of(this).get(LentViewModel.class);
        lentViewModel.getAllLents().observe(getViewLifecycleOwner(), new Observer<List<Stuff>>() {
            @Override
            public void onChanged(@Nullable List<Stuff> stuffs) {
                lentRecyclerViewAdapter.setLents(stuffs);
            }
        });

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StuffAddActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutLent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lentViewModel.refreshData(getActivity().getApplication());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void execute(String searchText) {
        lentViewModel.search(searchText);
    }
}
