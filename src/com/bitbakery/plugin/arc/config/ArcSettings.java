package com.bitbakery.plugin.arc.config;

/*
 * Copyright (c) Kurt Christensen, 2009
 *
 *  Licensed under the Artistic License, Version 2.0 (the "License"); you may not use this
 *  file except in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/artistic-license-2.0.php
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 *  OF ANY KIND, either express or implied. See the License for the specific language
 *  governing permissions and limitations under the License..
 */

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.RoamingTypeDisabled;
import com.intellij.util.xmlb.XmlSerializerUtil;

/**
 * Intelli-Arc configuration settings
 */
@State(
        name = "ArcSettings",
        storages = {
                @Storage(
                        id = "main",
                        file = "$APP_CONFIG$/arc-settings.xml"
                )}
)
public class ArcSettings implements PersistentStateComponent<ArcSettings>, RoamingTypeDisabled {

    public String arcHome = "";
    public String mzSchemeHome = "";
    public String arcInitializationFile = "as.scm";

    public ArcSettings getState() {
        return this;
    }

    public void loadState(ArcSettings that) {
        XmlSerializerUtil.copyBean(that, this);
    }

    public static ArcSettings getInstance() {
        return ServiceManager.getService(ArcSettings.class);
    }
}
