package command {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import model.ApplicationModelLocator;

	import event.NavigationEvent;

	public class NavigationCommand implements ICommand {
		private var _model:ApplicationModelLocator = ApplicationModelLocator.instance;

		override public function execute(event:CairngormEvent):void {
			var navigationEvent:NavigationEvent = NavigationEvent(event);
			var classModel:Object = _model[navigationEvent.model];

			classModel.selectedIndexView = classModel[navigationEvent.property];
		}
	}
}
