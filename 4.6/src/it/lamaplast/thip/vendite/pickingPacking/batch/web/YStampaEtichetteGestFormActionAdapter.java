package it.lamaplast.thip.vendite.pickingPacking.batch.web;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.web.BatchFormActionAdapter;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;

import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.generale.IntegrazioneThipLogis;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnr;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnrTM;
import it.thera.thip.vendite.pickingPacking.PackingList;

/**
 * 
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 12/02/2024
 * <br><br>
 * <b>71425	DSSOF3	08/02/2024</b>
 * <p>
 * Gestione check before stampa da gestionale.<br>
 * </p>
 */

public class YStampaEtichetteGestFormActionAdapter extends BatchFormActionAdapter{

	private static final long serialVersionUID = 4153963469594010777L;

	@Override
	public void processAction(ServletEnvironment se) throws ServletException, IOException {
		String action = se.getRequest().getParameter(ACTION);
		if(action != null && (action.equals("RUN_BATCH") || action.equals("RUN_AND_NEW_BATCH")) || action.equals("PRINT_BATCH") || action.equals("PREVIEW_BATCH")) {
			try {
				String barcode = se.getRequest().getParameter("Barcode");
				if(barcode != null && !barcode.equals("")) {
					if(barcode.length() >= 15) {
						if (barcode.charAt(0) == IntegrazioneThipLogis.VENDITA) {
							String key = KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(),
									"20"+barcode.substring(1, 5).trim(), barcode.substring(5).trim() });
							PackingList packingList = (PackingList) PackingList.elementWithKey(PackingList.class, key,PersistentObject.NO_LOCK);
							if (packingList == null) {
								ErrorMessage er = new ErrorMessage("YLAMA003", "Il barcode inserito non è valido!");
								se.addErrorMessage(er);
							}
						} else if (barcode.contains("PL")) {
							DocumentoVenRigaPplCnr docRig = getPallet(barcode);
							if (docRig == null) {
								ErrorMessage er = new ErrorMessage("YLAMA003", "Il barcode inserito non è valido!");
								se.addErrorMessage(er);
							}
						} else if (barcode.contains("SC")) {
							DocumentoVenRigaPplCnr docRig = getScatola(barcode);
							if (docRig == null) {
								ErrorMessage er = new ErrorMessage("YLAMA003", "Il barcode inserito non è valido!");
								se.addErrorMessage(er);
							}
						}else {
							ErrorMessage er = new ErrorMessage("YLAMA003", "Il barcode inserito non è valido!");
							se.addErrorMessage(er);
						}
					}else {
						ErrorMessage er = new ErrorMessage("YLAMA003", "Il barcode inserito non è valido!");
						se.addErrorMessage(er);
					}
				}else {
					ErrorMessage er = new ErrorMessage("BAS0000000");
					se.addErrorMessage(er);
				}
				if(se.isErrorListEmpity())
					super.processAction(se);
				else {
					String url = "com/thera/thermfw/common/ErrorListHandler.jsp";
					se.sendRequest(getServletContext(), url, false);
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}else
			super.processAction(se);
	}

	protected DocumentoVenRigaPplCnr getPallet(String barcode) {
		CachedStatement cs = null;
		ResultSet rs = null;
		DocumentoVenRigaPplCnr docRig = null;
		try {
			String select = "SELECT " + DocumentoVenRigaPplCnrTM.ID_ANNO_PKL + ","
					+ DocumentoVenRigaPplCnrTM.ID_NUMERO_PKL + "," + DocumentoVenRigaPplCnrTM.ID_RIGA_PKL + " FROM "
					+ DocumentoVenRigaPplCnrTM.TABLE_NAME;
			String where = " WHERE " + DocumentoVenRigaPplCnrTM.ID_ANNO_IMB + " = '" + barcode.substring(0, 4)
			+ "' AND " + DocumentoVenRigaPplCnrTM.ID_NUMERO_IMB + " = '" + barcode.substring(6).trim()
			+ "' AND " + DocumentoVenRigaPplCnrTM.ID_LIVELLO + "= '2'";
			cs = new CachedStatement(select + where);
			String key = "";
			rs = cs.executeQuery();
			if (rs.next()) {
				String anno = rs.getString(DocumentoVenRigaPplCnrTM.ID_ANNO_PKL) != null
						? rs.getString(DocumentoVenRigaPplCnrTM.ID_ANNO_PKL).trim()
								: "";
				String numero = rs.getString(DocumentoVenRigaPplCnrTM.ID_NUMERO_PKL) != null
						? rs.getString(DocumentoVenRigaPplCnrTM.ID_NUMERO_PKL).trim()
								: "";
				String riga = rs.getString(DocumentoVenRigaPplCnrTM.ID_RIGA_PKL) != null
						? rs.getString(DocumentoVenRigaPplCnrTM.ID_RIGA_PKL).trim()
								: "";
				key = KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), anno, numero, riga });
				docRig = (DocumentoVenRigaPplCnr) DocumentoVenRigaPplCnr.elementWithKey(DocumentoVenRigaPplCnr.class,
						key, PersistentObject.NO_LOCK);
			}

		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return docRig;
	}

	private DocumentoVenRigaPplCnr getScatola(String barcode) {
		CachedStatement cs = null;
		ResultSet rs = null;
		DocumentoVenRigaPplCnr docRig = null;
		try {
			String select = "SELECT " + DocumentoVenRigaPplCnrTM.ID_ANNO_PKL + ","
					+ DocumentoVenRigaPplCnrTM.ID_NUMERO_PKL + "," + DocumentoVenRigaPplCnrTM.ID_RIGA_PKL + " FROM "
					+ DocumentoVenRigaPplCnrTM.TABLE_NAME;
			String where = " WHERE " + DocumentoVenRigaPplCnrTM.ID_ANNO_IMB + " = '" + barcode.substring(0, 4)
			+ "' AND " + DocumentoVenRigaPplCnrTM.ID_NUMERO_IMB + " = '" + barcode.substring(6).trim()
			+ "' AND " + DocumentoVenRigaPplCnrTM.ID_LIVELLO + "= '1'";
			cs = new CachedStatement(select + where);
			String key = "";
			rs = cs.executeQuery();
			if (rs.next()) {
				String anno = rs.getString(DocumentoVenRigaPplCnrTM.ID_ANNO_PKL) != null
						? rs.getString(DocumentoVenRigaPplCnrTM.ID_ANNO_PKL).trim()
								: "";
				String numero = rs.getString(DocumentoVenRigaPplCnrTM.ID_NUMERO_PKL) != null
						? rs.getString(DocumentoVenRigaPplCnrTM.ID_NUMERO_PKL).trim()
								: "";
				String riga = rs.getString(DocumentoVenRigaPplCnrTM.ID_RIGA_PKL) != null
						? rs.getString(DocumentoVenRigaPplCnrTM.ID_RIGA_PKL).trim()
								: "";
				key = KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), anno, numero, riga });
				docRig = (DocumentoVenRigaPplCnr) DocumentoVenRigaPplCnr.elementWithKey(DocumentoVenRigaPplCnr.class,
						key, PersistentObject.NO_LOCK);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return docRig;
	}
}
