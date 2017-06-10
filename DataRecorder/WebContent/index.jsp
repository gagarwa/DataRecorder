<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.gagarwa.ai.recorder.AIRecorder"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Data Recorder</title>
<jsp:include page="style.jsp" />
</head>
<body>
	<div align=center>
		<h1>Data Recorder</h1>
		<%
			AIRecorder air = (AIRecorder) session.getAttribute("air");
			if (air == null) {
				session.setAttribute("air", new AIRecorder());
			}

			String output = "";
			String errors = "";

			String search = request.getParameter("search");
			String input = request.getParameter("input");
			String delete = request.getParameter("delete");

			try {
				if (search != null) {
					String subject = request.getParameter("subject");
					if (!subject.isEmpty())
						output = air.output(subject);
				}

				if (input != null) {
					String link = request.getParameter("link");
					String main = request.getParameter("main");
					String connector = request.getParameter("connector");

					if (!link.isEmpty())
						air.input(link, main, connector);
				}
			} catch (Exception e) {
				errors = e.getMessage();
			}
		%>
		<br>
		<table>
			<tr>
				<td><jsp:include page="script.jsp" /></td>
				<td>
					<form method="post" action="index.jsp">
						<table>
							<tr>
								<td><input type="text" name="subject"></td>
								<td><input type="submit" name="search" value="Search"></td>

							</tr>
						</table>
					</form> <br>
					<form method="post" action="index.jsp">
						<table>
							<tr>
								<td><span><b>Type</b></span></td>
								<td><select name="type">
										<option value="insert">Insert</option>
										<option value="equiv">Equivalence</option>
								</select></td>
							</tr>
							<tr>
								<td><span><b>Descriptor</b></span></td>
								<td><input type="text" name="link"></td>
							</tr>
							<tr>
								<td valign="top"><span>Data</span></td>
								<td><textarea cols=30 rows=2 name="linkData"></textarea></td>
							</tr>
							<tr>
								<td><span><b>Title</b></span></td>
								<td><input type="text" name="main"></td>
							</tr>
							<tr>
								<td valign="top"><span>Data</span></td>
								<td><textarea cols=30 rows=2 name="mainData"></textarea></td>
							</tr>
							<tr>
								<td><span><b>Connector</b></span></td>
								<td><input type="text" name="connector"></td>
							</tr>
							<tr>
								<td valign="top"><span>Data</span></td>
								<td><textarea cols=30 rows=2 name="connectorData"></textarea></td>
							</tr>
							<tr>
								<td />
								<td align=right><input type="submit" name="input"
									value="Input"> <input type="submit" name="delete"
									value="Delete"></td>
							</tr>
						</table>
					</form> <br>
					<form>
						<span><b>Output</b></span> <br>
						<textarea class="label" readonly cols=50 rows=10 name="errors"><%=output%></textarea>
					</form> <br>
					<form>
						<span><b>Errors</b></span> <br>
						<textarea class="label" readonly cols=50 rows=5 name="errors"><%=errors%></textarea>
					</form>
		</table>
	</div>
</body>
</html>