package com.s8.core.io.json.fields.maps;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.composing.JSON_ComposingException;
import com.s8.core.io.json.fields.FieldHandler;

public abstract class MapFieldHandler extends FieldHandler {

	public MapFieldHandler(String name, Field field) {
		super(name, field);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean compose(Object object, ComposingScope scope) throws JSON_ComposingException, IOException {


		// retrieve array
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new JSON_ComposingException(e.getMessage());
		}


		if(map!=null) {

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(" : ");

			ComposingScope enclosedScope = scope.enterSubscope('{', '}', true);

			enclosedScope.open();
			Object value;
			for(Entry<String, Object> entry : map.entrySet()) {

				enclosedScope.newItem();
				
				scope.append('"');
				scope.append(entry.getKey());
				scope.append('"');
				scope.append(" : ");
				
				value = entry.getValue();
				composeValue(enclosedScope, value);
			};
			enclosedScope.close();
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param scope
	 * @param item
	 * @throws JSON_ComposingException
	 * @throws IOException
	 */
	public abstract void composeValue(ComposingScope scope, Object item) throws JSON_ComposingException, IOException;

}
