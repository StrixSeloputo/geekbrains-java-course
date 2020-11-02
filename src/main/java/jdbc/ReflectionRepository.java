package jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ReflectionRepository<T> {
    // Создайте обобщенный класс вида ReflectionRepository<T>,
    // где T - тип класса, объекты которого необходимо сохранять в БД

    private final JDBC jdbc;
    private final String tableName;
    private final Map<String, DbColumnType> columnMap;
    private final Class<T> tClass;
    private final String sqlInsertPrep;
    private final String sqlSelectByIdPrep;


    //TODO *После сохранения должен вернуться объект с id, присвоенным базой данных

    public ReflectionRepository(JDBC jdbc, Class<T> tClass) throws SQLException {
        this.jdbc = jdbc;
        this.tClass = tClass;

        if (tClass.getAnnotation(DbTable.class) != null) {
            tableName = tClass.getAnnotation(DbTable.class).name();
        } else {
            throw new RuntimeException("Class of generalization must have @DbTable annotation");
        }

        try {
            Constructor<T> constructor = tClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Class with @DbTable annotation must have constructor without parameters");
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

        jdbc.createTableEx(prepareStatement(StatementType.CREATE));
        this.sqlInsertPrep = prepareStatement(StatementType.INSERT);
        this.sqlSelectByIdPrep = prepareStatement(StatementType.SELECT);
    }

    // ReflectionRepository должен позволять: добавлять объекты в таблицу и получать объект по id
    public void addObject(T obj) throws NoSuchFieldException, IllegalAccessException, SQLException {
        PreparedStatement preparedInsertStatement = jdbc.prepareStatement(sqlInsertPrep);
        int i = 1;
        for (Map.Entry<String, DbColumnType> column : columnMap.entrySet()) {
            Field field = obj.getClass().getDeclaredField(column.getKey());
            field.setAccessible(true);
            if (column.getValue().equals(DbColumnType.LONG)) {
                preparedInsertStatement.setLong(i++, (Long) field.get(obj));
            } else if (column.getValue().equals(DbColumnType.STRING)) {
                preparedInsertStatement.setString(i++, (String) field.get(obj));
            } else if (column.getValue().equals(DbColumnType.INTEGER)) {
                preparedInsertStatement.setInt(i++, (Integer) field.get(obj));
            }
        }
        jdbc.insertRowEx(preparedInsertStatement);
    }

    public T getObject(Long id) throws SQLException {
        PreparedStatement prepareStatement = jdbc.prepareStatement(sqlSelectByIdPrep);
        prepareStatement.setLong(1, id);
        ResultSet resultSet = jdbc.selectRowEx(prepareStatement);
        T obj;
        try {
            Constructor<T> constructor = tClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            obj = constructor.newInstance();

            Object value = null;
            while (resultSet.next()) {
                for (Map.Entry<String, DbColumnType> column : columnMap.entrySet()) {
                    if (column.getValue().equals(DbColumnType.LONG)) {
                        value = resultSet.getLong("id");
                    } else if (column.getValue().equals(DbColumnType.STRING)) {
                        value = resultSet.getString(column.getKey());
                    } else if (column.getValue().equals(DbColumnType.INTEGER)) {
                        value = resultSet.getInt(column.getKey());
                    }
                    Field field = obj.getClass().getDeclaredField(column.getKey());
                    field.setAccessible(true);
                    field.set(obj, value);
                }
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException("Something with the class with @DbTable annotation goes wrong");
        }

        return obj;
    }

    private String prepareStatement(StatementType stmtType) {
        StringBuilder prepStmt = new StringBuilder();
        if (stmtType.equals(StatementType.INSERT)) {
            prepStmt.append("INSERT INTO ").append(tableName);
            StringJoiner sjColumnNames = new StringJoiner(",", "(", ")");
            StringJoiner sjValuePlaceholders = new StringJoiner(",", "(", ")");
            for (Map.Entry<String, DbColumnType> column : columnMap.entrySet()) {
                DbColumnType columnType = columnMap.get(column.getKey());
                if (columnType != null) {
                    if (columnType.equals(DbColumnType.INTEGER)) {
                        sjColumnNames.add(column.getKey());
                        sjValuePlaceholders.add("?");
                    } else if (columnType.equals(DbColumnType.STRING)) {
                        sjColumnNames.add(column.getKey());
                        sjValuePlaceholders.add("?");
                    } else if (columnType.equals(DbColumnType.LONG)) {
                        sjColumnNames.add("id");
                        sjValuePlaceholders.add("?");
                    }
                }
            }
            prepStmt.append(sjColumnNames.toString()).append(" VALUES ").append(sjValuePlaceholders.toString()).append(";");
        } else if (stmtType.equals(StatementType.CREATE)) {
            prepStmt.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
            StringJoiner sjColumnDescription = new StringJoiner(",\n", " (\n", "    )");

            for (Map.Entry<String, DbColumnType> column : columnMap.entrySet()) {
                if (column.getValue().equals(DbColumnType.LONG)) {
                    sjColumnDescription.add("        id    INTEGER PRIMARY KEY AUTOINCREMENT");
                } else if (column.getValue().equals(DbColumnType.STRING)) {
                    sjColumnDescription.add(String.format("        %s    TEXT", column.getKey()));
                } else if (column.getValue().equals(DbColumnType.INTEGER)) {
                    sjColumnDescription.add(String.format("        %s    INTEGER", column.getKey()));
                }
            }
            prepStmt.append(sjColumnDescription.toString()).append(";");
        } else if (stmtType.equals(StatementType.SELECT)) {
            prepStmt.append("SELECT * FROM ").append(tableName).append(" WHERE id = ?;");
        }
        return prepStmt.toString();
    }

    public void printAll() throws SQLException {
        jdbc.selectAllPrint(tableName, columnMap);
    }
}
