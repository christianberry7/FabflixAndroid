package edu.uci.ics.fabflixmobile;

public class Movie {
    private final String name;
    private final short year;
    private final String id;
    private final String director;
    private final String actors;
    private final String genres;

    public Movie(String name, short year, String id, String director, String actors, String genres) {
        this.name = name;
        this.year = year;
        this.id = id;
        this.director = director;
        this.actors = actors;
        this.genres = genres;

    }

    public String getName() {
        return name;
    }

    public short getYear() {
        return year;
    }

    public String getId() {
        return id;
    }

    public String getDirector() {
        return director;
    }

    public String getStars() {
        return actors;
    } // parsing

    public String getGenres() {
        return genres;
    } // parsing
}