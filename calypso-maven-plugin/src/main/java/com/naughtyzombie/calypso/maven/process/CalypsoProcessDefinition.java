package com.naughtyzombie.calypso.maven.process;

import com.naughtyzombie.calypso.maven.ui.CalypsoProcessRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 11/12/12
 * Time: 09:45
 */
public class CalypsoProcessDefinition {
    private String name;
    private String className;

    public CalypsoProcessDefinition(CalypsoProcessRequest cpRequest) {
        setName(cpRequest.getName());
        setClassName(cpRequest.getClassName());
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isDefined() {
        return isNameDefined() && isClassNameDefined();
    }

    public boolean isPartiallyDefined() {
        return isNameDefined()==true || isClassNameDefined()==true;
    }

    public boolean isNameDefined() {
        return StringUtils.isNotEmpty(getName());
    }

    public boolean isClassNameDefined() {
        return StringUtils.isNotEmpty(getClassName());
    }
}
