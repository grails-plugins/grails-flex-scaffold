package command.${domainClass.propertyName} {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.controls.Alert;
	import mx.rpc.IResponder;

	import service.${domainClass.propertyName}.${className}BusinessDelegate;

	import event.${domainClass.propertyName}.${className}CRUDEvent;

	import model.ApplicationModelLocator;
	import model.${domainClass.propertyName}.${className}Model;

	public class ${className}ListCommand implements ICommand, IResponder {
		private var _model:${className}Model = ApplicationModelLocator.instance.${domainClass.propertyName}Model;

		public function execute(event:CairngormEvent):void {
			var e:${className}CRUDEvent = ${className}CRUDEvent(event);

			new ${className}BusinessDelegate(this).list();
		}

		public function result(data:Object):void {
			_model.editView = false;
			_model.listNoPaged = data.result;
		}

		public function fault(info:Object):void {
			_model.editView = false;

			if (info.fault.rootCause) {
				Alert.show(info.fault.rootCause.message,"Error");
			}
			else {
				Alert.show(info.fault.faultDetail,"Error");
			}
		}
	}
}
