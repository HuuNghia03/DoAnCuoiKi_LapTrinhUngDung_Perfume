package com.example.perfume;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private com.example.perfume.PerfumeDatabase perfumeDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plash_screen);
        perfumeDatabase = com.example.perfume.PerfumeDatabase.getInstance(SplashActivity.this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isFirstLaunch()) {
                    loadPerfumeFromFirestore();
                    loadNotesFromFirestore();
                    loadBrandsFromFirestore();
                    intent = new Intent(SplashActivity.this, com.example.perfume.first_activity.class); // ví dụ: màn giới thiệu
                    setFirstLaunchFalse(); // đánh dấu là đã mở app lần đầu
                } else {
                    intent = new Intent(SplashActivity.this, com.example.perfume.home_activity.class); // màn chính
                }

                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 2500);
    }

    private boolean isFirstLaunch() {
        return getSharedPreferences("prefs", MODE_PRIVATE)
                .getBoolean("isFirstLaunch", true);
    }

    private void setFirstLaunchFalse() {
        getSharedPreferences("prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("isFirstLaunch", false)
                .apply();
    }
    private void loadPerfumeFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("perfumes_list_25_1")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<com.example.perfume.PerfumeEntity> imageList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        com.example.perfume.PerfumeEntity perfume = new com.example.perfume.PerfumeEntity();
                        perfume.setBrand(doc.getString("Brand"));
                        perfume.setName(doc.getString("Perfume Name"));
                        perfume.setImg(doc.getString("img"));
                        perfume.setImgs(doc.getString("imgbg"));
                        perfume.setGender(doc.getString("Gender"));
                        perfume.setConcentration(doc.getString("Concentration"));
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

                        perfume.setVolumes(doc.getString("Volumes"));
                        perfume.setPrices(doc.getString("Prices"));
                        imageList.add(perfume);
                    }

                    // Lưu dữ liệu vào Room
                    new Thread(() -> {
                        perfumeDatabase.perfumeDao().deleteAll(); // xóa dữ liệu cũ nếu cần
                        perfumeDatabase.perfumeDao().insertList(imageList);
                        setFirstLaunchFalse(); // chỉ đặt khi đã lưu thành công
                    }).start();

                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE_FAIL", "Lỗi khi tải dữ liệu từ Firestore", e);
                    Toast.makeText(SplashActivity.this, "Không thể tải dữ liệu từ Firestore!", Toast.LENGTH_SHORT).show();
                });

    }
    private void loadNotesFromFirestore() {
        FirebaseFirestore dbNotes = FirebaseFirestore.getInstance();
        dbNotes.collection("Group_Note")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<com.example.perfume.Note> noteList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String category = doc.getString("category");
                        String notes = doc.getString("notes");
                        String imageUrl = doc.getString("imageUrl");
                        String description=doc.getString("description");
                        // Tạo đối tượng Note
                        noteList.add(new com.example.perfume.Note(category, notes, imageUrl,description));
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
    public void loadBrandsFromFirestore() {
        FirebaseFirestore dbBrands = FirebaseFirestore.getInstance();
        dbBrands.collection("Brands")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<com.example.perfume.BrandEntity> brandList = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        com.example.perfume.BrandEntity brand = new com.example.perfume.BrandEntity();
                        brand.setId(doc.getId()); // dùng doc ID làm id chính
                        brand.setName(doc.getString("name"));
                        brand.setBanner(doc.getString("banner"));
                        brand.setLogo(doc.getString("logo"));
                        brand.setPerfumes(doc.getString("perfumes"));
                        brand.setFounded(doc.getString("founded") );
                        brand.setFounder(doc.getString("founder"));
                        brand.setCountry(doc.getString("country"));
                        brand.setNotablePerfumes(doc.getString("notablePerfumes"));
                        brand.setSegment(doc.getString("segment"));
                        brand.setPopularity(doc.getString("popularity"));
                        brand.setStyle(doc.getString("style"));
                        brand.setLink(doc.getString("link"));
                        brand.setDescription(doc.getString("description"));

                        brandList.add(brand);
                    }
                    new Thread(() -> {
                        perfumeDatabase.BrandDao().insertBrandList(brandList);
                    }).start();
                })
                .addOnFailureListener(e -> {
                    Log.e("BrandRepo", "❌ Lỗi khi lấy dữ liệu từ Firestore", e);
                });
    }


}
