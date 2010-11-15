package service {

	import mx.rpc.IResponder;
	import mx.rpc.remoting.mxml.RemoteObject;

	import com.cubika.labs.pagination.PageFilter;

	public class BusinessDelegate {

		protected var _responder:IResponder;

		public function BusinessDelegate(responder:IResponder) {
			_responder = responder;
		}

		public function list():void {
			getService().list().addResponder(_responder);
		}

		public function paginateList(page:PageFilter):void {
			getService().paginateList(page).addResponder(_responder);
		}

		public function save(value:Object):void {
			getService().save(value).addResponder(_responder);
		}

		public function destroy(value:Object):void {
			getService().destroy(value).addResponder(_responder);
		}

		protected function getService():RemoteObject {
			return null;
		}
	}
}
