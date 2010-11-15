package event.${domainClass.propertyName} {

	import com.adobe.cairngorm.control.CairngormEvent;
	import com.cubika.labs.pagination.PageFilter;
	import command.${domainClass.propertyName}.${className}${typeName}GetPaginationListCommand;
	import control.ApplicationController;

	public class ${className}${typeName}GetPaginationEvent extends CairngormEvent {

		static public const EVENT_NAME:String = "get${className}${typeName}PaginationEvent";

		public var page:PageFilter;

		public function ${className}${typeName}GetPaginationEvent(_pageFilter:PageFilter) {
			super(EVENT_NAME);

			ApplicationController.instance.registerCommand(EVENT_NAME,${className}${typeName}GetPaginationListCommand);

			page = _pageFilter;
		}
	}
}
