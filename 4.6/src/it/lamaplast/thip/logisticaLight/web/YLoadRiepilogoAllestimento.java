package it.lamaplast.thip.logisticaLight.web;

import java.sql.SQLException;
import java.util.Vector;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.servlet.BaseServlet;

import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnr;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnrTM;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 08/02/2024
 * <br><br>
 * <b>71425	DSSOF3	08/02/2024</b>
 * <p>Prima stesura.<br>
 * Servlet necessaria al recupero e al display di imballi figli di un pallet.<br>
 * Setto sulla request la lista, in modo che poi nella jsp io possa fare il display di essa.
 * </p>
 */

public class YLoadRiepilogoAllestimento extends BaseServlet{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void processAction(ServletEnvironment se) throws Exception {
		String barcodePallet = getStringParameter(se.getRequest(), "BarcodePallet");
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
					if(pallet != null) {
						se.getRequest().setAttribute("RIEPILOGO", pallet.getImballiFigli());	 
						se.sendRequest(getServletContext(), "it/lamaplast/thip/logisticaLight/YRiepilogoAllestimentoPallet.jsp", true);
					}
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}

}
