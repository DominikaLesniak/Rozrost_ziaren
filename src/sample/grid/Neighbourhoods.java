package sample.grid;

import sample.neighbourhood.MooreNeighbourhood;
import sample.neighbourhood.Neighbourhood;
import sample.neighbourhood.VonNeumannNeighbourhood;

import java.util.Arrays;
import java.util.List;

public class Neighbourhoods {
    private final static List<String> NEIGHBOURHOODS = Arrays.asList("Von Neumanna", "Moore'a", "Heksagonalne", "Oktagonalne");

    public static List<String> getNeighbourhoods() {
        return NEIGHBOURHOODS;
    }

    public static Neighbourhood getNeighbourhoodForName(String name) {
        switch (name) {
            case "Von Neumanna":
                return new VonNeumannNeighbourhood();
            case "Moore'a":
                return new MooreNeighbourhood();
            case "Heksagonalne":
                return null;
            case "Oktagonalne":
                return null;
            default:
                return null;
        }
    }
}
