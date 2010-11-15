package control {

	import com.adobe.cairngorm.control.FrontController;

	public class ApplicationController extends FrontController {
		private static var _instance:ApplicationController;

		public function ApplicationController(enforcer:SingletonEnforcer) {}

		public static function get instance():ApplicationController {
			if (!_instance) {
				_instance = new ApplicationController(new SingletonEnforcer());
			}
			return _instance;
		}

		public function registerCommand(eventName:String,command:Class):void {
			if (!isRegister(eventName)) {
				addCommand(eventName,command);
			}
		}

		private function isRegister(eventName:String):Boolean {
			return commands[eventName] != null
		}
	}
}

class SingletonEnforcer{}
