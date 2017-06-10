<%@ page import="com.gagarwa.ai.recorder.AIRecorder"%>

<script src="//d3js.org/d3.v4.min.js"></script>

<%
	AIRecorder air = (AIRecorder) session.getAttribute("air");
	String recorderData = air.getRecorder().serialize();
%>

<svg width=1000 height=800></svg>
<script>
	var svg = d3.select("svg");
	var width = svg.attr("width");
	var height = svg.attr("height");

	var graph =
<%=recorderData%>
	;

	var dlinks = svg.append("g").attr("class", "dlinks").selectAll("line").data(graph.dlinks)
			.enter().append("line");

	dlinks.attr("x1", function(d) {
		var node = getNode(d.source);
		return getX(node.group, node.radians);
	}).attr("y1", function(d) {
		var node = getNode(d.source);
		return getY(node.group, node.radians);
	}).attr("x2", function(d) {
		var node = getNode(d.target);
		return getX(node.group, node.radians);
	}).attr("y2", function(d) {
		var node = getNode(d.target);
		return getY(node.group, node.radians);
	});

	var links = svg.append("g").attr("class", "links").selectAll("line").data(graph.links).enter()
			.append("line");

	links.attr("x1", function(d) {
		var node = getNode(d.source);
		return getX(node.group, node.radians);
	}).attr("y1", function(d) {
		var node = getNode(d.source);
		return getY(node.group, node.radians);
	}).attr("x2", function(d) {
		var node = getNode(d.target);
		return getX(node.group, node.radians);
	}).attr("y2", function(d) {
		var node = getNode(d.target);
		return getY(node.group, node.radians);
	});

	var nodes = svg.append("g").attr("class", "nodes").selectAll("circle").data(graph.cells)
			.enter().append("circle");

	nodes.attr("cx", function(d) {
		return getX(d.group, d.radians);
	}).attr("cy", function(d) {
		return getY(d.group, d.radians);
	}).attr("r", 30);

	var text = svg.append("g").attr("class", "text").selectAll("text").data(graph.cells).enter()
			.append("text");

	text.attr("x", function(d) {
		return getX(d.group, d.radians);
	}).attr("y", function(d) {
		return getY(d.group, d.radians);
	}).text(function(d) {
		return d.name;
	});

	function getNode(name) {
		for (var i = 0; i < graph.cells.length; i++)
			if (graph.cells[i].name == name)
				return graph.cells[i];
		return 0;
	}

	function getX(group, radians) {
		return Math.cos(radians + group) * (group * 50) + (width / 2);
	}

	function getY(group, radians) {
		return Math.sin(radians + group) * (group * 50) + (height / 2);
	}
</script>