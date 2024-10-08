package com.Rusya2054.wm.web.files.parser;

import com.Rusya2054.wm.web.files.template.Columns;

import java.util.*;

/**
 * static classe for parsing file columns
 */
public final class InputFileParser {

    private static final Map<String, List<String>> parserMapTemplate = new LinkedHashMap<>(){{
        put(Columns.getWellID(), List.of("Куст_Скв._М-е", "№ Скважины", "Well", "WellID", "wellID"));
        put(Columns.getRotationDirection(), List.of("Напр.вращен", "Направление вращения", "Направление вращения\nРежим работы", "Напр. вращ.", "Напр. вращения", "RotationDirection"));
        put(Columns.getDate(), List.of("Дата, Время", "Время", "Дата и время\nконтроллера", "Date"));
        put(Columns.getFrequency(), List.of("F, Гц", "F. Гц", "Частота вращения , Гц", "Частота вращения ПЭД (Гц), Гц", "F\n(Гц)", "Fвых, Гц", "Рабочая частота", "Fтек, Гц", "Frequency"));
        put(Columns.getCurPhaseA(), List.of("Ia, A", "Ia. A", "Ia\n(А)", "Ток фазы U, A", "Ia ПЭД, А", "Ток фазы A", "CurPhaseA"));
        put(Columns.getCurPhaseB(), List.of("Ib, A", "Ib. A", "Ib\n(А)", "Ток фазы V, A", "Ib ПЭД, А", "Ток фазы B", "CurPhaseB"));
        put(Columns.getCurPhaseC(), List.of("Ic, A", "Ic. A", "Ic\n(А)", "Ток фазы W, A", "Ic ПЭД, А", "Ток фазы C", "CurPhaseC"));
        put(Columns.getCurrentImbalance(), List.of("Дисбаланс по току , %", "Дисбаланс тока, %", "Дисб. I, %", "Дисб.I", "Дисб.I\n(%)", "Дисб.I ПЭД, %", "Дисбаланс токов", "CurrentImbalance"));
        put(Columns.getLineCurrent(), List.of("Id, A", "Id. A", "Id\n(А)", "Ток на накопителе, A", "Ток(Id)", "LineCurrent"));
        put(Columns.getLineVoltage(), List.of("Ud, В", "Ud. В", "Ud\n(В)", "Напряжение на накопителе, B", "Напряжение сети (Ud)", "LineVoltage"));
        put(Columns.getActivePower(), List.of("акт.P,кВт", "акт.P.кВт", "Pакт.\n(кВт)", "Активная мощность, КВт", "Рпэд, кВт", "Активная мощность", "ActivePower"));
        put(Columns.getTotalPower(), List.of("P, кВА", "P. кВА", "Мощность, кВт", "Мощность ПЭД, кВт", "Pполн.\n(кВА)", "Sпэд, кВА", "TotalPower"));
        put(Columns.getPowerFactor(), List.of("cos", "COSf ", "CosF", "COS", "Cos Ф", "Cos ф", "Коэффициент мощности", "PowerFactor"));
        put(Columns.getEngineLoad(), List.of("Загр., %", "Загр.. %", "Загрузка ПЭД, %", "Загрузка , %", "Загрузка\n(%)", "Загр.ПЭД, %", "Загр. ПЭД, %", "EngineLoad"));
        put(Columns.getInputVoltageAB(), List.of("Uab, В", "Uвх.AB.В", "Uвх.AB,В", "Uвх.AB, В", "Напряжение AB, B", "Напряжение фазы А, В", "Uab\n(В)", "Напряжение между фазами AB", "InputVoltageAB"));
        put(Columns.getInputVoltageBC(), List.of("Ubc, В", "Uвх.BC.В", "Uвх.BC,В", "Uвх.BC, В", "Напряжение ВС, B", "Напряжение фазы B, В", "Ubc\n(В)", "Напряжение между фазами BC", "InputVoltageBC"));
        put(Columns.getInputVoltageCA(), List.of("Uca, В", "Uвх.CA.В", "Uвх.CA,В", "Uвх.CA, В", "Напряжение СA, B", "Напряжение фазы C, В", "Uca\n(В)", "Напряжение между фазами CA", "InputVoltageCA"));
        put(Columns.getIntakePressure(), List.of("P, ат.", "P. ат.", "Давление , Атм", "Давление жидкости на приеме насоса, атм", "Давление жидкости на приеме насоса , ат", "Р приема\n(ат.)", "Pвх.эцн, кгс/см2", "Давление на приеме насоса", "IntakePressure"));
        put(Columns.getEngineTemp(), List.of("Tдвиг, °C", "Tдвиг. °C", "Температура двигателя , °С", "Температура ПЭД, °C", "T° масла ПЭД\n(°C)", "Температура масла ПЭД, °C", "T масла ПЭД, °C", "Температура двигателя", "EngineTemp"));
        put(Columns.getLiquidTemp(), List.of("Tжид, °C", "Tжид. °C", "T° жидкости\n(°C)", "Тжидк.БВ", "Температура жидкости на приеме насоса , °C", "T окр, °C", "Температура жидкости", "LiquidTemp"));
        put(Columns.getVibrationAccRadial(), List.of("Вибр X/Y, м/с2", "Вибр X/Y. м/с2", "Среднеквадратичная вибрация по осям X и Y, g", "Вибрация XY", "Вибрация, g", "Вибр.XY, g", "Вибрация вала насоса по оси X", "VibrationAccRadial"));
        put(Columns.getVibrationAccAxial(), List.of("Вибр Z, м/с2", "Вибр Z. м/с2", "Вибрация по оси Z, g", "Вибрация Z", "Вибр. Z, g", "Вибр.Z, g", "Вибрация вала насоса по оси Y", "VibrationAccAxial"));
        put(Columns.getLiquidflowRatio(), List.of("LiquidflowRatio"));
        put(Columns.getIsolationResistance(), List.of("R, кОм", "Сопротивление изоляции, кОм", "Rиз, кОм", "R. кОм", "IsolationResistance"));
    }};

