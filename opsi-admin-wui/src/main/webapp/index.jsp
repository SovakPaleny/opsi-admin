<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

Package pkg = cz.muni.ucn.opsi.api.client.Client.class.getPackage();
StringBuilder sb = new StringBuilder();
if (pkg.getImplementationTitle() != null) {
        sb.append(pkg.getImplementationTitle());
        sb.append("-");
        sb.append(pkg.getSpecificationVersion());
        sb.append(" (");
        sb.append(pkg.getImplementationVersion());
        sb.append(")");
} else {
		sb.append("development");
}
pageContext.setAttribute("currentVersion", sb.toString());

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<title>OPSI Admin</title>
<meta name="gwt:property" content="locale=cs"/>
<link rel="stylesheet" type="text/css" href="gxt/css/gxt-all.css" />
<link rel="stylesheet" type="text/css" href="gxt/desktop/css/desktop.css" />
<!--
<link rel="stylesheet" type="text/css" href="samples/css/resources.css" />
<link rel="stylesheet" type="text/css" href="samples/desktop/css/desktopapp.css" />
 -->
<style>
#loading {
  position: absolute;
  left: 40%;
  top: 40%;
  margin-left: auto;
  margin-right: auto;
  padding: 2px;
  z-index: 20001;
  height: auto;
  border: 1px solid #ccc;
  width: 300px;
}

#loading a {
  color: #225588;
}

#loading .loading-indicator {
  background: white;
  color: #444;
  font: bold 13px tahoma, arial, helvetica;
  padding: 10px;
  margin: 0;
  height: auto;
}

#loading .loading-indicator img {
  margin-right:8px;
  float:left;
  vertical-align:top;
}

#loading-msg {
  font: normal 10px arial, tahoma, sans-serif;
}

.search-item {
    font:normal 11px tahoma, arial, helvetica, sans-serif;
    padding:3px 10px 3px 10px;
    border:1px solid #fff;
    border-bottom:1px solid #eeeeee;
    white-space:normal;
    color:#555;
}
.search-item h3 {
    display:block;
    font:inherit;
    font-weight:bold;
    color:#222;
}

.search-item h3 span {
    float: right;
    font-weight:normal;
    margin:0 0 5px 5px;
    width:100px;
    display:block;
    clear:none;
}
</style>
</head>
<body style="overflow: hidden">

<div id="loading">
    <div class="loading-indicator">
    <img src="gxt/images/default/shared/large-loading.gif" width="32" height="32"/>OPSI Admin<br />
    <span id="loading-msg"><% out.print(pageContext.getAttribute("currentVersion")); %><br/>
    	Probíhá&nbsp;načítání&nbsp;aplikace...</span>
    </div>
</div>

<script language='javascript' src='cz.muni.ucn.opsi.wui.gwt.OpsiAdminApp/cz.muni.ucn.opsi.wui.gwt.OpsiAdminApp.nocache.js'></script>
<iframe src="javascript:''" id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>
<div id="x-desktop">
<img src="images/u2_banner.png" style="float:right;margin-top: 30px; margin-right: 30px;"/>
    <dl id="x-shortcuts">

    </dl>
</div>

</body>
</html>
