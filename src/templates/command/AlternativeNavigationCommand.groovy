package command {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import event.AlternativeNavigationEvent;
	import model.NavigationModelLocator;

	public class AlternativeNavigationCommand implements ICommand {
		public function execute(event:CairngormEvent):void {
			var e:AlternativeNavigationEvent = AlternativeNavigationEvent(event);
			NavigationModelLocator.instance.alternativeNavigation(e.viewFrom,e.viewTo);
		}
	}
}
