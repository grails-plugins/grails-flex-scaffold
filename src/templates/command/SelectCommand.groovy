package command.${domainClass.propertyName} {
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.controls.Alert;
	import mx.rpc.IResponder;

	import event.DefaultNavigationEvent;
	import event.${domainClass.propertyName}.${className}CRUDEvent;

	import model.ApplicationModelLocator;
	import model.${domainClass.propertyName}.${className}Model;

	public class ${className}SelectCommand implements ICommand {
		private var _model:${className}Model = ApplicationModelLocator.instance.${domainClass.propertyName}Model;

		public function execute(event:CairngormEvent):void {
			var crudEvent:${className}CRUDEvent = ${className}CRUDEvent(event);

			_model.selected = crudEvent.vo;
			_model.editView = true;

			_model.callFromPop = crudEvent.popUpName;

			if (!_model.callFromPop) {
				new DefaultNavigationEvent("${domainClass.propertyName}.list").dispatch();
			}
		}
	}
}
