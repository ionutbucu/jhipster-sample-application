package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ReportExecutionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("rid", table, columnPrefix + "_rid"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("error", table, columnPrefix + "_error"));
        columns.add(Column.aliased("url", table, columnPrefix + "_url"));
        columns.add(Column.aliased("jhi_user", table, columnPrefix + "_jhi_user"));
        columns.add(Column.aliased("additional_info", table, columnPrefix + "_additional_info"));

        columns.add(Column.aliased("report_rid", table, columnPrefix + "_report_rid"));
        return columns;
    }
}
