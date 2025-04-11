package com.example.perfume;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "perfumes")
public class PerfumeEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String pid;
    private String brand;
    private String name;
    private String parent;
    private String img;
    private String imgs;
    private String sex;
    private String origin;
    private Integer year;
    private String type;
    private String available;
    private String limited;
    private String collector;
    private Float rating;
    private Integer ratingVotes;
    private Float longevity;
    private Float sillage;
    private String video;
    private String brandUrl;
    private String brandImg;
    private String perfumers;
    private String designers;
    private String accords;
    private String top;
    private String heart;
    private String base;

    public PerfumeEntity() {}

    // 🔧 Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public String getImgs() { return imgs; }
    public void setImgs(String imgs) { this.imgs = imgs; }

    public String getGender() { return sex; }
    public void setGender(String sex) { this.sex = sex; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAvailable() { return available; }
    public void setAvailable(String available) { this.available = available; }

    public String getLimited() { return limited; }
    public void setLimited(String limited) { this.limited = limited; }

    public String getCollector() { return collector; }
    public void setCollector(String collector) { this.collector = collector; }

    public Float getRating() { return rating; }
    public void setRating(Float rating) { this.rating = rating; }

    public Integer getRatingVotes() { return ratingVotes; }
    public void setRatingVotes(Integer ratingVotes) { this.ratingVotes = ratingVotes; }

    public Float getLongevity() { return longevity; }
    public void setLongevity(Float longevity) { this.longevity = longevity; }

    public Float getSillage() { return sillage; }
    public void setSillage(Float sillage) { this.sillage = sillage; }

    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }

    public String getBrandUrl() { return brandUrl; }
    public void setBrandUrl(String brandUrl) { this.brandUrl = brandUrl; }

    public String getBrandImg() { return brandImg; }
    public void setBrandImg(String brandImg) { this.brandImg = brandImg; }

    public String getPerfumers() { return perfumers; }
    public void setPerfumers(String perfumers) { this.perfumers = perfumers; }

    public String getDesigners() { return designers; }
    public void setDesigners(String designers) { this.designers = designers; }

    public String getAccords() { return accords; }
    public void setAccords(String accords) { this.accords = accords; }

    public String getTop() { return top; }
    public void setTop(String top) { this.top = top; }

    public String getHeart() { return heart; }
    public void setHeart(String heart) { this.heart = heart; }

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
}
