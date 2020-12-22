package com.dfsek.terra.addon;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;

import java.util.ArrayList;
import java.util.List;

public class AddonYml implements ConfigTemplate {
    @Value("main-class")
    private String mainClass;
    @Value("contributors")
    @Default
    private List<String> contributors = new ArrayList<>();
    @Value("author")
    @Default
    private String author = "no author";
    @Value("api-version")
    @Default
    private String version = "0.0.0";

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AddonYml{" +
                "mainClass='" + mainClass + '\'' +
                ", contributors=" + contributors +
                ", author='" + author + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
