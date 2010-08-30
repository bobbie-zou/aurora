package aurora.presentation.component.std;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uncertain.composite.CompositeMap;
import uncertain.composite.TextParser;
import aurora.presentation.BuildSession;
import aurora.presentation.ViewContext;
import aurora.presentation.component.std.config.RadioConfig;

/**
 * Radio
 * @version $Id: Radio.java v 1.0 2010-8-27 下午01:05:16 IBM Exp $
 * @author <a href="mailto:njq.niu@hand-china.com">vincent</a>
 */
public class Radio extends Component {
	
	public void onCreateViewContent(BuildSession session, ViewContext view_context) throws IOException{
		super.onCreateViewContent(session, view_context);
		Map map = view_context.getMap();
		CompositeMap model = view_context.getModel();
		CompositeMap view = view_context.getView();	
		
		RadioConfig rc = RadioConfig.getInstance(view);
		String layout = rc.getLayout();
		String labelField = rc.getLabelField();
		String valueField = rc.getValueField();
		
		CompositeMap items = rc.getItems();
		if(items!=null){
			try {
				createOptions(session,map,items,layout,labelField,valueField, rc.getLabelExpression());
			} catch (JSONException e) {
				throw new IOException(e.getMessage());
			}
		}else {
			String ds = rc.getOptions();
			if(ds!=null){
				CompositeMap options = (CompositeMap)model.getObject(ds);
				if(options!=null)
				try {
					createOptions(session,map,options,layout,labelField,valueField, rc.getLabelExpression());
				} catch (JSONException e) {
					throw new IOException(e.getMessage());
				}
			}
			
		}
		addConfig("valueField", valueField);
		addConfig("selectIndex", new Integer(rc.getSelectIndex()));
		map.put(CONFIG, getConfigString());
	}
	
	
	private void createOptions(BuildSession session,Map map, CompositeMap items,String layout,String labelField,String valueField,String expression) throws JSONException {
		StringBuffer sb = new StringBuffer();
		List children = items.getChilds();
		List options = new ArrayList();
		if(children!=null){
			Iterator it = children.iterator();
			while(it.hasNext()){
				CompositeMap item = (CompositeMap)it.next();
				String label;
				if(expression!=null){
					label = TextParser.parse(expression, item);
				}else{
					label = item.getString(labelField, "");
				}
				label = session.getLocalizedPrompt(label);
				String value = item.getString(valueField, "");
				
				JSONObject option = new JSONObject(item);
				options.add(option);
				
				if(!"".equals(label)){
					label = ":"+label;
				}
				
				sb.append("<div class='item-radio-option'  style='text-align:left;");
				if("horizontal".equalsIgnoreCase(layout)) {
					sb.append("float:left'");
				}else{
					sb.append("'");				
				}
				sb.append(" itemvalue='"+value+"'>"); 
				sb.append("<div class='item-radio-img'></div>");
				sb.append("<label class='item-radio-lb'>"+label+"</label>");
				sb.append("</div>");
			}
		}
		addConfig("options", new JSONArray(options));
		map.put("options", sb.toString());
	}

}
