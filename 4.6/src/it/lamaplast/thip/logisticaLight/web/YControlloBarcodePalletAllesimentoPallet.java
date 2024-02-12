package it.lamaplast.thip.logisticaLight.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebElement;
import com.thera.thermfw.web.servlet.BaseServlet;

import it.lamaplast.thip.vendite.documentoVE.YDocumentoVendita;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPpl;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnr;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnrTM;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplLegame;

/**
 * <h1>Softre Solutions</h1> <br>
 * 
 * @author Daniele Signoroni 24/10/2023 <br>
 *         <br>
 *         <b>71270 DSSOF3 24/10/2023</b>
 *         <p>
 *         Prima stesura:<br>
 *         All'inserimento del barcode pallet verifico se l'utente puo'
 *         continuare. Inserira' poi la scatola...
 *         </p>
 *         <b>71425	DSSOF3	08/02/2024</b>
 *         <p>
 *         Aggiungere chiamata al riepilogo pallet, post sparata.<br>
 *         </p>
 */

public class YControlloBarcodePalletAllesimentoPallet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void processAction(ServletEnvironment se) throws Exception {
		try {
			PrintWriter out = se.getResponse().getWriter();
			out.println("<script>");
			String barcodePallet = getStringParameter(se.getRequest(), "BarcodePallet");
			String barcodeDocumento = getStringParameter(se.getRequest(), "BarcodeDocumento");
			if (barcodeDocumento != null) {
				if (barcodeDocumento.length() >= 13) {
					try {
						String anno = barcodePallet.substring(0, 4);
						String numero = barcodePallet.substring(6, 16);
						String serie = numero.substring(0, 2);
						if (serie.equals("PL")) {
							String where = " " + DocumentoVenRigaPplCnrTM.ID_AZIENDA + " = '"
									+ Azienda.getAziendaCorrente() + "' ";
							where += " AND " + DocumentoVenRigaPplCnrTM.ID_ANNO_IMB + " = '" + anno + "' ";
							where += " AND " + DocumentoVenRigaPplCnrTM.ID_NUMERO_IMB + " = '" + numero + "' ";
							where += " AND " + DocumentoVenRigaPplCnrTM.ID_LIVELLO + " = '2' ";
							Vector<DocumentoVenRigaPplCnr> listaPallet;
							try {
								listaPallet = DocumentoVenRigaPplCnr.retrieveList(DocumentoVenRigaPplCnr.class, where,
										"", false);
								if (listaPallet.size() > 0) {
									DocumentoVenRigaPplCnr pallet = listaPallet.get(0);
									YDocumentoVendita docVen = getDocumentoVenditaDaBarcode(barcodeDocumento);
									if (docVen != null) {
										if (docVen.getIdAzienda().equals(pallet.getIdAzienda())
												&& docVen.getAnnoDocumento().equals(pallet.getIdAnnoPkl())
												&& docVen.getNumeroDocumento().equals(pallet.getIdNumeroPkl())) {
											out.println(
													"parent.document.getElementById('BarcodePallet').readOnly = true;");
											out.println(
													"parent.document.getElementById('BarcodeScatola').removeAttribute('readonly');");
											out.println(
													"setTimeout(parent.document.getElementById('BarcodeScatola').focus(),1000);");
											out.println("parent.loadRiepilogoPallet();");
										} else {
											String lstPrl = "V" + docVen.getAnnoDocumento().substring(2, 4) + "  "
													+ docVen.getNumeroDocumento();
											out.println(
													"setTimeout(parent.window.alert('Pallet gia assegnato alla lista di prelievo "
															+ WebElement.formatStringForHTML(lstPrl) + "'),1000);");
										}
									}
									//controllo per fare display di copia pallet
									if(pallet.getImballiFigli().size() == 1) {
										DocumentoVenRigaPplCnr scatola = (DocumentoVenRigaPplCnr) pallet.getImballiFigli().get(0);
										if(scatola.getIdNumeroImb().contains("SC")) { //strettamente SC
											scatola.impostaRigheWrapper();
											if(scatola.getRigheL1Wrp().size() == 1) {
												DocumentoVenRigaPplLegame rigaLegame = (DocumentoVenRigaPplLegame) scatola.getRigheLegame().get(0);
												DocumentoVenRigaPpl rigaPrelievo = rigaLegame.getDocVenRigaPpl();
												BigDecimal residuo = rigaPrelievo.getQtaPEvaUmPrm().subtract(rigaLegame.getQuantitaImballo().getQuantitaInUMPrm());
												out.println("parent.document.getElementById('prelevataScatola').value = '"+rigaLegame.getQuantitaImballo().getQuantitaInUMPrm().toString()+"';");
												out.println("parent.document.getElementById('residuoRiga').value = '"+residuo.toString()+"';");
												out.println("parent.document.getElementById('img_copia_pallet').style.display = 'revert';");
											}else {
												out.println("parent.document.getElementById('prelevataScatola').value = '';");
												out.println("parent.document.getElementById('residuoRiga').value = '';");
												out.println("parent.document.getElementById('img_copia_pallet').style.display = 'none';");
											}
										}else {
											out.println("parent.document.getElementById('prelevataScatola').value = '';");
											out.println("parent.document.getElementById('residuoRiga').value = '';");
											out.println("parent.document.getElementById('img_copia_pallet').style.display = 'none';");
										}
									}else {
										out.println("parent.document.getElementById('prelevataScatola').value = '';");
										out.println("parent.document.getElementById('residuoRiga').value = '';");
										out.println("parent.document.getElementById('img_copia_pallet').style.display = 'none';");
									}
								} else {
									out.println("parent.document.getElementById('BarcodePallet').readOnly = true;");
									out.println(
											"parent.document.getElementById('BarcodeScatola').removeAttribute('readonly');");
									out.println(
											"setTimeout(parent.document.getElementById('BarcodeScatola').focus(),1000);");
									out.println("parent.loadRiepilogoPallet();");
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							out.println("setTimeout(parent.window.alert('Serie pallet non valida'),1000);");
						}
					} catch (IndexOutOfBoundsException e) {
						out.println("setTimeout(parent.window.alert('Formato pallet non valido'),1000);");
					}
				} else {
					out.println("setTimeout(parent.window.alert('Formato pallet non valido'),1000);");
				}
			} else {
				out.println("setTimeout(parent.window.alert('Barcode pallet vuoto'),1000);");
			}
			out.println("</script>");
		} catch (IOException e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected BigDecimal getResiduoPerArticolo(DocumentoVenRigaPpl rigaPrelievo) {
		ResultSet rs = null;
		try {
			String stmt = "SELECT SUM(P.QTA_P_EVA_UM_PRM) - SUM(L.QTA_IMB_UM_PRM) FROM THIP.DOC_VEN_PPL P "
					+ "LEFT OUTER JOIN THIP.DOC_VEN_PKR L "
					+ "ON P.ID_AZIENDA = L.ID_AZIENDA  "
					+ "AND P.ID_ANNO_DOC = L.R_ANNO_DOC  "
					+ "AND P.ID_NUMERO_DOC = L.R_NUMERO_DOC  "
					+ "AND P.ID_RIGA_DOC = L.R_RIGA_DOC  "
					+ "AND P.ID_DET_RIGA_DOC = L.R_DET_RIGA_DOC  "
					+ "WHERE  "
					+ "P.ID_AZIENDA = '"+rigaPrelievo.getIdAzienda()+"' "
					+ "AND P.ID_ANNO_DOC = '"+rigaPrelievo.getIdAnnoDoc()+"' "
					+ "AND P.ID_NUMERO_DOC = '"+rigaPrelievo.getIdNumeroDoc()+"' "
					+ "AND P.R_ARTICOLO = '"+rigaPrelievo.getIdArticolo()+"' "
					+ "";
			CachedStatement cs = null;
			cs = new CachedStatement(stmt);
			rs = cs.executeQuery();
			if(rs.next()) {
				return rs.getBigDecimal(1);
			}
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace(Trace.excStream);
			}
		}
		return BigDecimal.ZERO;
	}

	public static YDocumentoVendita getDocumentoVenditaDaBarcode(String barcodeDocumento) {
		String anno = barcodeDocumento.substring(1, 3);
		anno = "20" + anno;
		String numero = barcodeDocumento.substring(5, 15);
		try {
			YDocumentoVendita docVen = (YDocumentoVendita) YDocumentoVendita.elementWithKey(YDocumentoVendita.class,
					KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), anno, numero }),
					PersistentObject.NO_LOCK);
			return docVen;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;
	}
}
