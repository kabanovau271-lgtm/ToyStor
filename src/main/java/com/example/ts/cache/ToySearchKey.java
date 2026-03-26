package com.example.ts.cache;

import java.util.Objects;

public class ToySearchKey {

  private final String category;
  private final Double minPrice;
  private final int page;
  private final int size;

  public ToySearchKey(String category, Double minPrice, int page, int size) {
    this.category = category;
    this.minPrice = minPrice;
    this.page = page;
    this.size = size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ToySearchKey)) return false;
    ToySearchKey that = (ToySearchKey) o;
    return page == that.page &&
        size == that.size &&
        Objects.equals(category, that.category) &&
        Objects.equals(minPrice, that.minPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, minPrice, page, size);
  }
}