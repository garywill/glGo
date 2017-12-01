/*
 *  SGFFileFilter.java
 *
 *  gGo
 *  Copyright (C) 2002  Peter Strempel <pstrempel@t-online.de>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ggo.utils;

import ggo.gGo;

/**
 *  FileFilter subclass for SGF files
 *
 *@author     Peter Strempel
 *@version    $Revision: 1.4 $, $Date: 2002/10/08 14:47:03 $
 */
public class SGFFileFilter extends DynamicFileFilter {
    protected String[] getExtensions() {
        return new String[]{"sgf", "s"};
    }

    protected String getFormatDescriptor() {
        return gGo.getSGFResources().getString("SGF");
    }
}

