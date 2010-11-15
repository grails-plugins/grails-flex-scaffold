package command.${domainClass.propertyName} {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.controls.Alert;
	import mx.rpc.IResponder;

	import service.${domainClass.propertyName}.${className}BusinessDelegate;

	import event.${domainClass.propertyName}.${className}CRUDEvent;

	import model.ApplicationModelLocator;
	import model.${domainClass.propertyName}.${className}Model;

	import command.gfs.AbstractNavigationCommand;

	public class ${className}DeleteCommand extends AbstractNavigationCommand {

		public function ${className}DeleteCommand() {
			_model = ApplicationModelLocator.instance.${domainClass.propertyName}Model
		}

		override public function execute(event:CairngormEvent):void {
			var crudEvent:${className}CRUDEvent = ${className}CRUDEvent(event);

			new ${className}BusinessDelegate(this).destroy(crudEvent.vos);
		}

		override protected function doResult(data:Object):void {
			_model.editView = false;
			_model.removeItems(data.result);
		}

		override protected function doFault(info:Object):void {
			_model.editView = false;
		}
	}
}
