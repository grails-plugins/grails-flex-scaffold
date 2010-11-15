package command.${domainClass.propertyName} {
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.controls.Alert;
	import mx.rpc.IResponder;

	import service.${domainClass.propertyName}.${className}BusinessDelegate;

	import event.DefaultNavigationEvent;
	import event.${domainClass.propertyName}.${className}CRUDEvent;

	import model.ApplicationModelLocator;

	import command.gfs.AbstractNavigationCommand;

	public class ${className}SaveOrUpdateCommand extends AbstractNavigationCommand {

		public function ${className}SaveOrUpdateCommand() {
			_navigateKey = "${domainClass.propertyName}.edit";
			_model = ApplicationModelLocator.instance.${domainClass.propertyName}Model
		}

		override public function execute(event:CairngormEvent):void {
			var crudEvent:${className}CRUDEvent = ${className}CRUDEvent(event);

			new ${className}BusinessDelegate(this).save(crudEvent.vo);
		}

		override protected function doResult(data:Object):void {
			_model.editView = false;
			_model.updateList(data.result);
		}

		override protected function doFault(info:Object):void {
			//to implement if neccessary
		}
	}
}
