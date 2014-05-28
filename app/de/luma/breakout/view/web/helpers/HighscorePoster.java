package de.luma.breakout.view.web.helpers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.luma.breakout.view.web.models.Highscore;

public class HighscorePoster extends Thread {
	private Highscore highscore;
	private String serverUrl;

	public Highscore getHighscore() {
		return highscore;
	}

	public void setHighscore(Highscore highscore) {
		this.highscore = highscore;
	}

	@Inject
	public HighscorePoster(@Named("HighscoreServerUrl") String serverUrl) {
		this.serverUrl = serverUrl;
	}

	@Override
	public void run() {
		String msgBody = new Gson().toJson(highscore);

		try {
			URL url = new URL(serverUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", String.valueOf(msgBody.length()));

			OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );
			writer.write(msgBody);
			writer.flush();
			writer.close();

			connection.getInputStream().close();
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}