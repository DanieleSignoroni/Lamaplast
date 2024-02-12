package it.lamaplast.thip.vendite.pickingPacking.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.AvailableReport;
import com.thera.thermfw.batch.CrystalReportsInterface;
import com.thera.thermfw.batch.ElaboratePrintRunnable;
import com.thera.thermfw.batch.PrintingToolInterface;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.security.Authorizable;

import it.lamaplast.thip.logisticaLight.YRptStampaEtichette;
import it.lamaplast.thip.logisticaLight.YRptStampaEtichetteTM;
import it.lamaplast.thip.logisticaLight.YStoricoStampaEtichette;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.generale.IntegrazioneThipLogis;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnr;
import it.thera.thip.vendite.pickingPacking.DocumentoVenRigaPplCnrTM;
import it.thera.thip.vendite.pickingPacking.PackingList;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 08/02/2024
 * <br><br>
 * <b>71425	DSSOF3	08/02/2024</b>    
 * <p>Stampa etichette lato gestionale.</p>
 */

public class YStampaEtichetteGest extends ElaboratePrintRunnable implements Authorizable{

	public static final String YLAMA_005 = "YLAMA_005";

	protected String iBarcode;

	protected char iStampa;

	//
	public static final char STAMPA = '0';
	public static final char RISTAMPA = '1';
	//

	protected Integer rigaJob = 0;

	protected AvailableReport iAvailableRpt;

	public String getBarcode() {
		return iBarcode;
	}

	public void setBarcode(String iBarcode) {
		this.iBarcode = iBarcode;
	}

	public char getStampa() {
		return iStampa;
	}

	public void setStampa(char iStampa) {
		this.iStampa = iStampa;
	}

	public AvailableReport getiAvailableRpt() {
		return iAvailableRpt;
	}

	public void setiAvailableRpt(AvailableReport iAvailableRpt) {
		this.iAvailableRpt = iAvailableRpt;
	}

	protected boolean createAvailableReport() throws Exception{
		job.setReportCounter((short)0);
		iAvailableRpt = createNewReport(getReportId());
		if(iAvailableRpt == null)
			return false;

		try {
			setPrintToolInterface((PrintingToolInterface)Factory.createObject(CrystalReportsInterface.class));
			String s = printToolInterface.generateDefaultWhereCondition(iAvailableRpt, YRptStampaEtichetteTM.getInstance());
			iAvailableRpt.setWhereCondition(s);
			int res = iAvailableRpt.save();
			if(res < 0) {
				System.out.println("Problema di salvataggio availableReport, errorCode = " + res);
				return false;
			}
		}
		catch(SQLException e) {
			e.printStackTrace(Trace.excStream);
			return false;
		}
		return true;
	}

