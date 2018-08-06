package Excercise1;

public class PlayerAndGame {
    private int playerGameId;
    private int gameId;
    private int playerId;
    private String gameTitle;
    private String playingDate;
    private int score;

    public PlayerAndGame(int playerGameId, int gameId, int playerId, String gameTitle, String playingDate, int score) {
        super();
        this.playerGameId = playerGameId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.gameTitle = gameTitle;
        this.playingDate = playingDate;
        this.score = score;
    }

    public int getPlayerGameId() {
        return playerGameId;
    }

    public void setPlayerGameId(int playerGameId) {
        this.playerGameId = playerGameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getPlayingDate() {
        return playingDate;
    }

    public void setPlayingDate(String playingDate) {
        this.playingDate = playingDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
