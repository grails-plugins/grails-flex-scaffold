package event.${domainClass.propertyName} {
	import com.adobe.cairngorm.control.CairngormEvent;
	import command.${domainClass.propertyName}.${className}${typeName}Command;
	import control.ApplicationController;

	public class ${className}${typeName}Event extends CairngormEvent {
		public static const EVENT_NAME:String = "${typeName}${className}Event";

		public var vo:Object

		public function ${className}${typeName}Event(_vo:Object) {
			super(EVENT_NAME);

			vo = _vo;

			ApplicationController.instance.registerCommand(EVENT_NAME,${className}${typeName}Command);
		}
	}
}
