package com.phuonghn.pkm.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DataUtils {

    private static Logger log = LoggerFactory.getLogger(DataUtils.class);

    public static final char DEFAULT_ESCAPE_CHAR_QUERY = '\\';

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static BigDecimal safeToBigDecimal(Object obj) {
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        return BigDecimal.ZERO;
    }

    public static String safeToString(Object obj) {
        return Optional.ofNullable(obj).map(Object::toString).orElse("");
    }

    public static String setNullIfEmptyString(Object obj) {
        if (DataUtils.isNull(obj)) {
            return null;
        }
        return obj.toString();
    }

    public static Timestamp safeToTimestamp(Object obj) {
        return Optional.ofNullable(obj).map(o -> (Timestamp) o).orElse(null);
    }

    public static Integer safeToInteger(Object obj) {
        if (obj == null) {
            return 0;
        }
        return (Integer) obj;
    }

    public static Long safeToLong(Object obj1) {
        long result = 0L;
        if (obj1 != null) {
            if (obj1 instanceof BigDecimal) {
                return ((BigDecimal) obj1).longValue();
            }
            if (obj1 instanceof BigInteger) {
                return ((BigInteger) obj1).longValue();
            }
            try {
                result = Long.parseLong(obj1.toString());
            } catch (Exception ignored) {
            }
        }

        return result;
    }

    public static BigInteger safeToBigInteger(Object obj) {
        if (obj instanceof BigInteger) {
            return (BigInteger) obj;
        } else if (!isNull(obj)) {
            try {
                return new BigInteger(obj.toString());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return BigInteger.ZERO;
            }
        }
        return BigInteger.ZERO;
    }

    public static String makeLikeQuery(String s) {
        if (isNullOrEmpty(s))
            return null;
        s = s.trim().toLowerCase().replace("!", DEFAULT_ESCAPE_CHAR_QUERY + "!")
                .replace("%", DEFAULT_ESCAPE_CHAR_QUERY + "%")
                .replace("_", DEFAULT_ESCAPE_CHAR_QUERY + "_");
        return "%" + s + "%";
    }

    public static String timestampToString(Timestamp fromDate, String pattern) {
        return Optional.ofNullable(fromDate).map(tmp -> {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(tmp);
        }).orElse("");
    }

    public static String formatNumberWithComma(Double number, String pattern) {
        return Optional.ofNullable(number).map(tmp -> {
            DecimalFormat df = new DecimalFormat(pattern);
            return df.format(tmp);
        }).orElse("");

    }

    public static String dateToString(Date fromDate, String pattern) {
        return Optional.ofNullable(fromDate).map(tmp -> {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(tmp);
        }).orElse("");
    }

    public static Double safeToDouble(Object obj, Double defaultValue) {
        return Optional.ofNullable(obj).map(o -> {
            try {
                return Double.parseDouble(o.toString());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return defaultValue;
            }
        }).orElse(defaultValue);
    }

    public static Double safeToDouble(Object obj1) {
        return safeToDouble(obj1, 0.0);
    }

    public static Date safeToDate(Object obj) {
        if (obj instanceof Date) {
            return (Date) obj;
        }
        return null;
    }

    public static LocalDate safeToLocalDate(Object obj) {
        if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        return null;
    }

    public static List<String> changeParamTypeSqlToJava(String sqlType) {
        String[] tmp = sqlType.trim().split(",");
        List<String> stringList = new ArrayList<>();
        for (String s : tmp) {
            s = s.trim().replaceAll("\\s+,", "");
            StringBuilder builder = new StringBuilder();
            String[] words = s.split("[\\W_]+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (words.length > 1) {
                    if (i == 0) {
                        word = word.isEmpty() ? word : word.toLowerCase();
                    } else {
                        word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
                    }
                }
                builder.append(word);
            }
            stringList.add(builder.toString());
        }
        return stringList;
    }

    public static <T> List<T> convertListObjectsToClass(List<String> attConvert, List<Object[]> objects, Class<T> clazz) {
        if (DataUtils.isNullOrEmpty(objects)) {
            return new ArrayList<>();
        }
        return objects.stream().map(item -> {
            try {
                return convertObjectsToClass(attConvert, item, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public static <T> T convertObjectsToClass(List<String> attConvert, Object[] objects, Class<T> clazz) throws Exception {
        Object object = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < attConvert.size(); i++) {
            Field f;
            int finalIndex = i;
            f = Arrays.stream(fields).filter(item -> attConvert.get(finalIndex).equals(item.getName())).findFirst().orElse(null);
            if (f != null) {
                f.setAccessible(true);
                Class<?> t = f.getType();
                if (objects[i] == null)
                    continue;
                switch (t.getName()) {
                    case "java.lang.String":
                        if (objects[i] instanceof String || objects[i] instanceof Long || objects[i] instanceof BigInteger ||
                                objects[i] instanceof Integer || objects[i] instanceof BigDecimal) {
                            f.set(object, DataUtils.safeToString(objects[i]));
                        } else if (objects[i] instanceof java.sql.Date || objects[i] instanceof Date
                                || objects[i] instanceof Timestamp
                        ) {
                            f.set(object, date2StringByPattern(DataUtils.safeToDate(objects[i]), "dd/MM/yyyy HH:mm:ss"));
                        }
                        break;
                    case "java.lang.Long":
                    case "long":
                        f.set(object, DataUtils.safeToLong(objects[i]));
                        break;
                    case "java.lang.Double":
                    case "double":
                        f.set(object, DataUtils.safeToDouble(objects[i]));
                        break;
                    case "java.lang.Boolean":
                    case "boolean":
                        f.set(object, objects[i]);
                        break;
                    case "java.util.Date":
                        f.set(object, DataUtils.safeToDate(objects[i]));
                        break;
                    case "java.time.LocalDate":
                        f.set(object, DataUtils.safeToLocalDate(objects[i]));
                        break;
                    case "java.sql.Timestamp":
                        f.set(object, DataUtils.safeToTimestamp(objects[i]));
                        break;
                    case "java.lang.Integer":
                    case "int":
                        f.set(object, DataUtils.safeToInteger(objects[i]));
                        break;
                    case "java.math.BigInteger":
                        f.set(object, DataUtils.safeToBigInteger(objects[i]));
                        break;
                    case "java.math.BigDecimal":
                        f.set(object, DataUtils.safeToBigInteger(objects[i]));
                        break;
                    default:
                        break;
                }
            }
        }
        return (T) object;
    }

    public static String date2StringByPattern(Date date, String pattern) {
        if (date == null || DataUtils.isNullOrEmpty(pattern)) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static Date stringToDate(String dateStr, String pattern) throws ParseException {
        if (dateStr == null || dateStr.isEmpty())
            return new Date();
        DateFormat sourceFormat = new SimpleDateFormat(pattern);
        return sourceFormat.parse(dateStr);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cloneBean(T source) {
        try {
            if (source == null) {
                return null;
            } else {
                T dto = (T) source.getClass().getConstructor().newInstance();
                BeanUtils.copyProperties(source, dto);
                return dto;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isNotEquals(Object a, Object b) {
        if (a == null && b == null) {
            return false;
        }
        if (a != null && b != null) {
            Class cA = a.getClass();
            Class cB = b.getClass();
            if (cA == cB) {
                if (a.equals(b)) {
                    return false;
                }
                if (cA.equals(String.class)) {
                    String strA = String.valueOf(a);
                    String strB = String.valueOf(b);
                    if (strA.trim().equals(strB.trim())) {
                        return false;
                    }
                }
            }
            if ((cA.equals(Timestamp.class) && cB.equals(Date.class) && ((Timestamp) a).getTime() == ((Date) b).getTime())
                    || (cA.equals(Date.class) && cB.equals(Timestamp.class) && ((Date) a).getTime() == ((Timestamp) b).getTime())) {
                return false;
            }
        }
        if ((a != null && b == null && a.getClass().equals(String.class) && String.valueOf(a).trim().isEmpty()) ||
                (b != null && a == null && b.getClass().equals(String.class) && String.valueOf(b).trim().isEmpty())) {
            return false;
        }
        return true;
    }


    public static String StringValueOf(Object a) {
        if (a == null) {
            return null;
        }
        Class c = a.getClass();
        if (c.equals(Date.class) || c.equals(Timestamp.class)) {
            return DataUtils.date2StringByPattern((Date) a, "dd/MM/yyyy HH:mm:ss");
        }
        return String.valueOf(a).trim();
    }

    public static String getStrFirstDayOfPreviousMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return new SimpleDateFormat("yyyy-MM").format(cal.getTime()) + "-01";
    }

    public static List getMaxLength(List<List> list) {
        return list.stream().max(Comparator.comparingInt(List::size)).get();
    }

    public static Date getDayOf(Date date, int minusMonth, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, minusMonth);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return cal.getTime();
    }

}
