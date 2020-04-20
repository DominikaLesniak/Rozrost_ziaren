package sample.options;

import sample.neighbourhood.*;

import java.util.Arrays;
import java.util.List;

public class Neighbourhoods {
    private final static List<String> NEIGHBOURHOODS = Arrays.asList("Von Neumanna", "Moore'a", "Pentagonalne losowe", "Heksagonalne", "Z promieniem");

    public static List<String> getNeighbourhoods() {
        return NEIGHBOURHOODS;
    }

    public static Neighbourhood getNeighbourhoodForName(String name) {
        switch (name) {
            case "Von Neumanna":
                return new VonNeumannNeighbourhood();
            case "Moore'a":
                return new MooreNeighbourhood();
            case "Pentagonalne losowe":
                return new PentagonalRandomNeighbourhood();
            case "Heksagonalne":
                return new HexagonalNeighbourhood();
            default:
                return null;
        }
    }
}
