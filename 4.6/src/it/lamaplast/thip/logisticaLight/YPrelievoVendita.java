package it.lamaplast.thip.logisticaLight;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Database;

import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.logisticaLight.DatiDocumentoPPLRigaLL;
import it.thera.thip.logisticaLight.PrelievoVendita;
import it.thera.thip.logisticaLight.PsnLogisticaLight;
import it.thera.thip.magazzino.saldi.SaldoMagTestataTM;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPpl;

/**
 * <h1>Softre Solutions</h1> <br>
 * 
 * @author Daniele Signoroni 24/10/2023 <br>
 *         <br>
 *         <b>71270 DSSOF3 24/10/2023</b>
 *         <p>
 *         Prima stesura:<br>
 *         Nella griglia delle righe mostrare l'ubicazione che risiede sulla
 *         testata saldo.
 *         </p>
 *         <b>71305 TBSOF2 17/11/2023</b>
 *         <p>
 *         Ritorno le righe della lista di prelievo ordinate per ubicazione
 *         </p>
 *         <b>71425	TBSOF3	08/02/2024</b>
 *         <p>
 *         Override del metodo {@link #getImgCSSGiacenzaChk(DatiDocumentoPPLRigaLL, DocumentoVenRigaPpl)} per cambiare il semaforo di giacenza.<br>
 *         </p>
 */

public class YPrelievoVendita extends PrelievoVendita {

	protected static final String UBIC_SALDO_TES = "SELECT  " + SaldoMagTestataTM.UBICAZIONE + " FROM "
			+ SaldoMagTestataTM.TABLE_NAME + " WHERE " + SaldoMagTestataTM.ID_AZIENDA + " = ? " + " AND "
			+ SaldoMagTestataTM.ID_MAGAZZINO + " = ? " + " AND " + SaldoMagTestataTM.ID_ARTICOLO + " = ? ";

	public static CachedStatement cSelectUbicazioneSaldoTestata = new CachedStatement(UBIC_SALDO_TES);

	@Override
	public DatiDocumentoPPLRigaLL getDatiDocumentoPPLRigaLL(DocumentoVenRigaPpl riga) {
		DatiDocumentoPPLRigaLL rigaGen = super.getDatiDocumentoPPLRigaLL(riga);
		((YDatiDocumentoPPLRigaLL) rigaGen).setUbicazioneSaldoTestataPers(getUbicazionePersonalizzata(riga));
		return rigaGen;
	}

	/**
	 * <h1>Ordinamento righe prelievo</h1> <br>
	 * 
	 * @author Thomas Brescianini 17/11/2023 <br>
	 *         <br>
	 *         <b>71305 TBSOF3 17/11/2023</b>
	 *         <p>
	 *         Vado a ordinare le righe di prelievo in base al codice ubicazione di
	 *         testata saldo.
	 *         </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getRigheDocumentoSelezionato(String chiaveDoc, String campoRicerca, String tipoRicerca)
			throws SQLException {
		List righe = super.getRigheDocumentoSelezionato(chiaveDoc, campoRicerca, tipoRicerca);
		Collections.sort(righe, new YComparatorRighePrelievo());
		return righe;
	}

	/**
	 * <h1>Ubicazione saldo testata da riga prelievo</h1> <br>
	 * 
	 * @author Daniele Signoroni 24/10/2023 <br>
	 *         <br>
	 *         <b>71270 DSSOF3 24/10/2023</b>
	 *         <p>
	 *         Prima stesura:<br>
	 *         Vado a selezionare l'ubicazione tramite la chiave.
	 *         </p>
	 */
	public static String getUbicazionePersonalizzata(DocumentoVenRigaPpl riga) {
		ResultSet rs = null;
		try {
			PreparedStatement ps = cSelectUbicazioneSaldoTestata.getStatement();
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, Azienda.getAziendaCorrente());
			db.setString(ps, 2, riga.getIdMagazzino());
			db.setString(ps, 3, riga.getIdArticolo());
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1) != null ? rs.getString(1) : "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	@Override
	public String getImgCSSGiacenzaChk(DatiDocumentoPPLRigaLL datiDocRigaPPL, DocumentoVenRigaPpl rigaPPL) {
		String url = super.getImgCSSGiacenzaChk(datiDocRigaPPL, rigaPPL);
		if(url.equals("it/thera/thip/logisticaLight/images/ChkRosso.gif") || url.equals("it/thera/thip/logisticaLight/images/ChkGiallo.gif")) {
			BigDecimal qrl = datiDocRigaPPL.getQtaResidua();
			BigDecimal giacenza = getQtaGiacNet(rigaPPL.getIdArticolo(), rigaPPL.getIdMagazzino());
			if (giacenza == null || giacenza.compareTo(BigDecimal.ZERO) <= 0) {
				return "it/thera/thip/logisticaLight/images/ChkRosso.gif";
			}
			else if ( qrl.compareTo(giacenza) > 0) {
				return "it/thera/thip/logisticaLight/images/ChkGiallo.gif";
			}
		}
		return url;
	}
	
	@Override
	public BigDecimal getDefaultQtaDaPrelevare(DatiDocumentoPPLRigaLL datiDocRigaPPL, DocumentoVenRigaPpl rigaPPL) {
		boolean proponiQta = false;
	  	PsnLogisticaLight psnLogLight = PsnLogisticaLight.getCurrentPsnLogisLight();
	  	if(psnLogLight != null && psnLogLight.isProponiQuantita())
	  		proponiQta = true;
	  	if(!proponiQta)
	  		return BigDecimal.ZERO;
	  	BigDecimal qtaResidua = datiDocRigaPPL.getQtaResidua();
	  	BigDecimal giacenza = getQtaGiacNet(rigaPPL.getIdArticolo(), rigaPPL.getIdMagazzino());
	    if(giacenza.compareTo(qtaResidua) >= 0) {
	    	if(qtaResidua.compareTo(BigDecimal.ZERO) > 0)
	    		return qtaResidua;
	    	return BigDecimal.ZERO;
	    }
	    else {
	    	if(giacenza.compareTo(BigDecimal.ZERO) > 0)
	    		return giacenza;
	    	return BigDecimal.ZERO;
	    }
	}

	private BigDecimal getQtaGiacNet(String idArticolo, String idMagazzino) {
		CachedStatement cs = null;
		ResultSet rs = null;
		BigDecimal qta = null;
		try {
			String select = "SELECT QTA_GIAC_NT_PREL FROM SOFTRE.Y_DISPON_V01 WHERE ID_AZIENDA = '" + Azienda.getAziendaCorrente() + "' AND "
					+ "ID_ARTICOLO = '" + idArticolo + "' AND ID_MAGAZZINO = '" + idMagazzino + "'";
			cs = new CachedStatement(select);
			rs = cs.executeQuery();
			if(rs.next()) {
				qta = rs.getBigDecimal(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return qta;
	}
}
