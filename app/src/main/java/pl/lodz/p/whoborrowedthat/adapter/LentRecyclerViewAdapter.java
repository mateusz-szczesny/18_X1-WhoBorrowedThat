package pl.lodz.p.whoborrowedthat.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.controller.LentStuffDetailActivity;
import pl.lodz.p.whoborrowedthat.model.Stuff;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.STUFF_BUNDLE__KEY;

public class LentRecyclerViewAdapter extends RecyclerView.Adapter<LentRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<Stuff> stuffs;
    private SimpleDateFormat format;

    public LentRecyclerViewAdapter(Application application) {
        inflater = LayoutInflater.from(application.getApplicationContext());
        format = new SimpleDateFormat("yyyy-MM-dd");
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
            Date now = new Date();
            Date returnDate = current.getEstimatedReturnDate();
            Date diff = new Date(returnDate.getTime() - now.getTime());
            holder.daysLeft.setText(String.valueOf(diff.getTime() / (24 * 3600000)));

            holder.content.setText(current.getName());
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView daysLeft;


        private ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            daysLeft = itemView.findViewById(R.id.numberOfDaysLeft);
        }
    }
}
