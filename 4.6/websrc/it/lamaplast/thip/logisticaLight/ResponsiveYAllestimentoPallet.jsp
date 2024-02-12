<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///K:/Thip/4.7.0/websrcsvil/dtd/xhtml1-transitional.dtd">
<html>
<!-- WIZGEN Therm 2.0.0 as Form - multiBrowserGen = true -->
<%=WebGenerator.writeRuntimeInfo()%>
<head>
<%@ page contentType="text/html; charset=Cp1252"%>
<%@ page import= " 
  java.sql.*, 
  java.util.*, 
  java.lang.reflect.*, 
  javax.naming.*, 
  com.thera.thermfw.common.*, 
  com.thera.thermfw.type.*, 
  com.thera.thermfw.web.*, 
  com.thera.thermfw.security.*, 
  com.thera.thermfw.base.*, 
  com.thera.thermfw.ad.*, 
  com.thera.thermfw.persist.*, 
  com.thera.thermfw.gui.cnr.*, 
  com.thera.thermfw.setting.*, 
  com.thera.thermfw.collector.*, 
  com.thera.thermfw.batch.web.*, 
  com.thera.thermfw.batch.*, 
  com.thera.thermfw.pref.* 
"%> 
<%
  ServletEnvironment se = (ServletEnvironment)Factory.createObject("com.thera.thermfw.web.ServletEnvironment"); 
  BODataCollector YAllestPalletBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm YAllestPalletForm =  
     new com.thera.thermfw.web.WebForm(request, response, "YAllestPalletForm", "YAllestPallet", null, "it.lamaplast.thip.logisticaLight.web.YAllestimentoPalletFormActionAdapter", false, false, false, false, true, true, null, 1, true, "it/lamaplast/thip/logisticaLight/YAllestimentoPallet.js"); 
  YAllestPalletForm.setServletEnvironment(se); 
  YAllestPalletForm.setJSTypeList(jsList); 
  YAllestPalletForm.setHeader(null); 
  YAllestPalletForm.setFooter(null); 
  YAllestPalletForm.setWebFormModifierClass("it.lamaplast.thip.logisticaLight.web.YAllestimentoPalletWebFormModifier"); 
  YAllestPalletForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = YAllestPalletForm.getMode(); 
  String key = YAllestPalletForm.getKey(); 
  String errorMessage; 
  boolean requestIsValid = false; 
  boolean leftIsKey = false; 
  boolean conflitPresent = false; 
  String leftClass = ""; 
  try 
  {
     se.initialize(request, response); 
     if(se.begin()) 
     { 
        YAllestPalletForm.outTraceInfo(getClass().getName()); 
        String collectorName = YAllestPalletForm.findBODataCollectorName(); 
                YAllestPalletBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (YAllestPalletBODC instanceof WebDataCollector) 
            ((WebDataCollector)YAllestPalletBODC).setServletEnvironment(se); 
        YAllestPalletBODC.initialize("YAllestPallet", true, 1); 
        YAllestPalletForm.setBODataCollector(YAllestPalletBODC); 
        int rcBODC = YAllestPalletForm.initSecurityServices(); 
        mode = YAllestPalletForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           YAllestPalletForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = YAllestPalletBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              YAllestPalletForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<meta charset="utf-8">
<meta content="IE=edge" http-equiv="X-UA-Compatible">
<meta content="width=device-width, initial-scale=1" name="viewport">
<% 
  WebLink link_0 =  
   new com.thera.thermfw.web.WebLink(); 
 link_0.setHttpServletRequest(request); 
 link_0.setHRefAttribute("it/thera/thip/logisticaLight/css/extra/dataTables.bootstrap.min.css"); 
 link_0.setRelAttribute("stylesheet"); 
 link_0.setTypeAttribute("text/css"); 
  link_0.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/extra/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css">-->
<!-- DataTables Responsive CSS -->
<% 
  WebLink link_1 =  
   new com.thera.thermfw.web.WebLink(); 
 link_1.setHttpServletRequest(request); 
 link_1.setHRefAttribute("it/thera/thip/logisticaLight/css/extra/responsive.dataTables.min.css"); 
 link_1.setRelAttribute("stylesheet"); 
 link_1.setTypeAttribute("text/css"); 
  link_1.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/extra/responsive.dataTables.min.css" rel="stylesheet" type="text/css">-->
<% 
  WebLink link_2 =  
   new com.thera.thermfw.web.WebLink(); 
 link_2.setHttpServletRequest(request); 
 link_2.setHRefAttribute("it/thera/thip/logisticaLight/css/extra/buttons.dataTables.min.css"); 
 link_2.setRelAttribute("stylesheet"); 
 link_2.setTypeAttribute("text/css"); 
  link_2.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/extra/buttons.dataTables.min.css" rel="stylesheet" type="text/css">-->
<% 
  WebLink link_3 =  
   new com.thera.thermfw.web.WebLink(); 
 link_3.setHttpServletRequest(request); 
 link_3.setHRefAttribute("it/thera/thip/logisticaLight/css/extra/datatables.min.css"); 
 link_3.setRelAttribute("stylesheet"); 
 link_3.setTypeAttribute("text/css"); 
  link_3.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/extra/datatables.min.css" rel="stylesheet" type="text/css">-->
<!-- Bootstrap Core CSS -->
<% 
  WebLink link_4 =  
   new com.thera.thermfw.web.WebLink(); 
 link_4.setHttpServletRequest(request); 
 link_4.setHRefAttribute("it/thera/thip/logisticaLight/css/bootstrap/dist/css/bootstrap.min.css"); 
 link_4.setRelAttribute("stylesheet"); 
 link_4.setTypeAttribute("text/css"); 
  link_4.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">-->
<!-- Custom CSS -->
<% 
  WebLink link_5 =  
   new com.thera.thermfw.web.WebLink(); 
 link_5.setHttpServletRequest(request); 
 link_5.setHRefAttribute("it/thera/thip/logisticaLight/css/sb-admin-2.css"); 
 link_5.setRelAttribute("stylesheet"); 
 link_5.setTypeAttribute("text/css"); 
  link_5.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/sb-admin-2.css" rel="stylesheet" type="text/css">-->
<!-- Theme style -->
<% 
  WebLink link_6 =  
   new com.thera.thermfw.web.WebLink(); 
 link_6.setHttpServletRequest(request); 
 link_6.setHRefAttribute("it/thera/thip/logisticaLight/css/AdminLTE.min.css"); 
 link_6.setRelAttribute("stylesheet"); 
 link_6.setTypeAttribute("text/css"); 
  link_6.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/AdminLTE.min.css" rel="stylesheet" type="text/css">-->
<!-- JS -->
<% 
  WebScript script_0 =  
   new com.thera.thermfw.web.WebScript(); 
 script_0.setRequest(request); 
 script_0.setSrcAttribute("it/thera/thip/logisticaLight/ResponsiveSpedizioneComponenti.js"); 
 script_0.setLanguageAttribute(null); 
  script_0.write(out); 
%>
<!--<script src="it/thera/thip/logisticaLight/ResponsiveSpedizioneComponenti.js"></script>-->
<% 
  WebScript script_1 =  
   new com.thera.thermfw.web.WebScript(); 
 script_1.setRequest(request); 
 script_1.setSrcAttribute("it/thera/thip/logisticaLight/js/extra/jquery.min.js"); 
 script_1.setLanguageAttribute(null); 
  script_1.write(out); 
%>
<!--<script src="it/thera/thip/logisticaLight/js/extra/jquery.min.js"></script>-->
<% 
  WebScript script_2 =  
   new com.thera.thermfw.web.WebScript(); 
 script_2.setRequest(request); 
 script_2.setSrcAttribute("it/thera/thip/logisticaLight/js/extra/bootstrap.min.js"); 
 script_2.setLanguageAttribute(null); 
  script_2.write(out); 
%>
<!--<script src="it/thera/thip/logisticaLight/js/extra/bootstrap.min.js"></script>-->
<!-- Custom Fonts -->
<% 
  WebLink link_7 =  
   new com.thera.thermfw.web.WebLink(); 
 link_7.setHttpServletRequest(request); 
 link_7.setHRefAttribute("it/thera/thip/logisticaLight/css/font-awesome/css/font-awesome.min.css"); 
 link_7.setRelAttribute("stylesheet"); 
 link_7.setTypeAttribute("text/css"); 
  link_7.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">-->
<style type="text/css">
#sfondo {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: -1;
}

.container {
	padding-left: 0px;
	padding-right: 0px
}

form {
	background: rgb(232, 232, 232);
	height: 50%;
}

body {
	background-color: #FFFFFF;
	height: 100%;
	width: 100%;
	margin: 0;
	padding: 0;
}

a {
	color: #FFFFFF;
	text-decoration: none;
	display: inline-block;
	padding: 8px 16px;
}

a:hover {
	background-color: #ddd;
	color: black;
}

.previous {
	background-color: #f1f1f1;
	color: black;
}

.next {
	background-color: #f1f1f1;
	color: white;
}

.round {
	border-radius: 50%;
}

.icon-bar {
	width: 100%;
	/* Full-width */
	background-color: #555;
	/* Dark-grey background */
	overflow: auto;
	/* Overflow due to float */
}

.icon-bar a {
	float: left;
	/* Float links side by side */
	text-align: center;
	/* Center-align text */
	width: 20%;
	/* Equal width (5 icons with 20% width each = 100%) */
	padding: 12px 0;
	/* Some top and bottom padding */
	transition: all 0.3s ease;
	/* Add transition for hover effects */
	color: white;
	/* White text color */
	font-size: 36px;
	/* Increased font size */
}

.icon-bar a:hover {
	background-color: #000;
	/* Add a hover color */
}

.active {
	background-color: #4CAF50;
	/* Add an active/current color */
}

.panel-heading {
	font-size: 11pt;
}

td {
	/* 			border: 1px solid black; */
	padding: 3px;
}

.btn {
	background-color: #666666;
	color: #FFFFFF;
	font-family: Arial, Helvetica, sans-serif;
	padding: 0px;
	font-size: 15pt;
	cursor: pointer;
	font-weight: bolder;
	border-radius: 5px;
	height: fit-content;
	width: fit-content;
}
</style>
</head>
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=YAllestPalletForm.getBodyOnBeforeUnload()%>" onload="<%=YAllestPalletForm.getBodyOnLoad()%>" onunload="<%=YAllestPalletForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   YAllestPalletForm.writeBodyStartElements(out); 
%> 

	<div class="navbar-fixed-top" id="menubarResp">
		<!--FIX 38988-->
		<div class="container">
			<nav class="navbar navbar-default" style="margin-bottom: 0px">
				<div class="container-fluid">
					<div class="navbar-header">
						<button class="navbar-toggle collapsed" data-target="#navbar2" data-toggle="collapse" type="button">
							<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
						</button>
						<a class="navbar-brand" href="javascript:tornaAlPaginaMenu()"><img src="it/thera/thip/cs/images/LogoPanteraHeader.gif"> </a>
					</div>
					<div class="navbar-collapse collapse" id="navbar2">
						<ul class="nav navbar-nav navbar-right" style="font-size: 11pt">
							<li><a href="javascript:tornaAlPaginaMenu()">Home</a>
							<li><a href="it/thera/thip/logisticaLight/Login2.jsp">Esci</a>
						</ul>
					</div>
					<!--/.nav-collapse -->
				</div>
				<!--/.container-fluid -->
			</nav>
		</div>
		<div class="container">
			<h1 class="alert bg-aqua" style="padding: 0px; margin: 0px;">
				<!--FIX 38988 - inizio-->
				<!-- <a onclick="goBack()"><i class="fa fa-backward"></i></a>     <a onclick="menuForm()"><i class="fa fa-home"></i></a>     <a onclick="javascript:goNext()"><i class="fa fa-forward"></i></a> -->
				<a onclick="indietroSpecifica()"><i class="fa fa-backward"></i></a> <a onclick="javascript:tornaAlPaginaMenu()"><i class="fa fa-home"></i></a> <a onclick="indietroSpecifica()"><i class="fa fa-forward"></i></a>
				<!--FIX 38988 - fine-->
			</h1>
		</div>
	</div>
	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = YAllestPalletForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", YAllestPalletBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=YAllestPalletForm.getServlet()%>" id="form" method="post" name="form" style="height:100%"><%
  YAllestPalletForm.writeFormStartElements(out); 
%>

		<div class="container" id="tab1" style="margin-top:5rem;">
			<table cellpadding="2" cellspacing="0" height="25%" id="emptyborder">
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "BarcodeDocumento", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="BarcodeDocumento"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletBarcodeDocumento =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "BarcodeDocumento"); 
  YAllestPalletBarcodeDocumento.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletBarcodeDocumento.getClassType()%>" style="width:200px" id="<%=YAllestPalletBarcodeDocumento.getId()%>" maxlength="<%=YAllestPalletBarcodeDocumento.getMaxLength()%>" name="<%=YAllestPalletBarcodeDocumento.getName()%>" size="<%=YAllestPalletBarcodeDocumento.getSize()%>"><% 
  YAllestPalletBarcodeDocumento.write(out); 
