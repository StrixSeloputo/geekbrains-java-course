package jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class JDBC {

    private static Connection connection;
    private static Statement stmt;

    //== JDBC methods

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:reflection.db");
        stmt = connection.createStatement();


    }

    public static void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Таблица для хранения сущностей создается вручную, делать генерацию запроса CREATE TABLE не требуется.
    // В таблице должен быть столбец id (primary key, auto increment/bigserial).
    public static void createTableEx(String tableName, Map<String, DbColumnType> columnMap) throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + tableName;
        sqlCreate += " (\n" +
                "        id    INTEGER PRIMARY KEY AUTOINCREMENT\n";
        for (Map.Entry<String, DbColumnType> column : columnMap.entrySet()) {
            if (column.getValue().equals(DbColumnType.LONG)) {
                continue;
            } else if (column.getValue().equals(DbColumnType.STRING)) {
                sqlCreate += "        ," + column.getKey() + " TEXT\n";
            } else if (column.getValue().equals(DbColumnType.INTEGER)) {
                sqlCreate += "        ," + column.getKey() + " INTEGER\n";
            }
        }
        sqlCreate += "    );";
        System.out.println(sqlCreate);
        stmt.executeUpdate(sqlCreate);
    }

    public static int insertRowEx(String tableName, Map<String, DbColumnType> columnMap, Map<String, Object> valueMap) throws SQLException {
        StringBuilder sqlInsert = new StringBuilder();
        sqlInsert.append("INSERT INTO ").append(tableName);
        StringJoiner sjCol = new StringJoiner(",", "(", ")");
        StringJoiner sjVal = new StringJoiner(",", "(", ")");
        for (Map.Entry<String, Object> value : valueMap.entrySet()) {
            DbColumnType columnType = columnMap.get(value.getKey());
            if (columnType != null) {
                if (columnType.equals(DbColumnType.INTEGER)) {
                    sjCol.add(value.getKey());
                    sjVal.add(value.getValue().toString());
                } else if (columnType.equals(DbColumnType.STRING)) {
                    sjCol.add(value.getKey());
                    sjVal.add("'" + value.getValue().toString() + "'");
                } else if (columnType.equals(DbColumnType.LONG)) {
                    sjCol.add("id");
                    sjVal.add(value.getValue().toString());
                }
            }
        }
        sqlInsert.append(sjCol.toString()).append(" VALUES ").append(sjVal.toString()).append(";");
//        sqlInsert += sjCol.toString() + " VALUES " + sjVal.toString() + ";";
        System.out.println(sqlInsert);
        return stmt.executeUpdate(sqlInsert.toString());
    }

    public static Map<String, Object> selectRowByIdEx(String tableName, Map<String, DbColumnType> columnMap, Long id) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE id=" + id + ";");
        Map<String, Object> valueMap = new HashMap<>();
        while (rs.next()) {
            for (Map.Entry<String, DbColumnType> column : columnMap.entrySet()) {
                if (column.getValue().equals(DbColumnType.LONG)) {
                    valueMap.put(column.getKey(), rs.getLong("id"));
                } else if (column.getValue().equals(DbColumnType.STRING)) {
                    valueMap.put(column.getKey(), rs.getString(column.getKey()));
                } else if (column.getValue().equals(DbColumnType.INTEGER)) {
                    valueMap.put(column.getKey(), rs.getInt(column.getKey()));
                }
            }
        }
        return valueMap;
    }

    public static void selectAllPrint(String tableName, Map<String, DbColumnType> columnMap) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");
        System.out.println("================");
        while (rs.next()) {
            ResultSetMetaData md = rs.getMetaData();
            int colCount = md.getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                String columnName = md.getColumnName(i);
                if (columnMap.get(columnName).equals(DbColumnType.STRING)) {
                    System.out.println(columnName + ": " + rs.getString(i));
                } else if (columnMap.get(columnName).equals(DbColumnType.INTEGER)) {
                    System.out.println(columnName + ": " + rs.getInt(i));
                } else {
                    System.out.println(columnName + ": " + rs.getLong(i));
                }
            }

            System.out.println("----");
        }
    }

    public static void getMetadata() throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            String remarks = resultSet.getString("REMARKS");

            System.out.println(tableName + " :: " + remarks);
            ResultSet columns = databaseMetaData.getColumns(null, null, tableName, null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnSize = columns.getString("COLUMN_SIZE");
                String datatype = columns.getString("DATA_TYPE");
                String isNullable = columns.getString("IS_NULLABLE");
                String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
                System.out.println("  " + columnName + " : " + columnSize + " : " + datatype + " : " + isNullable + " : " + isAutoIncrement);
            }
        }
    }
}
