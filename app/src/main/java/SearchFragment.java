package com.example.perfume;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public SearchFragment() {}

    private RecyclerView parentRecyclerView;
    private List<com.example.perfume.SearchSection> parentItems = new ArrayList<>();
    private com.example.perfume.SearchSectionAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        parentRecyclerView = view.findViewById(R.id.recyclerViewParent);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gọi hàm tải ảnh từ Firestore
        loadPerfumeImagesFromFirestore();

        return view;
    }

    private void loadPerfumeImagesFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("perfumes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<com.example.perfume.PerfumeEntity> imageList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        com.example.perfume.PerfumeEntity perfume = new com.example.perfume.PerfumeEntity();
//                        String imgUrl = doc.getString("img");
//                        if (imgUrl != null) {
//                            imageList.add(new com.example.perfume.SearchItem(imgUrl));
//                        }
                        perfume.setImg(doc.getString("img"));
                        perfume.setBrand(doc.getString("brand"));
                        perfume.setName(doc.getString("name"));
                        perfume.setGender(doc.getString("sex"));
                        Double year=doc.getDouble("year");
                        if(year !=null) {
                            perfume.setYear(year.intValue());
                        }



                        imageList.add(perfume);
                    }

                    // Gán danh sách SearchItem vào một section
                    parentItems.clear(); // Xóa dữ liệu cũ nếu có
                    parentItems.add(new com.example.perfume.SearchSection("Explore by Perfume", imageList));

                    // Có thể thêm thêm section khác nếu cần
                    // parentItems.add(new SearchSection("Explore by Brand", ...));

                    sectionAdapter = new com.example.perfume.SearchSectionAdapter(getContext(), parentItems);
                    parentRecyclerView.setAdapter(sectionAdapter);
                })
                .addOnFailureListener(e -> Log.e("FIRESTORE_FAIL", e.getMessage()));
    }
}
