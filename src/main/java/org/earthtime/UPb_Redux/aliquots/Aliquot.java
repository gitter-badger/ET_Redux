
/*
 * Aliquot.java
 *
 * Created on June 7, 2007, 3:40 PM
 *
 *
 * Copyright 2006-2015 James F. Bowring and www.Earth-Time.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.earthtime.UPb_Redux.aliquots;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import org.earthtime.UPb_Redux.ReduxConstants;
import org.earthtime.UPb_Redux.ReduxConstants.ANALYSIS_PURPOSE;
import org.earthtime.UPb_Redux.fractions.Fraction;
import org.earthtime.UPb_Redux.valueModels.SampleDateInterceptModel;
import org.earthtime.UPb_Redux.valueModels.SampleDateModel;
import org.earthtime.UPb_Redux.valueModels.ValueModel;
import org.earthtime.dataDictionaries.DataDictionary;
import org.earthtime.dataDictionaries.SampleDateTypes;
import org.earthtime.ratioDataModels.AbstractRatiosDataModel;
import org.earthtime.ratioDataModels.pbBlankICModels.PbBlankICModel;
import org.earthtime.ratioDataModels.physicalConstantsModels.PhysicalConstantsModel;
import org.earthtime.ratioDataModels.tracers.TracerUPbModel;

/**
 *
 * @author James F. Bowring
 */
public abstract class Aliquot implements AliquotI, Serializable {

    // Class variables
    private static final long serialVersionUID = 6355007168312036059L;
    // Instance variables
    /**
     * SESAR produces IGSN and eventually we will tie to their database.
     */
    private String sampleIGSN;
    /**
     * Lab's local name for the Aliquot.
     */
    private String aliquotName;
    private String aliquotIGSN;
    private String laboratoryName;
    private String analystName;
    private String aliquotReference;
    private String aliquotInstrumentalMethod;
    private String aliquotInstrumentalMethodReference;
    private String aliquotComment;
    private BigDecimal calibrationUnct206_238;
    private BigDecimal calibrationUnct208_232;
    private BigDecimal calibrationUnct207_206;
    protected Vector<ValueModel> sampleDateModels;
    private AbstractRatiosDataModel physicalConstantsModel;
    private Vector<AbstractRatiosDataModel> pbBlanks;
    private Vector<AbstractRatiosDataModel> tracers;
    private Vector<ValueModel> alphaPbModels;
    private Vector<ValueModel> alphaUModels;
    private Vector<AbstractRatiosDataModel> MineralStandardModels;
    private Vector<Fraction> analysisFractions;
    private ANALYSIS_PURPOSE analysisPurpose;
    private String keyWordsCSV;

    //may 2014
    /**
     *
     */
    protected BigDecimal bestAgeDivider206_238;

    /**
     * Creates a new instance of Aliquot
     */
    public Aliquot() {
        this.sampleIGSN = ReduxConstants.DEFAULT_IGSN;
        this.aliquotName = ReduxConstants.DEFAULT_OBJECT_NAME;
        this.aliquotIGSN = ReduxConstants.DEFAULT_ALIQUOT_IGSN;
        this.laboratoryName = "NONE";
        this.analystName = "NONE";
        this.aliquotReference = "NONE";
        this.aliquotInstrumentalMethod = DataDictionary.AliquotInstrumentalMethod[0];
        this.aliquotInstrumentalMethodReference = "NONE";
        this.aliquotComment = "NONE";

        this.calibrationUnct206_238 = BigDecimal.ZERO;
        this.calibrationUnct207_206 = BigDecimal.ZERO;
        this.calibrationUnct208_232 = BigDecimal.ZERO;

        this.sampleDateModels = new Vector<>();

        this.physicalConstantsModel = PhysicalConstantsModel.getEARTHTIMEPhysicalConstantsModel();

        this.pbBlanks = new Vector<>();

        this.tracers = new Vector<>();
        this.alphaPbModels = new Vector<ValueModel>();
        this.alphaUModels = new Vector<>();

        this.MineralStandardModels = new Vector<>();

        this.analysisFractions = new Vector<>();

        this.analysisPurpose = ANALYSIS_PURPOSE.SingleAge;

        this.keyWordsCSV = "";

        this.bestAgeDivider206_238 = BigDecimal.ZERO;

    }

