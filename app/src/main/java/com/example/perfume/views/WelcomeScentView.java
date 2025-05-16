package com.example.perfume.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.perfume.R;
import com.example.perfume.adapters.ScentAdapter;
import com.example.perfume.entities.ScentEntity;
import com.example.perfume.entities.UserEntity;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeScentView extends LinearLayout {

    private CardStackView cardStackView;
    private List<ScentEntity> scentList;
    private CardStackLayoutManager manager;
    Map<Integer, String> likedCounts = new HashMap<>();
    private UserEntity userEntity;
    private String olfactive;


    // Constructor 1
    public WelcomeScentView(Context context,UserEntity userEntity, Runnable onNext) {
        super(context);
        this.userEntity=userEntity;
        init(context, onNext);

    }



    private void init(Context context, Runnable onNext) {


        LayoutInflater.from(context).inflate(R.layout.welcome_scent, this, true);

        // Ánh xạ cardStackView từ layout
        cardStackView = findViewById(R.id.card_stack_view);

        // Tạo dữ liệu mùi hương
        scentList = new ArrayList<>();
        scentList.add(new ScentEntity("Seaside", "Aquatic notes, Iody notes", R.drawable.seaside, "Aquatic"));
        scentList.add(new ScentEntity("Forest", "Green notes, Woody notes", R.drawable.forest, "Woody"));
        scentList.add(new ScentEntity("Vanilla", "Warm spices", R.drawable.vanila, "Powdery"));
        scentList.add(new ScentEntity("Orange", "Orange, Lemon zest", R.drawable.orange, "Citrus"));
        scentList.add(new ScentEntity("Strawberry", "Red fruits", R.drawable.strawberry, "Fruity"));
        scentList.add(new ScentEntity("Herbal", "Natural herbaceous vibe", R.drawable.herbal, "Herbal"));
        scentList.add(new ScentEntity("Rose", "Delicate flowers", R.drawable.rose, "Floral"));

        // Tạo Adapter
        ScentAdapter adapter = new ScentAdapter(scentList);

        // Khởi tạo CardStackLayoutManager
        manager = new CardStackLayoutManager(context, new com.yuyakaido.android.cardstackview.CardStackListener() {
            @Override
            public void onCardSwiped(Direction direction) {
                int index = manager.getTopPosition() - 1;
                if (index >= 0 && index < scentList.size()) {
                    if (direction == Direction.Right) {
                        String category = scentList.get(index).getCategory();
                        // Nếu đã có trong map thì +1, chưa có thì đặt = 1
                        likedCounts.put(index, category);

                    }
                }
                if (index == 6) {
                    // Chuyển Map thành chuỗi
                    StringBuilder olfactiveBuilder = new StringBuilder();
                    for (Map.Entry<Integer, String> entry : likedCounts.entrySet()) {
                        ScentEntity scent = scentList.get(entry.getKey());
                        // Thêm vào chuỗi với định dạng: "Tên mùi hương: Category"
                        if (olfactiveBuilder.length() > 0) {
                            olfactiveBuilder.append(", ");
                        }
                        olfactiveBuilder.append(entry.getValue());
                    }
                    // Gán chuỗi vào olfactive
                    olfactive = olfactiveBuilder.toString();
                   userEntity.setCategoryList(olfactive);

                    // Chạy animation và gọi onNext
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
                    postDelayed(onNext::run, 200);
                }
            }


            @Override
            public void onCardDragging(com.yuyakaido.android.cardstackview.Direction direction, float ratio) {
            }

            @Override
            public void onCardRewound() {
            }

            @Override
            public void onCardCanceled() {
            }

            @Override
            public void onCardAppeared(View view, int position) {
            }

            @Override
            public void onCardDisappeared(View view, int position) {
            }
        });

        // Thiết lập layout manager và adapter
        manager.setStackFrom(com.yuyakaido.android.cardstackview.StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(com.yuyakaido.android.cardstackview.Direction.HORIZONTAL);
        manager.setSwipeThreshold(0.3f);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(com.yuyakaido.android.cardstackview.SwipeableMethod.AutomaticAndManual);

        // Gán vào cardStackView
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }


}
