package com.example.perfume.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.perfume.AppDatabase;
import com.example.perfume.R;
import com.example.perfume.entity.BrandEntity;
import com.example.perfume.entity.NoteEntity;
import com.example.perfume.entity.PerfumeEntity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.plash_screen);

        appDatabase = AppDatabase.getInstance(this);

        new Handler().postDelayed(() -> {
            Intent intent;
            if (isFirstLaunch()) {
                loadPerfumeFromFirestore();
                loadNotesFromFirestore();
                loadBrandsFromFirestore();
                intent = new Intent(SplashActivity.this, FirstActivity.class);
                setFirstLaunchFalse();
            } else {
                intent = new Intent(SplashActivity.this, FirstActivity.class);
                // Nếu muốn chuyển thẳng tới WelcomeActivity, thay đổi intent ở đây
                // intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 1000);
    }

    private boolean isFirstLaunch() {
        return getSharedPreferences("prefs", MODE_PRIVATE)
                .getBoolean("isFirstLaunch", true);
    }

    private void setFirstLaunchFalse() {
        getSharedPreferences("prefs", MODE_PRIVATE).edit()
                .putBoolean("isFirstLaunch", false)
                .apply();
    }

    private void loadPerfumeFromFirestore() {
        FirebaseFirestore.getInstance().collection("perfumes_list_25_1")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<PerfumeEntity> perfumeList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        PerfumeEntity perfume = new PerfumeEntity();
                        perfume.setBrand(doc.getString("Brand"));
                        perfume.setName(doc.getString("Perfume Name"));
                        perfume.setImg(doc.getString("img"));
                        perfume.setImgs(doc.getString("imgbg"));
                        perfume.setGender(doc.getString("Gender"));
                        perfume.setConcentration(doc.getString("Concentration"));

                        Double year = doc.getDouble("Year");
                        if (year != null) perfume.setYear(year.intValue());

                        Double rating = doc.getDouble("Rating");
                        if (rating != null) perfume.setRating(rating.floatValue());

                        Double longevity = doc.getDouble("Longevity");
                        if (longevity != null) perfume.setLongevity(longevity.floatValue());

                        perfume.setBrandImg(doc.getString("imgBrand"));
                        perfume.setTop(doc.getString("Top Note"));
                        perfume.setHeart(doc.getString("Heart Note"));
                        perfume.setBase(doc.getString("Base Note"));

                        Double price = doc.getDouble("Price");
                        if (price != null) perfume.setPrice(price.floatValue());

                        perfume.setDescription(doc.getString("Description"));
                        perfume.setDesigners(doc.getString("Perfumer"));
                        perfume.setOlfactory(doc.getString("Olfactory Family"));
                        perfume.setPrices(doc.getString("Prices"));
                        perfume.setVolumes(doc.getString("Volumes"));

                        perfumeList.add(perfume);
                    }

                    new Thread(() -> {
                        appDatabase.perfumeDao().deleteAll();
                        appDatabase.perfumeDao().insertList(perfumeList);
                        setFirstLaunchFalse();
                    }).start();

                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE_FAIL", "Lỗi khi tải dữ liệu từ Firestore", e);
                    runOnUiThread(() -> Toast.makeText(SplashActivity.this,
                            "Không thể tải dữ liệu từ Firestore!", Toast.LENGTH_SHORT).show());
                });
    }

    private void loadNotesFromFirestore() {
        FirebaseFirestore.getInstance().collection("Group_Note")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<NoteEntity> noteList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        noteList.add(new NoteEntity(
                                doc.getString("category"),
                                doc.getString("notes"),
                                doc.getString("imageUrl"),
                                doc.getString("description")
                        ));
                    }
                    new Thread(() -> appDatabase.noteDao().insertList(noteList)).start();
                })
                .addOnFailureListener(e -> Log.e("FIRESTORE_FAIL", "Lỗi khi tải dữ liệu từ Firestore", e));
    }

    private void loadBrandsFromFirestore() {
        FirebaseFirestore.getInstance().collection("Brands")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<BrandEntity> brandList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        BrandEntity brand = new BrandEntity();
                        brand.setId(doc.getId());
                        brand.setName(doc.getString("name"));
                        brand.setBanner(doc.getString("banner"));
                        brand.setLogo(doc.getString("logo"));
                        brand.setPerfumes(doc.getString("perfumes"));
                        brand.setFounded(doc.getString("founded"));
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
                    new Thread(() -> appDatabase.BrandDao().insertBrandList(brandList)).start();
                })
                .addOnFailureListener(e -> Log.e("BrandRepo", "❌ Lỗi khi lấy dữ liệu từ Firestore", e));
    }
}
