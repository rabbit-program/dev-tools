package com.king.linux.tool;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.king.linux.database.ApplicationVo;
import com.king.linux.database.MapdbServer;

import jodd.bean.BeanUtil;
import jodd.introspector.ClassDescriptor;
import jodd.introspector.FieldDescriptor;
import jodd.introspector.Introspector;
import jodd.util.SystemUtil;

public abstract class Application {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected final static String CURR_DIR = SystemUtil.userDir();

	protected MapdbServer mapdbServer;

	/**
	 * @wbp.parser.entryPoint
	 */
	public Application(String dbName) {
		mapdbServer = MapdbServer.valueOf(dbName);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	protected void asyncExec(Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}

	protected String getText(Text txt) {
		if (txt == null) {
			return "";
		}
		return txt.getText().trim();
	}

	protected void setHistoryList(List list) {
		java.util.List<ApplicationVo> values = mapdbServer.values();
		Collections.sort(values);
		String[] items = new String[values.size()];
		for (int i = 0; i < values.size(); i++) {
			items[i] = values.get(i).getKey();
		}
		list.setItems(items);
	}

	protected Map<String, Object> getFieldData(Class<?>[] includes) {
		BeanUtil beanUtil = BeanUtil.declaredForced;
		Introspector introspector = beanUtil.getIntrospector();
		ClassDescriptor classDescriptor = introspector.register(this.getClass());
		FieldDescriptor[] fieldDescriptors = classDescriptor.getAllFieldDescriptors();
		java.util.List<String> includeList = Lists.newArrayList();
		java.util.Set<Class<?>> includeSet = Sets.newHashSet(Arrays.asList(includes));
		for (FieldDescriptor fieldDescriptor : fieldDescriptors) {
			if (includeSet.contains(fieldDescriptor.getField().getType())) {
				includeList.add(fieldDescriptor.getName());
			}
		}
		Map<String, Object> dbMap = Maps.newHashMap();
		for (String fieldName : includeList) {
			Text text = beanUtil.getProperty(this, fieldName);
			dbMap.put(fieldName, getText(text));
		}
		logger.info("get map json => [{}]", JSON.toJSONString(dbMap));
		return dbMap;
	}

	protected void setFieldData(String key) {
		ApplicationVo applicationVo = mapdbServer.get(key);
		logger.info("set map json => [{}]", JSON.toJSONString(applicationVo));
		BeanUtil beanUtil = BeanUtil.declaredForced;
		Introspector introspector = beanUtil.getIntrospector();
		ClassDescriptor classDescriptor = introspector.register(this.getClass());
		FieldDescriptor[] fieldDescriptors = classDescriptor.getAllFieldDescriptors();
		java.util.List<String> includeList = Lists.newArrayList();
		for (FieldDescriptor fieldDescriptor : fieldDescriptors) {
			includeList.add(fieldDescriptor.getName());
		}
		Map<String, Object> dbMap = applicationVo.getValue();
		// 数据库keys
		java.util.Set<String> keySet = dbMap.keySet();
		for (String fieldName : keySet) {
			// 如果存在则设置值
			if (includeList.contains(fieldName)) {
				Object text = beanUtil.getProperty(this, fieldName);
				if (text instanceof Text) {
					((Text) text).setText(dbMap.get(fieldName).toString());
				}
			}
		}
	}

}
