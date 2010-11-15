package event.${domainClass.propertyName} {
	import com.adobe.cairngorm.control.CairngormEvent;

	import control.ApplicationController;

	import command.${domainClass.propertyName}.${className}CreateCommand;
	import command.${domainClass.propertyName}.${className}DeleteCommand;
	import command.${domainClass.propertyName}.${className}ListCommand;
	import command.${domainClass.propertyName}.${className}SaveOrUpdateCommand;
	import command.${domainClass.propertyName}.${className}DeleteCommand;
	import command.${domainClass.propertyName}.${className}CancelCommand;
	import command.${domainClass.propertyName}.${className}SelectCommand;

	import vo.${domainClass.propertyName}.${className}VO;

	public class ${className}CRUDEvent extends CairngormEvent {

		static public var LIST_EVENT:String            = "list${className}Event";
		static public var SAVE_OR_UPDATE_EVENT:String  = "save${className}Event";
		static public var SELECT_EVENT:String          = "select${className}Event";
		static public var CREATE_EVENT:String          = "create${className}Event";
		static public var DELETE_EVENT:String          = "delete${className}Event";
		static public var CANCEL_EVENT:String          = "cancel${className}Event";

		public var vo:${className}VO;

		public var vos:Array;

		public var popUpName:String;

		public function ${className}CRUDEvent(eventType:String,value:${className}VO=null,popName:String = null) {
			super(eventType);

			registersCommands();

			vo = value;
			popUpName = popName;
		}

		private function registersCommands():void {
			ApplicationController.instance.registerCommand(LIST_EVENT,${className}ListCommand);
			ApplicationController.instance.registerCommand(SAVE_OR_UPDATE_EVENT,${className}SaveOrUpdateCommand);
			ApplicationController.instance.registerCommand(SELECT_EVENT,${className}SelectCommand);
			ApplicationController.instance.registerCommand(CREATE_EVENT,${className}CreateCommand);
			ApplicationController.instance.registerCommand(DELETE_EVENT,${className}DeleteCommand);
			ApplicationController.instance.registerCommand(CANCEL_EVENT,${className}CancelCommand);
		}
	}
}