    public static List<String> parseIndicatorsFile(List<String> strings, String sep, int nRows){
        if (!strings.isEmpty()) {
            List<String> columnHeades = Arrays.stream(strings.get(0).split(sep)).toList();

            // if columns finded, then index from columnHeades else -1
            Map<String, Integer> columnIndexMap = new LinkedHashMap<>(30);
            InputFileParser.parserMapTemplate.entrySet().forEach(p -> {
                columnHeades.stream().forEach(c -> {
                    if (p.getValue().contains(c)) {
                        columnIndexMap.put(p.getKey(), columnHeades.indexOf(c));
                    }
                });
                if (!columnIndexMap.keySet().contains(p.getKey())) {
                    columnIndexMap.put(p.getKey(), -1);
                }
            });

            List<String> result = new ArrayList<>(strings.size()){{add(String.join(sep, columnIndexMap.keySet()));}};

            strings.stream()
                    .skip(1)
                    .limit(nRows)
                    .filter(s -> s != null)
                    .forEach(r->{
                        String[] row = r.split(sep);
                        result.add(String.join(sep, columnIndexMap
                                .values()
                                .stream()
                                .map(v -> v.equals(-1) ? "0" : row[v])
                                .toArray(String[]::new)));
            });

            return result;
        } else{
            return new ArrayList<>();
        }

    }

    public static List<String> parseIndicatorsFile(List<String> strings, String sep){
        if (!strings.isEmpty()){
            List<String> columnHeades = Arrays.stream(strings.get(0).split(sep)).toList();

            // if columns finded, then index from columnHeades else -1
            Map<String, Integer> columnIndexMap = new LinkedHashMap<>(30);
            InputFileParser.parserMapTemplate.entrySet().forEach(p->{
                columnHeades.stream().forEach(c->{
                    if (p.getValue().contains(c)){
                        columnIndexMap.put(p.getKey(), columnHeades.indexOf(c));
                    }
                });
                if (!columnIndexMap.keySet().contains(p.getKey())){
                    columnIndexMap.put(p.getKey(), -1);
                }
            });

            List<String> result = new ArrayList<>(strings.size()){{add(String.join(sep, columnIndexMap.keySet()));}};
            strings.stream().skip(1).filter(s -> s != null).forEach(r->{
                String[] row = r.split(sep);
                result.add(String.join(sep, columnIndexMap
                        .values()
                        .stream()
                        .map(v -> v.equals(-1) ? "0" : row[v])
                        .toArray(String[]::new)));
            });
            return result;

        } else{
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {

        List<String> lst = List.of("WellID\t2\t5523", "1\t213\t123");
        List<String> emptyList = List.of("");
        List<String> emptyList1 = new ArrayList<>();
        List<String> nullList = Arrays.asList("first", null, "second", "third", null);;
        // InputFileParser.parseIndicatorsFile(lst, "\t").stream().forEach(System.out::println);
        InputFileParser.parseIndicatorsFile(nullList, "\t").stream().forEach(System.out::println);

//        InputFileParser.parseIndicatorsFile(emptyList, "\t").stream().forEach(System.out::println);
//        InputFileParser.parseIndicatorsFile(emptyList1, "\t").stream().forEach(System.out::println);

    }
}
