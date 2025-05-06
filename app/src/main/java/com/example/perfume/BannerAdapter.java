    package com.example.perfume;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.viewpager2.widget.ViewPager2;

    import java.util.ArrayList;
    import java.util.List;

    public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
//        private List<com.example.perfume.BannerItem> imageList;
//
//        public BannerAdapter(List<com.example.perfume.BannerItem> imageList) {
//            this.imageList = imageList;
//        }
        private List<com.example.perfume.BannerItem> bannerItemList;

        public BannerAdapter(List<com.example.perfume.BannerItem> bannerItemList) {
            this.bannerItemList = bannerItemList;
        }

        @NonNull
        @Override
        public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_search, parent, false);
//            return new BannerViewHolder(view);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item, parent, false);
            return new BannerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
//            List<com.example.perfume.BannerItem> bannerItems = new ArrayList<>();
//            for (com.example.perfume.BannerItem item : imageList) {
//                int resId = item.getImageResId();
//                bannerItems.add(new com.example.perfume.BannerItem(resId));
//            }
//
//            // Đảm bảo ViewPager2 cuộn ngang
//            holder.viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//
//            com.example.perfume.BannerItemAdapter itemAdapter = new com.example.perfume.BannerItemAdapter(bannerItems);
//            holder.viewPager2.setAdapter(itemAdapter);
            com.example.perfume.BannerItem item = bannerItemList.get(position);
            holder.imageBanner.setImageResource(item.getImageResId());
        }

//        @Override
//        public int getItemCount() {
//            return 1; // Vì chỉ có 1 block banner
//        }
        @Override
        public int getItemCount() {
            return bannerItemList.size();
        }

        static class BannerViewHolder extends RecyclerView.ViewHolder {
//            ViewPager2 viewPager2;
//
//            BannerViewHolder(View itemView) {
//                super(itemView);
//                viewPager2 = itemView.findViewById(R.id.bannerViewPager);
//
//            }
ImageView imageBanner;

            public BannerViewHolder(@NonNull View itemView) {
                super(itemView);
                imageBanner = itemView.findViewById(R.id.imageBanner);
            }
        }

    }
