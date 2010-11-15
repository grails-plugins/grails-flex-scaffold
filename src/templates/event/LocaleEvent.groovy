package event {
	import com.adobe.cairngorm.control.CairngormEvent;
	import command.LocaleCommand;
	import control.ApplicationController;

	public class LocaleEvent extends CairngormEvent {
		public static const EVENT_NAME:String = "localeEvent";

		public var prefix:String;

		/**
		 * @param String prefix
		 */
		public function LocaleEvent(prefix:String) {
			super(EVENT_NAME);

			ApplicationController.instance.registerCommand(EVENT_NAME,LocaleCommand);

			this.prefix = prefix;
		}
	}
}
