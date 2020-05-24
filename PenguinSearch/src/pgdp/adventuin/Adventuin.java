package pgdp.adventuin;

import pgdp.color.RgbColor;

public final class Adventuin {
    final private String name;
    final private int height;
    final private RgbColor color;
    final private HatType hatType;
    final private Language language;

    public Adventuin(String name, int height, RgbColor color, HatType hatType, Language language) {
//        assert height > 0 && color != null;
        this.name = name;
        this.height = height;
        this.color = color;
        this.hatType = hatType;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public RgbColor getColor() {
        return color;
    }

    public HatType getHatType() {
        return hatType;
    }

    public Language getLanguage() {
        return language;
    }


    //for testing purposes
    @Override
    public String toString() {
        return name + ", "+height+" tall, with a "+hatType;
    }
}
