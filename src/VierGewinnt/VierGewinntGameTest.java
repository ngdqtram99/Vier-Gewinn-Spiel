package VierGewinnt;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VierGewinntGameTest {
    /*
    Spalte 1[1, 2]
    Spalte 2[1, 2, 2, 1]
    Spalte 3[1, 1, 2, 2, 2, 1]
    Spalte 4[2, 1, 1, 2, 1]
    Spalte 5[1, 2, 2, 1]
    Spalte 6[]
    Spalte 7[2, 1, 2, 1]
    history = [2, 3, 9, 16, 10, 23, 17, 24, 1, 8, 0, 15, 22, 30, 37, 7, 31, 6, 4, 11, 13, 20, 27, 18, 25]
     */
    List<Integer> moves1 = List.of(2, 3, 9, 16, 10, 23, 17, 24, 1, 8, 0, 15, 22, 30, 37, 7, 31, 6, 4, 11, 13, 20, 27, 18, 25);
    VierGewinntGame g1 = VierGewinnt.of(moves1);

    /*
    VierGewinnt:
    Spalte 1[2, 2, 1, 1]
    Spalte 2[]
    Spalte 3[2, 2, 1, 1, 2]
    Spalte 4[1, 2, 2, 1, 1]
    Spalte 5[1, 2, 1]
    Spalte 6[1, 1, 1, 2, 2]
    Spalte 7[2]
    history = [3, 10, 4, 2, 5, 6, 12, 17, 19, 26, 24, 9, 16, 0, 23, 30, 31, 7, 14, 33, 21, 11, 18]
     */
    List<Integer> moves2 = List.of(2, 4, 11, 3, 5, 10, 17, 18, 24, 25, 31, 38, 12, 32, 39, 19, 9, 6, 16, 23, 26, 0, 30, 13, 20, 7, 14, 33, 37, 40, 27, 34, 41, 1, 8, 15, 22, 21, 28, 29, 35, 36);
    VierGewinntGame g2 = VierGewinnt.of(moves2);
    @Test
    void connect4() {
        System.out.println("Test connect4()");
        int lastPos = g1.getHistory().get(g1.getHistory().size()-1);
        assertEquals(Set.of(Set.of(1,9,17,25),Set.of(9,17,25,33),Set.of(17,25,33,41), //diagonal rechts nach links
                        Set.of(25,18,11,4), //vertical
                        Set.of(13,19,25,31),Set.of(19,25,31,37), //diagonal links nach rechts
                        Set.of(22,23,24,25),Set.of(23,24,25,26), Set.of(24,25,26,27)), //horizontal
                g1.connect4(lastPos),"Set von 4 Gewinnt an einer Position ist fehlerhaft");
    }

    @Test
    void pwin() {
        System.out.println("Test pwin()");
        assertEquals(Set.of(1,9,17,25),g1.pwin());
    }

    @Test
    void win() {
        System.out.println("Test win()");
        assertTrue(g1.win());
    }

    @Test
    void tied() {
        System.out.println("Test tied()");
        assertTrue(g2.tied());
    }

    @Test
    void getHistory() {
        System.out.println("Test getHistory()");
        assertEquals(moves1,g1.getHistory());
    }
}