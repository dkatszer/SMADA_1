package data;

/**
 * Created by doka on 2018-03-17.
 */
public class TrainingRow {
    public TrainingRow(DataRow features, DataRow result) {
        this.features = features;
        this.result = result;
    }

    public TrainingRow() {
    }

    public DataRow features;
    public DataRow result;
}
