package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ReportParamSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("rid", table, columnPrefix + "_rid"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("value", table, columnPrefix + "_value"));
        columns.add(Column.aliased("conversion_rule", table, columnPrefix + "_conversion_rule"));

        columns.add(Column.aliased("report_rid", table, columnPrefix + "_report_rid"));
        return columns;
    }
}
