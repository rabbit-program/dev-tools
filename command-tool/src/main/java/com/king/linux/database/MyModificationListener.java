package com.king.linux.database;

import org.mapdb.MapModificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyModificationListener<T> implements MapModificationListener<String, T> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void modify(String key, Object oldValue, Object newValue, boolean triggered) {
		// TODO Auto-generated method stub
		if (logger.isInfoEnabled()) {
			logger.info("key:[{}] oldValue:[{}] newValue:[{}] triggered:[{}]", key, oldValue, newValue, triggered);
		}
	}

}
