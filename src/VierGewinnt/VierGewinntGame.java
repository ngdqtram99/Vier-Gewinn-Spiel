package VierGewinnt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface VierGewinntGame {

    static final int COLUMNS = 7;
    static final int ROWS = 6;

    VierGewinntGame play(Move... c);
    Move bestMove();
    Move randomMove();
    List<Integer> getHistory();
    VierGewinntGame undo();
    boolean cango(int column);

    /**
     * Rückgabe alle mögliche Gewinnfälle an der letzten Position in der history-Liste unten mehreren Sets.
     * @param lastPos letzten Position
     * @return Set der Sets der Gewinnfälle
     */
    default Set<Set<Integer>> connect4(int lastPos) {
        IntPredicate pos_value = p -> p >= 0 && p <= 41; //Bedingung der Position-Werte des Gitters
        final Set<Set<Integer>> columns = Set.of(Set.of(0,1,2,3),Set.of(1,2,3,4),Set.of(2,3,4,5),Set.of(3,4,5,6),
                Set.of(0),Set.of(1),Set.of(2),Set.of(3),Set.of(4),Set.of(5),Set.of(6));

        Set<Set<Integer>> connect = new HashSet<>();

        //Diagonal 4 Gewinnt:
        //erzeugt Set diagonales Gewinns (Richtung von links nach rechts)
        IntFunction<Set<Integer>> dia_lr = n -> {
            return IntStream.iterate(n, m -> m + 8).limit(4).
                    filter(pos_value).boxed().collect(Collectors.toSet());
        };
        IntStream.iterate(lastPos,i -> i-8).limit(4).filter(pos_value).
                forEach(n -> connect.add(dia_lr.apply(n)));

        //erzeugt Set diagonales Gewinns (Richtung von rechts nach links)
        IntFunction<Set<Integer>> dia_rl = n -> {
            return IntStream.iterate(n, m -> m+6).limit(4).filter(pos_value).boxed().collect(Collectors.toSet());
        };
        IntStream.iterate(lastPos,i -> i-6).limit(4).filter(pos_value).
                forEach(n -> connect.add(dia_rl.apply(n)));

        //Vertikal 4 Gewinnt:
        //erzeugt Set des vertikalen Gewinns (immer nur ein)
        Set<Integer> verConnect = IntStream.iterate(lastPos, i -> i-7).limit(4).filter(pos_value).
                boxed().collect(Collectors.toSet());
        connect.add(verConnect);

        //Horizontal 4 Gewinnt
        //erzeugt Set des horizontalen Gewinns
        IntPredicate hor_filter =  f -> f/7 == lastPos/7;
        IntFunction<Set<Integer>> hor = n -> {
            return IntStream.iterate(n, m -> m+1).limit(4).filter(hor_filter).
                    boxed().collect(Collectors.toSet());
        };
        IntStream.iterate(lastPos,i -> i-1).limit(4).filter(hor_filter).
                forEach(n -> connect.add(hor.apply(n)));

        //Filter: 4 Gewinnt
        Predicate<Set<Integer>> dia4_filter = s -> {
            Set<Integer> tem_s = s.stream().mapToInt(m -> m/7).boxed().collect(Collectors.toSet());
            return columns.contains(tem_s) && s.size() == 4;
        };
        //Set von aller möglichen 4-Gewinnt-Fällen an der letzten Position (lastPos) (Rückgabe)
        return connect.stream().filter(dia4_filter).collect(Collectors.toSet());
    }

    /**
     * Rückgabe eines Sets der Gewinnpositionen eines Spielers
     * @return Set der Gewinnpositionen
     */
    default Set<Integer> pwin() {
        Set<Integer> p_connect4 = new HashSet<>(0);
        if (!getHistory().isEmpty()) {
            int lastPos = getHistory().get(getHistory().size() - 1);
            int whose_lastPos = (getHistory().size() % 2 == 0) ? 1 : 0;
            Set<Integer> player_positions = IntStream.range(0, getHistory().size()).
                    filter(i -> i % 2 == whose_lastPos).
                    mapToObj(getHistory()::get).collect(Collectors.toSet());

            Set<Set<Integer>> connect4 = connect4(lastPos);

            Set<Set<Integer>> tem_pconnect4 = connect4.stream().
                    filter(s -> s.stream().filter(player_positions::contains).count() == 4). //filtert passende connect4
                    collect(Collectors.toSet());
            if(!tem_pconnect4.isEmpty()) tem_pconnect4.forEach(p_connect4::addAll);
        }
        return p_connect4;
    };
    default boolean win() {return pwin().size() >= 4;}
    default boolean tied() {return getHistory().size() == 42 && !win();}
    default boolean gameOver() {return win() || tied();}
}
