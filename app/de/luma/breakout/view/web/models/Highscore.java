package de.luma.breakout.view.web.models;

public class Highscore {

	private String game;
	private String player;
	private int score;
	
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Highscore(String game, String player, int score) {
		super();
		this.game = game;
		this.player = player;
		this.score = score;
	}
	
}
