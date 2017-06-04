<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.gagarwa.ai.recorder.AIRecorder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Recorder</title>
<%@include file="style.jsp"%>
</head>
<body>
	<div align=center>
		<h1>Data Recorder</h1>
		<%
			String link = request.getParameter("link");
			String main = request.getParameter("main");
			String connector = request.getParameter("connector");
			String subject = request.getParameter("subject");

			AIRecorder air = (AIRecorder) session.getAttribute("air");
			if (air == null) {
				session.setAttribute("air", new AIRecorder());
			}

			try {
				if (!link.isEmpty())
					air.input(link, main, connector);

				if (!subject.isEmpty()) {
					String output = air.output(subject);
					output.replaceAll("\n", "<br>");
					out.println("<span>" + output + "</span><br>");
				}
			} catch (Exception e) {
				out.println("<span>" + e.getMessage() + "</span><br>");
			}
		%>
		<br>
		<%@include file="script.jsp"%>
		<br>
		<form method="post" action="index.jsp">
			<table>
				<tr>
					<th>Input Data</th>
					<th>Output Data</th>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td><span>Link</span></td>
								<td><input type="text" name="link"></td>
							</tr>
							<tr>
								<td><span>Main</span></td>
								<td><input type="text" name="main"></td>
							</tr>
							<tr>
								<td><span>Connector</span></td>
								<td><input type="text" name="connector"></td>
							</tr>
						</table>
					</td>
					<td>
						<table>
							<tr>
								<td><span>Subject</span></td>
								<td><input type="text" name="subject"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<input type="submit" value="Submit">
		</form>
	</div>
</body>
</html>