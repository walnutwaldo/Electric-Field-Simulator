package UI;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class HorizontalLayout extends UIComponent {

    private List<UIComponent> components;

    public HorizontalLayout() {
        super(0);
        components = new ArrayList<UIComponent>();
        width = 0;
        leftMargin = 0;
    }

    public void add(UIComponent uic) {
        components.add(uic);
        width += uic.leftMargin + uic.width;
        height = Math.max(height, uic.height);
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        for (UIComponent uic : components) {
            uic.draw(g);
            g.translate(uic.leftMargin + uic.width, 0);
        }
        g.setTransform(at);
    }

    public List<UIComponent> getComponents() {
        return components;
    }
}
