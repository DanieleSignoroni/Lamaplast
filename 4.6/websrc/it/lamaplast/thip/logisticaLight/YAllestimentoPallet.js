function YAllestPalletOL() {
	document.getElementById('BarcodeDocumento').focus();
	document.getElementById('BarcodePallet').readOnly = true;
	document.getElementById('BarcodeScatola').readOnly = true;
	document.getElementById('BarcodeDocumento').addEventListener("keydown", ricercaDocumento);
	document.getElementById('BarcodePallet').addEventListener("keydown", ricercaPallet);
	document.getElementById('BarcodeScatola').addEventListener("keydown", ricarcaScatola);
	document.getElementById('PesoNetto').addEventListener("change", cambioPesoNetto);
	document.getElementById('PesoLordo').addEventListener("change", cambioPesoLordo);
	document.getElementById('TaraContenitore').addEventListener("change", cambioTaraContenitore);
	document.getElementById('Volume').addEventListener("change", cambioVolume);
	document.getElementById('Altezza').addEventListener("change", cambioAltezza);
	document.getElementById('Lunghezza').addEventListener("change", cambioLunghezza);
	document.getElementById('Larghezza').addEventListener("change", cambioLarghezza);
	//	appendEvent(document.getElementById("BarcodeDocumento"), eventKEYDOWN, function() {
	//		document.getElementById("BarcodeDocumento").onkeydown; return ricercaDocumento();
	//	}
	//	);
	//	appendEvent(document.getElementById("BarcodeDocumento"), eventCHANGE, function() {
	//		document.getElementById("BarcodeDocumento").onchange; return ricercaDocumento();
	//	}
	//	);
	//	appendEvent(document.getElementById("BarcodePallet"), eventKEYDOWN, function() {
	//		document.getElementById("BarcodePallet").onkeydown; return ricercaPallet();
	//	}
	//	);
	//	appendEvent(document.getElementById("BarcodePallet"), eventCHANGE, function() {
	//		document.getElementById("BarcodePallet").onchange; return ricercaPallet();
	//	}
	//	);
	//	appendEvent(document.getElementById("BarcodeScatola"), eventKEYDOWN, function() {
	//		document.getElementById("BarcodeScatola").onkeydown; return ricarcaScatola();
	//	}
	//	);
	//	appendEvent(document.getElementById("BarcodeScatola"), eventCHANGE, function() {
	//		document.getElementById("BarcodeScatola").onchange; return ricarcaScatola();
	//	}
	//	);
}

function ricarcaScatola(evt) {
	if (evt.keyCode == 13) {
		if (!document.getElementById('BarcodeScatola').hasAttribute('readonly')) {
			var barcode = document.getElementById("BarcodeScatola").value;
			var barcodePallet = document.getElementById("BarcodePallet").value;
			var barcodeDocumento = document.getElementById("BarcodeDocumento").value;
			if (barcode != null && barcode != undefined && barcode.length > 15) {
				var f = document.getElementById('GrigliaDocumentiFrame').contentWindow;
				var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YControlloBarcodeScatolaAllesimentoPallet";
				url += "?BarcodeScatola=" + upperCaseValue(barcode);
				url += "&BarcodeDocumento=" + upperCaseValue(barcodeDocumento);
				url += "&BarcodePallet=" + upperCaseValue(barcodePallet);
				setLocationOnWindow(f, url);
			}
		}
	}
}

function ricercaPallet(evt) {
	if (evt.keyCode == 13) {
		if (!document.getElementById('BarcodePallet').hasAttribute('readonly')) {
			var barcode = document.getElementById("BarcodePallet").value;
			var barcodeDoc = document.getElementById("BarcodeDocumento").value;
			if (barcode != null && barcode != undefined && barcode.length > 15) {
				var f = document.getElementById('GrigliaDocumentiFrame').contentWindow;
				setLocationOnWindow(f, "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YControlloBarcodePalletAllesimentoPallet?BarcodePallet=" + upperCaseValue(barcode) + "&BarcodeDocumento=" + barcodeDoc);
			}
		}
	}
}

