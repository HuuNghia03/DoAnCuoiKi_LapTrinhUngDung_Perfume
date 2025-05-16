package com.example.perfume.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfume.R;
import com.example.perfume.adapters.NewsAdapter;

public class DiscoverFragment extends Fragment {
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private com.example.perfume.NewsDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setHasFixedSize(true);

        // Initialize DB helper
        dbHelper = new com.example.perfume.NewsDatabaseHelper(getContext());

        // If the database is empty, insert sample data
        if (dbHelper.getAllNews().isEmpty()) {
            insertSampleData();
        }

        // Set adapter to RecyclerView
        newsAdapter = new NewsAdapter(dbHelper.getAllNews());

        newsRecyclerView.setAdapter(newsAdapter);

        return view;
    }
    private void insertSampleData() {
        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "A Crystalized Sensation of Luxury",
                "Baccarat Rouge 540 is a fragrance that redefines luxury with its radiant and complex character. Opening with saffron and jasmine, it quickly draws you into a warm heart of amberwood and ambergris. The base is both earthy and sweet, with fir resin and cedar creating a long-lasting trail that is as seductive as it is elegant. It’s a scent that lives on skin like a second identity.\n" +
                        "\n" +
                        "The iconic red and gold bottle captures its precious, jewel-like nature. Loved by perfume connoisseurs worldwide, Baccarat Rouge 540 is not for the faint of heart. It’s a fragrance for those who embrace boldness and aren’t afraid to be unforgettable.",
                "July 17, 2024",
                R.drawable.maison_francis,
                R.drawable.maison_francis_logo
        ));
        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "A Symphony of Water and Purity: The Essence of Nature in a Bottle",
                "Issey Miyake L’Eau d’Issey is more than just a fragrance – it’s a poetic interpretation of water, purity, and harmony with nature. Inspired by the idea of a drop of water on a woman’s skin, this perfume captures the essence of freshness and serenity in the most elegant way. The composition opens with airy top notes of lotus, freesia, and cyclamen, blended perfectly with the crisp brightness of melon. This transitions into a heart of delicate peony, lily, and carnation, adding a floral depth that remains soft yet unforgettable. The base of cedar, sandalwood, musk, and amber gives the fragrance a subtle earthiness that grounds it beautifully.\n" +
                        "\n" +
                        "What makes L’Eau d’Issey a timeless masterpiece is its minimalist yet sophisticated design – from the sleek glass bottle capped with a silver cone to the clear, aquatic scent that never overwhelms. Ideal for women who embrace simplicity, grace, and inner peace, this fragrance is perfect for daily wear and effortlessly enhances your natural aura. It's a scent that doesn’t try too hard, but leaves a quiet, unforgettable impression.",
                "April 7, 2025",
                R.drawable.leau_dissey, // bạn cần tạo hình này trong drawable
                R.drawable.leau_dissey_logo // và cả logo nếu có
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "Mystic Bliss, the new addition to the native collection",
                "Drawing from the serene beauty of Flinders Island and the benefits of Kunzea, Mystic Bliss unveils a delicately powdered and mysterious fragrance. An invitation to travel to an island beyond the horizon, illuminated by the Aurora Australis.\n\n" +
                        "Each ingredient of our Mystic Bliss has been meticulously chosen by Dimitri Weber, and expertly blended by Florian Gallo.Among these notes is Immortelle, or Everlasting Flower, which lends its faceted and gentle scent to the heart of the fragrance.",
                "09/04/2025",
                R.drawable.mysticbliss, // bạn cần tạo hình này trong drawable
                R.drawable.img_3 // và cả logo nếu có
        ));
        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "Essential Pafums: a new packaging,a new scent",
                "Vibrant and spicy,this new fragrance by Anne Flipo for Essential Parfums reinvents orange blossom. Avibrant marriage of turmeric, ginger, pink and black peppers, enhanced by neroli and jasmine sambac. Woody notes of vetiver and sandalwood, certified \"For Life,\" complete this unique creation.\n\n" +
                        "Discover the new bottle design of Essential Parfums through Néroli Botanica.",
                "2 WEEKS AGO",
                R.drawable.essentailparfums,
                R.drawable.essentail_logo
        ));
        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "Extrait Blanc: Purity begins ELISIRE with a white sheet",
                "Extrait Blanc,the latest creation from Elisire, is an olfactory symphony where the milky and addictive tones of Sandalwood contrast with the liveliness of Ginger and Pink Pepper, while floral notes ofTuberose and Orange Blossom add an enchanting sweetness. This fragrance embodies the purity and brilliance of bright white.",
                "9 MONTHS AGO",
                R.drawable.extraitblanc,
                R.drawable.elisire_logo
        ));
        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "Bleu de Chanel - Eau de Parfum",
                "Bleu de Chanel is a symbol of modern masculinity. It opens with a fresh burst of bergamot, followed by warm notes of sandalwood and amber. The scent evokes confidence, strength, and elegance, making it perfect for men who want to assert their style and charisma. Whether for everyday use or special occasions, Bleu de Chanel is a timeless choice that never goes unnoticed.",
                "09/02/2025",
                R.drawable.chanel_bleu_de_chanel_parfum,
                R.drawable.eau_de_parfum
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "Wild Elegance in a Bottle: Discover the Charm of Dior Sauvage",
                "Dior Sauvage Eau de Parfum is not just a fragrance — it’s a bold statement of freedom and strength. Crafted for the modern man who dares to be different, Sauvage opens with fresh notes of Calabrian bergamot that instantly awaken the senses. As the scent develops, it reveals spicy heart notes of Sichuan pepper and lavender, then settles into a deep, smoky base of ambroxan and vanilla.\n" +
                        "\n" +
                        "The sleek, midnight-blue bottle with its magnetic cap reflects the mysterious elegance that defines Sauvage. Whether worn during daytime adventures or evening outings, this fragrance adapts effortlessly to any moment, leaving a captivating trail behind.\n" +
                        "\n" +
                        "Long-lasting and powerful, Dior Sauvage is a favorite among those who seek a rugged yet refined identity. It’s more than just a scent — it’s a companion to your charisma.\n" +
                        "\n" +
                        "Ready to make an impression? Dior Sauvage is waiting.",
                "April 9, 2025",
                R.drawable.dior_sauvage,
                R.drawable.sau_sauvage_logo
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "A Bold and Feminine Signature Scent",
                "Chanel Coco Mademoiselle is the perfect blend of sophistication and sensuality. Bursting with fresh orange and bergamot, it transitions into romantic jasmine and rose, then finishes with vetiver and patchouli. It’s elegant yet daring — ideal for the confident, independent woman.",
                "April 9, 2025",
                R.drawable.chanel_coco,
                R.drawable.chanel_logo
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "The Night Has a New Scent",
                "YSL’s La Nuit de L’Homme is crafted for the mysterious man of the night. With cardamom at the top, lavender in the heart, and cedar in the base, it’s spicy, warm, and deeply seductive. This fragrance is your perfect evening companion.",
                "April 9, 2025",
                R.drawable.ysl_la_nuit,
                R.drawable.ysl_logo
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "A Bouquet of Sunshine in a Bottle",
                "Daisy by Marc Jacobs is a playful, fresh fragrance full of joy. Opening with wild berries and violet leaves, it blossoms into white floral notes and finishes with a soft musk. Light, youthful, and radiant — like spring in every spray.",
                "April 9, 2025",
                R.drawable.daisy_marc,
                R.drawable.daisy_logo
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "Power of the Ocean, Elegance of the Earth",
                "Acqua di Giò Profumo is a tribute to nature's power — blending the freshness of sea spray with the warmth of volcanic rocks. The fragrance begins with a crisp, aquatic burst of bergamot and marine notes, evoking the vastness of the ocean. As it dries down, deep tones of incense and patchouli bring intensity and earthiness, grounding the airy opening with a sophisticated finish. This contrast of light and dark makes it both refreshing and mysterious.\n" +
                        "\n" +
                        "Designed for the modern man who is in tune with both strength and stillness, Acqua di Giò Profumo is timeless and versatile. Whether worn in the boardroom or at a seaside escape, it exudes confidence and refinement. The black, gradient glass bottle symbolizes its duality — a fragrance that is both free and grounded.",
                "March 20, 2025",
                R.drawable.acqua_di,
                R.drawable.acqua_di_logo
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "The Scent of Ambition",
                "Lancôme Idôle is a fragrance crafted for women who lead with purpose. It opens with a radiant burst of rose and citrus, immediately creating an aura of freshness and determination. At the heart lies pure jasmine, adding elegance and sensuality, while a clean base of white musk gives the scent a modern, skin-like finish. It's a bold floral composition that feels both airy and empowering.\n" +
                        "\n" +
                        "Sleek in design and light in texture, Idôle is made for women who aren’t afraid to dream big and stand tall. The ultra-thin bottle reflects minimalism and ambition — it fits perfectly into your hand, your bag, and your lifestyle. It’s more than just a fragrance; it’s a declaration of self-worth and forward motion.",
                "March 10, 2024",
                R.drawable.lancome_idole,
                R.drawable.lancome_idole_logo
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "Dark, Luxurious, and Unforgettable",
                "Black Orchid by Tom Ford is the epitome of opulence and mystery. It opens with a rich blend of black truffle and plum, leading into a lush heart of orchid, spices, and lotus wood. The scent is bold, dramatic, and utterly captivating. Its dark, velvety character makes it stand out from the crowd — perfect for evenings when you want to leave an impression that lingers.\n" +
                        "\n" +
                        "Packaged in a sleek, black bottle with gold accents, Black Orchid is more than a fragrance — it’s a statement. Whether worn by men or women, it commands attention and exudes confidence. Ideal for formal events, romantic nights, or when you simply want to embrace your inner power.",
                "March 2, 2024",
                R.drawable.tom_ford_black,
                R.drawable.tom_ford_logo
        ));

        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                "Escape to the Coast",
                "Jo Malone’s Wood Sage & Sea Salt offers a refreshing departure from traditional perfumes. It opens with airy sea salt and ambrette seed, evoking the wind-swept British coast. Earthy sage and driftwood notes follow, grounding the fragrance with a soft, mineral warmth. The result is clean, natural, and quietly evocative — like a peaceful walk along the shoreline.\n" +
                        "\n" +
                        "Unisex and minimalist in both scent and presentation, this fragrance is ideal for those who seek simplicity and serenity. Perfect for daily wear or as a calming signature scent, Wood Sage & Sea Salt connects you to nature, freedom, and fresh air — wherever you are.",
                " June 1, 2025",
                R.drawable.jo_malone,
                R.drawable.jo_malone_logo
        ));


        dbHelper.insertNewsItem(new com.example.perfume.NewsItem(
                " One Fragrance. All Genders. All Time.",
                "CK One by Calvin Klein broke all the rules when it was released — a fragrance made to be shared by everyone. It begins with bright notes of pineapple, papaya, and citrus, giving it a clean and inviting start. The heart introduces a gentle blend of green tea, violet, and nutmeg, while the base features musk and amber for a warm, intimate finish. The overall effect is fresh, balanced, and comfortably familiar.\n" +
                        "\n" +
                        "With its clear bottle and minimalist label, CK One is a cultural icon of the '90s — and it still feels just as relevant today. It's easygoing, universal, and perfect for everyday wear. Whether you're heading to class, work, or just living in the moment, CK One fits right in.",
                " August 10, 2024",
                R.drawable.ck_one,
                R.drawable.ck_one_logo
        ));

    }
}
