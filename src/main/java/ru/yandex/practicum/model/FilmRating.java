package ru.yandex.practicum.model;

public class FilmRating {
    private Long ratingId;
    private String ratingName;

    public FilmRating(Long ratingId, String ratingName) {
        this.ratingId = ratingId;
        this.ratingName = ratingName;
    }

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public String getRatingName() {
        return ratingName;
    }

    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }
}

