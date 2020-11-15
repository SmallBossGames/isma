package env.project;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Bessonov Alex on 20.07.2016.
 */
@XmlType
@XmlEnum(String.class)
public enum IsmaProjectType {
    @XmlEnumValue("LISMA_PDE") LISMA_PDE,
    @XmlEnumValue("LISMA_EPS") LISMA_EPS
}
