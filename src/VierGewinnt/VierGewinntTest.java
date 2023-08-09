package VierGewinnt;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VierGewinntTest {

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

    List<Integer> moves1 = new ArrayList<>(List.of(3, 10, 4, 2, 5, 6, 12, 17, 19, 26, 24, 9, 16, 0, 23, 30, 31, 7, 14, 33, 21, 11, 18));

    @Test
    void bestMove() {
        System.out.println("Test bestMove()");
        assertEquals(5,VierGewinnt.of(moves1).bestMove().column);
    }

    @Test
    void monteCarlo_score() {
        System.out.println("Test monteCarlo_score()");
        assertEquals(20,VierGewinnt.of(moves1).play(Move.of(5)).monteCarlo_score());
    }

    @Test
    void nextMoveScore(){
        System.out.println("Test nextMoveScore()");
        assertEquals(20,VierGewinnt.of(moves1).nextMoveScore()[4]);
    }

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

    List<Integer> moves2 = List.of(2, 3, 9, 16, 10, 23, 17, 24, 1, 8, 0, 15, 22, 30, 37, 7, 31, 6, 4, 11, 13, 20, 27, 18);
    @Test
    void cango() {
        System.out.println("Test cango()");
        int column = 3;
        assertFalse(VierGewinnt.of(moves2).cango(column-1));
    }
    /*
    Spalte 1[1, 2]
    Spalte 2[1, 2, 2, 1]
    Spalte 3[1, 1, 2, 2, 2, 1]
    Spalte 4[2, 1, 1, 2, 1]
    Spalte 5[1, 2, 2]
    Spalte 6[]
    Spalte 7[2, 1, 2, 1]*/
    @Test
    void getGrid() {

        System.out.println("Test getGrid()");
        assertEquals(List.of(List.of(1,2),
                             List.of(1,2,2,1),
                             List.of(1,1,2,2,2,1),
                             List.of(2,1,1,2,1),
                             List.of(1,2,2),
                             List.of(),
                             List.of(2,1,2,1)),
                     VierGewinnt.of(moves2).getGrid());
    }

}