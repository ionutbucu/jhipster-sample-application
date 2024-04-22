package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ReportSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("rid", table, columnPrefix + "_rid"));
        columns.add(Column.aliased("cid", table, columnPrefix + "_cid"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("query", table, columnPrefix + "_query"));
        columns.add(Column.aliased("query_type", table, columnPrefix + "_query_type"));
        columns.add(Column.aliased("file_name", table, columnPrefix + "_file_name"));
        columns.add(Column.aliased("report_type", table, columnPrefix + "_report_type"));
        columns.add(Column.aliased("license_holder", table, columnPrefix + "_license_holder"));
        columns.add(Column.aliased("owner", table, columnPrefix + "_owner"));

        columns.add(Column.aliased("datasource_rid", table, columnPrefix + "_datasource_rid"));
        columns.add(Column.aliased("metadata_rid", table, columnPrefix + "_metadata_rid"));
        return columns;
    }
}
