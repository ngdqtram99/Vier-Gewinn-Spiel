package VierGewinnt;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

class Move {
    int column;

    private Move(int column) {
        assert column >=1 && column <= VierGewinntGame.COLUMNS : "Die Spalte passt nicht.";
        this.column = column;
    }

    public static Move of(int column){ return new Move(column); }

    @Override
    public String toString() {
        return "Die Spalte "+column;
    }
}

class VierGewinnt implements VierGewinntGame {

    /* List<Integer>.size entspricht 6 Zeilen des Gitters. List<List<Integer> size entspricht 7 Spalten.
      0     0   7   14  21  28  35
      1     1   8   15  22  29  36
      2     2   9   16  23  30  37
      3     3   10  17  24  31  38
      4     4   11  18  25  32  39
      5     5   12  19  26  33  40
      6     6   13  20  27  34  41
      c/r   0   1   2   3   4   5
      Position wird in der history_Liste speichert:
      pos = c + 7*r
     */
    List<List<Integer>> grid;
    List<Integer> history;
    Logger logger = Logger.getLogger(VierGewinnt.class.getSimpleName());

    private VierGewinnt(List<Integer> history) {
        this.history = history;
        assert history.size() <= COLUMNS*ROWS : "Es gibt mehr Steine für das Gitter als erlaubt";
        assert new HashSet<>(history).size() == history.size() : "Es gibt 2 einander überlappenden Steine.";
        assert history.stream().noneMatch(n -> n < 0 || n > 41) : "history-Liste enthält unpassende Position (pos <0 or pos > 41)";
        this.grid = getGrid();
    }

    public static VierGewinnt of(List<Integer> history) {
        return new VierGewinnt(history);
    }

    /**
     * beginnt das Spiel, initialisiert das Gitter
     * @return das Spiel am Beginn
     */
    public static VierGewinnt newGame() {
        return new VierGewinnt(new ArrayList<>());
    }

    /**
     * spielt mit der gewählten Spalte
     * @param column gewählte Spalte
     * @return Spiel nach dem Spielzug
     */
    public VierGewinnt play(Move column) {
        int col = column.column-1; //Column turns to trivial number
        //System.out.println("col"+col);
        assert !gameOver() : "Das Spiel hat beendet.";
        VierGewinnt v = VierGewinnt.of(history);

        int player = v.history.size() % 2 == 0 ? 1 : 2;
        int row = v.grid.get(col).size();
        assert row<=ROWS : "Die Reihe ist ungültig.";
        v.grid.get(col).add(player);
        //System.out.println("row"+row);
        int pos = col + 7*row;
        assert pos >= 0 && pos <= 41 : pos+" ist unpassende Position";
        v.history.add(pos);
        //logger.info("Der Spieler "+player+" zieht einen Stein in die "+column+". Spalte.");
        return v;
    }

    /**
     * spielt mehrere Züge
     * @param columns Die gewählten Spalten
     * @return Spiel nach den Spielzügen
     */
    public VierGewinnt play(Move... columns) {
        VierGewinnt g = this;

        for (Move move : columns) {
            g = g.play(move);
        }
        return g;
    }

    /**
     * gibt die beste Spalte zurück
     * @return die beste Spalte
     */
    public Move bestMove() {
        logger.setLevel(Level.OFF); //Ein-/Abschalten für Logger-Protokoll
        assert !gameOver() : "Das Spiel hat beendet.";
        int[] nextMove = nextMoveScore();
        assert Arrays.stream(nextMove).max().isPresent() : "Kein Maximum wird gefunden.";
        int max_score = Arrays.stream(nextMove).max().getAsInt();
        //Liste der Besten
        List<Integer> same_max_col = IntStream.range(0, nextMove.length).
                filter(n -> nextMove[n] == max_score).boxed().toList(); // Mehr als eine Spalte erreicht das Maximum

        int r = new Random().nextInt(same_max_col.size());
        int best_col = same_max_col.get(r);
        assert cango(best_col) : "Can't go best move";
        logger.info("Der Spieler hat den besten Zug in der Spalte "+(best_col+1)+" gewählt.");
        return Move.of(best_col+1);
    }

    /**
     * Rückgabe der Score-Liste aller Spalten. Jeder Index entspricht vom links nach rechts die Spalten 1 bis 7.
     * Die Liste hilft, den besten Zug (mit Maximum) zu wählen.
     * @return Score-Liste jeder Spalte
     */
    public int[] nextMoveScore() {
        int[] nextMove = new int[COLUMNS];
        for (int i = 0; i < COLUMNS; i++) {
            nextMove[i] = Integer.MIN_VALUE;
        }
        VierGewinnt g = VierGewinnt.of(history);

        logger.info("Negamax Methode mit Scores, die durch Monte-Carlo Methode berechnet werden.");
        for(int col = 1; col <= COLUMNS; col++) {
            logger.info("Der Score der Spalte "+col+" :");
            System.out.println("check"+g.grid);

            if (!g.cango(col-1)) nextMove[col-1] = -20; // Die Spalte ist voll
            else  {
                g = g.play(Move.of(col));

                // Wenn der Spieler gewonnen hat
                if (g.gameOver()) {
                    nextMove[col-1] = 10;
                    g = g.undo();
                    continue;
                }

                // Wenn der Spieler nicht blockiert und der Sieger 100 % Chance zum Gewinnen hat
                for (int next_col = 1; next_col <= COLUMNS; next_col++) {
                    g = g.play(Move.of(next_col));
                    if (g.gameOver()) {
                        nextMove[col - 1] = -15; g = g.undo(); break;
                    }
                    g = g.undo();
                }
                if (nextMove[col-1] == Integer.MIN_VALUE) nextMove[col - 1] = negamax(1);
                g = g.undo();
            }
        }
        logger.info("Score Array "+ Arrays.toString(nextMove));
        System.out.println("Score Array "+ Arrays.toString(nextMove));
        return nextMove;
    }


