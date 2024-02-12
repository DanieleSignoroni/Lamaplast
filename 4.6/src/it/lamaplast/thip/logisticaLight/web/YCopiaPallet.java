package it.lamaplast.thip.logisticaLight.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.thera.thermfw.base.ResourceLoader;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.servlet.BaseServlet;

import it.lamaplast.thip.logisticaLight.YAllestimentoPallet;
import it.lamaplast.thip.vendite.pickingPacking.YDocumentoVenRigaPplCnr;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.comuniVenAcq.QuantitaInUMRif;
import it.thera.thip.base.generale.Numeratore;
import it.thera.thip.base.generale.NumeratoreHandler;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.logisticaLight.DatiDocumentoPPLLL;
import it.thera.thip.logisticaLight.DatiDocumentoPPLRigaLL;
import it.thera.thip.logisticaLight.SaldiUbicazionePrt;
import it.thera.thip.vendite.pickingPacking.DocumentoVenPPL;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPpl;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnr;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplLegame;

/**
 * 
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 09/02/2024
 * <br><br>
 * <b>71425	DSSOF3	09/02/2024</b>
 * <p>
 * Servlet necessaria alla copia di un pallet contenente una scatola e un solo articolo.<br>
 * La servlet in questione va a simulare la funzione di "Prelievo vendita", seguento lo standard.<br>
 * In seguito va a porre la scatola creata, con serie presa da un parametro, su un pallet.<br>
 * Il codice del pallet mi viene sparato dall'utente in una window.prompt().
 * </p>
 */

