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
        db.collection("perfumes_list_25")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<PerfumeEntity> imageList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        PerfumeEntity perfume = new PerfumeEntity();
                        perfume.setBrand(doc.getString("Brand"));
                        perfume.setName(doc.getString("Perfume Name"));
                        perfume.setImg(doc.getString("img"));
                        perfume.setImgs(doc.getString("imgbg"));
                        perfume.setGender(doc.getString("Gender"));
                        Double year = doc.getDouble("Year");
                        perfume.setYear(year.intValue());
                        Double rating = doc.getDouble("Rating");
                        if (rating != null) perfume.setRating(rating.floatValue());
                        Double longevity = doc.getDouble("Longevity");
                        if (longevity != null) perfume.setLongevity(longevity.floatValue());
                        perfume.setBrandImg(doc.getString("imgBrand"));
                        perfume.setTop(doc.getString("Top Note"));
                        perfume.setHeart(doc.getString("Heart Note"));
                        perfume.setBase(doc.getString("Base Note"));
                        Double price = doc.getDouble("Price");
                        perfume.setPrice(price.floatValue());
                        perfume.setDescription(doc.getString("Description"));
                        perfume.setDesigners(doc.getString("Perfumer"));
                        perfume.setOlfactory(doc.getString("Olfactory Family"));

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
                    sectionAdapter = new SearchSectionAdapter(getContext(), getParentFragmentManager(), parentItems);

                    parentRecyclerView.setAdapter(sectionAdapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE_FAIL", "Lỗi khi tải dữ liệu từ Firestore", e);
                    Toast.makeText(getContext(), "Không thể tải dữ liệu từ Firestore!", Toast.LENGTH_SHORT).show();
                });

    }
    private void loadNotesFromFirestore() {
        FirebaseFirestore dbNotes = FirebaseFirestore.getInstance();
        dbNotes.collection("Group_Note")
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
                sectionAdapter = new SearchSectionAdapter(getContext(), getParentFragmentManager(), parentItems);

                parentRecyclerView.setAdapter(sectionAdapter);
            });
        }).start();
    }

}