%>
</td>
					<td><a onclick="indietroDaBarcode()"><i aria-hidden="true" class="fa fa-arrow-circle-o-up fa-2x" style="color:black;"></i></a></td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "BarcodePallet", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="BarcodePallet"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletBarcodePallet =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "BarcodePallet"); 
  YAllestPalletBarcodePallet.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletBarcodePallet.getClassType()%>" style="width:200px" id="<%=YAllestPalletBarcodePallet.getId()%>" maxlength="<%=YAllestPalletBarcodePallet.getMaxLength()%>" name="<%=YAllestPalletBarcodePallet.getName()%>" size="<%=YAllestPalletBarcodePallet.getSize()%>"><% 
  YAllestPalletBarcodePallet.write(out); 
%>
</td>
					<td><a onclick="datiTecniciPallet()"><i aria-hidden="true" class="fa fa-pencil fa-2x" style="color:black;"></i></a></td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "BarcodeScatola", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="BarcodeScatola"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletBarcodeScatola =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "BarcodeScatola"); 
  YAllestPalletBarcodeScatola.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletBarcodeScatola.getClassType()%>"  style="width:200px" id="<%=YAllestPalletBarcodeScatola.getId()%>" maxlength="<%=YAllestPalletBarcodeScatola.getMaxLength()%>" name="<%=YAllestPalletBarcodeScatola.getName()%>" size="<%=YAllestPalletBarcodeScatola.getSize()%>"><% 
  YAllestPalletBarcodeScatola.write(out); 
