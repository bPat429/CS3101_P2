package main;

public class League {
    private String name;
    private int year;

    public League (String name, int year) {
        this.name = name;
        this.year = year;
    }

    public String toString(){//overriding the toString() method
        return name + ", " + year;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }
}
