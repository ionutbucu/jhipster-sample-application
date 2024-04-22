package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ReportColumnMappingSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("rid", table, columnPrefix + "_rid"));
        columns.add(Column.aliased("source_column_name", table, columnPrefix + "_source_column_name"));
        columns.add(Column.aliased("source_column_index", table, columnPrefix + "_source_column_index"));
        columns.add(Column.aliased("column_title", table, columnPrefix + "_column_title"));
        columns.add(Column.aliased("lang", table, columnPrefix + "_lang"));

        columns.add(Column.aliased("report_rid", table, columnPrefix + "_report_rid"));
        return columns;
    }
}
