package it.lamaplast.thip.logisticaLight.web;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebElement;
import com.thera.thermfw.web.servlet.BaseServlet;

import it.lamaplast.thip.logisticaLight.YAllestimentoPallet;
import it.lamaplast.thip.vendite.documentoVE.YDocumentoVendita;
import it.lamaplast.thip.vendite.pickingPacking.YDocumentoVenRigaPplCnr;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnrTM;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplLegame;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplLegameTM;

/**
 * <h1>Softre Solutions</h1> <br>
 * 
 * @author Daniele Signoroni 24/10/2023 <br>
 *         <br>
 *         <b>71270 DSSOF3 24/10/2023</b>
 *         <p>
 *         Prima stesura:<br>
 *         Inserimento barcode lista e barcode pallet sono andati a buon
 *         fine.<br>
 *         Qui vado a pallettizzare la scatola a sistema.<br>
 *         </p>
 *         <b>71372	DSSOF3	10/01/2024</b>
 *         <p>
 *         Settaggio misure mancanti nella creazione del pallet.<br>
 *         Lo standard non gestisce il reperimento di esse dal contenitore.<br>
 *         <list>
 *         1.Altezza 
 *         2.Larghezza
 *         3.Lunghezza
 *         4.Varie um (peso,volume,dimensioni)
 *         5.Tara contenitore
 *         </list>
 *         Aggiunto metodo per gestire le SQLExc e avvisare l'utente.
 *         </p>
 *         <b>71425	DSSOF3	08/02/2024</b>
 *         <p>
 *         Aggiungere chiamata al riepilogo pallet, post pallettizzazione.<br>
 *         </p>
 */