function ricercaDocumento(evt) {
	if (evt.keyCode == 13) {
		if (!document.getElementById('BarcodeDocumento').hasAttribute('readonly')) {
			var barcode = document.getElementById("BarcodeDocumento").value;
			if (barcode != null && barcode != undefined && barcode.length >= 15) {
				var f = document.getElementById('GrigliaDocumentiFrame').contentWindow;
				setLocationOnWindow(f, "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YControlloBarcodeDocAllesimentoPallet?BarcodeDocumento=" + upperCaseValue(barcode));
			}
		}
	}
}

function indietroSpecifica() {
	if (document.getElementById('BarcodeMisure').hasAttribute('readonly')) {
		document.getElementById('tabMisure').style.display = 'none';
		document.getElementById('tab1').style.display = 'revert';
	}
}

function indietroDaBarcode() {
	if (!document.getElementById('BarcodeDocumento').hasAttribute('readonly')) {
		//se sono all'inserimento barcode e schiacciano indietro torno al menu'
		tornaAlPaginaMenu();
	} else {
		if (document.getElementById('BarcodeScatola').hasAttribute('readonly')
			&& !document.getElementById('BarcodePallet').hasAttribute('readonly')) {
			//sono indietro da pallet
			document.getElementById('BarcodePallet').setAttribute('readonly', true);
			document.getElementById('desc1').remove();
			if (document.getElementById('desc2') != null)
				document.getElementById('desc2').remove();
			document.getElementById('BarcodeDocumento').value = '';
			document.getElementById('BarcodeDocumento').removeAttribute('readonly');
			document.getElementById('BarcodeDocumento').focus();
		} else if (!document.getElementById('BarcodeScatola').hasAttribute('readonly')) {
			document.getElementById('BarcodeScatola').setAttribute('readonly', true);
			if (document.getElementById('rimuovimi') != null)
				document.getElementById('rimuovimi').remove();
			document.getElementById('BarcodePallet').value = '';
			document.getElementById('BarcodePallet').removeAttribute('readonly');
			document.getElementById('BarcodePallet').focus();
		}
	}
}

function datiTecniciPallet() {
	if (document.getElementById('BarcodePallet').value != '') {
		var barcode = document.getElementById("BarcodePallet").value;
		if (barcode != null && barcode != undefined && barcode.length > 15) {
			document.getElementById('BarcodeMisure').value = barcode;
			document.getElementById('BarcodeMisure').setAttribute('readonly', true);
			document.getElementById('tab1').style.display = 'none';
			document.getElementById('tabMisure').style.display = 'revert';
			var f = document.getElementById('MisureErrorFrame').contentWindow;
			var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YRecuperaMisure";
			setLocationOnWindow(f, url + "?Barcode=" + upperCaseValue(barcode) + "&IdLivello=2");
		}
	}
}

