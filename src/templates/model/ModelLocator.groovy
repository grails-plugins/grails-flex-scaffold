package model {

	/*IMPORTS*/

	public class ApplicationModelLocator {
		static private var _instance:ApplicationModelLocator;

		// Locale var
		[Bindable]
		public var localePrefix:String = 'message';

		public var popup:*;

		[Bindable]
		public var currentView:int = 0;

		[Bindable]
		public var logged:Boolean = false;

		/*PROPERTIES*/

		public function ApplicationModelLocator(enforcer:SingletonEnforcer) {}

		static public function get instance():ApplicationModelLocator {
			if (!_instance) {
				_instance = new ApplicationModelLocator(new SingletonEnforcer());
			}

			return _instance;
		}

		/*GETTERS*/
	}
}

class SingletonEnforcer {}