public class YControlloBarcodeScatolaAllesimentoPallet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({})
	@Override
	protected void processAction(ServletEnvironment se) throws Exception {
		PrintWriter out = se.getResponse().getWriter();
		try {
			out.println("<script>");
			String barcodeScatola = getStringParameter(se.getRequest(), "BarcodeScatola");
			String barcodePallet = getStringParameter(se.getRequest(), "BarcodePallet");
			String barcodeDocumento = getStringParameter(se.getRequest(), "BarcodeDocumento");
			if (barcodeScatola == null) {
				out.println("setTimeout(parent.window.alert('Barcode pallet vuoto'),1000);");
			} else if (barcodeScatola.length() < 15) {
				out.println("setTimeout(parent.window.alert('Formato pallet non valido'),1000);");
			} else {
				gestionePallettizzazione(out, barcodeDocumento, barcodeScatola, barcodePallet);
				out.println("</script>");
			}
		} catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		} finally {
			out.println("</script>");
			out.close();
		}
	}

	public void gestionePallettizzazione(PrintWriter out, String barcodeDocumento, String barcodeScatola,
			String barcodePallet) {
		try {
			YDocumentoVendita docVen = YControlloBarcodePalletAllesimentoPallet
					.getDocumentoVenditaDaBarcode(barcodeDocumento);
			if (docVen != null) {
//				String annoSC = barcodeScatola.substring(0, 4);
				String numeroSC = barcodeScatola.substring(6, 16);
				String serieSC = numeroSC.substring(0, 2);
				String annoBC = barcodeDocumento.substring(1, 3);
				annoBC = "20" + annoBC;
//				String numeroBC = barcodeDocumento.substring(5, 15);
				String annoPL = barcodePallet.substring(0, 4);
				String numeroPL = barcodePallet.substring(6, 16);
				String seriePL = numeroPL.substring(0, 2);
				if (serieSC.equals("SC") && seriePL.equals("PL") || serieSC.equals("MX")) {
					YDocumentoVenRigaPplCnr scatola = ricercaScatolaDaBarcode(barcodeScatola);
					if (scatola != null) {
						if (isLegamePresente(barcodeScatola)) {
							out.println("setTimeout(parent.window.alert('Scatola gia pallettizzata'),1000);");
						} else {
							YDocumentoVenRigaPplCnr pallet = ricercaPalletDaBarcode(barcodePallet);
							List<YDocumentoVenRigaPplCnr> righeSel = new ArrayList<YDocumentoVenRigaPplCnr>();
							try {
								if (pallet != null) { // esiste quindi aggiungi scatola
									righeSel.add(scatola);
									pallet.setRighePklSel(righeSel);
								} else { // non esiste quindi codifica
									pallet = codificaPallet(annoPL, numeroPL, docVen, scatola, righeSel);
									pallet.assegnaNumeratore = false;
								}
								if (pallet.save() > 0) {
									ConnectionManager.commit();
									String barcodePalletNew = pallet.getIdAnnoImb() + "  " + pallet.getIdNumeroImb();
									out.println("parent.document.getElementById('BarcodePallet').value = '"
											+ WebElement.formatStringForHTML(barcodePalletNew) + "';");
								} else {
									ConnectionManager.rollback();
									out.println(
											"setTimeout(parent.window.alert('Errore nella codifca del pallet'),1000);");
									return;
								}
								pallet.setServeRicalcoloPesi(true);// setto il ricalcola e ri-salvo
								pallet.setSalvaSoloOggetto(true);
								// scatola.setImballato(true); lo fa lo std
								YAllestimentoPallet.scriviLog(Azienda.getAziendaCorrente(), docVen, pallet, scatola);
								if (scatola.save() >= 0 && pallet.save() >= 0) {
									ConnectionManager.commit();
									out.println("parent.document.getElementById('BarcodeScatola').value = '';");
									out.println(
											"setTimeout(parent.document.getElementById('BarcodeScatola').focus(),1000);");
									String ris = "Regist." + barcodeScatola;
									out.println("parent.addDescrizioneRegistrazioneAvvenuta('"
											+ WebElement.formatStringForHTML(ris) + "');");
									out.println("parent.loadRiepilogoPallet();");
								} else {
									ConnectionManager.rollback();
								}
							} catch (SQLException e) {
								out.println("setTimeout(parent.window.alert('"+WebElement.formatStringForHTML(createMessageForSQLException(e))+"'),1000);");
								e.printStackTrace(Trace.excStream);
							}
						}
					} else {
						out.println("setTimeout(parent.window.alert('Codice scatola errato'),1000);");
					}
				} else {
					out.println("setTimeout(parent.window.alert('Serie pallet non valida'),1000);");
				}
			} else {
				out.println("setTimeout(parent.window.alert('Codice barcode errato'),1000);");
			}
		} catch (IndexOutOfBoundsException e) {
			out.println("setTimeout(parent.window.alert('Formato pallet non valido'),1000);");
		}
	}
	
	protected static String createMessageForSQLException(SQLException sqlException)
	{
		return String.valueOf(sqlException.getErrorCode())+": " + sqlException.getLocalizedMessage();
	}

	protected YDocumentoVenRigaPplCnr codificaPallet(String annoPL, String numeroPL, YDocumentoVendita docVen,
			YDocumentoVenRigaPplCnr scatola, List<YDocumentoVenRigaPplCnr> righeSel) {
		YDocumentoVenRigaPplCnr pallet = (YDocumentoVenRigaPplCnr) Factory.createObject(YDocumentoVenRigaPplCnr.class);
		pallet.setIdAzienda(Azienda.getAziendaCorrente());
		pallet.setIdAnnoImb(annoPL);
		pallet.setIdNumeroImb(numeroPL);
		pallet.setIdLivello((short) 2);
		// pallet.getNumeratoreImb().setIdNumeratore("IMBALLI");
		// pallet.getNumeratoreImb().setIdSerie("PL");
		// pallet.getNumeratoreImb().setIdAzienda(Azienda.getAziendaCorrente());
		Articolo artPallet = getArticoloPallet();
		//pallet.setArticoloCnr(artPallet);
		pallet.setIdArticoloCnr(artPallet.getIdArticolo());
		pallet.setIdTipoCnr(artPallet.getIdClasseC());
		pallet.setIdAnnoPkl(docVen.getAnnoDocumento());
		pallet.setIdNumeroPkl(docVen.getNumeroDocumento());
		pallet.setIdNumeroImb(numeroPL);
		pallet.setIdAnnoImb(annoPL);
		righeSel.add(scatola);
		pallet.setRighePklSel(righeSel);
		if(artPallet.getArticoloDatiTecnici().getAltezza() != null) { //71372	Inizio
			pallet.setAltezza(artPallet.getArticoloDatiTecnici().getAltezza());
		}
		if(artPallet.getArticoloDatiTecnici().getLunghezza() != null) {
			pallet.setLunghezza(artPallet.getArticoloDatiTecnici().getLunghezza());
		}
		if(artPallet.getArticoloDatiTecnici().getLarghezza() != null) {
			pallet.setLarghezza(artPallet.getArticoloDatiTecnici().getLarghezza());
		} 
		if(artPallet.getArticoloDatiTecnici().getVolume() != null) {
			pallet.setVolume(artPallet.getArticoloDatiTecnici().getVolume());
		} 
		if(artPallet.getArticoloDatiTecnici().getUMDimensioni() != null) {
			pallet.setIdUMDimensioni(artPallet.getArticoloDatiTecnici().getIdUMDimensioni());
		}
		if(artPallet.getArticoloDatiTecnici().getUMVolume() != null) {
			pallet.setIdUMVolume(artPallet.getArticoloDatiTecnici().getIdUMVolume());
		}
		if(artPallet.getArticoloDatiTecnici().getUMPeso() != null) {
			pallet.setIdUMPeso(artPallet.getArticoloDatiTecnici().getIdUMPeso());
		}
		if(artPallet.getArticoloDatiTecnici().getPeso() != null) {
			pallet.setTaraCnr(artPallet.getArticoloDatiTecnici().getPeso());
		}else if(artPallet.getArticoloDatiTecnici().getPesoNetto() != null) {
			pallet.setTaraCnr(artPallet.getArticoloDatiTecnici().getPesoNetto());
		}
		//71372	Fine
		return pallet;
	}

	public Articolo getArticoloPallet() {
		try {
			Articolo artPallet = (Articolo) Articolo.elementWithKey(Articolo.class,
					KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), "PL_01" }),
					PersistentObject.NO_LOCK);
			return artPallet;
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;

	}

	/**
	 * <h1>Presenza legami</h1> <br>
	 * </br>
	 * 
	 * @author Daniele Signoroni
	 *         <p>
	 *         Controllo che nella tabella
	 *         {@link DocumentoVenRigaPplLegameTM#TABLE_NAME} non vi sia gia'
	 *         presente un legame codificato, ovvero controllo che la scatola non
	 *         sia gia' stata utilizzata.
	 *         </p>
	 * @param barcodeSC
	 * @return true se usata, false se utilizzabile
	 */
	@SuppressWarnings("unchecked")
	public boolean isLegamePresente(String barcodeSC) {
		String annoSC = barcodeSC.substring(0, 4);
		String numeroSC = barcodeSC.substring(6, 16);
		String where;
		where = " " + DocumentoVenRigaPplLegameTM.ID_AZIENDA + " = '" + Azienda.getAziendaCorrente() + "' ";
		where += " AND " + DocumentoVenRigaPplLegameTM.R_ANNO_IMBFGL + " = '" + annoSC + "' ";
		where += " AND " + DocumentoVenRigaPplLegameTM.R_NUMERO_IMBFGL + " = '" + numeroSC + "' ";
		Vector<DocumentoVenRigaPplLegame> legami;
		try {
			legami = DocumentoVenRigaPplLegame.retrieveList(DocumentoVenRigaPplLegame.class, where, "", false);
			if (legami.size() > 0) {
				return true;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(Trace.excStream);
		} catch (InstantiationException e) {
			e.printStackTrace(Trace.excStream);
		} catch (IllegalAccessException e) {
			e.printStackTrace(Trace.excStream);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static YDocumentoVenRigaPplCnr ricercaScatolaDaBarcode(String barcode) {
		String annoSC = barcode.substring(0, 4);
		String numeroSC = barcode.substring(6, 16);
		Vector<YDocumentoVenRigaPplCnr> listaPallet;
		String where = "";
		YDocumentoVenRigaPplCnr pallet = null;
		where = " " + DocumentoVenRigaPplCnrTM.ID_AZIENDA + " = '" + Azienda.getAziendaCorrente() + "' ";
		where += " AND " + DocumentoVenRigaPplCnrTM.ID_ANNO_IMB + " = '" + annoSC + "' ";
		where += " AND " + DocumentoVenRigaPplCnrTM.ID_NUMERO_IMB + " = '" + numeroSC + "' ";
		where += " AND " + DocumentoVenRigaPplCnrTM.ID_LIVELLO + " = '1' ";
		try {
			listaPallet = YDocumentoVenRigaPplCnr.retrieveList(YDocumentoVenRigaPplCnr.class, where, "", false);
			if (listaPallet.size() > 0) {
				pallet = listaPallet.get(0);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(Trace.excStream);
		} catch (InstantiationException e) {
			e.printStackTrace(Trace.excStream);
		} catch (IllegalAccessException e) {
			e.printStackTrace(Trace.excStream);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return pallet;
	}

	@SuppressWarnings("unchecked")
	public static YDocumentoVenRigaPplCnr ricercaPalletDaBarcode(String barcode) {
		String annoPL = barcode.substring(0, 4);
		String numeroPL = barcode.substring(6, 16);
		Vector<YDocumentoVenRigaPplCnr> listaPallet;
		String where = "";
		YDocumentoVenRigaPplCnr pallet = null;
		where = " " + DocumentoVenRigaPplCnrTM.ID_AZIENDA + " = '" + Azienda.getAziendaCorrente() + "' ";
		where += " AND " + DocumentoVenRigaPplCnrTM.ID_ANNO_IMB + " = '" + annoPL + "' ";
		where += " AND " + DocumentoVenRigaPplCnrTM.ID_NUMERO_IMB + " = '" + numeroPL + "' ";
		where += " AND " + DocumentoVenRigaPplCnrTM.ID_LIVELLO + " = '2' ";
		try {
			listaPallet = YDocumentoVenRigaPplCnr.retrieveList(YDocumentoVenRigaPplCnr.class, where, "", false);
			if (listaPallet.size() > 0) {
				pallet = listaPallet.get(0);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(Trace.excStream);
		} catch (InstantiationException e) {
			e.printStackTrace(Trace.excStream);
		} catch (IllegalAccessException e) {
			e.printStackTrace(Trace.excStream);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return pallet;
	}

}
