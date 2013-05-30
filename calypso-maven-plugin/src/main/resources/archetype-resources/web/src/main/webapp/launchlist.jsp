<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
 <body>
    <h3>Environment</h3>
    <form method="post">
        <select name="envSelect" onchange="window.location = 'launch?envName=' + this.value;">
            <c:forEach var="envName" items="${requestScope.envNames}">
                <option value="${envName}" <c:if test="${requestScope.envName==envName}">selected</c:if> >${envName}</option>
            </c:forEach>
        </select>
    </form>
    <h3>Available Applications</h3>

 	<table>
 	<c:forEach var="app" items="${requestScope.supportedApps}">
 		<tr>
 			<td><h4>${app}</h4></td>
 		</tr>
 		<tr>
 			<td>
 			<% Map<String, Object> configurations = (Map<String, Object>)request.getAttribute("supportedConfigs"); 
 			Object supportedAppConfigs;
 			if(configurations.containsKey(pageContext.getAttribute("app"))) {
 				supportedAppConfigs = configurations.get(pageContext.getAttribute("app"));
 			} else {
 				supportedAppConfigs = "default";
 			}
 			pageContext.setAttribute("supportedAppConfigs",supportedAppConfigs);%>
 			<c:forEach var="config" items="${supportedAppConfigs}">
 				<ul>
 					<li><a href="launch?envName=${envName}&app=${app}&config=${config}">${config}</a></li>
 				</ul>
 			</c:forEach>
 			</td>
 		</tr>
	</c:forEach>
	</table>
 </body>
</html>