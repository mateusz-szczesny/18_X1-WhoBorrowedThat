package pl.lodz.p.whoborrowedthat.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.controller.BorrowedStuffDetailActivity;
import pl.lodz.p.whoborrowedthat.controller.LentStuffDetailActivity;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.STUFF_BUNDLE__KEY;

public class BorrowsRecyclerViewAdapter extends RecyclerView.Adapter<BorrowsRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<Stuff> stuffs;
    private Application application;

    public BorrowsRecyclerViewAdapter(Application application) {
        this.application = application;
        inflater = LayoutInflater.from(application.getApplicationContext());
    }

    @NonNull
    @Override
    public BorrowsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.stuff_item, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull BorrowsRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (stuffs != null) {
            final Stuff current = stuffs.get(position);
            holder.content.setText(current.getName());
            if (current.getNotified()) {
                holder.notification.setVisibility(View.VISIBLE);
            } else {
                holder.notification.setVisibility(View.INVISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = SharedPrefHelper.getUserFormSP(application);

                    ApiManager.getInstance().readNotification(user, stuffs.get(position).getId(), new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Log.d("RESPONSE", "Remainder sent!");
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.d("RESPONSE", "Cannot send a remainder :(");
                        }
                    });

                    Intent intent = new Intent(v.getContext(), BorrowedStuffDetailActivity.class);
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

    public void setStuffs(List<Stuff> stuffs){
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
        private final ImageView notification;

        private ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            notification = itemView.findViewById(R.id.notification);
        }
    }
}
