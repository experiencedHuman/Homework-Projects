package pgdp.adventuin;

public enum Language {
    ENGLISH, GERMAN;

    public String getLocalizedChristmasGreeting(String greeterName) {
        if (this == ENGLISH)
            return ""+greeterName+"wishes you a Merry Christmas!";
        else if (this == GERMAN)
            return "Fröhliche Weihnachten wünscht dir"+greeterName+"!";

        throw new RuntimeException("undefined language");
    }
}
