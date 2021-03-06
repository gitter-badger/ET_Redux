/*
 * SampleTreeChangeI.java
 *
 * Created on March 19, 2008, 7:32 PM
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

package org.earthtime.UPb_Redux.dateInterpretation;

/**
 *
 * @author James F. Bowring
 */
public interface SampleTreeChangeI {

    /**
     * 
     * @param nodeInfo
     */
    abstract void sampleTreeChangeAnalysisMode(Object nodeInfo);

    /**
     * 
     * @param nodeInfo
     */
    abstract void sampleTreeChangeCompilationMode(Object nodeInfo);
}
