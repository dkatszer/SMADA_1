package readers;

import data.DataRow;
import data.TrainingRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * Created by doka on 2018-03-17.
 */
public class ExcelDataRowReader {
    protected Sheet datatypeSheet;
    private Map<String, DataRow> classes;

    public ExcelDataRowReader(FileInputStream excelFile) throws IOException {
        Workbook workbook = new HSSFWorkbook(excelFile);
        this.datatypeSheet = workbook.getSheetAt(0);
    }

    public List<TrainingRow> readTrainingData() {
        List<TrainingRow> trainingRows = new ArrayList<>();
        final List<Row> rows = StreamSupport.stream(datatypeSheet.spliterator(), false)
                .filter(currentRow -> currentRow.getCell(currentRow.getLastCellNum() - 1) != null)
                .collect(Collectors.toList());
        for (Row currentRow : rows) {
            TrainingRow trainingRow = new TrainingRow();
            trainingRow.features = readFeatureValues(currentRow);
            trainingRow.result = readExpectedResult(currentRow);
            trainingRows.add(trainingRow);
        }

        return trainingRows;
    }

    private List<String> readClasses() {
        return StreamSupport.stream(datatypeSheet.spliterator(), false)
                .map(currentRow -> currentRow.getCell(currentRow.getLastCellNum() - 1))
                .filter(Objects::nonNull)
                .map(Cell::getStringCellValue)
                .distinct()
                .collect(Collectors.toList());
    }

    protected DataRow readFeatureValues(Row currentRow) {
        List<Double> featureValues = new ArrayList<>();
        for (Cell currentCell : currentRow) {
            final CellType cellType = currentCell.getCellTypeEnum();
            if (cellType == CellType.NUMERIC) {
                featureValues.add(currentCell.getNumericCellValue());
            } else {
                break;
            }
        }
        return new DataRow(featureValues);
    }

    private DataRow readExpectedResult(Row currentRow) {
        Cell currentCell = currentRow.getCell(currentRow.getLastCellNum() - 1);
        String stringCellValue = currentCell.getStringCellValue();
        return getClasses().get(stringCellValue);
    }

    public List<DataRow> readTestData() {
        return StreamSupport.stream(datatypeSheet.spliterator(), false)
                .map(this::readFeatureValues)
                .collect(Collectors.toList());
    }

    public Map<String, DataRow> getClasses() {
        if (classes == null) {
            classes = readClassesDefinition();
        }
        return classes;
    }

    public int getNumberOfClasses() {
        return getClasses().size();
    }

    private Map<String, DataRow> readClassesDefinition() {
        Map<String, DataRow> result = new HashMap<>();
        final List<String> declaredClasses = readClasses();
        for (int i = 0; i < declaredClasses.size(); i++) {
            final List<Double> classDef = new ArrayList<>(Collections.nCopies(declaredClasses.size(), 0d));
            classDef.set(i, 1D);
            result.put(declaredClasses.get(i), new DataRow(classDef));
        }
        return result;
    }

    public List<TrainingRow> normalizeTrainingRowsByFeatures(List<TrainingRow> data) {
        List<DataRow> featuresData = data.stream().map(e -> e.features).collect(Collectors.toList());
        List<DataRow> normalizedFeatureData = normalizeDataByFeatures(featuresData);
        return IntStream.range(0, normalizedFeatureData.size())
                .mapToObj(idx ->
                        new TrainingRow(
                                normalizedFeatureData.get(idx),
                                data.get(idx).result))
                .collect(Collectors.toList());
    }

    public List<DataRow> normalizeDataByFeatures(List<DataRow> data) {
        if (data == null || data.isEmpty()) {
            throw new InvalidParameterException();
        }
        final int featuresQuantity = data.get(0).size();

        //copy constructor
        List<DataRow> result = data.stream().map(dataRow -> new DataRow(dataRow)).collect(Collectors.toList());

        for (int i = 0; i < featuresQuantity; i++) {
            final int j = i;
            DoubleSummaryStatistics statistics = result.stream().mapToDouble(e -> e.getValue(j)).summaryStatistics();
            result.forEach(e -> e.setValue(
                    j,
                    normalize(
                            e.getValue(j),
                            statistics.getMin(),
                            statistics.getMax())
                    )
            );
        }
        return result;
    }

    private double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }
}
