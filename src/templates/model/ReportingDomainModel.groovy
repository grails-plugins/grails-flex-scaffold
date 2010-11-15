<%
import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU
import org.cubika.labs.scaffolding.utils.ConstraintValueUtils as CVU
%>

package model.${domainClass.propertyName} {

	import mx.collections.ArrayCollection;
	import com.cubika.labs.controls.reporting.column.ReportingColumnDescriptor;

	[Bindable]
	public class ${className}ReportingModel {

		public var className:String = "${domainClass.naturalName}";
		public var columnDataProvider:ArrayCollection;

		public function ${className}ReportingModel():void {
			var propertyArray:Array = new Array();
<%
FSU.getPropertiesWithoutIdentity(domainClass,true).each {
	String requestParameter = ""
	def domainClassProperty = it.referencedDomainClass
	if (domainClassProperty) {
		requestParameter = CVU.getLabeledProperty(domainClassProperty)
	}
	println """			propertyArray.push(new ReportingColumnDescriptor("${domainClass.propertyName}", "${it.name}","${it.naturalName}","${requestParameter}"));"""
}
%>
			columnDataProvider = new ArrayCollection(propertyArray);
		}
	}
}
