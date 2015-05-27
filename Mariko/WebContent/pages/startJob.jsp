<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<html>
<head>

</head>
<body>

 <h3>Click to start the Job</h3>
	<s:form action="dashboard">
		<%-- <s:textfield name="username" label="Username" />
		<s:password name="password" label="Password" /> --%>
	  <h2>
	<s:checkboxlist label="Select Jobs to be executed" list="jobs" 
	   name="yourJob" value="defaultJob" />
</h2> 

   <sx:datetimepicker name="date1" label="Select Cron Time" displayFormat="dd-MM-yyyy" />    
		<s:submit />
	</s:form>
 
</body>
</html>