	protected ErrorMessage checkBarcode() {
		try {
			String key = KeyHelper.buildObjectKey(new String[] { 
					Azienda.getAziendaCorrente(),
					"20" + this.getBarcode().substring(1, 5).trim(),
					this.getBarcode().substring(5).trim()
			});
			PackingList packingList;
			packingList = (PackingList) PackingList.elementWithKey(PackingList.class, key,
					PersistentObject.NO_LOCK);
			if (packingList == null) {
				return new ErrorMessage(YLAMA_005,"Non esiste nessun imballo con quel barcode");
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
			return new ErrorMessage(YLAMA_005,e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace(Trace.excStream);
			return new ErrorMessage(YLAMA_005,"Formato barcode errato");
		}
		return null;
	}

	@Override
	public boolean createReport() {
		boolean isOk;
		try {
			isOk = createAvailableReport();
			if(isOk == false) {
				output.println("ERRORE: AvailableReport non disponibile!!!");
				return false;
			}else {
				isOk = stampaEtichette();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.createReport();
	}

	protected boolean stampaEtichette() {
		boolean ok = true;
		try {
			String barcode = this.getBarcode();
			if (barcode.charAt(0) == IntegrazioneThipLogis.VENDITA) {
				ok = stampaLivelloDocumentoVendita(barcode);
			} else if (barcode.contains("PL")) {
				ok = stampaLivelloPallet(barcode);
			} else if (barcode.contains("SC")) {
				ok = stampaLivelloScatola(barcode);
			}
		}catch (SQLException e) {
			output.println("<ERRORE: "+e.getMessage());
			e.printStackTrace(Trace.excStream);
			ok = false;
		}
		return ok;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean stampaLivelloScatola(String barcode) throws SQLException {
		boolean ok = true;
		DocumentoVenRigaPplCnr docRig = getScatola(barcode);
		if (docRig != null) {
			List scatole = new ArrayList();
			scatole.add(docRig);
			ok = riempiTabellRpt(scatole, this.getStampa());
		}
		return ok;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean stampaLivelloPallet(String barcode) throws SQLException {
		boolean ok = true;
		DocumentoVenRigaPplCnr pallet = getPallet(barcode);
		List scatole = pallet.getImballiFigli();
		List pallets = new ArrayList();
		pallets.add(pallet);
		ok = riempiTabellRpt(pallets,this.getStampa());
		ok = riempiTabellRpt(scatole,this.getStampa());
		return ok;
	}

	@SuppressWarnings("rawtypes")
	protected boolean stampaLivelloDocumentoVendita(String barcode) throws SQLException {
		boolean ok = true;
		String key = KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(),
				"20" + barcode.substring(1, 5).trim(), barcode.substring(5).trim() });
		PackingList packingList = (PackingList) PackingList.elementWithKey(PackingList.class, key,PersistentObject.NO_LOCK);
		List scatole = packingList.getImballiPerLivello(new Short("1"));
		List pallet = packingList.getImballiPerLivello(new Short("2"));
		ok = riempiTabellRpt(pallet, this.getStampa());
		ok = riempiTabellRpt(scatole, this.getStampa());
		return ok;
	}

	/**
	 * L'etichetta e' da stampare solo in 3 casi:<br>
	 * <ul>
	 * 	<li>Non e' presente un record in {@link YStoricoStampaEtichette} </li>
	 *  <li>L'utente ha aggiornato l'anagrafica {@link DocumentoVenRigaPplCnr} </li>
	 *  <li>L'utente ha scelto {{@link #RISTAMPA}} nella form di lancio</li>
	 * </ul>
	 * @param row
	 * @param storico
	 * @param tipoStampa
	 * @return
	 */
	protected static boolean isDaStampare(DocumentoVenRigaPplCnr row, YStoricoStampaEtichette storico, char tipoStampa) {
		if(!storico.isOnDB()) {
			return true;
		}
		if (storico.getDatiComuniEstesi().getTimestampAgg() .compareTo(row.getDatiComuniEstesi().getTimestampAgg()) < 0 || tipoStampa == RISTAMPA) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	protected boolean riempiTabellRpt(List rows, char stampa) throws SQLException {
		Iterator iterRows = rows.iterator();
		while(iterRows.hasNext()) {
			DocumentoVenRigaPplCnr row = (DocumentoVenRigaPplCnr) iterRows.next();
			YStoricoStampaEtichette storico = (YStoricoStampaEtichette) Factory.createObject(YStoricoStampaEtichette.class);
			storico.setKey(row.getKey());
			storico.retrieve();
			if(isDaStampare(row, storico, stampa)) {
				storico.getDatiComuniEstesi().setTimestampAgg(row.getDatiComuniEstesi().getTimestampAgg());
				YRptStampaEtichette rpt = (YRptStampaEtichette) Factory.createObject(YRptStampaEtichette.class);
				rpt.setIdAzienda(Azienda.getAziendaCorrente());
				rpt.setIdAnnoImb(row.getIdAnnoImb());
				rpt.setIdNumeroImb(row.getIdNumeroImb());
				rpt.setIdAnnoDoc(row.getDocumentoVenPPL().getIdAnnoDoc());
				rpt.setIdNumeroDoc(row.getDocumentoVenPPL().getIdNumeroDoc());
				rpt.setBatchJobId(this.getBatchJob().getBatchJobId());
				rpt.setReportNr(1);
				rpt.setRigaJobId(rigaJob++);
				rpt.setLivello(row.getIdLivello());
				if (row.getImballoPadre() != null) {
					DocumentoVenRigaPplCnr imbPadre = row.getImballoPadre();
					rpt.setAnnoImbPadre(imbPadre.getIdAnnoImb());
					rpt.setNumeroImbPadre(imbPadre.getIdNumeroImb());
				}
				int rc1 = rpt.save();
				int rc2 = storico.save(true);
				if (rc1+rc2 > 0) {
					ConnectionManager.commit();
				} else {
					ConnectionManager.rollback();
					return false;
				}
			}
		}
		return true;
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

	protected DocumentoVenRigaPplCnr getScatola(String barcode) {
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

	@Override
	protected String getClassAdCollectionName() {
		return "YStampaEtichetteGest";
	}


}
