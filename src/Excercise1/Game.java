package Excercise1;

public class Game {
    private int gameId;
    private String gameTitle;

    public Game() {
        super();
    }

    public Game(int gameId, String gameTitle) {
        super();
        this.gameId = gameId;
        this.gameTitle = gameTitle;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

}