    /**
     *
     * @param aliquotIGSN
     */
    public Aliquot(String aliquotIGSN) {
        this();
        this.aliquotIGSN = aliquotIGSN.trim();
    }

    // public accessors
    /**
     *
     * @return
     */
    public String getSampleIGSN() {
        return sampleIGSN;
    }

    /**
     *
     * @return
     */
    public String getSampleIGSNnoRegistry() {
        String retVal = "";
        String parse[] = sampleIGSN.split("\\.");
        if (parse.length > 0) {
            // returns index 0 if no registry, 1 otherwise
            retVal = parse[parse.length - 1];
        }

        return retVal;
    }

    /**
     *
     * @param sampleIGSN
     */
    @Override
    public void setSampleIGSN(String sampleIGSN) {
        this.sampleIGSN = sampleIGSN.trim();
    }

    /**
     *
     * @return
     */
    @Override
    public String getAliquotIGSN() {
        return aliquotIGSN;
    }

    /**
     *
     * @param aliquotIGSN
     */
    @Override
    public void setAliquotIGSN(String aliquotIGSN) {
        this.aliquotIGSN = aliquotIGSN.trim();
    }

    /**
     *
     * @return
     */
    @Override
    public String getLaboratoryName() {
        return laboratoryName;
    }

    /**
     *
     * @param laboratoryName
     */
    @Override
    public void setLaboratoryName(String laboratoryName) {
        this.laboratoryName = laboratoryName.trim();
    }

    /**
     *
     * @return
     */
    @Override
    public String getAnalystName() {
        return analystName;
    }

    /**
     *
     * @param analystName
     */
    @Override
    public void setAnalystName(String analystName) {
        this.analystName = analystName.trim();

    }

    /**
     *
     * @return
     */
    @Override
    public String getAliquotReference() {
        return aliquotReference;
    }

    /**
     *
     * @param aliquotReference
     */
    @Override
    public void setAliquotReference(String aliquotReference) {
        this.aliquotReference = aliquotReference.trim();
    }

    /**
     *
     * @return
     */
    @Override
    public String getAliquotInstrumentalMethod() {
        return aliquotInstrumentalMethod;
    }

    /**
     *
     * @param aliquotInstrumentalMethod
     */
    @Override
    public void setAliquotInstrumentalMethod(String aliquotInstrumentalMethod) {
        this.aliquotInstrumentalMethod = aliquotInstrumentalMethod.trim();
    }
    /* "ID-TIMS",
     "SHRIMP Ion Probe",
     "Cameca Ion Probe",
     "Quad ICPMS",
     "HR-ICPMS",
     "MC-ICPMS"
     */

    public boolean usesIDTIMS() {
        return (aliquotInstrumentalMethod.equalsIgnoreCase("ID-TIMS"));
    }

    public boolean usesMCIPMS() {
        return (aliquotInstrumentalMethod.equalsIgnoreCase("MC-ICPMS"));
    }

    /**
     *
     * @return
     */
    @Override
    public String getAliquotInstrumentalMethodReference() {
        return aliquotInstrumentalMethodReference;
    }

    /**
     *
     * @param aliquotInstrumentalMethodReference
     */
    @Override
    public void setAliquotInstrumentalMethodReference(String aliquotInstrumentalMethodReference) {
        this.aliquotInstrumentalMethodReference = aliquotInstrumentalMethodReference.trim();
    }

    /**
     *
     * @return
     */
    @Override
    public String getAliquotComment() {
        return aliquotComment;
    }

    /**
     *
     * @param aliquotComment
     */
    @Override
    public void setAliquotComment(String aliquotComment) {
        this.aliquotComment = aliquotComment.trim();
    }

