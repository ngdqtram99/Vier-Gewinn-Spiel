package VierGewinnt;

import java.util.*;
import java.util.stream.IntStream;

public class Versuch {

    public static int negamax (int node, int depth, int player) {
        if(depth == 0) {
            System.out.println("I'm in the deepest, at node: "+node);
            int last = new Random().nextInt(1,7);
            System.out.println("I am the last, my value is "+last);
            return last;
        }
        int value = -Integer.MAX_VALUE;
        int self = new Random().nextInt(1,7) * player;
        System.out.println("Node's value is "+self);
        System.out.println("I am in depth"+ depth+ ",at node "+node);
        for (int c=0; c <2;c++) {
            value = Math.max(value,-negamax(c,depth-1,-player));
        }
        value = value+self;
        System.out.println("Max of next depth is"+value);
        return value;
    }

    public static void main(String[] args) {

        /*VierGewinntGame game = VierGewinnt.newGame();
        System.out.println(game);
        int state = 1;
        int count = 1;
        while (count-- >= 0) {
            game = game.play(game.bestMove());
            System.out.println("End a move"+game);
        }*/
        /*while(!game.gameOver()) {
            switch (state) {
                case 1: game = game.play(game.bestMove());

                case -1: game = game.play(game.randomMove());
            }
            System.out.println(game);
            state *=-1;
            //player *= -1;
            //Player2 win
        }*/


        List<Integer> moves = List.of(2, 3, 9, 16, 10, 23, 17, 24, 1, 8, 0, 15, 22, 30, 37, 7, 31, 6, 4, 11, 13, 20, 27, 18, 25);
        List<Integer> move2 = new ArrayList<>(List.of(3, 10, 4, 2, 5, 6, 12, 17, 19, 26, 24, 9, 16, 0, 23, 30, 31, 7, 14, 33, 21, 11, 18));
        List<Integer> m = new ArrayList<>(List.of(0,7,14,21,28,35,42));
        List<Integer> n = new ArrayList<>(List.of(3, 4, 11, 10, 17, 18, 25, 24, 31, 32, 38, 39, 2, 9, 1, 0, 8, 15, 16, 22, 23, 29, 36, 30, 7, 5));
        VierGewinntGame g = VierGewinnt.of(n);

        /*System.out.println(g);
        g = g.play(g.bestMove());
        System.out.println(g);
        */

        VierGewinntGame g1 = VierGewinnt.newGame();
        int lastpos = g1.getHistory().get(g1.getHistory().size()-1);
        int y = g1.getHistory().isEmpty()? 5: 5-lastpos/7;
        System.out.println("y = "+ y);
    }
}
