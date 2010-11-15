package service.${domainClass.propertyName} {

	import service.BusinessDelegate;
	import mx.rpc.IResponder;
	import mx.rpc.remoting.mxml.RemoteObject;

	public class ${className}BusinessDelegate extends BusinessDelegate {

		static private var _service:RemoteObject;

		public function ${className}BusinessDelegate(responder:IResponder) {
			super(responder);
		}

		override protected function getService():RemoteObject {
			if (!_service) {
				_service = new RemoteObject("${domainClass.propertyName}Service");
				_service.showBusyCursor = true;
				_service.concurrency = "multiple"
			}

			return _service;
		}
<%
import org.cubika.labs.scaffolding.utils.ConstraintValueUtils as CVU
CVU.actions(domainClass).each {
	println "		public function ${it.toLowerCase()}(vo:Object):void {"
	println "			getService().${it.toLowerCase()}(vo).addResponder(_responder);"
	print   "		}"
}
%>	}
}
