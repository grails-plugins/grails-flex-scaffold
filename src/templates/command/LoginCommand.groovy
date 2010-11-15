package command.gfs {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import event.gfs.LoginEvent;

	import model.ApplicationModelLocator;

	import mx.controls.Alert;
	import mx.managers.CursorManager;
	import mx.managers.PopUpManager;
	import mx.messaging.config.ServerConfig;
	import mx.rpc.IResponder;

	public class LoginCommand implements ICommand, IResponder {
		public var appModel:ApplicationModelLocator = ApplicationModelLocator.instance;

		public function execute(event:CairngormEvent):void {
			var loginEvent:LoginEvent = LoginEvent(event);

			CursorManager.setBusyCursor();

			ServerConfig.getChannelSet("my-amf").login(loginEvent.username,loginEvent.password).addResponder(this);
		}

		public function result(data:Object):void {
			appModel.logged = true;
			CursorManager.removeBusyCursor();
			//appModel.currentUser = data.result;
			PopUpManager.removePopUp(appModel.popup);
			appModel.currentView = 1;
		}

		public function fault(info:Object):void {
			CursorManager.removeBusyCursor();
			Alert.show(info.fault.faultString,"Error");
		}
	}
}
