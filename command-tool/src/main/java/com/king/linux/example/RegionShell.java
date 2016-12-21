package com.king.linux.example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class RegionShell {
	public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display, SWT.NO_TRIM);
        Region region = new Region();
        region.add(circle(20, 500, 300));
 
        shell.setRegion(region);
        shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
        Listener l = new Listener() {
            int startX, startY;
 
            public void handleEvent(Event e) {
                if (e.type == SWT.KeyDown && e.character == SWT.ESC) {
                    shell.dispose();
                }
                if (e.type == SWT.MouseDown && e.button == 1) {
                    startX = e.x;
                    startY = e.y;
                }
                if (e.type == SWT.MouseMove && (e.stateMask & SWT.BUTTON1) != 0) {
                    Point p = shell.toDisplay(e.x, e.y);
                    p.x -= startX;
                    p.y -= startY;
                    shell.setLocation(p);
                }
            }
        };
        shell.addListener(SWT.KeyDown, l);
        shell.addListener(SWT.MouseDown, l);
        shell.addListener(SWT.MouseMove, l);
        shell.addListener(SWT.Paint, l);
 
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        region.dispose();
        display.dispose();
 
    }
 
    static int[] circle(int r, int offsetX, int offsetY) {
        int[] polygon = new int[8 * r + 4];
        // x^2 + y^2 = r^2
        for (int i = 0; i < 2 * r + 1; i++) {
            int x = i - r;
            int y = (int)Math.sqrt(r*r - x*x);
            polygon[2*i] = offsetX + x;
            polygon[2*i+1] = offsetY + y;
            polygon[8*r - 2*i - 2] = offsetX + x;
            polygon[8*r - 2*i - 1] = offsetY - y;
        }
        return polygon;
    }
}

