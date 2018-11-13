package pl.lodz.p.whoborrowedthat.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.adapter.BorrowsRecyclerViewAdapter;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.viewmodel.BorrowViewModel;

import java.util.List;

import static pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper.getUserFormSP;

public class BorrowListFragment extends Fragment {

    private BorrowViewModel borrowViewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public BorrowListFragment() {
    }

    public static BorrowListFragment newInstance() {
        return new BorrowListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.borrow_item_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.listOfBorrowedStuff);

        final BorrowsRecyclerViewAdapter borrowsRecyclerViewAdapter = new BorrowsRecyclerViewAdapter(context);
        recyclerView.setAdapter(borrowsRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        borrowViewModel = ViewModelProviders.of(this).get(BorrowViewModel.class);
        borrowViewModel.getAllBorrows().observe(this, new Observer<List<Stuff>>() {
            @Override
            public void onChanged(@Nullable List<Stuff> stuffs) {
                borrowsRecyclerViewAdapter.setStuffs(stuffs);
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutBorrow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                borrowViewModel.refreshData(getActivity().getApplication());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }
}