%>
</td>
					<td><a onclick="datiTecniciScatola()"><i aria-hidden="true" class="fa fa-pencil fa-2x" style="color:black;"></i></a></td>
				</tr>
				<!-- 71425 DSSOF3 Aggiungere frame per visualizzare all'utente il riepilogo dell'allestimento -->
				<tr height="100%">
					<td></td>
					<td colspan="2" height="100%" valign="top"><iframe frameborder="0" id="FrameRiepilogoPallet" marginheight="0" marginwidth="0" name="FrameRiepilogoPallet" src=""> </iframe></td>
				</tr>
				<input type="hidden" id="prelevataScatola">
				<input type="hidden" id="residuoRiga">
				<!-- 71425 DSSOF3 Fine aggiunta -->
				<tr height="100%">
					<td colspan="2" height="100%" valign="top"><iframe class="FrameBackgroundPrlVenDoc" frameborder="0" id="GrigliaDocumentiFrame" marginheight="0" marginwidth="0" name="GrigliaDocumentiFrame" src> </iframe></td>
				</tr>
			</table>
		</div>
		<div class="container" id="tabMisure" style="margin-top:5rem;display:none">
			<table cellpadding="2" cellspacing="0" height="25%">
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "BarcodeMisure", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="BarcodeMisure"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletBarcodeMisure =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "BarcodeMisure"); 
  YAllestPalletBarcodeMisure.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletBarcodeMisure.getClassType()%>" id="<%=YAllestPalletBarcodeMisure.getId()%>" maxlength="<%=YAllestPalletBarcodeMisure.getMaxLength()%>" name="<%=YAllestPalletBarcodeMisure.getName()%>" size="<%=YAllestPalletBarcodeMisure.getSize()%>"><% 
  YAllestPalletBarcodeMisure.write(out); 
