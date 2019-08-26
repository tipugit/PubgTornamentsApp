package com.the.ultimate.tournament.games.data;

/**
 * Created by rajan on 15/01/16.
 */
public class MatchResult {

    private String playerKills;
    private String playerName;
    private String playerWinning;
    private String position;

    public MatchResult() {
    }

    public MatchResult(String playerKills, String playerName, String playerWinning, String position) {
        this.playerKills = playerKills;
        this.playerName = playerName;
        this.playerWinning = playerWinning;
        this.position = position;
    }

    public String getPlayerKills() {
        return playerKills;
    }

    public void setPlayerKills(String playerKills) {
        this.playerKills = playerKills;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerWinning() {
        return playerWinning;
    }

    public void setPlayerWinning(String playerWinning) {
        this.playerWinning = playerWinning;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