    /**
     *
     * @return
     */
    public AbstractRatiosDataModel getPhysicalConstants() {
        return physicalConstantsModel;
    }

    /**
     *
     * @param physicalConstants
     */
    public void setPhysicalConstants(AbstractRatiosDataModel physicalConstants) {
        this.physicalConstantsModel = physicalConstants;
    }

    /**
     *
     * @return
     */
    public Vector<AbstractRatiosDataModel> getPbBlanks() {
        return pbBlanks;
    }

    /**
     *
     * @return
     */
    public Vector<AbstractRatiosDataModel> getPbBlanksForXMLSerialization() {
        // remove placeholder <none>
        Vector<AbstractRatiosDataModel> temp = new Vector<AbstractRatiosDataModel>();
        for (AbstractRatiosDataModel p : pbBlanks) {
            if (!(p.equals(PbBlankICModel.getNoneInstance()))) {
                temp.add(p);
            }
        }
        return temp;
    }

    /**
     *
     * @param pbBlankNameAndVersion
     * @return
     */
    public AbstractRatiosDataModel getAPbBlank(String pbBlankNameAndVersion) {
        // we look for name of PbBlank by walking list
        Iterator it = getPbBlanks().iterator();
        AbstractRatiosDataModel retval = null;
        while (it.hasNext()) {
            retval = (AbstractRatiosDataModel) it.next();
            if (retval.getNameAndVersion().
                    equalsIgnoreCase(pbBlankNameAndVersion.trim())) {
                return retval;
            } else {
                retval = null;
            }
        }
        return retval;
    }

    /**
     *
     * @param pbBlanks
     */
    public void setPbBlanks(Vector<AbstractRatiosDataModel> pbBlanks) {
        this.pbBlanks = pbBlanks;
    }

    /**
     *
     * @return
     */
    public Vector<AbstractRatiosDataModel> getTracers() {
        return tracers;
    }

    /**
     *
     * @return
     */
    public Vector<AbstractRatiosDataModel> getTracersForXMLSerialization() {
        // remove placeholder <none>
        Vector<AbstractRatiosDataModel> temp = new Vector<AbstractRatiosDataModel>();
        for (AbstractRatiosDataModel t : tracers) {
            if (!(t.equals(TracerUPbModel.getNoneInstance()))) {//         getNameAndVersion().startsWith( "<none>" )|| t.getNameAndVersion().startsWith( ReduxConstants.NONE)) ) {
                temp.add(t);
            }
        }
        return temp;
    }

    /**
     *
     * @param tracerNameandVersion
     * @return
     */
    public AbstractRatiosDataModel getATracer(String tracerNameandVersion) {
        // we look for name of Tracer by walking list
        Iterator it = getTracers().iterator();
        AbstractRatiosDataModel retval = null;
        while (it.hasNext()) {
            retval = (AbstractRatiosDataModel) it.next();
            if (retval.getNameAndVersion().
                    equalsIgnoreCase(tracerNameandVersion.trim())) {
                return retval;
            } else {
                retval = null;
            }
        }

        return retval;
    }

    /**
     *
     * @param tracers
     */
    public void setTracers(Vector<AbstractRatiosDataModel> tracers) {
        this.tracers = tracers;
    }

    /**
     * @return the alphaPbModels
     */
    public Vector<ValueModel> getAlphaPbModels() {
        return alphaPbModels;
    }

    /**
     *
     * @return
     */
    public Vector<ValueModel> getAlphaPbModelsForXMLSerialization() {
        // remove placeholder <none>
        Vector<ValueModel> temp = new Vector<ValueModel>();
        for (ValueModel a : alphaPbModels) {
            if (!(a.getName().startsWith("<none>") || a.getName().startsWith(ReduxConstants.NONE))) {
                temp.add(a);
            }
        }
        return temp;
    }

