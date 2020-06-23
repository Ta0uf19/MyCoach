package io.mycoach.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.common.base.Strings;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;


import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mycoach.R;
import io.mycoach.model.Message;
import io.mycoach.model.User;
import io.mycoach.repository.AuthRepository;
import io.mycoach.service.BotResponse;
import io.mycoach.service.Service;
import io.mycoach.utils.MenuUtils;
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
    private static final String TAG = "ChatFragment";
    private ImageLoader imageLoader;

    private MessagesListAdapter<Message> messagesAdapter;

    private Service botService;
    private User user;
    private MutableLiveData<Boolean> loading;
    private User botUser;

    @BindView(R.id.loading_chat) ProgressBar pgLoader;
    @BindView(R.id.messagesList) MessagesList messagesList;
    @BindView(R.id.input) MessageInput messageInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        this.loading = new MutableLiveData<Boolean>();
        this.imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        };

        this.messageInput.setInputListener(this);

        this.botService = new Service();

        // set bot as a user
        this.botUser = new User();
        this.botUser.setName("MyCoashBot");
        this.botUser.setAvatar("https://i.imgur.com/SsGLs1r.png");


        // check user if is auth?
        // then load content from firebase
        loadUser();

        // init adapter
        initAdapter();

        // show menu
        MenuUtils.showNavigationMenu(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // si les données chargé.
        this.loading.observe(getViewLifecycleOwner(), status -> {
            if(!status)
                askQuestions();
        });
    }



    public void askQuestions() {

        String username = (user != null) ? " "+user.getName() : "";
        Message msg1 = new Message("MYCOASH_TAG", botUser, "Bonjour" + username + ", mon nom est MyCoach");
        messagesAdapter.addToStart(msg1, true);

        Message msg2 = new Message("MYCOASH_TAG", botUser, "Je suis votre coach virtuelle, permettez-moi de vous poser quelques questions pour mieux vous connaître");
        messagesAdapter.addToStart(msg2, true);

        Message msg3 = new Message("MYCOASH_TAG", botUser, "Combien de fois tu peux t’entrainer dans la semaine ?");
        messagesAdapter.addToStart(msg3, true);
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
               .sendMessage(user.getEmail(), input.toString())
               .enqueue(new Callback<BotResponse>() {
                   @Override
                   public void onResponse(Call<BotResponse> call, Response<BotResponse> response) {
                       if(response.isSuccessful()) {
                           BotResponse resp = response.body();
                           Log.d("BotService fullfilment", resp.toString());

                           Message message = new Message("MYCOACH_TAG", botUser, resp.getFulfillmentText());
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


        Message message = new Message(user.getId(), user, input.toString());
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


    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
    }


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
        messagesAdapter = new MessagesListAdapter<>(AuthRepository.getAuth().getCurrentUser().getUid(), imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setDateHeadersFormatter(this);
        messagesList.setAdapter(messagesAdapter);
    }

    /**
     * Load user from db
     */
    private void loadUser() {
        startLoading();
        FirebaseUser currentUser = AuthRepository.getAuth().getCurrentUser();

        Log.d("currentUser", currentUser.getEmail());
        if(Strings.isNullOrEmpty(currentUser.getEmail())) {
            stopLoading();
            return;
        }

        AuthRepository.find(currentUser.getEmail()).observe(getViewLifecycleOwner(), user -> {
            this.user = user;

//            if(this.user.isNew()) {
//                this.user.setNew(false);
//                AuthRepository.update(this.user);
//            }

            stopLoading();

            Log.d(TAG, "i received entity user " + user);
        });
    }

    private void startLoading() {
        this.pgLoader.setVisibility(View.VISIBLE);
        this.loading.setValue(true);
    }
    private void stopLoading() {
        this.pgLoader.setVisibility(View.INVISIBLE);
        this.loading.setValue(false);
    }
}
