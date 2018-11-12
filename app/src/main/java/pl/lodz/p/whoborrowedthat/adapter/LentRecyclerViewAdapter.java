package pl.lodz.p.whoborrowedthat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.controller.BorrowedStuffDetailActivity;
import pl.lodz.p.whoborrowedthat.controller.LentStuffDetailActivity;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.viewmodel.LentViewModel;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.STUFF_BUNDLE__KEY;

public class LentRecyclerViewAdapter extends RecyclerView.Adapter<LentRecyclerViewAdapter.ViewHolder> {

    public void setVM(LentViewModel lvm) {
        this.lvm = lvm;
    }

    private LentViewModel lvm;
    private final LayoutInflater inflater;
    private List<Stuff> stuffs;

    public LentRecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.stuff_item, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull LentRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (stuffs != null) {
            final Stuff current = stuffs.get(position);
            //TODO: set specific data for one item after layout specified

            holder.content.setText(current.getDesc());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), LentStuffDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(STUFF_BUNDLE__KEY, stuffs.get(position));
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            holder.content.setText("Name not found...");
        }
    }

    public void setLents(List<Stuff> stuffs){
        this.stuffs = stuffs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (stuffs != null)
            return stuffs.size();
        else return 0;
    }

    //TODO: set specific data for one item after layout specified
    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;

        private ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
