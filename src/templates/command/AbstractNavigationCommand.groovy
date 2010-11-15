package command.gfs {

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import event.DefaultNavigationEvent;

	import mx.controls.Alert;
	import mx.core.FlexGlobals;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	import mx.rpc.IResponder;

	public class AbstractNavigationCommand implements ICommand, IResponder {
		protected var _model:Object;

		protected var _navigateKey:String;

		public function execute(event:CairngormEvent):void {
			throw new Error("execute must be overridden ");
		}

		public function result(data:Object):void {
			doResult(data);

			if (_navigateKey) {
				navigate(_navigateKey)
			}
		}

		public function fault(info:Object):void {
			if (info.fault.rootCause) {
				Alert.show(info.fault.rootCause.message,"Error");
			}
			else {
				Alert.show(info.fault.faultDetail,"Error");
			}
			doFault(info)
		}

		protected function navigate(value:String):void {
			if (!_model.callFromPop) {
				new DefaultNavigationEvent(value).dispatch();
			}
			else {
				var obj:Object = FlexGlobals.topLevelApplication.systemManager.topLevelSystemManager.getChildByName(_model.callFromPop);
				PopUpManager.removePopUp(IFlexDisplayObject(obj));
				_model.callFromPop = null;
			}
		}

		protected function doResult(data:Object):void {throw new Error("doResult must be overridden ")}

		protected function doFault(data:Object):void {throw new Error("doFault must be overridden ")}
	}
}
