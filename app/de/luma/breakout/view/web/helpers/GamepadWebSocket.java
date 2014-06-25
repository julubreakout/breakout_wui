package de.luma.breakout.view.web.helpers;

import play.libs.F.Callback;
import play.mvc.WebSocket;
import akka.actor.ActorRef;
import de.luma.breakout.communication.PLAYER_INPUT;
import de.luma.breakout.communication.messages.GameInputMessage;

public class GamepadWebSocket extends WebSocket<String> {

	private final ActorRef gameInstance;
	//private final ActorRef parentActor;

	public GamepadWebSocket(ActorRef gameInstance) {
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
					gameInstance.tell(new GameInputMessage(inputType), null);
				}
			}
		});
	}

}
