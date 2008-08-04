package com.bitbakery.plugin.arc.config;

import com.bitbakery.plugin.arc.ArcIcons;
import static com.bitbakery.plugin.arc.ArcStrings.message;
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

@State(name = "ArcConfiguration",
        storages = {@Storage(id = "main", file = "$APP_CONFIG$/arc-settings.xml")})
public class ArcConfiguration implements ApplicationComponent, Configurable, PersistentStateComponent<ArcConfiguration> {

    public String arcInitializationFile = "as.scm";
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
        return "ArcConfiguration";
    }


    @Nls
    public String getDisplayName() {
        return message("plugin.arc.name");
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

    public ArcConfiguration getState() {
        return this;
    }

    public void loadState(ArcConfiguration that) {
        this.mzSchemeHome = that.mzSchemeHome;
        this.arcHome = that.arcHome;
        this.arcInitializationFile = that.arcInitializationFile;
    }
}
