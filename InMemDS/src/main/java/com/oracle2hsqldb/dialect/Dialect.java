/*
 * Schemamule, a library for automating database schema tasks
 * Copyright (C) 2006, Moses M. Hohman and Rhett Sutphin
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St., 5th Floor, Boston, MA  02110-1301

 * To contact the authors, send email to:
 * { mmhohman OR rsutphin } AT sourceforge DOT net
 */

package com.oracle2hsqldb.dialect;


import javax.sql.DataSource;

import com.oracle2hsqldb.Column;
import com.oracle2hsqldb.DefaultValue;

import java.sql.SQLException;
import java.util.Iterator;

/**
 * @author Moses Hohman
 */
public interface Dialect {
    // For getSize
    int NO_SIZE = -1;
    
    boolean supportsUnique();

    boolean supportsIdentityColumns();

    boolean supportsViews();

    boolean supportsSequences();

    String getIdentityColumnString();

    Iterator getTables(DataSource dataSource, String schemaName) throws SQLException;

    Iterator getColumns(DataSource dataSource, String schemaName) throws SQLException;

    Iterator getSequences(DataSource dataSource, String schemaName) throws SQLException;

    Iterator getPrimaryKeys(DataSource dataSource, String schemaName);

    Iterator getUniqueKeys(DataSource dataSource, String schemaName);

    int getType(String dataTypeName);

    String getTypeName(int type);

    String getTypeName(Column column);

    int getSize(Column column);

    DefaultValue parseDefaultValue(String defaultValue, int type);

    String formatDefaultValue(Column column);

    String getDriverClassName();

    String getShutdownSql();

    String getNextSequenceValueSql(String sequenceName);
}
