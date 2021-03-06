/*
 * KoslerAgilent7700FileHandler
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
package org.earthtime.Tripoli.rawDataFiles.handlers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.earthtime.Tripoli.fractions.TripoliFraction;
import org.earthtime.Tripoli.sessions.TripoliSessionInterface;
import org.earthtime.utilities.FileHelper;
import org.earthtime.archivingTools.URIHelper;

/**
 *
 * @author James F. Bowring
 */
public class KoslerAgilent7700FileHandler extends AbstractRawDataFileHandler implements //
        Comparable<AbstractRawDataFileHandler>,
        Serializable {

    private static KoslerAgilent7700FileHandler instance = null;
    private File[] analysisFiles;

    /**
     *
     * @param massSpec
     * @param rawDataFileTemplate
     */
    private KoslerAgilent7700FileHandler( //
            /*
             * AbstractMassSpecSetup massSpec,// AbstractRawDataFileTemplate
             * rawDataFileTemplate
             */) {

        super();//massSpec, rawDataFileTemplate );

        NAME = "Kosler Agilent 7700 File";

        aboutInfo = "Details: This is the Kosler protocol for an Agilent 7700.";
    }

    /**
     *
     * @return
     */
    public static KoslerAgilent7700FileHandler getInstance( //
            /*
             * AbstractMassSpecSetup massSpec,// AbstractRawDataFileTemplate
             * rawDataFileTemplate
             */) {
        if (instance == null) {
            instance = new KoslerAgilent7700FileHandler();//massSpec, rawDataFileTemplate );
        }
        return instance;
    }

    /**
     *
     * @param tripoliRawDataFolder
     * @return
     */
    @Override
    public File validateAndGetHeaderDataFromRawIntensityFile(File tripoliRawDataFolder) {
        String dialogTitle = "Select an Agilent 7700 Raw Data Folder:";

        rawDataFile = FileHelper.AllPlatformGetFolder(dialogTitle, tripoliRawDataFolder);

        return rawDataFile;
    }

    /**
     *
     * @param loadDataTask the value of loadRawDataTaskListener
     * @param usingFullPropagation the value of usingFullPropagation
     * @param leftShadeCount the value of leftShadeCount
     * @param ignoreFirstFractions the value of ignoreFirstFract
     * @return ions
     */
    @Override
    public File getAndLoadRawIntensityDataFile(SwingWorker loadDataTask, boolean usingFullPropagation, int leftShadeCount, int ignoreFirstFractions) {

        // Agilent has folder of folders plus some xls files
        analysisFiles = rawDataFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.toLowerCase().endsWith(".csv"));
            }
        });

        if (analysisFiles.length > 0) {
            String onPeakFileContents = URIHelper.getTextFromURI(analysisFiles[0].getAbsolutePath());
            if (isValidRawDataFileType(analysisFiles[0]) //
                    && //
                    areKeyWordsPresent(onPeakFileContents)) {
                // create fractions from raw data and perform corrections and calculate ratios
                loadRawDataFile(loadDataTask, usingFullPropagation, leftShadeCount, 0);
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    new String[]{"Selected raw data folder does not contain valid files."},
                    "ET Redux Warning",
                    JOptionPane.WARNING_MESSAGE);

            rawDataFile = null;
        }

        return rawDataFile;
    }

    /**
     *
     * @return
     */
    @Override
    protected boolean areKeyWordsPresent(String fileContents) {
        boolean retVal = true;

        retVal = retVal && fileContents.contains(getRawDataFileTemplate().getStartOfFirstLine().trim());

        return retVal;
    }

    /**
     *
     * @param fractionID
     * @return
     */
    @Override
    public boolean isStandardFractionID(String fractionID) {
        boolean retVal = false;
        for (String standardID : getRawDataFileTemplate().getStandardIDs()) {
            retVal = retVal || fractionID.toUpperCase().contains(standardID.toUpperCase());
        }

        return retVal;
    }

    /**
     *
     *
     * @param loadDataTask the value of loadDataTask
     * @param usingFullPropagation the value of usingFullPropagation
     * @param leftShadeCount the value of leftShadeCount
     * @param ignoreFirstFractions the value of ignoreFirstFractions
     * @return
     */
    @Override
    protected SortedSet<TripoliFraction> loadRawDataFile(SwingWorker loadDataTask, boolean usingFullPropagation, int leftShadeCount, int ignoreFirstFractions) {
        tripoliFractions = new TreeSet<>();

        // assume we are golden        
        for (int f = 0; f < analysisFiles.length; f++) {

            if (loadDataTask.isCancelled()) {
                break;
            }
            loadDataTask.firePropertyChange("progress", 0, ((100 * f) / analysisFiles.length));

            // there should be one only .csv file per folder
            String fractionID = analysisFiles[f].getName().toUpperCase().replace(".CSV", "");

            // get file contents
            String fractionFileContents = URIHelper.getTextFromURI(analysisFiles[f].getAbsolutePath());
            String[] fractionFileRows = fractionFileContents.split("\n");

            // first get time stamp for file in row 2
            String timeStampFromRow2[] = fractionFileRows[2].split(" :")[1].split(" +");

            String fractionDate = //
                    timeStampFromRow2[1] + " " // day/month/year
                    + timeStampFromRow2[2] + " " // hour:min:sec
                    + timeStampFromRow2[3] + " " // AM/PM
                    ;

            // Get the default MEDIUM/SHORT DateFormat
            DateFormat fractionTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

            // Parse the fractionDateValue
            Date fractionDateValue;
            try {
                fractionDateValue = fractionTimeFormat.parse(fractionDate);

                // assume change to peak at line 119 for now
                int assumedBackgrounRowCount = 180;
                long fractionBackgroundTimeStamp = fractionDateValue.getTime();
                long fractionPeakTimeStamp = fractionDateValue.getTime() + assumedBackgrounRowCount * massSpec.getCOLLECTOR_DATA_FREQUENCY_MILLISECS();

                System.out.println("\n**** AGILENT FractionID " + fractionID + "  " + fractionDateValue.toString() + "  row count = " + fractionFileRows.length);

                // then process whole file because it includes the background as well as the peak                   
                // create background and peak 
                // note each row has relative time stamp which we are hiding for now by using frequency of read
                int expectedRowsOfData = rawDataFileTemplate.getBlockSize();
                // scan data has background columns then peak columns per row
                String[][] scanData = //
                        new String[assumedBackgrounRowCount][massSpec.getVIRTUAL_COLLECTOR_COUNT()];

                //TODO possible missing condition here if file lengths vary from template spec and fractionFileRows is too big
                for (int i = 0; i < expectedRowsOfData; i++) {

                    // handle case where there is not as many lines of data as expected
                    String[] fractionCollectorsColumns = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",};
                    if (fractionFileRows.length > (i + rawDataFileTemplate.getBlockStartOffset())) {
                        fractionCollectorsColumns = //
                                fractionFileRows[i + rawDataFileTemplate.getBlockStartOffset()].split(",");
                    }

                    if (i < assumedBackgrounRowCount) {
                        // column 5 is first isotope
                        // background
                        for (int j = 5; j < (massSpec.getVIRTUAL_COLLECTOR_COUNT() / 2) + 5; j++) {
                            scanData[i][j - 5] = fractionCollectorsColumns[j].trim();
                        }
                    } else {
                        // onpeak
                        for (int j = 5; j < (massSpec.getVIRTUAL_COLLECTOR_COUNT() / 2) + 5; j++) {
                            scanData[i - assumedBackgrounRowCount][(massSpec.getVIRTUAL_COLLECTOR_COUNT() / 2) + j - 5] //
                                    = fractionCollectorsColumns[j].trim();
                        }
                    }
                }

                // extract isStandard
                boolean isStandard = isStandardFractionID(fractionID);

                TripoliFraction tripoliFraction = //                           
                        new TripoliFraction( //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                //
                                fractionID, //
                                massSpec.getCommonLeadCorrectionHighestLevel(), //
                                isStandard,
                                fractionBackgroundTimeStamp, //
                                fractionPeakTimeStamp,massSpec.rawRatiosFactory(scanData, isStandard, fractionID, usingFullPropagation, null));

                tripoliFraction.shadeDataActiveMapLeft(leftShadeCount);
                tripoliFractions.add(tripoliFraction);
//                System.out.println( "Successfully added frac " + fractionID + " >>  " + tripoliFractions.add( tripoliFraction ) );
//
//                System.out.println( "                                       Count of stored fractions = " + tripoliFractions.size() );
//                System.out.println( fractionID //
//                        + " " + isStandard + " time = " + TimeToString.secondsAsLongToTimeString( fractionPeakTimeStamp ) );

                // }
            } catch (ParseException parseException) {
                // TODO: drop out here
            }
        }

        if (tripoliFractions.isEmpty()) {
            tripoliFractions = null;
        }

        return tripoliFractions;
    }
}
