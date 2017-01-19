/*
 * Copyright (C) 2017 Félix Pedrozo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package buscaminas;

public class Bandera {
    private int x, y;

    Bandera (int x, int y) {
        setX(x);
        setY(y);
    }

    public void setX (int x) {
        this.x = x;
    }

    public int getX () {
        return x;
    }

    public void setY (int y) {
        this.y = y;
    }

    public int getY () {
        return y;
    }

    @Override
    public boolean equals (Object object) {
        if (object instanceof Bandera) {
            Bandera ban = (Bandera)object;
            if (x == ban.x && y == ban.y)
                return true;
        }
        return false;
    }

    @Override
    public int hashCode () {
        int result = 17;
        result = 37 * result + x;
        result = 37 * result + y;

        return result;
    }
}
