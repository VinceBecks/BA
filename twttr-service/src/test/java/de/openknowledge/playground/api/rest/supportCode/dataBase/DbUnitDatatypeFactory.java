package de.openknowledge.playground.api.rest.supportCode.dataBase;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DbUnitDatatypeFactory {
    DEFAULT(null, "org.dbunit.dataset.datatype.DefaultDataTypeFactory"),

    H2("org.h2.Driver", "org.dbunit.ext.h2.H2DataTypeFactory"),

    MYSQL("com.mysql.jdbc.Driver", "org.dbunit.ext.mysql.MySqlDataTypeFactory"),

    ORACLE("oracle.jdbc.OracleDriver", "org.dbunit.ext.oracle.OracleDataTypeFactory"),

    POSTGRESQL("org.postgresql.Driver", "org.dbunit.ext.postgresql.PostgresqlDataTypeFactory");


    private static final Map<String, DbUnitDatatypeFactory> LOOKUP = new HashMap<>();

    static {
        for (DbUnitDatatypeFactory factory : EnumSet.allOf(DbUnitDatatypeFactory.class)) {
            LOOKUP.put(factory.getDatabaseDriverClazz(), factory);
        }
    }


    private final String databaseDriverClazz;

    private final String datatypeFactory;

    DbUnitDatatypeFactory(final String databaseDriverClazz, final String datatypeFactory) {
        this.databaseDriverClazz = databaseDriverClazz;
        this.datatypeFactory = datatypeFactory;
    }

    public static String getDatatypeFactory(final String databaseDriver) {
        return LOOKUP.get(databaseDriver).getDatatypeFactory();
    }

    public String getDatabaseDriverClazz() {
        return databaseDriverClazz;
    }

    public String getDatatypeFactory() {
        return datatypeFactory;
    }
}
