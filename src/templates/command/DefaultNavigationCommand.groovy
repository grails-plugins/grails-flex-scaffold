package command {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import event.DefaultNavigationEvent;
	import model.NavigationModelLocator;

	public class DefaultNavigationCommand implements ICommand {
		public function execute(event:CairngormEvent):void {
			var e:DefaultNavigationEvent = DefaultNavigationEvent(event);
			NavigationModelLocator.instance.defaultNavigation(e.viewFrom);
		}
	}
}
