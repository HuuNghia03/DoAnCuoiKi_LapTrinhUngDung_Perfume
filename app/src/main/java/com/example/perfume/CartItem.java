package com.example.perfume;

import com.example.perfume.PerfumeEntity;

public class CartItem {
    private PerfumeEntity perfume;
    private int quantity;
    private int volume;
   private float pricePerVolume;

    public CartItem(PerfumeEntity perfume, int quantity, int volume, float pricePerVolume) {
        this.perfume = perfume;
        this.quantity = quantity;
        this.volume = volume;
        this.pricePerVolume = pricePerVolume;
    }

    public PerfumeEntity getPerfume() {
        return perfume;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public float getPricePerVolume() {
        return pricePerVolume;
    }

    public void setPerfume(PerfumeEntity perfume) {
        this.perfume = perfume;
    }

    public void setPricePerVolume(float pricePerVolume) {
        this.pricePerVolume = pricePerVolume;
    }

    public float getTotalPrice() {
        return pricePerVolume * quantity;
    }
}
