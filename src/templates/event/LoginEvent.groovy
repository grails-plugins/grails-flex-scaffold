package event.gfs {
	import com.adobe.cairngorm.control.CairngormEvent;
	import command.gfs.LoginCommand;
	import control.ApplicationController;

	public class LoginEvent extends CairngormEvent {
		public static const EVENT_NAME:String = "loginEvent";

		public var username:String
		public var password:String

		public function LoginEvent(username:String, password:String) {
			super(EVENT_NAME);

			this.username = username;
			this.password = password;

			ApplicationController.instance.registerCommand(EVENT_NAME,LoginCommand);
		}
	}
}
