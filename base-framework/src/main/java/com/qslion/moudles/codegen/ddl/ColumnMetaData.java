package com.qslion.moudles.codegen.ddl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * ecms
 *
 * @author Gray.Z
 * @date 2018/11/9.
 */
public class ColumnMetaData {

    private final String name;
    private final String typeName;
    private final int columnSize;
    private final int decimalDigits;
    private final boolean isNullable;
    private final int typeCode;
    private final String comment;

    ColumnMetaData(ResultSet rs) throws SQLException {
        this.name = rs.getString("COLUMN_NAME");
        this.columnSize = rs.getInt("COLUMN_SIZE");
        this.decimalDigits = rs.getInt("DECIMAL_DIGITS");
        this.isNullable = rs.getInt("NULLABLE") > 0;
        this.comment = rs.getString("REMARKS");
        this.typeCode = rs.getInt("DATA_TYPE");
        this.typeName = (new StringTokenizer(rs.getString("TYPE_NAME"), "() ")).nextToken();
    }

    public String getName() {
        return this.name;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public int getColumnSize() {
        return this.columnSize;
    }

    public int getDecimalDigits() {
        return this.decimalDigits;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "ColumnMetaData(" + this.name + ')';
    }

    public int getTypeCode() {
        return this.typeCode;
    }

}
