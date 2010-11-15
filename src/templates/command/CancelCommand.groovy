package command.${domainClass.propertyName} {
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.controls.Alert;
	import mx.rpc.IResponder;

	import event.DefaultNavigationEvent;
	import event.${domainClass.propertyName}.${className}CRUDEvent;

	import model.ApplicationModelLocator;
	import model.${domainClass.propertyName}.${className}Model;

	import mx.core.FlexGlobals;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;

	public class ${className}CancelCommand implements ICommand {
		private var _model:${className}Model = ApplicationModelLocator.instance.${domainClass.propertyName}Model;

		public function execute(event:CairngormEvent):void {
			if (!_model.callFromPop) {
				new DefaultNavigationEvent("${domainClass.propertyName}.edit").dispatch();
			}
			else {
				var obj:Object = FlexGlobals.topLevelApplication.systemManager.topLevelSystemManager.getChildByName(_model.callFromPop);
				PopUpManager.removePopUp(IFlexDisplayObject(obj));
				_model.callFromPop = null;
			}
			//No es una buena practica, pero me resulta de mas generar un evento/comando para nullear el selected en el model
			_model.selected = null;
		}
	}
}
