package io.mycoach.ui.chat;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;


import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mycoach.R;
import io.mycoach.model.Message;
import io.mycoach.model.User;
import io.mycoach.service.BotResponse;
import io.mycoach.service.BotService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment implements
        MessageInput.InputListener,
        DateFormatter.Formatter,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener
{

    private int selectionCount;
    private Date lastLoadedDate;
    private static final int TOTAL_MESSAGES_COUNT = 100;

    private static final String TAG = "ChatFragment";
    protected final String senderId = "0";
    protected ImageLoader imageLoader;

    protected MessagesListAdapter<Message> messagesAdapter;

    private BotService botService;
    private User user;
    private User botUser;


    @BindView(R.id.messagesList) MessagesList messagesList;
    @BindView(R.id.input) MessageInput messageInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        this.imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        };

        this.messageInput.setInputListener(this);

        this.botService = new BotService();
        this.botUser = new User("1", "CoashBot", "nothing");
        this.botUser.setAvatar("https://i.imgur.com/SsGLs1r.png");

        this.user = new User("0", "User", "email@gmail.com");

        // init adapter
        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Message msg1 = new Message(getRandomId(), botUser, "Bonjour, mon nom est MyCoach");
        messagesAdapter.addToStart(msg1, true);

        Message msg2 = new Message(getRandomId(), botUser, "Je suis votre coach virtuelle, permettez-moi de vous poser quelques questions pour mieux vous connaître");
        messagesAdapter.addToStart(msg2, true);
    }


    /**
     * Fires when user press send button
     * @param input
     * @return
     */
    @Override
    public boolean onSubmit(CharSequence input) {

        Log.d(TAG, input.toString());

       botService
               .sendMessage("3f", input.toString())
               .enqueue(new Callback<BotResponse>() {
                   @Override
                   public void onResponse(Call<BotResponse> call, Response<BotResponse> response) {
                       if(response.isSuccessful()) {
                           BotResponse resp = response.body();
                           Log.d("BotService fullfilment", resp.toString());

                           Message message = new Message(getRandomId(), botUser, resp.getFulfillmentText());
                           messagesAdapter.addToStart(message, true);

                       } else {
                           System.out.println(response.errorBody());
                       }
                   }

                   @Override
                   public void onFailure(Call<BotResponse> call, Throwable t) {
                       t.printStackTrace();
                   }
               });


        Message message = new Message(getRandomId(), user, input.toString());
        messagesAdapter.addToStart(message, true);
       // messagesAdapter.addToStart(MessagesFixtures.getTextMessage(input.toString()), true);
        return true;
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return "Aujourd'hui";
        } else if (DateFormatter.isYesterday(date)) {
            return "Hier";
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

//    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
//        return new MessagesListAdapter.Formatter<Message>() {
//            @Override
//            public String format(Message message) {
//                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
//                        .format(message.getCreatedAt());
//
//                String text = message.getText();
//                if (text == null) text = "[attachment]";
//
//                return String.format(Locale.getDefault(), "%s: %s (%s)",
//                        message.getUser().getName(), text, createdAt);
//            }
//        };
//    }
    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
    }

//    protected void loadMessages() {
//        new Handler().postDelayed(new Runnable() {  //imitation of internet connection
//            @Override
//            public void run() {
//                ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
//                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
//                messagesAdapter.addToEnd(messages, false);
//            }
//        }, 1000);
//    }

    /**
     * Load more message
     * @param page
     * @param totalItemsCount
     */
    @Override
    public void onLoadMore(int page, int totalItemsCount) {
       Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
    }

    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setDateHeadersFormatter(this);
        messagesList.setAdapter(messagesAdapter);
    }

    static String getRandomId() {
        return Long.toString(UUID.randomUUID().getLeastSignificantBits());
    }
}
