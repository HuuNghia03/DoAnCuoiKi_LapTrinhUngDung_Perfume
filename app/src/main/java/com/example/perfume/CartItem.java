package com.example.perfume;
import com.example.perfume.PerfumeEntity;
public class CartItem {
    private PerfumeEntity perfume;
    private int quantity;

    public CartItem(PerfumeEntity perfume, int quantity) {
        this.perfume = perfume;
        this.quantity = quantity;
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
}
