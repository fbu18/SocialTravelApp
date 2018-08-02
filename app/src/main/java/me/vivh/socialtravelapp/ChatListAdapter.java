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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.vivh.socialtravelapp.model.Message;
import me.vivh.socialtravelapp.model.Trip;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{
    interface Callback{
        void openChat(@NonNull Trip trip);
    }

    private ChatListAdapter.Callback inputCallback;
    private List<Trip> mChats;
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
        Trip chatTrip = mChats.get(i);
        ParseUser currentUser = ParseUser.getCurrentUser();
        viewHolder.tvGroupName.setText(chatTrip.getName());
        viewHolder.tvDate.setText(chatTrip.getDateString());
        viewHolder.tvDescription.setText(chatTrip.getDescription());
        viewHolder.tvChatText.setText("Chat with your fellow explorers!");


        try{
            String url = chatTrip.getAttraction().fetchIfNeeded().getParseFile("image").getUrl();
            Glide.with(context).load(url)
                    .apply(
                            RequestOptions.placeholderOf(R.drawable.background_gradient)
                                    .fitCenter()
                                    .transform(new RoundedCornersTransformation(10, 0)))
                    .into(viewHolder.ivAttractionPic);
        }catch(ParseException e){
            e.printStackTrace();
        }

        if (chatTrip.getLastMessage() == null) {
            viewHolder.tvTimestamp.setText("");
        } else {
            final Message lastMessage = chatTrip.getLastMessage();
            final ParseUser cUser = currentUser;
            final ChatListAdapter.ViewHolder h = viewHolder;
            lastMessage.fetchInBackground(new GetCallback<Message>() {
                @Override
                public void done(Message object, ParseException e) {
                    try {
                        if (object.getUser().getObjectId().equals(cUser.getObjectId())) {
                            h.tvChatText.setText("You: " + lastMessage.getBody());
                        } else {
                            try {
                                h.tvChatText.setText(object.getUsername()+ ": " + lastMessage.getBody());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                        h.tvTimestamp.setText(lastMessage.getTimestamp());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvGroupName) TextView tvGroupName;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.ivAttractionPic) ImageView ivAttractionPic;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.tvChatText) TextView tvChatText;
        @BindView(R.id.tvTimestamp) TextView tvTimestamp;

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

