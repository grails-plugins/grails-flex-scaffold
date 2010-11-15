package event {
	import com.adobe.cairngorm.control.CairngormEvent;
	import command.AlternativeNavigationCommand;
	import control.ApplicationController;

	public class AlternativeNavigationEvent extends CairngormEvent {
		public static const EVENT_NAME:String = "alternativeNavigation";

		public var viewTo:String;
		public var viewFrom:String;

		/**
		 * @param viewFrom
		 * @param viewTo
		 */
		public function AlternativeNavigationEvent(viewFrom:String,viewTo:String) {
			super(EVENT_NAME);

			ApplicationController.instance.registerCommand(EVENT_NAME,AlternativeNavigationCommand);

			this.viewFrom = viewFrom;
			this.viewTo = viewTo;
		}
	}
}
