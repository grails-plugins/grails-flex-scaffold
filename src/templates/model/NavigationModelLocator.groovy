package model {

	import mx.core.FlexGlobals;

	[Bindable]
	public class NavigationModelLocator {
		static private var _instance:NavigationModelLocator;

		private var defaultNavigationMap:Object = {};
		private var navigationAlternativeMap:Object = {};

		/**
		 * @param enforcer - force singleton pattern
		 */
		public function NavigationModelLocator(enforcer:SingletonEnforcer) {
			//DefaultNavigationMap - Not Remove
		}

		/**
		 * @return NavigationModelLocator instance
		 */
		static public function get instance():NavigationModelLocator {
			if (!_instance) {
				_instance = new NavigationModelLocator(new SingletonEnforcer());
			}

			return _instance;
		}

		/**
		 * @param viewFrom
		 * @param viewTo
		 */
		public function alternativeNavigation(viewFrom:String,viewTo:String):void {
			goTo(getRootView(viewTo),getView(viewTo));
			registerBackView(viewFrom,viewTo);
		}

		/**
		 * @param viewTo
		 */
		public function defaultNavigation(viewFrom:String):void {
			var rootView:String = getRootView(viewFrom);
			var view:String = getView(viewFrom);
			var viewTo:String = defaultNavigationMap[rootView].nav[view]

			goTo(rootView,viewTo);
			goToBack(viewFrom);
		}

		/**
		 * @param viewFrom
		 * @param viewTo
		 */
		private function registerBackView(viewFrom:String,viewTo:String):void {
			navigationAlternativeMap[viewTo] = viewFrom;
		}

		/**
		 * @param view
		 */
		private function goToBack(view:String):void {
			var viewBack:String = navigationAlternativeMap[view];
			if (viewBack) {
				navigationAlternativeMap[view] = null;
				goTo(getRootView(viewBack),getView(viewBack));
			}
		}

		/**
		 * @param rootView
		 * @param viewTo
		 */
		private function goTo(rootView:String, viewTo:String):void {
			var root:Object = FlexGlobals.topLevelApplication.principalView.getView(defaultNavigationMap[rootView].name)
			root.selectedView = defaultNavigationMap[rootView][viewTo];
		}

		/**
		 * @param view
		 * @return
		 */
		private function getRootView(view:String):String {
			return view.split(".")[0];
		}

		/**
		 * @param view
		 * @return
		 */
		private function getView(view:String):String {
			return view.split(".")[1];
		}
	}
}

class SingletonEnforcer {}
