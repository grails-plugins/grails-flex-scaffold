package command$pkg {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.rpc.IResponder;

	import event$pkg.${className}Event;

	public class ${className}Command implements ICommand, IResponder {
		public function execute(event:CairngormEvent):void {
			var ${artifactName}Event:${className}Event = ${className}Event(event);

			//implement your business here!
		}

		public function result(data:Object):void {
			//implement
		}

		public function fault(info:Object):void {
			//implement
		}
	}
}
