package VierGewinnt;

import processing.core.PApplet;

public class VG_Graphik extends PApplet {
    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"VierGewinnt"},new VG_Graphik());
    }

    VierGewinntGame game;
    public void settings() {
        size(700,490);
    }
    public void setup() {
        graphic();
        System.out.println("Spiel Viergewinn!\n"+"Player 1's turn");
        game = VierGewinnt.newGame();
    }
    public void graphic(){
        background(51,153,255);
        for (int y = 0; y < 420; y += 70) {
            for (int x = 0; x < 490; x += 70) {
                ellipseMode(CORNER);
                stroke(0);
                strokeWeight(1);
                fill(255);
                ellipse(x,y,68,68);
            }
        }
        button();
    }
    public void draw() {

    }
    // Nach dem Drucken einer Spalte im Gitter
    public void mousePressed() {
        //Click das Gitter
        if(!game.gameOver() && mouseX<490) {
            int column = abs(mouseX / 70);
            if(game.cango(column)) {
                game = game.play(Move.of(column + 1));
                System.out.println("User\n" + game); //Anzeige, um den Zug von Bestmove nicht zu verwechseln
                int lastPos = game.getHistory().get(game.getHistory().size() - 1); //letzte Pos in history
                int row = game.getHistory().isEmpty() ? 5 :
                        5 - lastPos / 7;
                //System.out.println("row=" + row);
                //0,1,2,3,4,5
                //1,2,3,4,5,6
                int x = column * 70;
                int y = row * 70;

                int player = game.getHistory().size() % 2 == 0 ? 2 : 1;
                System.out.println("End the turn of player " + player);
                p_color(player);
                //System.out.println(player);
                ellipse(x, y, 68, 68);
                player = player == 1 ? 2 : 1;
                System.out.println("Player "+player+"'s turn");
                if (game.gameOver()) {
                    stroke(51, 153, 255);
                    strokeWeight(2); //newline
                    fill(51, 153, 255);
                    ellipse(595, 280, 68, 68);
                    win();
                    tie();
                } else {
                    p_color(player);
                    ellipse(595, 280, 68, 68);
                }
            }
        }

        undo(); //Click das Undo-Button
        newgame(); //Click das new-game-Button
        bestmove(); //Click best-move
    }

    //Farbe für jeder Spieler
    public void p_color(int player) {
        noStroke();
        if(player == 1) fill(255, 153, 255);
        else fill(255, 255, 153);
    }

    //Nach dem Gewinn
    public void win() {
        if(game.win()) {
            stroke(204,0,0);
            strokeWeight(10);
            noFill();
            for (int move : game.pwin()) {
                int col = move% VierGewinntGame.COLUMNS;
                int row = 5-move/ VierGewinntGame.COLUMNS;
                int x = col*70;
                int y = row*70;
                ellipse(x,y,68,68);
            }
            int player = game.getHistory().size()%2 == 0? 2 : 1;
            fill(204,0,0);
            textAlign(CENTER);
            textMode(SHAPE);
            textSize(30);
            text("PLAYER "+player+" WIN!",595,325);
        }
    }

    //Nach dem Unentschieden
    public void tie() {
        if(game.tied()) {
            fill(255,0,0);
            textAlign(CENTER);
            textMode(SHAPE);
            textSize(30);
            text("TIED!",595,325);
        }
    }

    //Nach dem Drucken eines Buttons (Neu, Best Move oder Zurück)
    public void button() {
        for(int i = 1; i <= 3; i ++) {
            fill(153,204,255);
            rectMode(CENTER);
            rect(595,70*i, 190, 60);
            textAlign(CENTER);

            textSize(30);
            fill(0);
            textMode(SHAPE);
            switch (i){
                case 1: text("ZURÜCK",595,80);
                case 2: text("NEU",595,150);
                case 3: text("BEST MOVE",595,220);
            }
        }
    }

    //Wenn Neu-Button gedruckt wird
    public void newgame() {
        if(mouseY>=110 && mouseY<=170 && mouseX>=500 && mouseX<=690) setup();
    }

    //Wenn Zurück-Button gedruckt wird
    public void undo() {
        if(!game.getHistory().isEmpty() && mouseY>=40 && mouseY<=100 && mouseX>=500 && mouseX<=690) {

            game = game.undo();
            System.out.println("Undo \n"+game);
            graphic();
            for(int pos : game.getHistory()) {
                int col = pos%7;
                int row = 5-pos/7;
                int x = col*70;
                int y = row*70;

                int ipos = game.getHistory().indexOf(pos);
                int player = ipos%2 == 0? 1:2;
                p_color(player);
                ellipse(x,y,68,68);
            }
            int player = (game.getHistory().size() % 2 == 0)? 1: 2;
            p_color(player);
            ellipse(595,280,68,68);
            System.out.println("Spieler "+player+" hat sein letztes Stein zurückgenommen.");
        }
    }

    //Wenn Bestmove-Button gedruckt wird
    public void bestmove() {
        if(mouseY>=180 && mouseY<=240 && mouseX>=500 && mouseX<=690) {
            System.out.println("Best move");
            game = game.play(game.bestMove());
            System.out.println(game);
            int pos = game.getHistory().get(game.getHistory().size()-1);
            int col = pos%7;
            int row = 5-pos/7;
            int x = col*70;
            int y = row*70;

            int player = game.getHistory().size()%2 == 0? 2:1;
            System.out.println("End the turn of player "+player);

            p_color(player);
            ellipse(x,y,68,68);
            player = player == 1? 2:1;
            if(game.gameOver()) {
                fill(51,153,255);
                ellipse(595,280,68,68);
                win();
                tie();
            }
            else {
                p_color(player);
                ellipse(595,280,68,68);
            }
        }
    }
}
