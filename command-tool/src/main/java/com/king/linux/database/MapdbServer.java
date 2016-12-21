package com.king.linux.database;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DB.TreeMapMaker;
import org.mapdb.DBMaker;
import org.mapdb.DBMaker.Maker;
import org.mapdb.Serializer;

import com.google.common.collect.Lists;

import jodd.util.SystemUtil;

public class MapdbServer {

	private final static String dbFile = SystemUtil.userDir() + File.separator + "monitor.db";
	
	private final static Maker MAKER = DBMaker.fileDB(dbFile).closeOnJvmShutdown();

	private final static MyModificationListener<ApplicationVo> mapModificationListener = new MyModificationListener<>();

	private MapdbServer() {
		
	}
	
	private String tableName;
	
	private MapdbServer(String tableName) {
		this.tableName = tableName;
	}

	public static MapdbServer valueOf(String dbName) {
		return new MapdbServer(dbName);
	}
	
	@SuppressWarnings("unchecked")
	public void put(String key, ApplicationVo value) {
		DB db = MAKER.make();
		try {
			TreeMapMaker<String, ApplicationVo> treeMapMaker =  db.treeMap(tableName, Serializer.STRING, Serializer.JAVA);
			treeMapMaker.modificationListener(mapModificationListener);
			BTreeMap<String, ApplicationVo> bTreeMap = treeMapMaker.createOrOpen();
			bTreeMap.put(key, value);
		} finally{
			db.commit();
			db.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public ApplicationVo get(String key) {
		DB db = MAKER.make();
		try {
			TreeMapMaker<String, ApplicationVo> treeMapMaker =  db.treeMap(tableName, Serializer.STRING, Serializer.JAVA);
			treeMapMaker.modificationListener(mapModificationListener);
			BTreeMap<String, ApplicationVo> bTreeMap = treeMapMaker.createOrOpen();
			return bTreeMap.get(key);
		} finally{
			db.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void remove(String key) {
		DB db = MAKER.make();
		try {
			TreeMapMaker<String, ApplicationVo> treeMapMaker =  db.treeMap(tableName, Serializer.STRING, Serializer.JAVA);
			treeMapMaker.modificationListener(mapModificationListener);
			BTreeMap<String, ApplicationVo> bTreeMap = treeMapMaker.createOrOpen();
			bTreeMap.remove(key);
		} finally{
			db.commit();
			db.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ApplicationVo> values() {
		DB db = MAKER.make();
		try {
			TreeMapMaker<String, ApplicationVo> treeMapMaker =  db.treeMap(tableName, Serializer.STRING, Serializer.JAVA);
			treeMapMaker.modificationListener(mapModificationListener);
			BTreeMap<String, ApplicationVo> bTreeMap = treeMapMaker.createOrOpen();
			Iterator<ApplicationVo> iterator =  bTreeMap.descendingValueIterator();
			List<ApplicationVo> list = Lists.newLinkedList();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
			return list;
		} finally{
			db.close();
		}
	}

	public String getTableName() {
		return tableName;
	}
}
