package me.usainsrht.rockpaperscissors;

public enum RPSElement {

    ROCK,
    PAPER,
    SCISSORS;


    RPSResult over(RPSElement other) {
        if (this == other) return RPSResult.DRAW;

        if (this == ROCK) {
            if (other == PAPER) return RPSResult.LOSE;
            else if (other == SCISSORS) return RPSResult.WIN;
        } else if (this == PAPER) {
            if (other == SCISSORS) return RPSResult.LOSE;
            else if (other == ROCK) return RPSResult.WIN;
        } else if (this == SCISSORS) {
            if (other == ROCK) return RPSResult.LOSE;
            else if (other == PAPER) return RPSResult.WIN;
        }

        throw new RuntimeException("Something went wrong!");
    }

}
