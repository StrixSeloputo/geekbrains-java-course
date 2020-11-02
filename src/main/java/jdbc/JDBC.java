package jdbc;

import java.sql.*;
import java.util.Arrays;
import java.util.Map;

public class JDBC implements AutoCloseable {

    private static Connection connection;
    private static Statement stmt;

    public JDBC() throws SQLException, ClassNotFoundException {
        connect();
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

    //==== JDBC methods

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:reflection.db");
        stmt = connection.createStatement();


    }

    public void disconnect() throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    // Таблица для хранения сущностей создается вручную, делать генерацию запроса CREATE TABLE не требуется.
    // В таблице должен быть столбец id (primary key, auto increment/bigserial).
    public void createTableEx(String sqlCreateStatement) throws SQLException {
        System.out.println(sqlCreateStatement);
        stmt.executeUpdate(sqlCreateStatement);
    }

    public void insertRowEx(PreparedStatement preparedInsertStatement) throws SQLException {
        int[] result = preparedInsertStatement.executeBatch();
        System.out.println(Arrays.toString(result));
    }

    public ResultSet selectRowEx(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeQuery();
    }

    public void selectAllPrint(String tableName, Map<String, DbColumnType> columnMap) throws SQLException {
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

    public void getMetadata() throws SQLException {
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

    public PreparedStatement prepareStatement(String sqlStatement) throws SQLException {
        return connection.prepareStatement(sqlStatement);
    }

}