%>
</td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "PesoNetto", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="PesoNetto"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletPesoNetto =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "PesoNetto"); 
  YAllestPalletPesoNetto.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletPesoNetto.getClassType()%>" id="<%=YAllestPalletPesoNetto.getId()%>" maxlength="<%=YAllestPalletPesoNetto.getMaxLength()%>" name="<%=YAllestPalletPesoNetto.getName()%>" size="<%=YAllestPalletPesoNetto.getSize()%>"><% 
  YAllestPalletPesoNetto.write(out); 
%>
</td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "TaraContenitore", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="TaraContenitore"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletTaraContenitore =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "TaraContenitore"); 
  YAllestPalletTaraContenitore.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletTaraContenitore.getClassType()%>" id="<%=YAllestPalletTaraContenitore.getId()%>" maxlength="<%=YAllestPalletTaraContenitore.getMaxLength()%>" name="<%=YAllestPalletTaraContenitore.getName()%>" size="<%=YAllestPalletTaraContenitore.getSize()%>"><% 
  YAllestPalletTaraContenitore.write(out); 
%>
</td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "PesoLordo", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="PesoLordo"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletPesoLordo =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "PesoLordo"); 
  YAllestPalletPesoLordo.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletPesoLordo.getClassType()%>" id="<%=YAllestPalletPesoLordo.getId()%>" maxlength="<%=YAllestPalletPesoLordo.getMaxLength()%>" name="<%=YAllestPalletPesoLordo.getName()%>" size="<%=YAllestPalletPesoLordo.getSize()%>"><% 
  YAllestPalletPesoLordo.write(out); 