    /**
     * Negamax-Methode
     * @param depth Tiefe
     * @return Maximaler Score von Move m
     */
     /*
     int negaMax( int depth ) {
     if ( depth == 0 ) return evaluate();
     int max = -infinity;
        for ( all moves)  {
            score = -negaMax( depth - 1 );
            if( score > max )
            max = score;
        }
     return max;
     }
     */
    public int negamax(int depth) {

        VierGewinnt n = VierGewinnt.of(history);

        if (depth == 0 || n.gameOver()) {
            if(depth == 0) logger.info("Die letzte Tiefe: Monte-Carlo-Score ="+monteCarlo_score());
            else logger.info("Spielzug zum Gewinn.");
            return monteCarlo_score();
        }

        int value = Integer.MIN_VALUE;
        for(int c = 1; c <= COLUMNS; c++) {
            if (n.cango(c-1)) {
                logger.info("In der Spalte "+c);
                n = n.play(Move.of(c));
                value = Math.max(value, -negamax(depth - 1));
                n = n.undo();
            }
            else return -20;
        }
        logger.info("Tiefe "+depth+" ,Maximum = "+value);
        return value;
    }

    /**
     * evaluiert das Spiel mit dem vorgegebenen Zug mithilfe der Monte-Carlo-Methode. Der berechnete Score passt dem
     * besten Zug für den Spieler, der im Zug ist.
     * @return Wahrscheinlichkeit zum Gewinn
     */
    public int monteCarlo_score() {
        //assert column.column >= 1 && column.column<=7 : "Die Spalte passt nicht.";

        VierGewinnt g = VierGewinnt.of(history); //erzeugt neue ähnliche Instanz zum später Vergleich

        if(g.gameOver()) {
            if(win()) return 10;
            else return 5; //Tied
        }

        //Monte Carlo Methode
        int exp = 400; //Anzahl der Experimente, die durchgeführt werden.
        int score = 0; //Anzahl der Gewinne oder Unentschieden
        while(exp-- > 0) {
            assert !g.gameOver() : "Das Game hat beendet, der Anfangszustand kommt nicht zurück.";
            int count = 0; //zählt die zufälligen Spielzüge

            while(!g.gameOver()) {
                g = g.play(g.randomMove());
                count++;
            }
            assert(g.gameOver()) : "Das Experiment-Spiel hat nicht beendet";

            if(g.win()) {
                //Checkt, ob der Spieler mit diesem Gitter der Sieger ist
                if(history.size()%2 == (history.size()-count)%2) score++;
            }
            else score++; //Tied
            while (count-- > 0) g = g.undo();
        }

        score = (int) Math.round(score/20.) -10;
        assert score >= -10 && score <= 10: "Evaluierter Score passt nicht.";

        return score;
    }


    /**
     * gibt eine zufällige Spalte zurück
     * @return zufällige Spalte
     */
    public Move randomMove() {
        assert !gameOver() : "Das Spiel hat beendet.";
        int column;
        do {
            column = new Random().nextInt(COLUMNS);
        } while (!cango(column));
        //logger.info("Der Spieler hat zufällig eine Spalte gewählt.");
        return Move.of(column+1);
    }

    /**
     * Rückgabe der history-Liste
     * @return history-Liste
     */
    public List<Integer> getHistory() {
        return history;
    }

    /**
     * erzeugt das Gitter von der history-Liste
     * @return das Gitter
     */
    public List<List<Integer>> getGrid() {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < COLUMNS; i++) {
            g.add(new ArrayList<>());
        }
        /*
        pos = 7*r + c
        => r = pos/r , c = pos%r
         */
        for (int pos : history) {
            int player = history.indexOf(pos) % 2 == 0? 1:2;
            int col = pos%COLUMNS;
            assert col < g.size() : pos+" ist eine unpassende Position.";
            assert g.get(col).size() == pos/COLUMNS : "Der Stein vor "+pos+" fehlt.";
            g.get(col).add(player);
        }
        return g;
    }


    /**
     * checkt, ob die Spalte voll ist, wenn nicht, kann der Spieler nicht in diese Spalte gehen
     * @param column Die gewählte Spalte
     * @return Die Spalte ist nicht voll oder nicht
     */
    public boolean cango(int column) {return grid.get(column).size() <= ROWS;}

    /**
     * löscht den letzten Zug
     * @return Spiel nach dem Rückgang
     */
    public VierGewinnt undo() {
        assert !history.isEmpty() : "Keinen Zug zum Rückgang";
        VierGewinnt v = VierGewinnt.of(history);
        v.history.remove(v.history.size()-1);
        v = VierGewinnt.of(v.history);
        assert v.history.size() == v.grid.stream().mapToInt(List::size).sum() : "Letzter Zug wurde nicht vom Gitter rausgezogen.";
        int player = (v.history.size() %2 == 0)? 1 :2;
        //logger.info("Der Spieler "+player+" hat den letzten Stein zurückgenommen.");
        return v;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("VierGewinnt:\n");
        for (int i = 0; i < COLUMNS; i++) {
            s.append("Spalte ").append(i + 1).append(grid.get(i)).append("\n");
        }
        return s +"history = "+history;
    }

}