    /**
     *
     * @param alphaPbModelName
     * @return
     */
    public ValueModel getAnAlphaPbModel(String alphaPbModelName) {
        // we look for name of AlphaPbModel by walking list
        Iterator it = getAlphaPbModels().iterator();
        ValueModel retval = null;
        while (it.hasNext()) {
            retval = (ValueModel) it.next();
            if (retval.getName().
                    equalsIgnoreCase(alphaPbModelName.trim())) {
                return retval;
            } else {
                retval = null;
            }
        }

        return retval;
    }

    /**
     * @param alphaPbModels the alphaPbModels to set
     */
    public void setAlphaPbModels(Vector<ValueModel> alphaPbModels) {
        this.alphaPbModels = alphaPbModels;
    }

    /**
     * @return the alphaUModels
     */
    public Vector<ValueModel> getAlphaUModels() {
        return alphaUModels;
    }

    /**
     *
     * @return
     */
    public Vector<ValueModel> getAlphaUModelsForXMLSerialization() {
        // remove placeholder <none>
        Vector<ValueModel> temp = new Vector<ValueModel>();
        for (ValueModel a : alphaUModels) {
            if (!(a.getName().startsWith("<none>") || a.getName().startsWith(ReduxConstants.NONE))) {
                temp.add(a);
            }
        }
        return temp;
    }

    /**
     *
     * @param alphaUModelName
     * @return
     */
    public ValueModel getAnAlphaUModel(String alphaUModelName) {
        // we look for name of AlphaUModel by walking list
        Iterator it = getAlphaUModels().iterator();
        ValueModel retval = null;
        while (it.hasNext()) {
            retval = (ValueModel) it.next();
            if (retval.getName().
                    equalsIgnoreCase(alphaUModelName.trim())) {
                return retval;
            } else {
                retval = null;
            }
        }
        return retval;
    }

    /**
     * @param alphaUModels the alphaUModels to set
     */
    public void setAlphaUModels(Vector<ValueModel> alphaUModels) {
        this.alphaUModels = alphaUModels;
    }

    /**
     *
     * @return
     */
    public Vector<Fraction> getAnalysisFractions() {
        return analysisFractions;
    }

    /**
     *
     * @param analysisFractions
     */
    public void setAnalysisFractions(Vector<Fraction> analysisFractions) {
        this.analysisFractions = analysisFractions;
    }

    /**
     *
     * @return
     */
    public String getAliquotName() {
        return aliquotName;
    }

    /**
     *
     * @param aliquotName
     */
    public void setAliquotName(String aliquotName) {
        if (aliquotName.trim().length() > 0) {
            this.aliquotName = aliquotName.trim();
        }
    }

    /**
     *
     * @return
     */
    public Vector<AbstractRatiosDataModel> getMineralStandardModels() {
        return MineralStandardModels;
    }

    /**
     *
     * @param MineralStandards
     */
    public void setMineralStandardModels(Vector<AbstractRatiosDataModel> MineralStandards) {
        this.MineralStandardModels = MineralStandards;
    }

    /**
     *
     * @param modelNameAndVersion
     * @return
     */
    public AbstractRatiosDataModel getAMineralStandardModelByName(String modelNameAndVersion) {
        Iterator<AbstractRatiosDataModel> it = getMineralStandardModels().iterator();
        AbstractRatiosDataModel retval = null;
        while (it.hasNext()) {
            retval = it.next();
            if (retval.getNameAndVersion().
                    equalsIgnoreCase(modelNameAndVersion.trim())) {
                return retval;
            } else {
                retval = null;
            }
        }
        return retval;
    }

    /**
     *
     * @return
     */
    public Vector<ValueModel> getSampleDateModels() {
        return sampleDateModels;
    }

