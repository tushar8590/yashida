/**
 * 
 */
package com.charliechocolatefactory.quartz.scheduler.jobs;

import java.util.List;

/**
 * @author Tushar
 *
 */
public interface CustomMatcher {

	public void setChildTables(List<String> children);
	public void getMappedData(String childTableName) throws Exception;
	public void setMainTable(String mainTable);
	public void setTargetTable(String targetTable);
	
}
