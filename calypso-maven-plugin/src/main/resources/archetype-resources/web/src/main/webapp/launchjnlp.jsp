<%@page contentType="application/x-java-jnlp-file" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<?xml version="1.0" encoding="utf-8"?>
<!-- application/x-java-jnlp-file --> 
<jnlp spec="1.0+" codebase="${requestScope.baseURL}" href="launch?app=${requestScope.app}">

	<information> 
		<title>Calypso ${requestScope.app} Application</title> 
		<vendor>Calypso Technology, Inc.</vendor> 
		<homepage href="${requestScope.baseURL}/"/> 
		<icon href="images/logo.gif"/> 
		<offline-allowed/> 
	</information> 

	<security> 
		<all-permissions/> 
	</security> 

	<resources> 
		<j2se version="1.6+" java-vm-args="${requestScope.vmargs}"/>
		<property name="sun.rmi.dgc.client.gcInterval" value="3600000"/>
		<property name="sun.rmi.dgc.server.gcInterval" value="3600000"/>
		
		<!-- auto generated jar list --> 
		<c:forEach var="jar" items="${requestScope.jars}"><jar href="${jar}" main="false"/>
		</c:forEach>
	</resources> 

	<application-desc main-class="${requestScope.app}"> 
		<c:forEach var="arg" items="${requestScope.args}"><argument>${arg}</argument></c:forEach>
	</application-desc>
</jnlp> 