    /**
     *
     * @return
     */
    public Vector<ValueModel> legalizeSampleDateModels() {
        // created april 2010 to remove any sample date model that has no fractions
        // in preparation for publishing as xml

        Vector<ValueModel> tempSampleDateModels = new Vector<ValueModel>();
        boolean existsPreferredDate = false;

        // first update models to clean up fraction lists
        updateSampleDateModels();

        // now check for empties
        for (ValueModel vm : sampleDateModels) {
            if (!((SampleDateModel) vm).getIncludedFractionIDsVector().isEmpty()) {
                tempSampleDateModels.add(vm);
                if (((SampleDateModel) vm).isPreferred()) {
                    existsPreferredDate = true;
                }
            }
        }

        sampleDateModels = tempSampleDateModels;

        // guarantee preferred date model
        if (!existsPreferredDate && (sampleDateModels.size() > 0)) {
            ((SampleDateModel) sampleDateModels.get(0)).setPreferred(true);
        }
        return sampleDateModels;
    }

    /**
     *
     * @param sampleDateModels
     */
    public void setSampleDateModels(Vector<ValueModel> sampleDateModels) {
        this.sampleDateModels = sampleDateModels;
    }

    /**
     *
     * @param modelName
     * @return
     */
    public ValueModel getASampleDateModelByName(String modelName) {
        Iterator it = sampleDateModels.iterator();
        ValueModel retval = null;
        while (it.hasNext()) {
            retval = (SampleDateModel) it.next();
            if (retval.getName().
                    equalsIgnoreCase(modelName.trim())) {
                return retval;
            } else {
                retval = null;
            }
        }
        return retval;
    }

    /**
     *
     * @return
     */
    public ValueModel getPreferredSampleDateModel() {
        ValueModel retVal = null;
        Iterator it = sampleDateModels.iterator();

        while (it.hasNext()) {
            retVal = (SampleDateModel) it.next();
            if (((SampleDateModel) retVal).isPreferred()) {
                return retVal;
            }
        }

        return retVal;
    }

    /**
     *
     * @return
     */
    public Vector<ValueModel> determineUnusedSampleDateModels() {
        Vector<ValueModel> retVal = new Vector<ValueModel>();
        // choose models not already in use by Aliquot
        for (int i = 0; i < SampleDateTypes.getSampleDateModelTypes().length; i++) {
            if (getASampleDateModelByName(SampleDateTypes.getSampleDateType(i)) == null) {
                ValueModel tempModel = null;
                if (SampleDateTypes.getSampleDateType(i).endsWith("intercept")) {
                    tempModel = //
                            new SampleDateInterceptModel(//
                                    SampleDateTypes.getSampleDateType(i),
                                    SampleDateTypes.getSampleDateTypeMethod(i),
                                    SampleDateTypes.getSampleDateTypeName(i),
                                    BigDecimal.ZERO,
                                    "ABS",
                                    BigDecimal.ZERO);
                } else {
                    tempModel = //
                            new SampleDateModel(//
                                    SampleDateTypes.getSampleDateType(i),
                                    SampleDateTypes.getSampleDateTypeMethod(i),
                                    SampleDateTypes.getSampleDateTypeName(i),
                                    BigDecimal.ZERO,
                                    "ABS",
                                    BigDecimal.ZERO);
                }
                ((SampleDateModel) tempModel).setAliquot(this);
                retVal.add(tempModel);
            }
        }
        return retVal;
    }

    /**
     *
     * @param sampleDateModel
     */
    public void setPreferredSampleDateModel(ValueModel sampleDateModel) {
        // set all to false
        for (ValueModel sam : sampleDateModels) {
            ((SampleDateModel) sam).setPreferred(false);
        }
        ((SampleDateModel) sampleDateModel).setPreferred(true);
        Collections.sort(sampleDateModels);
    }

    /**
     *
     * @param sampleDateModelName
     * @return
     */
    public boolean containsSampleDateModelByName(String sampleDateModelName) {
        boolean retVal = false;
        for (ValueModel sam : sampleDateModels) {
            if (sam.getName().equalsIgnoreCase(sampleDateModelName.trim())) {
                retVal = true;
            }
        }
        return retVal;
    }

    /**
     *
     * @param sampleDateModelName
     * @return
     */
    public ValueModel getSampleDateModelByName(String sampleDateModelName) {
        ValueModel retVal = null;
        for (ValueModel sdm : sampleDateModels) {
            if (sdm.getName().equalsIgnoreCase(sampleDateModelName.trim())) {
                retVal = sdm;
            }
        }
        return retVal;
    }

