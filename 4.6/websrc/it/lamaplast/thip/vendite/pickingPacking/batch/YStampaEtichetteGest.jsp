<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///K:/Thip/4.7.0/websrcsvil/dtd/xhtml1-transitional.dtd">
<html>
<!-- WIZGEN Therm 2.0.0 as Batch form - multiBrowserGen = true -->
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
  BODataCollector YStampaEtichetteGestBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm YStampaEtichetteGestForm =  
     new com.thera.thermfw.web.WebFormForBatchForm(request, response, "YStampaEtichetteGestForm", "YStampaEtichetteGest", "Arial,10", "it.lamaplast.thip.vendite.pickingPacking.batch.web.YStampaEtichetteGestFormActionAdapter", false, false, false, true, true, true, null, 0, true, "it/lamaplast/thip/vendite/pickingPacking/batch/YStampaEtichetteGest.js"); 
  YStampaEtichetteGestForm.setServletEnvironment(se); 
  YStampaEtichetteGestForm.setJSTypeList(jsList); 
  YStampaEtichetteGestForm.setHeader("it.thera.thip.cs.PantheraHeader.jsp"); 
  YStampaEtichetteGestForm.setFooter("com.thera.thermfw.common.Footer.jsp"); 
  ((WebFormForBatchForm)  YStampaEtichetteGestForm).setGenerateThReportId(true); 
  ((WebFormForBatchForm)  YStampaEtichetteGestForm).setGenerateSSDEnabled(true); 
  YStampaEtichetteGestForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = YStampaEtichetteGestForm.getMode(); 
  String key = YStampaEtichetteGestForm.getKey(); 
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
        YStampaEtichetteGestForm.outTraceInfo(getClass().getName()); 
        String collectorName = YStampaEtichetteGestForm.findBODataCollectorName(); 
				 YStampaEtichetteGestBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (YStampaEtichetteGestBODC instanceof WebDataCollector) 
            ((WebDataCollector)YStampaEtichetteGestBODC).setServletEnvironment(se); 
        YStampaEtichetteGestBODC.initialize("YStampaEtichetteGest", true, 0); 
        int rcBODC; 
        if (YStampaEtichetteGestBODC.getBo() instanceof BatchRunnable) 
          rcBODC = YStampaEtichetteGestBODC.initSecurityServices("RUN", mode, true, false, true); 
        else 
          rcBODC = YStampaEtichetteGestBODC.initSecurityServices(mode, true, true, true); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           YStampaEtichetteGestForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = YStampaEtichetteGestBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              YStampaEtichetteGestForm.setBODataCollector(YStampaEtichetteGestBODC); 
              YStampaEtichetteGestForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<title>Stampa etichette</title>
<% 
  WebToolBar myToolBarTB = new com.thera.thermfw.web.WebToolBar("myToolBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  myToolBarTB.setParent(YStampaEtichetteGestForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/com/thera/thermfw/batch/PrintRunnableMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>
<% 
  WebLink link_0 =  
   new com.thera.thermfw.web.WebLink(); 
 link_0.setHttpServletRequest(request); 
 link_0.setHRefAttribute("thermweb/css/thermGrid.css"); 
 link_0.setRelAttribute("STYLESHEET"); 
 link_0.setTypeAttribute("text/css"); 
  link_0.write(out); 
%>
<!--<link href="thermweb/css/thermGrid.css" rel="STYLESHEET" type="text/css">-->
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=YStampaEtichetteGestForm.getBodyOnBeforeUnload()%>" onload="<%=YStampaEtichetteGestForm.getBodyOnLoad()%>" onunload="<%=YStampaEtichetteGestForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   YStampaEtichetteGestForm.writeBodyStartElements(out); 
%> 

	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = YStampaEtichetteGestForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", YStampaEtichetteGestBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=YStampaEtichetteGestForm.getServlet()%>" method="post" name="myForm" style="height:100%"><%
  YStampaEtichetteGestForm.writeFormStartElements(out); 
%>

		<table cellpadding="2" cellspacing="2" width="100%">
			<tr>
				<td style="height: 0"><% myToolBarTB.writeChildren(out); %> 
</td>
			</tr>
			<tr>
				<td>
					<table width="100%">
						<tr valign="top">
							<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YStampaEtichetteGest", "Barcode", null); 
   label.setParent(YStampaEtichetteGestForm); 
%><label class="<%=label.getClassType()%>" for="Barcode"><%label.write(out);%></label><%}%></td>
						</tr>
						<tr>
							<td><% 
  WebTextInput YStampaEtichetteGestBarcode =  
     new com.thera.thermfw.web.WebTextInput("YStampaEtichetteGest", "Barcode"); 
  YStampaEtichetteGestBarcode.setParent(YStampaEtichetteGestForm); 
%>
<input class="<%=YStampaEtichetteGestBarcode.getClassType()%>" id="<%=YStampaEtichetteGestBarcode.getId()%>" maxlength="<%=YStampaEtichetteGestBarcode.getMaxLength()%>" name="<%=YStampaEtichetteGestBarcode.getName()%>" size="<%=YStampaEtichetteGestBarcode.getSize()%>"><% 
  YStampaEtichetteGestBarcode.write(out); 
%>
</td>
						</tr>
						<tr>
							<td><% 
  WebComboBox YStampaEtichetteGestStampa =  
     new com.thera.thermfw.web.WebComboBox("YStampaEtichetteGest", "Stampa", null); 
  YStampaEtichetteGestStampa.setParent(YStampaEtichetteGestForm); 
%>
<select id="<%=YStampaEtichetteGestStampa.getId()%>" name="<%=YStampaEtichetteGestStampa.getName()%>"><% 
  YStampaEtichetteGestStampa.write(out); 
%> 
</select></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td style="height: 0"><% 
  WebErrorList errorList = new com.thera.thermfw.web.WebErrorList(); 
  errorList.setParent(YStampaEtichetteGestForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>--></td>
			</tr>
		</table>
	<%
  YStampaEtichetteGestForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = YStampaEtichetteGestForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", YStampaEtichetteGestBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              YStampaEtichetteGestForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, YStampaEtichetteGestBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, YStampaEtichetteGestBODC.getErrorList().getErrors()); 
           if(YStampaEtichetteGestBODC.getConflict() != null) 
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
     if(YStampaEtichetteGestBODC != null && !YStampaEtichetteGestBODC.close(false)) 
        errors.addAll(0, YStampaEtichetteGestBODC.getErrorList().getErrors()); 
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
     String errorPage = YStampaEtichetteGestForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", YStampaEtichetteGestBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = YStampaEtichetteGestForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
