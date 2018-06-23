package data;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by doka on 2018-03-17.
 */
public class DataRow implements Iterable<Double>{
    private List<Double> featureValues;

    public DataRow(List<Double> featureValues) {
        this.featureValues = featureValues;
    }
    public DataRow(DataRow dataRow){
        this.featureValues = dataRow.featureValues.stream().map(e->Double.valueOf(e.doubleValue())).collect(Collectors.toList());
    }

    public int size(){
        return featureValues.size();
    }

    public Double getValue(int featureIdx){
        return featureValues.get(featureIdx);
    }

    public void setValue(int featureIdx, double value){
        featureValues.set(featureIdx,value);
    }

    public DataRow discritizeByHighestValue(){
        if(featureValues.size()==0){
            throw new RuntimeException("DataRow cannot be empty");
        }
        final int idx = featureValues.indexOf(featureValues.stream().max(Double::compareTo).get());
        final List<Double> result = new ArrayList<>(Collections.nCopies(featureValues.size(), 0D));
        result.set(idx,1D);
        return new DataRow(result);
    }

    @Override
    public Iterator<Double> iterator() {
        return featureValues.iterator();
    }

    @Override
    public void forEach(Consumer<? super Double> action) {
        featureValues.forEach(action);
    }

    @Override
    public Spliterator<Double> spliterator() {
        return featureValues.spliterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRow dataRow = (DataRow) o;
        return Objects.equals(featureValues, dataRow.featureValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureValues);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        featureValues.forEach(e->builder.append(e).append(" , "));
        return "DataRow{" +
                builder.toString() +
                '}';
    }
}
