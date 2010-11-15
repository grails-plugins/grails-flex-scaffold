package command.${domainClass.propertyName} {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.rpc.IResponder;

	import event.DefaultNavigationEvent;
	import event.${domainClass.propertyName}.${className}CRUDEvent;

	import model.ApplicationModelLocator;
	import model.${domainClass.propertyName}.${className}Model;
	import vo.${domainClass.propertyName}.${className}VO;

	public class ${className}CreateCommand implements ICommand {
		private var _model:${className}Model = ApplicationModelLocator.instance.${domainClass.propertyName}Model;

		public function execute(event:CairngormEvent):void {

			var crudEvent:${className}CRUDEvent = ${className}CRUDEvent(event);

			_model.selected = new ${className}VO();
			_model.editView = false;

			_model.callFromPop = crudEvent.popUpName;

			if (!_model.callFromPop) {
				new DefaultNavigationEvent("${domainClass.propertyName}.list").dispatch();
			}
		}
	}
}
