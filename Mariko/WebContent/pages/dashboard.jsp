<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head></head>
<body>

 
	<h2>
		Running Jobs
		<s:property value="status" />
	</h2>
	
	<s:iterator value="runningJobs"> 
	  	<s:property/>
	 </s:iterator>
	 
	 <s:property value="date1"/>
 
</body>
</html>