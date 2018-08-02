package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.vivh.socialtravelapp.model.Message;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case 1:
                View chatViewMe = inflater.inflate(R.layout.item_chat_me, parent, false);
                return new ViewHolderMe(chatViewMe);
            case 0:
                View chatViewOther = inflater.inflate(R.layout.item_chat_other, parent, false);
                return new ViewHolderOther(chatViewOther);
        }
        View chatViewMe = inflater.inflate(R.layout.item_chat_me, parent, false);
        return new ViewHolderMe(chatViewMe);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Message message = mMessages.get(position);

        switch (holder.getItemViewType()) {
            case 1:
                final ViewHolderMe viewHolderMe = (ViewHolderMe) holder;
                viewHolderMe.mInfoMe.setText(message.getTimestamp());
                viewHolderMe.mMessageMe.setText(message.getBody());
                displayProfilePicture(message, viewHolderMe.mProfilePic);
                if (message.getImage() != null) {
                    viewHolderMe.mChatImageMe.setVisibility(View.VISIBLE);
                    displayChatImage(message, viewHolderMe.mChatImageMe);
                }
                else {
                    viewHolderMe.mChatImageMe.setVisibility(View.GONE);
                }
                break;
            case 0:
                final ViewHolderOther viewHolderOther = (ViewHolderOther) holder;
                viewHolderOther.mInfoOther.setText(message.getTimestamp());
                viewHolderOther.mMessageOther.setText(message.getBody());
                try { displayProfilePicture(message, viewHolderOther.mProfilePic);
                    displayProfilePicture(message, viewHolderOther.mProfilePic); }
                catch (Exception e) {e.printStackTrace();}
                if (message.getImage() != null) {
                    viewHolderOther.mChatImageOther.setVisibility(View.VISIBLE);
                    displayChatImage(message, viewHolderOther.mChatImageOther);
                }
                else {
                    viewHolderOther.mChatImageOther.setVisibility(View.GONE);
                }
                break;
        }
    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    public class ViewHolderMe extends RecyclerView.ViewHolder {
        TextView mMessageMe;
        TextView mInfoMe;
        ImageView mProfilePic;
        ImageView mChatImageMe;

        public ViewHolderMe(View itemView) {
            super(itemView);
            mMessageMe = itemView.findViewById(R.id.tvMessageMe);
            mInfoMe = itemView.findViewById(R.id.tvInfoMe);
            mProfilePic = itemView.findViewById(R.id.ivProfilePic);
            mChatImageMe = itemView.findViewById(R.id.ivChatImage);
        }
    }

    public class ViewHolderOther extends RecyclerView.ViewHolder {
        TextView mMessageOther;
        TextView mInfoOther;
        ImageView mProfilePic;
        ImageView mChatImageOther;

        public ViewHolderOther(View itemView) {
            super(itemView);
            mMessageOther = itemView.findViewById(R.id.tvMessageOther);
            mInfoOther = itemView.findViewById(R.id.tvInfoOther);
            mProfilePic = itemView.findViewById(R.id.ivProfilePic);
            mChatImageOther = itemView.findViewById(R.id.ivChatImage);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mMessages.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Message> list) {
        mMessages.addAll(list);
        notifyDataSetChanged();
    }


    private void displayProfilePicture(Message message, ImageView profileView) {
        Glide.with(mContext).load(message.getProfilePic().getUrl())
                .apply(
                        RequestOptions.placeholderOf(R.drawable.background_gradient)
                                .fitCenter()
                                .circleCrop())
                .into(profileView);
    }

    private void displayChatImage(Message message, ImageView chatImage) {
        Log.d("ChatAdapter","Entered displayChatImage");
        Glide.with(mContext).load(message.getImage().getUrl())
                .apply(
                        RequestOptions.placeholderOf(R.drawable.background_gradient)
                                .fitCenter()
                                .transform(new RoundedCornersTransformation(10, 0)))
                .into(chatImage);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessages.get(position);
        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);
        if (isMe) {
            return 1;
        } else {
            return 0;
        }
    }
}