    /**
     *
     */
    public final void updateSampleDateModels() {
        // Nov 2008
        // process all sampleDateModels' included fraction vectors to remove missing fractions
        Vector<String> includedFractionIDs = ((UPbReduxAliquot) this).getAliquotFractionIDs();
        Vector<String> excludedFractionIDs = new Vector<String>();

        boolean existsPreferredDate = false;
        for (ValueModel SAM : sampleDateModels) {
            for (String fractionID : ((SampleDateModel) SAM).getIncludedFractionIDsVector()) {
                if (!includedFractionIDs.contains(fractionID)) {
                    excludedFractionIDs.add(fractionID);
                }
            }
            // remove found exclusions (these are ones that were rejected after processing
            for (String fractionID : excludedFractionIDs) {
                ((SampleDateModel) SAM).getIncludedFractionIDsVector().remove(fractionID);
            }

            if (((SampleDateModel) SAM).isPreferred()) {
                existsPreferredDate = true;
            }

            // oct 2014 per Matt Rioux email report
            try {
                ((SampleDateModel) SAM).setAliquot(this);
                ((SampleDateModel) SAM).CalculateDateInterpretationForAliquot();
            } catch (Exception e) {
            }
        }

        // guarantee preferred date model
        if (!existsPreferredDate && (sampleDateModels.size() > 0)) {
            ((SampleDateModel) sampleDateModels.get(0)).setPreferred(true);
        }
    }

    /**
     *
     * @return
     */
    public BigDecimal getCalibrationUnct206_238() {
        return calibrationUnct206_238;
    }

    /**
     *
     * @param calibrationUnct206_238
     */
    public void setCalibrationUnct206_238(BigDecimal calibrationUnct206_238) {
        this.calibrationUnct206_238 = calibrationUnct206_238;
    }

    /**
     *
     * @return
     */
    public BigDecimal getCalibrationUnct208_232() {
        return calibrationUnct208_232;
    }

    /**
     *
     * @param calibrationUnct208_232
     */
    public void setCalibrationUnct208_232(BigDecimal calibrationUnct208_232) {
        this.calibrationUnct208_232 = calibrationUnct208_232;
    }

    /**
     *
     * @return
     */
    public BigDecimal getCalibrationUnct207_206() {
        return calibrationUnct207_206;
    }

    /**
     *
     * @param calibrationUnct207_206
     */
    public void setCalibrationUnct207_206(BigDecimal calibrationUnct207_206) {
        this.calibrationUnct207_206 = calibrationUnct207_206;
    }

    /**
     * @return the analysisPurpose
     */
    public ANALYSIS_PURPOSE getAnalysisPurpose() {
        if (analysisPurpose == null) {
            analysisPurpose = ANALYSIS_PURPOSE.NONE;
        }
        return analysisPurpose;
    }

    /**
     * @param analysisPurpose the analysisPurpose to set
     */
    public void setAnalysisPurpose(ANALYSIS_PURPOSE analysisPurpose) {
        this.analysisPurpose = analysisPurpose;
    }

    /**
     * @return the keyWordsCSV
     */
    public String getKeyWordsCSV() {
        if (keyWordsCSV == null) {
            keyWordsCSV = "";
        }
        return keyWordsCSV;
    }

    /**
     * @param keyWordsCSV the keyWordsCSV to set
     */
    public void setKeyWordsCSV(String keyWordsCSV) {
        this.keyWordsCSV = keyWordsCSV.trim();
    }

    /**
     * @return the bestAgeDivider206_238
     */
    public BigDecimal getBestAgeDivider206_238() {
        return bestAgeDivider206_238;
    }

    /**
     * @param bestAgeDivider206_238 the bestAgeDivider206_238 to set
     */
    public void setBestAgeDivider206_238(BigDecimal bestAgeDivider206_238) {
        this.bestAgeDivider206_238 = bestAgeDivider206_238;
    }
}