public class YCopiaPallet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	protected void processAction(ServletEnvironment se) throws Exception {
		PrintWriter out = se.getResponse().getWriter();
		out.println("<script language='JavaScript1.2'>");
		String barcodePallet = getStringParameter(se.getRequest(), "BarcodePallet");
		String listaPallets = getStringParameter(se.getRequest(), "ListaPallets");
		if(barcodePallet != null) {
			DocumentoVenRigaPplCnr pallet = YControlloBarcodeScatolaAllesimentoPallet.ricercaPalletDaBarcode(barcodePallet);
			DocumentoVenRigaPplCnr scatola = null;
			DocumentoVenRigaPpl rigaPPL = null;
			DocumentoVenRigaPplLegame rigaLegame = null;
			if(pallet != null) {
				if(pallet.getImballiFigli().size() == 1) {
					scatola = (DocumentoVenRigaPplCnr) pallet.getImballiFigli().get(0);
					scatola.impostaRigheWrapper();
					if(scatola.getRigheL1Wrp().size() == 1) {
						rigaLegame = (DocumentoVenRigaPplLegame) scatola.getRigheLegame().get(0);
						rigaPPL = rigaLegame.getDocVenRigaPpl();
					}
				}
				NumeratoreHandler numH = new NumeratoreHandler();
				Numeratore numeratoreImballi = (Numeratore) Numeratore.elementWithKey(Numeratore.class,
						KeyHelper.buildObjectKey(new String[] {
								Azienda.getAziendaCorrente(),
								"IMBALLI"
						}), 0);
				numH.setNumeratore(numeratoreImballi);
				String serieScatoleAutomatiche = ParametroPsn.getValoreParametroPsn("YCopiaPallet", "SerieScatola");
				DatiDocumentoPPLRigaLL rigaDaPrelevare = getRigaSelezionataDaPrelevare(rigaPPL, rigaLegame);
				if(rigaDaPrelevare == null) {
					String textError = ResourceLoader.getString("it.thera.thip.logisticaLight.resources.PrelievoVendita", "MsgErroriGenerazioneDoc");
					String textErrorDatiMancante = ResourceLoader.getString("it.thera.thip.logisticaLight.resources.PrelievoVendita", "MsgErroriDatiMancanti");				
					textError += "\n\n" + textErrorDatiMancante;			
					out.println("parent.alert('" + com.thera.thermfw.web.WebElement.formatStringForHTML(textError) + "');");
					return;
				}
				String[] pallets = listaPallets.split(",");
				for (int i = 0; i < pallets.length; i++) {
					NumeratoreHandler numImb = scatola.getNumeratoreImb();
			  		numImb.setIdSerie(serieScatoleAutomatiche);
					String barcodeScatola = numH.getAnno() + "  " + numImb.getIdProgressivo();
					String barcode = pallets[i];
					YDocumentoVenRigaPplCnr palletNuovo = YControlloBarcodeScatolaAllesimentoPallet.ricercaPalletDaBarcode(barcode);
					if(palletNuovo != null) {
						String textError = "Il barcode : "+barcode+" e gia stato sparato \n Va sparato un barcode nuovo!";
						out.println("setTimeout(parent.alert('" + com.thera.thermfw.web.WebElement.formatStringForHTML(textError) + "'),1000);");
						out.println("</script>");
						return;
					}
					DatiDocumentoPPLLL datiDocPPLLL = (DatiDocumentoPPLLL) Factory.createObject("it.thera.thip.logisticaLight.DatiDocumentoPPLLL");
					datiDocPPLLL.setChiaveRigaPPLSelezionata(rigaPPL.getKey());
					datiDocPPLLL.setChiaveRigaPKLSelezionata("");		
					datiDocPPLLL.setChiaveDocumento(rigaPPL.getDocumentoVenPPL().getKey());			
					datiDocPPLLL.setIdCausaleDoc("LV");
					datiDocPPLLL.setIdMagazzinoUscita("001");
					datiDocPPLLL.setUbicazioneUscita("GENERICA");
					datiDocPPLLL.setIdSerieNum("LV");
					datiDocPPLLL.setIdAzienda(rigaPPL.getIdAzienda());
					datiDocPPLLL.setIdAnnoDoc(rigaPPL.getIdAnnoDoc());
					datiDocPPLLL.setIdNumeroDoc(rigaPPL.getIdNumeroDoc());
					datiDocPPLLL.setBarcodeUDS(barcodeScatola);
					datiDocPPLLL.setIdCliente(rigaPPL.getDocumentoVenPPL().getIdCliente());				
					datiDocPPLLL.setErrorsForced(false);
					datiDocPPLLL.setRighe(Arrays.asList(rigaDaPrelevare));
					List errori = datiDocPPLLL.confermaPrelievoVendita();
					if(errori.isEmpty()) {
						String annoPL = barcode.substring(0,4);
						String numeroPL = barcode.substring(6, barcode.length());
						palletNuovo = codificaPallet(
								annoPL,
								numeroPL,
								rigaPPL.getDocumentoVenPPL(),
								YControlloBarcodeScatolaAllesimentoPallet.ricercaScatolaDaBarcode(barcodeScatola)
						);
						palletNuovo.assegnaNumeratore = false;
						if (palletNuovo.save() > 0) {
							ConnectionManager.commit();
							pallet.setServeRicalcoloPesi(true);// setto il ricalcola e ri-salvo
							pallet.setSalvaSoloOggetto(true);
							YAllestimentoPallet.scriviLog(Azienda.getAziendaCorrente(), rigaPPL.getDocumentoVenPPL(), pallet, scatola);
							if (scatola.save() >= 0 && pallet.save() >= 0) {
								ConnectionManager.commit();
							}
						}
					}
				}
			}
		}else {

		}
		out.println("</script>");	
	}
	
	protected YDocumentoVenRigaPplCnr codificaPallet(String annoPL, String numeroPL, DocumentoVenPPL docVen,
			YDocumentoVenRigaPplCnr scatola) {
		YDocumentoVenRigaPplCnr pallet = (YDocumentoVenRigaPplCnr) Factory.createObject(YDocumentoVenRigaPplCnr.class);
		pallet.setIdAzienda(Azienda.getAziendaCorrente());
		pallet.setIdAnnoImb(annoPL);
		pallet.setIdNumeroImb(numeroPL);
		pallet.setIdLivello((short) 2);
		Articolo artPallet = getArticoloPallet();
		pallet.setIdArticoloCnr(artPallet.getIdArticolo());
		pallet.setIdTipoCnr(artPallet.getIdClasseC());
		pallet.setIdAnnoPkl(docVen.getAnnoDocumento());
		pallet.setIdNumeroPkl(docVen.getNumeroDocumento());
		pallet.setIdNumeroImb(numeroPL);
		pallet.setIdAnnoImb(annoPL);
		pallet.setRighePklSel(Arrays.asList(scatola));
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
	
	protected DatiDocumentoPPLRigaLL getRigaSelezionataDaPrelevare(DocumentoVenRigaPpl rigaPPL, DocumentoVenRigaPplLegame rigaLegame) {
		DatiDocumentoPPLRigaLL rigaDocPPLLL = (DatiDocumentoPPLRigaLL) Factory.createObject("it.thera.thip.logisticaLight.DatiDocumentoPPLRigaLL");			
		rigaDocPPLLL.setIdAnnoDoc(rigaPPL.getIdAnnoDoc());
		rigaDocPPLLL.setIdNumeroDoc(rigaPPL.getIdNumeroDoc());
		rigaDocPPLLL.setIdRigaDoc(rigaPPL.getIdRigaDoc());
		rigaDocPPLLL.setIdDetRigaDoc(rigaPPL.getIdDetRigaDoc());				
		rigaDocPPLLL.setIdRigaLotto(rigaPPL.getIdRigaLotto());
		rigaDocPPLLL.setTipoDocTrasfGenerato(SaldiUbicazionePrt.TIPO_DOC_PPL);				
		rigaDocPPLLL.setRigaPPL(rigaPPL);
		rigaDocPPLLL.setIdArticolo(rigaPPL.getIdArticolo());
		rigaDocPPLLL.setIdVersione(rigaPPL.getIdVersione());
		rigaDocPPLLL.setIdConfigurazione(rigaPPL.getIdConfigurazione());
		rigaDocPPLLL.setIdLotto("-");
		rigaDocPPLLL.setIdMatricola(null);
		rigaDocPPLLL.setIdCommessa(rigaPPL.getIdCommessa());   
		rigaDocPPLLL.setIdCliente(rigaPPL.getDocumentoVenPPL().getIdCliente());				
		calcoloQuantitaRiga(rigaPPL, rigaDocPPLLL, rigaLegame.getQuantitaImballo().getQuantitaInUMPrm());
		rigaDocPPLLL.setUbicazioneRigaDoc("GENERICA");
		if(rigaDocPPLLL.isDatiRigaMancanteInternal())
			return null;

		return rigaDocPPLLL;
	}

	public void calcoloQuantitaRiga(DocumentoVenRigaPpl rigaPPL, DatiDocumentoPPLRigaLL rigaDocPPLLL, BigDecimal decQta) {
		QuantitaInUMRif qtaInUMRif = rigaPPL.calcoloQuantitaRiga(decQta);
		rigaDocPPLLL.setQuantitaPrm(qtaInUMRif.getQuantitaInUMPrm());
		rigaDocPPLLL.setQuantitaRif(qtaInUMRif.getQuantitaInUMRif());
		rigaDocPPLLL.setQuantitaSec(qtaInUMRif.getQuantitaInUMSec());
	}
}
