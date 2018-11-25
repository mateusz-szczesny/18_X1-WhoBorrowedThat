package pl.lodz.p.whoborrowedthat.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
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
import pl.lodz.p.whoborrowedthat.controller.StuffAddActivity;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.STUFF_BUNDLE__KEY;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<UserRelation> friends;
    private Application application;

    public FriendsRecyclerViewAdapter(Context context, Application application) {
        this.application = application;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FriendsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.fragment_firend, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull FriendsRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (friends != null) {
            final UserRelation ur = friends.get(position);

            User user = SharedPrefHelper.getUserFormSP(application);

            User relatedUser;
            relatedUser = ur.getRelatedUser();
            if (user.getId() == relatedUser.getId()) {
                relatedUser = ur.getRelatingUser();
            }
            holder.content.setText(relatedUser.getUsername() + " (" + relatedUser.getEmail() + ")");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: do sth on friend item click
                }
            });
        } else {
            holder.content.setText("Name not found...");
        }
    }

    public void setFriends(List<UserRelation> stuffs){
        this.friends = stuffs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (friends != null)
            return friends.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;

        private ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
