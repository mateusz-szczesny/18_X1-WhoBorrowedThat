package pl.lodz.p.whoborrowedthat.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import pl.lodz.p.whoborrowedthat.viewmodel.UserRelationViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<UserRelation> friends;
    private Application application;
    private UserRelationViewModel userRelationViewModel;

    public FriendsRecyclerViewAdapter(Application application) {
        this.application = application;
        inflater = LayoutInflater.from(application.getApplicationContext());
    }

    public void setVM(UserRelationViewModel viewModel) {
        userRelationViewModel = viewModel;
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

            holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = SharedPrefHelper.getUserFormSP(application);

                    ApiManager.getInstance().removeUserRelation(user, friends.get(position).getId(), new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(application.getApplicationContext(),
                                        "Fried removed!",
                                        Toast.LENGTH_LONG).show();
                            }
                            userRelationViewModel.refreshData(application);
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Log.d("RESPONSE", "Cannot send a remainder :(");
                        }
                    });
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
        private final ImageButton removeBtn;

        private ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            removeBtn = itemView.findViewById(R.id.removeRelation);
        }
    }
}
