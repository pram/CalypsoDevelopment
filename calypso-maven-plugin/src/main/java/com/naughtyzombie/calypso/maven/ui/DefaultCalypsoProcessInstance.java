package com.naughtyzombie.calypso.maven.ui;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 11/12/12
 * Time: 11:14
 */
public class DefaultCalypsoProcessInstance implements CalypsoProcessInstance {

    private String id;
    private String qualifier;

    public DefaultCalypsoProcessInstance(String id) {
        this.id=id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getQualifier() {
        return qualifier;
    }

    @Override
    public int compareTo(Object o) {
        CalypsoProcessInstance otherInstance = (CalypsoProcessInstance) o;

        int result = 0;
        /*if (id != null) {
            String otherId = otherInstance.getId();
            if ((id.length() > otherId.length()) && id.startsWith(otherId)) {
                result = -1;
            } else if ((id.length() < otherId.length()) && otherId.startsWith(id)) {
                result = 1;
            }
        }

        if (result==0) {
            if (qualifier != null) {
                String otherQualifier = otherInstance.getQualifier();
                if ((id.length() > otherQualifier.length()) && id.startsWith(otherQualifier)) {
                    result = -1;
                } else if ((id.length() < otherQualifier.length()) && otherQualifier.startsWith(id)) {
                    result = 1;
                }
            }
        }*/

        if (!this.id.equalsIgnoreCase(otherInstance.getId())) {
            result = this.id.compareTo(otherInstance.getId());
        } else if (!this.qualifier.equalsIgnoreCase(otherInstance.getQualifier())) {
            result = this.qualifier.compareTo(otherInstance.getQualifier());
        }

        return result;

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultCalypsoProcessInstance that = (DefaultCalypsoProcessInstance) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (qualifier != null ? !qualifier.equals(that.qualifier) : that.qualifier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DefaultCalypsoProcessInstance{" +
                "id='" + id + '\'' +
                ", qualifier='" + qualifier + '\'' +
                '}';
    }
}
