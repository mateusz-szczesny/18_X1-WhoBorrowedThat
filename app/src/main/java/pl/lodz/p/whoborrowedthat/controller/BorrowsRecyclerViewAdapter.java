package pl.lodz.p.whoborrowedthat.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.model.Borrow;
import pl.lodz.p.whoborrowedthat.viewmodel.BorrowViewModel;

import java.util.List;

public class BorrowsRecyclerViewAdapter extends RecyclerView.Adapter<BorrowsRecyclerViewAdapter.ViewHolder> {

    public void setVM(BorrowViewModel bvm) {
        this.bvm = bvm;
    }

    private BorrowViewModel bvm;
    private final LayoutInflater inflater;
    private List<Borrow> borrows ;

    public BorrowsRecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BorrowsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.borrow_item, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull BorrowsRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (borrows != null) {
            final Borrow current = borrows.get(position);
            //TODO: set specific data for one item after layout specified
            holder.id.setText(current.getName());
            holder.content.setText(current.getDesc());
        } else {
            holder.content.setText("Name not found...");
        }
    }

    public void setBorrows(List<Borrow> borrows){
        this.borrows = borrows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (borrows != null)
            return borrows.size();
        else return 0;
    }

    //TODO: set specific data for one item after layout specified
    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView id;
        private final TextView content;

        private ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.item_number);
            content = itemView.findViewById(R.id.content);
        }
    }
}
