package VierGewinnt;

import java.util.Arrays;

public class MonteCarlo {
    public static int[] evaluiert(VierGewinntGame game) {
        int exp = 100;
        int[] res = new int[3];
        VierGewinntGame copygame = VierGewinnt.of(game.getHistory());
        while (exp-- > 0) {
            //System.out.println("Exp="+exp);
            assert !copygame.gameOver() : "Not change to start state";
            int c = 0;
            while(!copygame.gameOver()) {
                copygame = copygame.play(copygame.randomMove());
                c++;
            }
            //System.out.println("Im after gameover "+copygame);
            if(copygame.tied()) {
                //System.out.println("Im in tied "+copygame);
                res[1]++;
                //System.out.println("tie"+copygame.getHistory());
            }
            else {
                int player = (copygame.getHistory().size() % 2 == 0) ? 2:1;
                if(player == 1) res[0]++;
                else res[2]++;
            }
            //System.out.println(exp+"\n"+copygame);
            while (c-- > 0) copygame = copygame.undo();
            //System.out.println("Im after undo "+copygame);
            //System.out.println("End of exp "+exp);
            //if(exp == 500 || exp == 700)System.out.println("Check middle"+copygame.getHistory());
        }
        return res;
    }

    public static void main(String[] args) {

        int test = 3;
        VierGewinntGame game = VierGewinnt.newGame();
        Move[] moves = {Move.of(4), Move.of(3), Move.of(4)};
        game = game.play(moves);

        System.out.println("Check + " +game);
        while (test-- > 0) {
            System.out.println("Im doing test "+test);
            int[] r = evaluiert(game);
            System.out.println(Arrays.toString(r));
        }

    }
}
