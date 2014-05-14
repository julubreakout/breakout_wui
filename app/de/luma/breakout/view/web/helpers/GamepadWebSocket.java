package de.luma.breakout.view.web.helpers;

import de.luma.breakout.controller.IGameController;
import de.luma.breakout.controller.IGameController.PLAYER_INPUT;
import play.libs.F.Callback;
import play.mvc.WebSocket;
import views.html.helper.input;

public class GamepadWebSocket extends WebSocket<String> {

	private final IGameController gameInstance;

	public GamepadWebSocket(IGameController gameInstance) {
		this.gameInstance = gameInstance;
	}

	@Override
	public void onReady(play.mvc.WebSocket.In<String> in, play.mvc.WebSocket.Out<String> out) {
		in.onMessage(new Callback<String>() {

			@Override
			public void invoke(String message) throws Throwable {
				// message is a signed int. 
				// -5 = move 5x to the left
				// 5 = move 5x to the right
				int value = Integer.parseInt(message);

				PLAYER_INPUT inputType = PLAYER_INPUT.LEFT; 
				if (value > 0) {
					inputType = PLAYER_INPUT.RIGHT;
				}

				for (int i = 0; i < Math.abs(value); i++) {
					gameInstance.processGameInput(inputType);
				}
			}
		});
	}

}
