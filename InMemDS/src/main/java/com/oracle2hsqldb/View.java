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

package com.oracle2hsqldb;

/**
 * @author Rhett Sutphin
 */
public class View extends Table {
    private String text;

    public View(String name, String text) {
        super(name, Type.VIEW);
        this.text = text;
    }

    public String text() {
        return text;
    }

    public static class Spec extends Table.Spec {
        private String text;

        public Spec(String tableName, String tableTypeName, String text) {
            super(tableName, tableTypeName);
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public Table getTable() {
            return new View(getTableName(), getText());
        }
    }
}
