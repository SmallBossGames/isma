package env.project;

import javax.xml.bind.annotation.*;

/**
 * Created by Bessonov Alex on 20.07.2016.
 */
@XmlRootElement
public class RunConfiguration {

    private String name;

    private Double startTime;
    private Double endTime;укаукпуп
    private Double step;

    private String method;
    private Boolean accurate;
    private Double accuracy;
    private Boolean stable;
    private Boolean parallel;

    private String intgServer;
    private Integer intgPort;

    private ResultStorageType resultStorage;

    private Boolean simplifyCheck;
    private String simplifyAlgorithmCombo;
    private Double simplifyToleranceField;

    private Boolean eventDetectionCheck;
    private Double eventDetectionGammaField;
    private Boolean eventDetectionStepBoundCheck;
    private Double eventDetectionStepBoundLowField;


    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public Double getStartTime() {
        return startTime;
    }

    @XmlElement
    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    @XmlElement
    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public Double getStep() {
        return step;
    }

    @XmlElement
    public void setStep(Double step) {
        this.step = step;
    }

    public String getMethod() {
        return method;
    }

    @XmlElement
    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getAccurate() {
        return accurate;
    }

    @XmlElement
    public void setAccurate(Boolean accurate) {
        this.accurate = accurate;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    @XmlElement
    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Boolean getStable() {
        return stable;
    }

    @XmlElement
    public void setStable(Boolean stable) {
        this.stable = stable;
    }

    public Boolean getParallel() {
        return parallel;
    }

    @XmlElement
    public void setParallel(Boolean parallel) {
        this.parallel = parallel;
    }

    public String getIntgServer() {
        return intgServer;
    }

    @XmlElement
    public void setIntgServer(String intgServer) {
        this.intgServer = intgServer;
    }

    public Integer getIntgPort() {
        return intgPort;
    }

    @XmlElement
    public void setIntgPort(Integer intgPort) {
        this.intgPort = intgPort;
    }

    public ResultStorageType getResultStorage() {
        return resultStorage;
    }

    @XmlElement
    public void setResultStorage(ResultStorageType resultStorage) {
        this.resultStorage = resultStorage;
    }

    public Boolean getSimplifyCheck() {
        return simplifyCheck;
    }

    @XmlElement
    public void setSimplifyCheck(Boolean simplifyCheck) {
        this.simplifyCheck = simplifyCheck;
    }

    public String getSimplifyAlgorithmCombo() {
        return simplifyAlgorithmCombo;
    }

    @XmlElement
    public void setSimplifyAlgorithmCombo(String simplifyAlgorithmCombo) {
        this.simplifyAlgorithmCombo = simplifyAlgorithmCombo;
    }

    public Double getSimplifyToleranceField() {
        return simplifyToleranceField;
    }

    @XmlElement
    public void setSimplifyToleranceField(Double simplifyToleranceField) {
        this.simplifyToleranceField = simplifyToleranceField;
    }

    public Boolean getEventDetectionCheck() {
        return eventDetectionCheck;
    }

    @XmlElement
    public void setEventDetectionCheck(Boolean eventDetectionCheck) {
        this.eventDetectionCheck = eventDetectionCheck;
    }

    public Double getEventDetectionGammaField() {
        return eventDetectionGammaField;
    }

    @XmlElement
    public void setEventDetectionGammaField(Double eventDetectionGammaField) {
        this.eventDetectionGammaField = eventDetectionGammaField;
    }

    public Boolean getEventDetectionStepBoundCheck() {
        return eventDetectionStepBoundCheck;
    }

    @XmlElement
    public void setEventDetectionStepBoundCheck(Boolean eventDetectionStepBoundCheck) {
        this.eventDetectionStepBoundCheck = eventDetectionStepBoundCheck;
    }

    public Double getEventDetectionStepBoundLowField() {
        return eventDetectionStepBoundLowField;
    }

    @XmlElement
    public void setEventDetectionStepBoundLowField(Double eventDetectionStepBoundLowField) {
        this.eventDetectionStepBoundLowField = eventDetectionStepBoundLowField;
    }

    @XmlType
    @XmlEnum(String.class)
    public enum ResultStorageType {
        @XmlEnumValue("STORAGE_FILE") FILE,
        @XmlEnumValue("STORAGE_MEMORY") MEMORY
    }
}
