package com.example.perfume;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import com.example.perfume.SearchSection;
import com.example.perfume.SearchSectionAdapter;
import com.example.perfume.PerfumeDatabase;
import com.example.perfume.PerfumeEntity;
import com.example.perfume.BrandWithImage;
import com.example.perfume.Note;
import com.example.perfume.NoteDao;

public class SearchFragment extends Fragment {

    public SearchFragment() {
    }

    private RecyclerView parentRecyclerView;
    private List<SearchSection<?>> parentItems = new ArrayList<>();
    private SearchSectionAdapter sectionAdapter;
    private PerfumeDatabase perfumeDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        parentRecyclerView = view.findViewById(R.id.recyclerViewParent);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        perfumeDatabase = PerfumeDatabase.getInstance(requireContext());

        // Kiểm tra có phải lần đầu mở app không
        if (isFirstLaunch()) {
            loadPerfumeFromFirestore();
            loadNotesFromFirestore();

        } else {
            loadPerfumeFromRoom();

        }

        return view;
    }

    private boolean isFirstLaunch() {
        return requireContext()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .getBoolean("isFirstLaunch", true);
    }

    private void setFirstLaunchFalse() {
        requireContext()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("isFirstLaunch", false)
                .apply();
    }

    private void loadPerfumeFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("perfumes_list")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<PerfumeEntity> imageList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        PerfumeEntity perfume = new PerfumeEntity();
                        perfume.setPid(doc.getString("pid"));
                        perfume.setBrand(doc.getString("brand"));
                        perfume.setName(doc.getString("name"));
                        perfume.setImg(doc.getString("img"));
                        perfume.setParent(doc.getString("parent"));
                        perfume.setImgs(doc.getString("imgs"));
                        perfume.setGender(doc.getString("sex"));
                        perfume.setOrigin(doc.getString("origin"));
                        Double year = doc.getDouble("year");
                        perfume.setYear(year != null ? year.intValue() : 2025);
                        perfume.setType(doc.getString("type"));
                        perfume.setAvailable(doc.getString("available"));
                        perfume.setLimited(doc.getString("limited"));
                        perfume.setCollector(doc.getString("collector"));
                        Double rating = doc.getDouble("rating");
                        if (rating != null) perfume.setRating(rating.floatValue());
                        Long ratingVotes = doc.getLong("ratingVotes");
                        if (ratingVotes != null) perfume.setRatingVotes(ratingVotes.intValue());

                        Double longevity = doc.getDouble("longevity");
                        if (longevity != null) perfume.setLongevity(longevity.floatValue());

                        Double sillage = doc.getDouble("sillage");
                        if (sillage != null) perfume.setSillage(sillage.floatValue());

                        perfume.setVideo(doc.getString("video"));
                        perfume.setBrandUrl(doc.getString("brandUrl"));
                        perfume.setBrandImg(doc.getString("brandImg"));
                        perfume.setPerfumers(doc.getString("perfumers"));
                        perfume.setDesigners(doc.getString("designers"));
                        perfume.setAccords(doc.getString("accords"));
                        perfume.setTop(doc.getString("top"));
                        perfume.setHeart(doc.getString("heart"));
                        perfume.setBase(doc.getString("base"));

                        imageList.add(perfume);
                    }

                    // Lưu dữ liệu vào Room
                    new Thread(() -> {
                        perfumeDatabase.perfumeDao().deleteAll(); // xóa dữ liệu cũ nếu cần
                        perfumeDatabase.perfumeDao().insertList(imageList);
                        setFirstLaunchFalse(); // chỉ đặt khi đã lưu thành công
                    }).start();

                    // Hiển thị dữ liệu lên RecyclerView
                    parentItems.clear();
                    parentItems.add(new SearchSection("Explore by Perfume", imageList));
                    sectionAdapter = new SearchSectionAdapter(getContext(), parentItems);
                    parentRecyclerView.setAdapter(sectionAdapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE_FAIL", "Lỗi khi tải dữ liệu từ Firestore", e);
                    Toast.makeText(getContext(), "Không thể tải dữ liệu từ Firestore!", Toast.LENGTH_SHORT).show();
                });

    }
    private void loadNotesFromFirestore() {
        FirebaseFirestore dbNotes = FirebaseFirestore.getInstance();
        dbNotes.collection("perfume_notes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Note> noteList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String category = doc.getString("category");
                        String notes = doc.getString("notes");
                        String imageUrl = doc.getString("imageUrl");

                        // Tạo đối tượng Note
                        noteList.add(new Note(category, notes, imageUrl));
                    }

                    // Lưu nốt hương vào Room
                    new Thread(() -> {
                        perfumeDatabase.noteDao().insertList(noteList);
                    }).start();
                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE_FAIL", "Lỗi khi tải dữ liệu từ Firestore", e);
                });
    }


    private void loadPerfumeFromRoom() {
        new Thread(() -> {
            List<PerfumeEntity> perfumeList = perfumeDatabase.perfumeDao().getAllPerfumes();
            List<BrandWithImage> brandList = perfumeDatabase.perfumeDao().getAllBrandsWithImage();
            List<Note> noteList = perfumeDatabase.noteDao().getAllNotes();

            // Lấy tối đa 10 phần tử đầu tiên nếu có đủ
            List<PerfumeEntity> topPerfumes = perfumeList.size() > 10 ? perfumeList.subList(0, 10) : perfumeList;
            List<BrandWithImage> topBrands = brandList.size() > 10 ? brandList.subList(0, 10) : brandList;
            List<Note> topNotes = noteList.size() > 5 ? noteList.subList(0, 5) : noteList;

            requireActivity().runOnUiThread(() -> {
                parentItems.clear();
                parentItems.add(new SearchSection<>("Explore by Perfume", topPerfumes));
                parentItems.add(new SearchSection<>("Explore by Brands", topBrands));
                parentItems.add(new SearchSection<>("Explore by Notes", topNotes));
                sectionAdapter = new SearchSectionAdapter(getContext(), parentItems);
                parentRecyclerView.setAdapter(sectionAdapter);
            });
        }).start();
    }

}
