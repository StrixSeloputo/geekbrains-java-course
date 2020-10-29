package jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReflectionRepository<T> {
    // Создайте обобщенный класс вида ReflectionRepository<T>,
    // где T - тип класса, объекты которого необходимо сохранять в БД

    private JDBC jdbc;
    private String tableName;
    private Map<String, DbColumnType> columnMap;
    private Class<T> tClass;
    private String sqlCreate;
    private String sqlInsertPrep;
    private String sqlSelectByIdPrep;


    // *После сохранения должен вернуться объект с id, присвоенным базой данных

    public ReflectionRepository(JDBC jdbc, Class<T> tClass) throws SQLException {
        this.jdbc = jdbc;
        this.tClass = tClass;

        if (tClass.getAnnotation(DbTable.class) != null) {
            tableName = tClass.getAnnotation(DbTable.class).name();
        } else {
            throw new RuntimeException("Class of generalization must have @DbTable annotation");
        }

        // Классы-сущности могут иметь поля типов: String, int (Integer). id имеет тип Long.
        // Связь между разметкой класса аннотациями и запросом осуществляется через Reflection API.
        Field[] publicFields = tClass.getDeclaredFields();
        columnMap = new HashMap<>(publicFields.length);
        for (Field field : publicFields) {
            field.setAccessible(true);
            if (field.getAnnotation(DbId.class) != null) {
                if (!Long.class.equals(field.getType()) && !long.class.equals(field.getType())) {
                    throw new RuntimeException("The field with @DbId annotation must have type of Long or long");
                }
                columnMap.put(field.getName(), DbColumnType.LONG);
            } else if (field.getAnnotation(DbColumn.class) != null) {
                if (String.class.equals(field.getType())) {
                    columnMap.put(field.getName(), DbColumnType.STRING);
                } else if (Integer.class.equals(field.getType()) || int.class.equals(field.getType())) {
                    columnMap.put(field.getName(), DbColumnType.INTEGER);
                } else {
                    throw new RuntimeException("The field with @DbColumn annotation must have type of String or Integer");
                }
            }
        }

        jdbc.createTableEx(tableName, columnMap);
    }

    // ReflectionRepository должен позволять: добавлять объекты в таблицу и получать объект по id
    public void addObject(T obj) throws NoSuchFieldException, IllegalAccessException, SQLException {
        Map<String, Object> valueMap = new HashMap<>(columnMap.size());
        for (Map.Entry<String, DbColumnType> column : columnMap.entrySet()) {
            Field field = obj.getClass().getDeclaredField(column.getKey());
            field.setAccessible(true);
            valueMap.put(field.getName(), field.get(obj));
        }
        jdbc.insertRowEx(tableName, columnMap, valueMap);
    }

    public T getObject(Long id) throws NoSuchFieldException, IllegalAccessException, SQLException {
        Map<String, Object> valueMap;
        valueMap = jdbc.selectRowByIdEx(tableName, columnMap, id);

        T obj;
        try {
            Constructor<T> constructor = tClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            obj = constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Class with @DbTable annotation must have constructor without parameters");
        }

        for (Map.Entry<String, Object> value : valueMap.entrySet()) {
            Field field = obj.getClass().getDeclaredField(value.getKey());
            field.setAccessible(true);
            field.set(obj, value.getValue());
        }

        return obj;
    }

    public void printAll() throws SQLException {
        jdbc.selectAllPrint(tableName, columnMap);
    }
}
