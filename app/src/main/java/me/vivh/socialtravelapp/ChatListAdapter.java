package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Trip;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{
    interface Callback{
        void openChat(@NonNull Trip trip);
    }

    private ChatListAdapter.Callback inputCallback;
    private List<Trip> mChats;
    private List<String> mMembers;
    Context context;


    public ChatListAdapter(List<Trip> trips, ChatListAdapter.Callback callback) {
        mChats = trips;
        inputCallback = callback;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.item_chat_list, viewGroup, false);
        final ChatListAdapter.ViewHolder viewHolder = new ChatListAdapter.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputCallback.openChat(mChats.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder viewHolder, int i) {
        Trip trip = mChats.get(i);

        viewHolder.tvGroupName.setText(trip.getName());
        viewHolder.tvDate.setText(trip.getDateString());
        viewHolder.tvDescription.setText(trip.getDescription());
        viewHolder.tvMembers.setText("Send a message to your TripMates!");


        try{
            String url = trip.getAttraction().fetchIfNeeded().getParseFile("image").getUrl();
            Glide.with(context).load(url)
                    .apply(
                            RequestOptions.placeholderOf(R.drawable.background_gradient)
                                    .circleCrop())
                    .into(viewHolder.ivAttractionPic);
        }catch(ParseException e){
            e.printStackTrace();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvGroupName) TextView tvGroupName;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.ivAttractionPic) ImageView ivAttractionPic;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.tvMembers) TextView tvMembers;

        public ViewHolder(View itemView){

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public void clear() {
        mChats.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Trip> list) {
        mChats.addAll(list);
        notifyDataSetChanged();
    }

}
