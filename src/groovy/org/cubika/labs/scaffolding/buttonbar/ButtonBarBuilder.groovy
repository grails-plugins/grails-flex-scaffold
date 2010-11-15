/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cubika.labs.scaffolding.buttonbar

import org.cubika.labs.scaffolding.utils.ConstraintValueUtils as CVU

/**
 * @author Gonzalo Clavell
 */
class ButtonBarBuilder {

	static String getImports(domainClass) {
		if (!CVU.isReportable(domainClass)) {
			return ""
		}

		"			import mx.events.ItemClickEvent"
	}

	static String getProperties(domainClass) {
		if (CVU.isReportable(domainClass)) {
			return """
			//Control Panel properties
			[Bindable]
			private var _controlPanelDataProvider:Array;
			//End Control Panel properties
"""
		}

		"\n"
	}

	static String getComponent(domainClass) {
		if (CVU.isReportable(domainClass)) {
			return """
	<!-- Control Panel View -->
	<mx:Canvas clipContent="false" height="0" includeInLayout="false">
		<mx:ToggleButtonBar id="controlPanel" y="-6" x="6" dataProvider="{_controlPanelDataProvider}" itemClick="controlPanelClickHandler(event)" styleName="reportingToogleButtonBar"/>
	</mx:Canvas>
	<!-- End Control Panel View -->
"""
		}

		"\n"
	}

	static String getInitFunctionCall(domainClass) {
		if (CVU.isReportable(domainClass)) {
			return "				//ControlPanel init function\n" +
				"				initControlPanel();"
		}

		""
	}

	static String getMethods(domainClass) {
		if (CVU.isReportable(domainClass)) {
			return "			//Control Panel Methods\n" +
				getInitFunctionMethod(domainClass) +
				getClickHandler(domainClass) +
				"			//End Control Panel Methods"
		}

		"\n"
	}

	static String getInitFunctionMethod(domainClass) {
		"""
			private function initControlPanel():void {
				_controlPanelDataProvider = new Array();
				for each(var obj:Object in vs.getChildren()) {
					if (obj.name != "${domainClass.propertyName}Edit") {
						_controlPanelDataProvider.push(obj);
					}
				}
			}
"""
	}

	static String getClickHandler(domainClass) {
		"""
			private function controlPanelClickHandler(event:ItemClickEvent):void {
				selectedView = event.item.name;
			}
"""
	}
}