function datiTecniciScatola() {
	var barcode = document.getElementById("BarcodeScatola").value;
	if (barcode != null && barcode != undefined && barcode.length > 15) {
		if (document.getElementById('descReg') != null) {
			document.getElementById('BarcodeMisure').value = barcode;
			document.getElementById('BarcodeMisure').setAttribute('readonly', true);
			document.getElementById('tab1').style.display = 'none';
			document.getElementById('tabMisure').style.display = 'revert';
			var f = document.getElementById('MisureErrorFrame').contentWindow;
			var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YRecuperaMisure";
			setLocationOnWindow(f, url + "?Barcode=" + upperCaseValue(barcode) + "&IdLivello=1");
		}
	} else if (document.getElementById('descReg') != null) {
		var inputString = document.getElementById('descReg').innerHTML;
		const dotIndex = inputString.indexOf(".");
		if (dotIndex !== -1)
			barcode = inputString.substring(dotIndex + 1);
		document.getElementById('BarcodeMisure').value = barcode;
		document.getElementById('BarcodeMisure').setAttribute('readonly', true);
		document.getElementById('tab1').style.display = 'none';
		document.getElementById('tabMisure').style.display = 'revert';
		var f = document.getElementById('MisureErrorFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YRecuperaMisure";
		setLocationOnWindow(f, url + "?Barcode=" + upperCaseValue(barcode) + "&IdLivello=1");
	}
}

function cambioPesoNetto() {
	var barcode = document.getElementById("BarcodeMisure").value;
	if (barcode != null && barcode != undefined && barcode.length > 15) {
		var pesoNetto = 0.00;
		var pesoLordo = 0.00;
		var tara = 0.00;
		var volume = 0;
		var altezza = 0;
		var lunghezza = 0;
		var larghezza = 0;
		if (getFieldValueDecimal('PesoNetto') != null) {
			pesoNetto = getFieldValueDecimal('PesoNetto');
		}
		if (getFieldValueDecimal('PesoLordo') != null) {
			pesoLordo = getFieldValueDecimal('PesoLordo');
		}
		if (getFieldValueDecimal('TaraContenitore') != null) {
			tara = getFieldValueDecimal('TaraContenitore');
		}
		var f = document.getElementById('MisureErrorFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YInserimentoDatiTecniciAllestimento";
		url += "?Barcode=" + upperCaseValue(barcode);
		if (barcode.includes("PL")) {
			url += "&IdLivello=2";
		} else {
			url += "&IdLivello=1";
		}
		url += "&PesoNetto=" + pesoNetto;
		url += "&PesoLordo=" + pesoLordo;
		url += "&TaraContenitore=" + tara;
		url += "&IsChangedPesoNetto=true";
		url += "&IsChangedPesoLordo=false";
		url += "&IsChangedTara=false";
		setLocationOnWindow(f, url);
	}
}

function cambioPesoLordo() {
	var barcode = document.getElementById("BarcodeMisure").value;
	if (barcode != null && barcode != undefined && barcode.length > 15) {
		var pesoNetto = 0.00;
		var pesoLordo = 0.00;
		var tara = 0.00;
		var volume = 0;
		var altezza = 0;
		var lunghezza = 0;
		var larghezza = 0;
		if (getFieldValueDecimal('PesoNetto') != null) {
			pesoNetto = getFieldValueDecimal('PesoNetto');
		}
		if (getFieldValueDecimal('PesoLordo') != null) {
			pesoLordo = getFieldValueDecimal('PesoLordo');
		}
		if (getFieldValueDecimal('TaraContenitore') != null) {
			tara = getFieldValueDecimal('TaraContenitore');
		}
		var f = document.getElementById('MisureErrorFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YInserimentoDatiTecniciAllestimento";
		url += "?Barcode=" + upperCaseValue(barcode);
		if (barcode.includes("PL")) {
			url += "&IdLivello=2";
		} else {
			url += "&IdLivello=1";
		}
		url += "&PesoNetto=" + pesoNetto;
		url += "&PesoLordo=" + pesoLordo;
		url += "&TaraContenitore=" + tara;
		url += "&IsChangedPesoNetto=false";
		url += "&IsChangedPesoLordo=true";
		url += "&IsChangedTara=false";
		setLocationOnWindow(f, url);
	}
}

function cambioTaraContenitore() {
	var barcode = document.getElementById("BarcodeMisure").value;
	if (barcode != null && barcode != undefined && barcode.length > 15) {
		var pesoNetto = 0.00;
		var pesoLordo = 0.00;
		var tara = 0.00;
		var volume = 0;
		var altezza = 0;
		var lunghezza = 0;
		var larghezza = 0;
		if (getFieldValueDecimal('PesoNetto') != null) {
			pesoNetto = getFieldValueDecimal('PesoNetto');
		}
		if (getFieldValueDecimal('PesoLordo') != null) {
			pesoLordo = getFieldValueDecimal('PesoLordo');
		}
		if (getFieldValueDecimal('TaraContenitore') != null) {
			tara = getFieldValueDecimal('TaraContenitore');
		}
		var f = document.getElementById('MisureErrorFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YInserimentoDatiTecniciAllestimento";
		url += "?Barcode=" + upperCaseValue(barcode);
		if (barcode.includes("PL")) {
			url += "&IdLivello=2";
		} else {
			url += "&IdLivello=1";
		}
		url += "&PesoNetto=" + pesoNetto;
		url += "&PesoLordo=" + pesoLordo;
		url += "&TaraContenitore=" + tara;
		url += "&IsChangedPesoNetto=false";
		url += "&IsChangedPesoLordo=false";
		url += "&IsChangedTara=true";
		setLocationOnWindow(f, url);
	}
}

function cambioVolume() {
	var barcode = document.getElementById("BarcodeMisure").value;
	if (barcode != null && barcode != undefined && barcode.length > 15) {
		var volume = 0;
		if (getFieldValueDecimal('Volume') != null) {
			volume = getFieldValueDecimal('Volume');
		}
		var f = document.getElementById('MisureErrorFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YInserimentoDatiTecniciAllestimento";
		url += "?Barcode=" + upperCaseValue(barcode);
		if (barcode.includes("PL")) {
			url += "&IdLivello=2";
		} else {
			url += "&IdLivello=1";
		}
		url += "&Volume=" + volume;
		url += "&IsChangedVolume=true";
		setLocationOnWindow(f, url);
	}
}

function cambioAltezza() {
	var barcode = document.getElementById("BarcodeMisure").value;
	if (barcode != null && barcode != undefined && barcode.length > 15) {
		var volume = 0;
		if (getFieldValueDecimal('Altezza') != null) {
			volume = getFieldValueDecimal('Altezza');
		}
		var f = document.getElementById('MisureErrorFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YInserimentoDatiTecniciAllestimento";
		url += "?Barcode=" + upperCaseValue(barcode);
		if (barcode.includes("PL")) {
			url += "&IdLivello=2";
		} else {
			url += "&IdLivello=1";
		}
		url += "&Altezza=" + volume;
		url += "&IsChangedAltezza=true";
		setLocationOnWindow(f, url);
	}
}

function cambioLarghezza() {
	var barcode = document.getElementById("BarcodeMisure").value;
	if (barcode != null && barcode != undefined && barcode.length > 15) {
		var volume = 0;
		if (getFieldValueDecimal('Larghezza') != null) {
			volume = getFieldValueDecimal('Larghezza');
		}
		var f = document.getElementById('MisureErrorFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YInserimentoDatiTecniciAllestimento";
		url += "?Barcode=" + upperCaseValue(barcode);
		if (barcode.includes("PL")) {
			url += "&IdLivello=2";
		} else {
			url += "&IdLivello=1";
		}
		url += "&Larghezza=" + volume;
		url += "&IsChangedLarghezza=true";
		setLocationOnWindow(f, url);
	}
}

function cambioLunghezza() {
	var barcode = document.getElementById("BarcodeMisure").value;
	if (barcode != null && barcode != undefined && barcode.length > 15) {
		var volume = 0;
		if (getFieldValueDecimal('Lunghezza') != null) {
			volume = getFieldValueDecimal('Lunghezza');
		}
		var f = document.getElementById('MisureErrorFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YInserimentoDatiTecniciAllestimento";
		url += "?Barcode=" + upperCaseValue(barcode);
		if (barcode.includes("PL")) {
			url += "&IdLivello=2";
		} else {
			url += "&IdLivello=1";
		}
		url += "&Lunghezza=" + volume;
		url += "&IsChangedLunghezza=true";
		setLocationOnWindow(f, url);
	}
}

function addDescrizioneBarcodeDocumento(txt1, txt2) {
	var tr = document.getElementById("BarcodeDocumento").parentNode.parentNode;
	var desc1 = document.createElement('tr');
	desc1.setAttribute('id', 'desc1');
	var cell1 = document.createElement('td');
	var cellDesc1 = document.createElement('td');
	var label1 = document.createElement('label');
	label1.innerHTML = txt1;
	cellDesc1.appendChild(label1);
	desc1.appendChild(cell1);
	desc1.appendChild(cellDesc1);
	//71425 inserire la img della copia pallet
	var td1 = document.createElement('td');
	var a1 = document.createElement('a');
	a1.setAttribute("id", "img_copia_pallet");
	a1.setAttribute('onclick', 'copiaPallet()');
	a1.style.display = 'none';
	var i1 = document.createElement('i');
	a1.appendChild(i1);
	td1.appendChild(a1);
	var classNames = 'fa fa-regular fa-copy fa-2x'.split(' ');
	classNames.forEach(function(className) {
		i1.classList.add(className);
	});
	i1.style.color = 'black';
	desc1.appendChild(td1);
	//71425	Fine
	tr.parentNode.insertBefore(desc1, tr.nextSibling);
	if (txt2 != null) {
		var desc2 = document.createElement('tr');
		desc2.setAttribute('id', 'desc2');
		var cell2 = document.createElement('td');
		var cellDesc2 = document.createElement('td');
		var label2 = document.createElement('label');
		label2.innerHTML = txt2;
		cellDesc2.appendChild(label2);
		desc2.appendChild(cell2);
		desc2.appendChild(cellDesc2);
		tr.parentNode.insertBefore(desc2, tr.nextSibling);
	}
}

/** 
 * <b>71425	DSSOF3	09/02/2024</b>
 * <p>
 * Nuova funzione di copia pallet.
 * Viene chiesto all'utente quanti pallet, uguali a quello presente nel barcode vuole creare,
 * in base a questo tramite window.prompt vengono chiesti i barcode e poi viene lanciata una servlet.
 * </p>
*/
function copiaPallet() {
	var quantitaImballataScatola = document.getElementById('prelevataScatola').value;
	var residuoDaPrelevare = document.getElementById('residuoRiga').value;
	var numberOfBoxes = prompt("Quanti pallet vuoi creare?");
	// Validate if the input is a valid number
	if (numberOfBoxes === null || isNaN(numberOfBoxes) || numberOfBoxes < 1) {
		alert("Immettere un valore maggiore di zero");
		return;
	} else {
		let res = quantitaImballataScatola * numberOfBoxes;
		if (res > residuoDaPrelevare) {
			alert("Non e' possibile creare " + numberOfBoxes + " pallet \n La quantita' eccederebbe di = " + (residuoDaPrelevare - res));
			return;
		}
	}
	numberOfBoxes = parseInt(numberOfBoxes);
	var boxNumbers = [];
	for (var i = 1; i <= numberOfBoxes; i++) {
		var boxNumber = prompt("Dammi il numero del pallet " + i + " di " + numberOfBoxes);
		if (boxNumber === null) {
			return; // Exit the function
		}
		if (boxNumber === null || boxNumber.trim() === "") {
			alert("Inserire il numero di pallet");
			i--; // Decrement i to repeat the current iteration
			continue;
		}
		if (!boxNumber.includes("PL") && boxNumber.length != 16) {
			alert("Formato pallet errato, re-inserire");
			i--; // Decrement i to repeat the current iteration
			continue;
		}
		boxNumbers.push(boxNumber);
	}
	var f = document.getElementById('GrigliaDocumentiFrame').contentWindow;
	var barcodePallet = document.getElementById('BarcodePallet').value;
	var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YCopiaPallet";
	var serializedArray = boxNumbers.map(encodeURIComponent).join(',');
	url += "?BarcodePallet=" + barcodePallet + "&ListaPallets=" + serializedArray;
	setLocationOnWindow(f, url);
}

function addDescrizioneRegistrazioneAvvenuta(txt) {
	var tr = document.getElementById("BarcodeScatola").parentNode.parentNode;
	if (document.getElementById('rimuovimi') != null || document.getElementById('rimuovimi') == undefined) {
		//Rimozione post aggiunta riepilogo	71425
		if (document.getElementById('rimuovimi') != null)
			document.getElementById('rimuovimi').remove();
		var desc1 = document.createElement('tr');
		desc1.setAttribute('id', 'rimuovimi');
		var cell1 = document.createElement('td');
		var cellDesc1 = document.createElement('td');
		var label1 = document.createElement('label');
		label1.setAttribute('id', 'descReg');
		label1.innerHTML = txt;
		cellDesc1.appendChild(label1);
		desc1.appendChild(cell1);
		desc1.appendChild(cellDesc1);
		tr.parentNode.insertBefore(desc1, tr.nextSibling);
	} else {
		document.getElementById('descReg').value = txt;
	}
}

/**
 * <b>71425	DSSOF3	09/02/2024</b>
 * <p>
 * Nuova funzione.<br>
 * Questa serve per, una volta sparato il pallet, vedere le scatole all'interno di esso.<br>
 * Le scatole sono visualizzate in un iframe con all'interno una jsp, tutto fatto tramite la servlet sotto dichiarata.
 * </p>
 */
function loadRiepilogoPallet() {
	var BarcodePallet = document.getElementById("BarcodePallet").value;
	var grigliaFrame = document.getElementById("FrameRiepilogoPallet");
	grigliaFrame.style.display = displayBlock;
	var url = "/" + webAppPath + "/" + servletPath + "/it.lamaplast.thip.logisticaLight.web.YLoadRiepilogoAllestimento?BarcodePallet=" + BarcodePallet;
	setLocationOnWindow(grigliaFrame.contentWindow, url);
}