%>
</td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "Lunghezza", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="Lunghezza"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletLunghezza =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "Lunghezza"); 
  YAllestPalletLunghezza.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletLunghezza.getClassType()%>" id="<%=YAllestPalletLunghezza.getId()%>" maxlength="<%=YAllestPalletLunghezza.getMaxLength()%>" name="<%=YAllestPalletLunghezza.getName()%>" size="<%=YAllestPalletLunghezza.getSize()%>"><% 
  YAllestPalletLunghezza.write(out); 
%>
</td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "Larghezza", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="Larghezza"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletLarghezza =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "Larghezza"); 
  YAllestPalletLarghezza.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletLarghezza.getClassType()%>" id="<%=YAllestPalletLarghezza.getId()%>" maxlength="<%=YAllestPalletLarghezza.getMaxLength()%>" name="<%=YAllestPalletLarghezza.getName()%>" size="<%=YAllestPalletLarghezza.getSize()%>"><% 
  YAllestPalletLarghezza.write(out); 
%>
</td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "Altezza", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="Altezza"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletAltezza =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "Altezza"); 
  YAllestPalletAltezza.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletAltezza.getClassType()%>" id="<%=YAllestPalletAltezza.getId()%>" maxlength="<%=YAllestPalletAltezza.getMaxLength()%>" name="<%=YAllestPalletAltezza.getName()%>" size="<%=YAllestPalletAltezza.getSize()%>"><% 
  YAllestPalletAltezza.write(out); 
%>
</td>
				</tr>
				<tr valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YAllestPallet", "Volume", null); 
   label.setParent(YAllestPalletForm); 
%><label class="<%=label.getClassType()%>" for="Volume"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YAllestPalletVolume =  
     new com.thera.thermfw.web.WebTextInput("YAllestPallet", "Volume"); 
  YAllestPalletVolume.setParent(YAllestPalletForm); 
%>
<input class="<%=YAllestPalletVolume.getClassType()%>" id="<%=YAllestPalletVolume.getId()%>" maxlength="<%=YAllestPalletVolume.getMaxLength()%>" name="<%=YAllestPalletVolume.getName()%>" size="<%=YAllestPalletVolume.getSize()%>"><% 
  YAllestPalletVolume.write(out); 
%>
</td>
				</tr>
				
				<tr height="100%">
					<td colspan="2" height="100%" valign="top"><iframe class="FrameBackgroundPrlVenDoc" frameborder="0" id="MisureErrorFrame" marginheight="0" marginwidth="0" name="MisureErrorFrame" src> </iframe></td>
				</tr>
			</table>
		</div>
	<%
  YAllestPalletForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = YAllestPalletForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", YAllestPalletBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              YAllestPalletForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, YAllestPalletBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, YAllestPalletBODC.getErrorList().getErrors()); 
           if(YAllestPalletBODC.getConflict() != null) 
                conflitPresent = true; 
     } 
     else 
        errors.add(new ErrorMessage("BAS0000010")); 
  } 
  catch(NamingException e) { 
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("CBS000025", errorMessage));  } 
  catch(SQLException e) {
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("BAS0000071", errorMessage));  } 
  catch(Throwable e) {
     e.printStackTrace(Trace.excStream);
  }
  finally 
  {
     if(YAllestPalletBODC != null && !YAllestPalletBODC.close(false)) 
        errors.addAll(0, YAllestPalletBODC.getErrorList().getErrors()); 
     try 
     { 
        se.end(); 
     }
     catch(IllegalArgumentException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
     catch(SQLException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
  } 
  if(!errors.isEmpty())
  { 
      if(!conflitPresent)
  { 
     request.setAttribute("ErrorMessages", errors); 
     String errorPage = YAllestPalletForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", YAllestPalletBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = YAllestPalletForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
