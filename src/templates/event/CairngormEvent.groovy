package event${pkg} {
	import com.adobe.cairngorm.control.CairngormEvent;

	import command${pkg}.${className}Command;

	import control.ApplicationController;

	public class ${className}Event extends CairngormEvent {
		public static const EVENT_NAME:String = "${artifactName}Event";

		public function LocaleEvent() {
			super(EVENT_NAME);

			ApplicationController.instance.registerCommand(EVENT_NAME,${className}Command);
		}
	}
}
