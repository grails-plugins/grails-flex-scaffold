package event {
	import com.adobe.cairngorm.control.CairngormEvent;
	import command.DefaultNavigationCommand;
	import control.ApplicationController;

	public class DefaultNavigationEvent extends CairngormEvent {
		public static const EVENT_NAME:String = "defaultNavigationEvent";

		public var viewFrom:String;

		/**
		 * @param viewFrom
		 */
		public function DefaultNavigationEvent(viewFrom:String) {
			super(EVENT_NAME);

			ApplicationController.instance.registerCommand(EVENT_NAME,DefaultNavigationCommand);

			this.viewFrom = viewFrom;
		}
	}
}
