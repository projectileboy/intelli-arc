package com.bitbakery.plugin.arc.repl;

import com.bitbakery.plugin.arc.ArcIcons;
import com.bitbakery.plugin.arc.config.ArcConfigurationForm;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@State(name = "ReplApplicationComponent",
        storages = {@Storage(id = "main", file = "$APP_CONFIG$/arc-settings.xml")})
public class ReplApplicationComponent implements ApplicationComponent, Configurable, PersistentStateComponent<ReplApplicationComponent> {

    public String arcInitializationFile;
    public String mzSchemeHome;
    public String arcHome;

    private volatile ArcConfigurationForm form;


    public void initComponent() {
        // Do nothing
    }

    public void disposeComponent() {
        // Do nothing
    }

    @NotNull
    public String getComponentName() {
        return "ReplApplicationComponent";
    }


    public String getArcInitializationFile() {
        return arcInitializationFile;
    }

    public void setArcInitializationFile(String arcInitializationFile) {
        this.arcInitializationFile = arcInitializationFile;
    }

    public String getMzSchemeHome() {
        return mzSchemeHome;
    }

    public void setMzSchemeHome(final String mzSchemeHome) {
        this.mzSchemeHome = mzSchemeHome;
    }

    public String getArcHome() {
        return arcHome;
    }

    public void setArcHome(final String arcHome) {
        this.arcHome = arcHome;
    }


    @Nls
    public String getDisplayName() {
        return com.bitbakery.plugin.arc.ArcResourceBundle.message("plugin.arc.name");
    }

    public Icon getIcon() {
        return ArcIcons.ARC_CONFIG_ICON;
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        // TODO - How does one get a help topic created and loaded? Is this it?
        return null;
    }

    public JComponent createComponent() {
        if (form == null) {
            form = new ArcConfigurationForm();
            form.setData(this);
        }
        return form.getRootComponent();
    }

    public boolean isModified() {
        return form != null && form.isModified(this);
    }

    public void apply() throws ConfigurationException {
        if (form != null) {
            form.getData(this);
        }
    }

    public void reset() {
        if (form != null) {
            form.setData(this);
        }
    }

    public void disposeUIResources() {
        form = null;
    }

    public ReplApplicationComponent getState() {
        return this;
    }

    public void loadState(ReplApplicationComponent that) {
        this.mzSchemeHome = that.mzSchemeHome;
        this.arcHome = that.arcHome;
        this.arcInitializationFile = that.arcInitializationFile;
    }
}
