package command {
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import event.LocaleEvent;
	import model.ApplicationModelLocator;
	import com.cubika.labs.utils.MultipleRM;
	
	public class LocaleCommand implements ICommand {
		public function execute(event:CairngormEvent):void {
			var e:LocaleEvent = LocaleEvent(event);
			MultipleRM.localePrefix = e.prefix;
		}
	}
}
