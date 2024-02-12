<!-- 
	71425	DSSOF3	09/02/2024	-	Funzione di riepilogo pallet, con highlight a pallettizzazione avvenuta.
 -->
<%@page import="java.util.Iterator"%>
<%@page import="it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnr"%>
<%@page import="java.util.List"%>
<%
List<DocumentoVenRigaPplCnr> scatole = (List<DocumentoVenRigaPplCnr>) request.getAttribute("RIEPILOGO");
%>
<html>
<body>
<div>
	<%
	Iterator<DocumentoVenRigaPplCnr> iterScatole = scatole.iterator();
	while(iterScatole.hasNext()){
		DocumentoVenRigaPplCnr scatola = (DocumentoVenRigaPplCnr) iterScatole.next();
		String barcode = scatola.getIdAnnoImb() + "  " + scatola.getIdNumeroImb().substring(0,2) + scatola.getIdNumeroImb().substring(2);
		%>
			<span id="<%=barcode%>" style="font-weight:bold;"><%=barcode%></span><br>
		<%
	}
	%>
</div>
</body>
<script type="text/javascript">
/*
 * In questo pezzo vado a fare l'highlight della scatola appena sparata.
 * In modo che l'utente si renda conto di quello che ha appena sparato a discapito di quello che gia' c'era.
 * Quello che ho appena sparato e' contenuto in 'descReg'.
 */
if(parent.document.getElementById('descReg') != null && parent.document.getElementById('descReg') != undefined){
	var originalValue = parent.document.getElementById('descReg').innerHTML;
	var indexOfDot = originalValue.indexOf('.');
	var extractedValue = originalValue.substring(indexOfDot + 1);
	//Get all span elements in the document
	var spans = document.querySelectorAll('span');
	
	// Iterate over each span
	spans.forEach(function(span) {
	    // Check if the span's text content contains the desired value
	    if (span.textContent.includes(extractedValue)) {
	        // Apply a highlight style to the matching span
	        span.style.backgroundColor = '#8ef78e';
	    }
	});
}
</script>
</html>