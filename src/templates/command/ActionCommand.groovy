package command.${domainClass.propertyName} {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.controls.Alert;
	import mx.rpc.IResponder;

	import service.${domainClass.propertyName}.${className}BusinessDelegate;

	import event.${domainClass.propertyName}.${className}${typeName}Event;

	import model.ApplicationModelLocator;

	import command.gfs.AbstractNavigationCommand;

	public class ${className}${typeName}Command implements ICommand,IResponder {

		public function execute(event:CairngormEvent):void {
			var e:${className}${typeName}Event = ${className}${typeName}Event(event);

			new ${className}BusinessDelegate(this).${typeName.toLowerCase()}(e.vo);
		}

		public function result(data:Object):void {
			Alert.show(data.result);
		}

		public function fault(info:Object):void {
			if (info.fault.rootCause) {
				Alert.show(info.fault.rootCause.message,"Error");
			}
			else {
				Alert.show(info.fault.faultDetail,"Error");
			}
		}
	}
}
